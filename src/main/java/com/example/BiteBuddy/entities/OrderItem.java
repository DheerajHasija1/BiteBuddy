package com.example.BiteBuddy.entities;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JsonIgnore
    // @JoinColumn(name = "order_id")
    private Order order; 

    @ManyToOne 
    private Food food;

    private int quantity;

    private Long totalPrice;

    private List<String> ingredients;

}
