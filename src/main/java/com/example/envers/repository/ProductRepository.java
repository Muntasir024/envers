package com.example.envers.repository;

import com.example.envers.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    Product findBySku(String sku);
}
