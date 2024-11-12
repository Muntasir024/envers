package com.example.envers.service;

import com.example.envers.model.Product;
import com.example.envers.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    public Product findById(Integer id){
        return productRepository.findById(id).get();
    }
    public Product findBySku(String sku){
        return productRepository.findBySku(sku);
    }
    public List<Product> findAll(){
        return productRepository.findAll();
    }
    public Product save(Product product){
        return productRepository.save(product);
    }
}
