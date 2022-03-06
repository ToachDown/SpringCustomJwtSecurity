package com.example.jwtcleanspring.repository;

import com.example.jwtcleanspring.model.Food;
import com.example.jwtcleanspring.model.enums.Taste;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.*;

@Repository
public class FoodRepository {

    private final List<Food> foods;

    public FoodRepository() {
        this.foods = List.of(
                new Food("apple", List.of(Taste.SWEET), BigDecimal.valueOf(123), "Germany"),
                new Food("beef", List.of(Taste.SALTY, Taste.SPICY), BigDecimal.valueOf(777), "USA"),
                new Food("potato", List.of(Taste.SWEET, Taste.SPICY), BigDecimal.valueOf(79), "Belarus"),
                new Food("kandy", List.of(Taste.SWEET), BigDecimal.valueOf(222), "Ukraine")
        );
    }

    public List<Food> getAllFood() {
        return foods;
    }
}
