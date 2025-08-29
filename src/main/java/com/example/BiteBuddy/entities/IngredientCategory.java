package com.example.BiteBuddy.entities;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class IngredientCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;

    @JsonIgnore
    @ManyToOne
    private Restaurant restaurant;


    @OneToMany(mappedBy = "category",cascade = CascadeType.ALL)
    private List<IngredientItem> ingredients = new ArrayList<>();

}