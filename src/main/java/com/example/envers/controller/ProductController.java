package com.example.envers.controller;

import com.example.envers.model.EntityRev;
import com.example.envers.model.Product;
import com.example.envers.repository.GenericRevisionRepository;
import com.example.envers.service.ProductService;
import com.example.envers.utill.AuditContext;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/product")
public class ProductController {
    private static final Logger log = LoggerFactory.getLogger(ProductController.class);
    @Autowired
    private ProductService productService;
    @Autowired
    private GenericRevisionRepository genericRevisionRepository;

    @RequestMapping(value = "/saveProduct/{username}", method = RequestMethod.POST)
    public ResponseEntity<Product> saveProduct(@RequestBody Product product, @PathVariable String username) {
        Product productObject = productService.findBySku(product.getSku());
        if(productObject != null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        else {
            AuditContext.setUsername(username);
            Authentication auth = new UsernamePasswordAuthenticationToken(username, null, null);
            SecurityContextHolder.getContext().setAuthentication(auth);

            return new ResponseEntity<>(productService.save(product), HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/updateProduct/{username}", method = RequestMethod.PUT)
    public ResponseEntity<Product> updateProduct(@RequestBody Product product, @PathVariable String username) {
        Product productObject = productService.findBySku(product.getSku());
        if(productObject.getQuantity() != null)
            productObject.setQuantity(product.getQuantity());
        AuditContext.setUsername(username);
        Authentication auth = new UsernamePasswordAuthenticationToken(username, null, null);
        SecurityContextHolder.getContext().setAuthentication(auth);
        return new ResponseEntity<>(productService.save(productObject), HttpStatus.OK);
    }

    @RequestMapping(value = "/updatePrice", method = RequestMethod.PUT)
    public ResponseEntity<Product> updatePrice(@RequestBody Product product) {
        Product productObject = productService.findBySku(product.getSku());
        if(product.getPrice() != null)
            productObject.setPrice(product.getPrice());
        return new ResponseEntity<Product>(productService.save(productObject), HttpStatus.OK);
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ResponseEntity<List<Product>> list() {
        log.info("get all called");
        List<Product> p = productService.findAll();
        if(p != null)
            return new ResponseEntity<List<Product>>(p, HttpStatus.OK);
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @RequestMapping(value = "/{sku}", method = RequestMethod.GET)
    public ResponseEntity<Product> getListBySku(@PathVariable String sku) {
        Product p = productService.findBySku(sku);
        if(p != null)
            return new ResponseEntity<Product>(p, HttpStatus.OK);
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @RequestMapping("/revisionList/{sku}")
    public ResponseEntity<List<EntityRev<Product>>> getRevisionsBySku(@PathVariable String sku) {
        Product p = productService.findBySku(sku);
        List<EntityRev<Product>> revisionList = genericRevisionRepository.revisionList(p.getId(), Product.class);
        if(revisionList != null)
            return new ResponseEntity<List<EntityRev<Product>>>(revisionList, HttpStatus.OK);
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @RequestMapping("/displayAllProductAudits")
    public void displayAllProductAudits() {
        List<Object[]> audits = genericRevisionRepository.getAllProductAudits();

        for (Object[] audit : audits) {
            Product product = (Product) audit[0];
            Object revisionInfo = audit[1];
            String revisionType = audit[2].toString();  // Type of revision (ADD, MOD, DEL)

            System.out.println("Product at Revision: " + product);
            System.out.println("Revision Info: " + revisionInfo);
            System.out.println("Revision Type: " + revisionType);
            System.out.println("---------------------------------");
        }
//        return genericRevisionRepository.getAllProductAudits();
    }
}
