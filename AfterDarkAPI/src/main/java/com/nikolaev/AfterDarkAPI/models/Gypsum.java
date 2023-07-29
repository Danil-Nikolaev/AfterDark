package com.nikolaev.AfterDarkAPI.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "Gypsum")
public class Gypsum {

    @Id
    @GeneratedValue
    private long id;
    private String name;
    private String description;
    private int price;
    private int quanity;

    public Gypsum() {
    }

    public Gypsum(long id, String name, String description, int price, int quanity) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.quanity = quanity;
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

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getQuanity() {
        return quanity;
    }

    public void setQuanity(int quanity) {
        this.quanity = quanity;
    }

}
