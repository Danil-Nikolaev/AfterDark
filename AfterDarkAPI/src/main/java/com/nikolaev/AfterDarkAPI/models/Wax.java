package com.nikolaev.AfterDarkAPI.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "Wax")
public class Wax {
    
    @Id
    @GeneratedValue
    private long id;

    private String name;
    private String description;
    private int quanity;
    private int price;

    public Wax() {}

    public Wax(long id, String name, String description, int quanity, int price) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.quanity = quanity;
        this.price = price;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getQuanity() {
        return quanity;
    }

    public void setQuanity(int quanity) {
        this.quanity = quanity;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

}
