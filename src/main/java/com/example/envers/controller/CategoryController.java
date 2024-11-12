package com.example.envers.controller;

import com.example.envers.model.Category;
import com.example.envers.model.EntityRev;
import com.example.envers.model.Product;
import com.example.envers.repository.GenericRevisionRepository;
import com.example.envers.service.CategoryService;
import com.example.envers.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ProductService productService;
    @Autowired
    private GenericRevisionRepository genericRevisionRepository;

    @RequestMapping(value = "/saveCategory", method = RequestMethod.PUT)
    public ResponseEntity<Category> saveCategory(@RequestBody Category category) {
        Category cat = categoryService.findByName(category.getName());
        if(cat != null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        else
            return new ResponseEntity<Category>(categoryService.save(category), HttpStatus.OK);
    }

    @RequestMapping(value = "/updateProduct/{sku}", method = RequestMethod.PUT)
    public ResponseEntity<Category> updateProduct(@PathVariable String sku, @RequestBody Category category) {
        Product product = productService.findBySku(sku);
        Category cat = categoryService.findByName(category.getName());
        cat.getProducts().add(product);
        categoryService.save(cat);
        return new ResponseEntity<Category>(cat, HttpStatus.OK);
    }

    @RequestMapping(value = "/deleteProduct/{sku}", method = RequestMethod.PUT)
    public ResponseEntity<Category> deleteProduct(@PathVariable String sku, @RequestBody Category category) {
        Product product = productService.findBySku(sku);
        Category cat = categoryService.findByName(category.getName());
        cat.getProducts().remove(product);
        categoryService.save(cat);
        return new ResponseEntity<Category>(cat, HttpStatus.OK);
    }

    @RequestMapping("/{name}")
    public ResponseEntity<Category> getCategoryByName(@PathVariable String name) {
        Category cat = categoryService.findByName(name);
        if(cat != null)
            return new ResponseEntity<Category>(cat, HttpStatus.OK);
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @RequestMapping("/revisions/{name}")
    public ResponseEntity<List<EntityRev<Category>>> getRevisions(@PathVariable String name) {
        Category cat = categoryService.findByName(name);
        List<EntityRev<Category>> revision = genericRevisionRepository.revisionList(cat.getId(), Category.class);
        if(revision != null)
            return new ResponseEntity<List<EntityRev<Category>>>(revision, HttpStatus.OK);
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
