package com.kitchenapp.kitchenappapi.repository;

import com.kitchenapp.kitchenappapi.model.Category;
import com.kitchenapp.kitchenappapi.model.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, String> {

    Optional<Category> findByName(String name);
}