package com.magazyn.database;

import javax.persistence.*;

@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String name;

    private double weight;

    @ManyToOne
    @JoinColumn(name = "type_id")
    private Type type;

//    @OneToOne(mappedBy = "all_things")
//    private AllThings allThings;

    public Product(String name, double weight, Type type) {
        this.name = name;
        this.weight = weight;
        this.type = type;
    }


    public Product() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWeight() {
        return name;
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

//    public AllThings getAllThings() {
//        return allThings;
//    }
//
//    public void setAllThings(AllThings allThings) {
//        this.allThings = allThings;
//    }
}
