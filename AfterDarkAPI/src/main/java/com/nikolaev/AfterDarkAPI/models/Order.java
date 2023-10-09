package com.nikolaev.AfterDarkAPI.models;

import java.util.List;


import com.nikolaev.AfterDarkAPI.enummuration.PaymentMethod;
import com.nikolaev.AfterDarkAPI.enummuration.PurchaseService;
import com.nikolaev.AfterDarkAPI.enummuration.StageOfWork;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "orders") 
public class Order {

    @Id
    @GeneratedValue
    private long id;

    private PurchaseService purchaseService;
    private PaymentMethod paymentMethod;
    private StageOfWork stageOfWork;
    private String communication;
    private String address;
    private boolean paid;
    private String dateOfPurchase;
    private String dateOfDelivery;
    private int price;

    @OneToMany
    private List<Candle> candles;

    @ManyToOne
    private User user;

    public Order(long id, PurchaseService purchaseService, PaymentMethod paymentMethod, String communication,
            String address, List<Candle> candles, String dateOfPurchase, String dateOfDelivery, StageOfWork stageOfWork,
            int price, User user) {
        this.id = id;
        this.purchaseService = purchaseService;
        this.paymentMethod = paymentMethod;
        this.communication = communication;
        this.address = address;
        this.candles = candles;
        this.dateOfPurchase = dateOfPurchase;
        this.dateOfDelivery = dateOfDelivery;
        this.stageOfWork = stageOfWork;
        this.price = price;
        this.user = user;
    }

    public Order() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public PurchaseService getPurchaseService() {
        return purchaseService;
    }

    public void setPurchaseService(PurchaseService purchaseService) {
        this.purchaseService = purchaseService;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getCommunication() {
        return communication;
    }

    public void setCommunication(String communication) {
        this.communication = communication;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<Candle> getCandles() {
        return candles;
    }

    public void setCandles(List<Candle> candles) {
        this.candles = candles;
    }

    public String getDateOfPurchase() {
        return dateOfPurchase;
    }

    public void setDateOfPurchase(String dateOfPurchase) {
        this.dateOfPurchase = dateOfPurchase;
    }

    public String getDateOfDelivery() {
        return dateOfDelivery;
    }

    public void setDateOfDelivery(String dateOfDelivery) {
        this.dateOfDelivery = dateOfDelivery;
    }

    public StageOfWork getStageOfWork() {
        return stageOfWork;
    }

    public void setStageOfWork(StageOfWork stageOfWork) {
        this.stageOfWork = stageOfWork;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public boolean isPaid() {
        return paid;
    }

    public void setPaid(boolean paid) {
        this.paid = paid;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}
