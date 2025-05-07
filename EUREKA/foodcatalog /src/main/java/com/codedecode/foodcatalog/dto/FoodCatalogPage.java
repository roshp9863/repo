package com.codedecode.foodcatalog.dto;

import com.codedecode.foodcatalog.entity.FoodItem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class FoodCatalogPage {

    List<FoodItem> foodItemList;
    Restaurant restaurant;


}
