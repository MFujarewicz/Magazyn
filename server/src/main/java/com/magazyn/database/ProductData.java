package com.magazyn.database;

import javax.persistence.*;

@Entity
public class ProductData {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer ID;

    private String name;

    private double weight;

    @ManyToOne
    @JoinColumn(name = "ID_type")
    private Type type;

//    @OneToOne(mappedBy = "all_things")
//    private Product allThings;

    public ProductData(String name, double weight, Type type) {
        this.name = name;
        this.weight = weight;
        this.type = type;
    }


    public ProductData() {
    }

    public Integer getID() {
        return ID;
    }

    public void setID(Integer ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

//    public Product getAllThings() {
//        return allThings;
//    }
//
//    public void setAllThings(Product allThings) {
//        this.allThings = allThings;
//    }
}
