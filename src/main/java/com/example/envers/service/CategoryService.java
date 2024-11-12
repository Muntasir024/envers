package com.example.envers.service;

import com.example.envers.model.Category;
import com.example.envers.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    public Category findById(Integer id){
        return categoryRepository.findById(id).get();
    }
    public Category findByName(String name){
        return categoryRepository.findByName(name);
    }
    public Category save(Category category){
        return categoryRepository.save(category);
    }
}
