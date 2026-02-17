package com.codedecode.foodcatalog.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class FoodItem {

    @SequenceGenerator(
            name           = "food_item_seq",
            sequenceName   = "food_item_seq",
            allocationSize = 1     // ← no pooling
            /* initialValue only applies on DDL create */
    )
    @Id
    @GeneratedValue(
            strategy  = GenerationType.SEQUENCE,
            generator = "food_item_seq"
    )
    private int id;

    private String itemName;

    private String itemDescription;

    private boolean isVeg;

    private double price;

    private Integer restaurantId;

    @Column(nullable = false, columnDefinition = "int default 0")
    private Integer quantity;


}
