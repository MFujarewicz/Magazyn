package com.magazyn.database;

import javax.persistence.*;
import java.util.List;

@Entity
public class Type {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String name;

    @OneToMany(mappedBy = "type")
    private List<Product> products;

    public Type(String name) {
        this.name = name;
    }

    public Type() {
    }

    public Integer getId(){
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
}
