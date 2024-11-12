package com.example.envers.repository;

import com.example.envers.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
    public Category findByName(String name);
}
