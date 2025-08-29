package com.example.BiteBuddy.entities;

import java.util.List;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    // @JoinColumn(name = "order_id")
    private Order order; 

    @ManyToOne 
    private Food food;

    private int quantity;

    private Long totalPrice;

    private List<String> ingredients;

}
