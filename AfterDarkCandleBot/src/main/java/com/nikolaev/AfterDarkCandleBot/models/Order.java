package com.nikolaev.AfterDarkCandleBot.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;

@Entity(name = "order_bot")
public class Order {

    @Id
    @NotNull
    long chatId;
    String name;
    String phone;
    String address;

    public Order() {}
    

    public Order(@NotNull long chatId, String name, String phone, String address) {
        this.chatId = chatId;
        this.name = name;
        this.phone = phone;
        this.address = address;
    }


    public Order(long chatId) {
        this.chatId = chatId;
    }

    public long getChatId() {
        return chatId;
    }

    public void setChatId(long chatId) {
        this.chatId = chatId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

}
