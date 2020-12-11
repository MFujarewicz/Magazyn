package com.magazyn.database;

import com.magazyn.State;

import javax.persistence.*;
import java.util.List;

@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer ID;

    @ManyToOne
    @JoinColumn(name = "product_data_id")
    private ProductData productData;

    @Enumerated(EnumType.ORDINAL)
    private State state;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<Job> jobs;

    @OneToOne(mappedBy = "product")
    private ProductLocation productLocation;

    public Product(ProductData productData, State state) {
        this.productData = productData;
        this.state = state;
    }

    public Product() {
    }

    public Integer getID() {
        return ID;
    }

    public void setID(Integer id) {
        this.ID = id;
    }

    public ProductData getProductData() {
        return productData;
    }

    public void setProductData(ProductData productData) {
        this.productData = productData;
    }

    public List<Job> getJobs() {
        return jobs;
    }

    public void setJobs(List<Job> jobs) {
        this.jobs = jobs;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public ProductLocation getProductLocation() {
        return productLocation;
    }

    public void setProductLocation(ProductLocation productLocation) {
        this.productLocation = productLocation;
    }
}
