package com.example.BiteBuddy.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.BiteBuddy.entities.User;
import com.example.BiteBuddy.service.UserService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class JwtTokenValidator extends OncePerRequestFilter {

    @Autowired
    private JwtProvider jwtProvider;  
    
    @Autowired
    private UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

                String path = request.getRequestURI();
                
                 // **ADD THIS - Skip JWT validation for public endpoints**
                if (isPublicEndpoint(path)) {
                    filterChain.doFilter(request, response);
                    return;
                }
        String token = extractJwtFromRequest(request);
        
        try {
            if (StringUtils.hasText(token)) {
                // Add "Bearer " prefix because JwtProvider expects it
                String email = jwtProvider.getEmailFromToken("Bearer " + token);
                
                if (email != null && !email.equals("null")) {
                    User user = userService.findUserByEmail(email);
                    
                    // Create authorities based on user role
                    List<GrantedAuthority> authorities = new ArrayList<>();
                    String roleName = user.getRole().name();
                    // Remove "ROLE_" prefix if it exists, then add it back
                    if (roleName.startsWith("ROLE_")) {
                        authorities.add(new SimpleGrantedAuthority(roleName));
                    } else {
                        authorities.add(new SimpleGrantedAuthority("ROLE_" + roleName));
                    }
                    
                    Authentication auth = new UsernamePasswordAuthenticationToken(
                        email, null, authorities);
                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            }
        } catch (Exception e) {
            System.out.println("JWT validation failed: " + e.getMessage());     
            SecurityContextHolder.clearContext();
        }
        
        filterChain.doFilter(request, response);
    }
    
    private String extractJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }



    private boolean isPublicEndpoint(String path) {
    return path.startsWith("/restaurants") ||
           path.startsWith("/auth/") ||
           path.startsWith("/food") ||
           path.startsWith("/categories/restaurant/") ||
           path.equals("/") ||
           path.startsWith("/static/") ||
           path.startsWith("/css/") ||
           path.startsWith("/js/");
}
}


