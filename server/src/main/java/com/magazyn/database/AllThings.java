package com.magazyn.database;

import com.magazyn.State;

import javax.persistence.*;

@Entity
@Table(name = "All_things")
public class AllThings {
    @Id
    private Integer id;

//    @OneToOne
//    @JoinColumn(name = "product_id")
//    private Product product;

//    @OneToOne(mappedBy = "product_id")
//    private Jobs jobs;

    @Enumerated(EnumType.ORDINAL)
    private State state;

    public AllThings(Product product, Jobs jobs, State state) {
//        this.product = product;
//        this.jobs = jobs;
        this.state = state;
    }

    public AllThings() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

//    public Product getProduct() {
//        return product;
//    }
//
//    public void setProduct(Product product) {
//        this.product = product;
//    }

//    public Jobs getJobs() {
//        return jobs;
//    }
//
//    public void setJobs(Jobs jobs) {
//        this.jobs = jobs;
//    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }
}
