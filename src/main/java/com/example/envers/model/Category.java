package com.example.envers.model;

import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.util.List;

@Audited
@Entity
public class Category {
    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator = "category_Sequence")
    @SequenceGenerator(name = "category_Sequence", sequenceName = "CATEGORY_SEQ")
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @ManyToMany
    @JoinTable(
            name="category_product"
    )
    List<Product> products;

    public String getName() {
        return name;
    }

    public Long getId() {
        return id;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProduct(List<Product> products) {
        this.products = products;
    }
}
