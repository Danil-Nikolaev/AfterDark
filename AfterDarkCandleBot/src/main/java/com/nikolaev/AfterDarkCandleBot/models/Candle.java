package com.nikolaev.AfterDarkCandleBot.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;

@Entity
public class Candle {
    @Id
    @NotNull
    private long chatId;
    private long shapeId;
    private long colorShapeId;
    private long smellId;
    private long wickId;

    public Candle(long chatId) {
        this.chatId = chatId;
    }

    public Candle(long chatId, long shapeId, long colorShapeId, long smellId, long wickId) {
        this.chatId = chatId;
        this.shapeId = shapeId;
        this.colorShapeId = colorShapeId;
        this.smellId = smellId;
        this.wickId = wickId;
    }

    public Candle() {
    }

    public long getChatId() {
        return chatId;
    }

    public void setChatId(long chatId) {
        this.chatId = chatId;
    }

    public long getShapeId() {
        return shapeId;
    }

    public void setShapeId(long shapeId) {
        this.shapeId = shapeId;
    }

    public long getColorShapeId() {
        return colorShapeId;
    }

    public void setColorShapeId(long colorShapeId) {
        this.colorShapeId = colorShapeId;
    }

    public long getSmellId() {
        return smellId;
    }

    public void setSmellId(long smellId) {
        this.smellId = smellId;
    }

    public long getWickId() {
        return wickId;
    }

    public void setWickId(long wickId) {
        this.wickId = wickId;
    }

}
