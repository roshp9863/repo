package com.codedecode.foodcatalog.controller;

import com.codedecode.foodcatalog.dto.FoodCatalogPage;
import com.codedecode.foodcatalog.dto.FoodItemDTO;
import com.codedecode.foodcatalog.service.FoodCatalogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/foodCatalog")
public class FoodCatalogController {

    @Autowired
    private FoodCatalogService foodCatalogService;

    // Add methods to handle HTTP requests here
//    @GetMapping("/fetchAllRestaurants")
//    public ResponseEntity<List<FoodItemDTO>> fetchAllRestaurants() {
//        return ResponseEntity.ok(foodCatalogService.findAllRestaurants());
//    }

    @PostMapping("/addFoodItem")
    public ResponseEntity<FoodItemDTO> addFoodItem(@RequestBody FoodItemDTO restaurantDTO) {
        FoodItemDTO dto = foodCatalogService.addFoodItem(restaurantDTO);
        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }

    @GetMapping("/fetchRestaurantAndFoodItemsById/{id}")
    public ResponseEntity<FoodCatalogPage> fetchRestaurantDetailsWithFoodMenu(@PathVariable Integer id) {
        System.out.println("I AM IN CONTROLLER");
        return ResponseEntity.ok(foodCatalogService.fetchFoodCataloguePageService(id));
    }

}
