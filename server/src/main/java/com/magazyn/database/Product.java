package com.magazyn.database;

import com.magazyn.State;

import javax.persistence.*;

@Entity
public class Product {
    @Id
    private Integer ID;

//    @OneToOne
//    @JoinColumn(name = "product_data_id")
//    private Product product;

//    @OneToOne(mappedBy = "product_id")
//    private Job jobs;

    @Enumerated(EnumType.ORDINAL)
    private State state;

    public Product(ProductData productData, Job job, State state) {
//        this.product = product;
//        this.job = job;
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

//    public Product getProduct() {
//        return product;
//    }
//
//    public void setProduct(Product product) {
//        this.product = product;
//    }

//    public Job getJobs() {
//        return jobs;
//    }
//
//    public void setJobs(Job jobs) {
//        this.jobs = jobs;
//    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }
}
