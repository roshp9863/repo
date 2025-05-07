package com.codedecode.foodcatalog.service;

import com.codedecode.foodcatalog.dto.FoodCatalogPage;
import com.codedecode.foodcatalog.dto.FoodItemDTO;
import com.codedecode.foodcatalog.dto.Restaurant;
import com.codedecode.foodcatalog.entity.FoodItem;
import com.codedecode.foodcatalog.repo.FoodItemRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class FoodCatalogService {

    @Autowired
    private FoodItemRepo foodItemRepo;

    @Autowired
    private RestTemplate restTemplate;

//    public List<FoodItemDTO> findAllRestaurants() {
//        List<FoodItem> restaurants = foodItemRepo.findAll();
//        return restaurants.stream()
//                .map(FoodItemMapper.INSTANCE::foodItemToFoodItemDTO)
//                .collect(Collectors.toList());
//    }

    public FoodItemDTO addFoodItem(FoodItemDTO foodItemDTO) {

        FoodItem foodItem = foodItemRepo.save(toEntity(foodItemDTO));
        return toDto(foodItem);
    }

    public FoodCatalogPage fetchFoodCataloguePageService(Integer id) {

        //food item list from this microservice db
        //restaurant details from restaurant microservice db

        List<FoodItem> foodItemList = fetchFoodItemList(id);
        Restaurant restaurant = fetchRestaurantDetailsFromRestaurantMS(id);
        FoodCatalogPage foodCatalogPage = createFoodCatalogPage(foodItemList, restaurant);
        return foodCatalogPage;


    }

    private FoodCatalogPage createFoodCatalogPage(List<FoodItem> foodItemList, Restaurant restaurant) {
        FoodCatalogPage foodCatalogPage = new FoodCatalogPage();
        foodCatalogPage.setFoodItemList(foodItemList);
        foodCatalogPage.setRestaurant(restaurant);
        return foodCatalogPage;
    }

    private List<FoodItem> fetchFoodItemList(Integer id) {
        return foodItemRepo.findByRestaurantId(id);
    }

    private Restaurant fetchRestaurantDetailsFromRestaurantMS(Integer id) {
        return restTemplate.getForObject("http://localhost:9091/restaurant/fetchById/" + id, Restaurant.class);
    }

    private FoodItemDTO toDto(FoodItem r) {
        FoodItemDTO dto = new FoodItemDTO();
        dto.setId(r.getId());
        dto.setItemName(r.getItemName());
        dto.setVeg(r.isVeg());
        dto.setPrice(r.getPrice());
        dto.setRestaurantId(r.getRestaurantId());
        dto.setQuantity(r.getQuantity());
        dto.setItemDescription(r.getItemDescription());
        return dto;
    }

    private FoodItem toEntity(FoodItemDTO r) {
        FoodItem dto = new FoodItem();
        dto.setId(r.getId());
        dto.setItemName(r.getItemName());
        dto.setVeg(r.isVeg());
        dto.setPrice(r.getPrice());
        dto.setRestaurantId(r.getRestaurantId());
        dto.setQuantity(r.getQuantity());
        dto.setItemDescription(r.getItemDescription());
        return dto;
    }


}
