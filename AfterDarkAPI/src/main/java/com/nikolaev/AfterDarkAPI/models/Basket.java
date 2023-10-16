package com.nikolaev.AfterDarkAPI.models;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "basket")
public class Basket {

    @Id
    @GeneratedValue
    private long id;

    @OneToOne
    private User user;

    @ManyToMany
    private List<Candle> candles;

    public Basket(long id, User user, List<Candle> candles) {
        this.id = id;
        this.user = user;
        this.candles = candles;
    }

    public Basket() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Candle> getCandles() {
        return candles;
    }

    public void setCandles(List<Candle> candles) {
        this.candles = candles;
    }

}
