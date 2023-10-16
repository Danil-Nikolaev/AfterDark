package com.nikolaev.AfterDarkAPI.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "candle")
public class Candle {

    public Candle(long id, String name, String description, int quanity, int price, boolean custom,
            ColorShape colorShape, Smell smell, Wax wax, Wick wick, Shape shape) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.quanity = quanity;
        this.price = price;
        this.custom = custom;
        this.colorShape = colorShape;
        this.smell = smell;
        this.wax = wax;
        this.wick = wick;
        this.shape = shape;
    }

    public Candle() {
    }

    @Id
    @GeneratedValue
    private long id;

    private String name;
    private String description;
    private int quanity;
    private int price;
    private boolean custom;

    @ManyToOne
    @JoinColumn(name = "color_shape_id")
    private ColorShape colorShape;

    @ManyToOne
    @JoinColumn(name = "smell_id")
    private Smell smell;

    @ManyToOne
    @JoinColumn(name = "wax_id")
    private Wax wax;

    @ManyToOne
    @JoinColumn(name = "wick_id")
    private Wick wick;

    @ManyToOne
    @JoinColumn(name = "shape_id")
    private Shape shape;

    @Override
    public boolean equals(Object obj) {
        Candle candle = (Candle) obj;

        long smellId = candle.getSmell().getId();
        long shapeId = candle.getShape().getId();
        long colorShapeId = candle.getColorShape().getId();
        long wickId = candle.getWick().getId();

        long thisSmellId = this.getSmell().getId();
        long thisShapeId = this.getShape().getId();
        long thisColorShapeId = this.getColorShape().getId();
        long thisWickId = this.getWick().getId();

        if (thisSmellId == smellId && thisShapeId == shapeId && thisColorShapeId == colorShapeId
                && thisWickId == wickId) {
                    return true;
        }

        return false;
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

    public boolean isCustom() {
        return custom;
    }

    public void setCustom(boolean custom) {
        this.custom = custom;
    }

    public ColorShape getColorShape() {
        return colorShape;
    }

    public void setColorShape(ColorShape colorShape) {
        this.colorShape = colorShape;
    }

    public Smell getSmell() {
        return smell;
    }

    public void setSmell(Smell smell) {
        this.smell = smell;
    }

    public Wax getWax() {
        return wax;
    }

    public void setWax(Wax wax) {
        this.wax = wax;
    }

    public Wick getWick() {
        return wick;
    }

    public void setWick(Wick wick) {
        this.wick = wick;
    }

    public Shape getShape() {
        return shape;
    }

    public void setShape(Shape shape) {
        this.shape = shape;
    }
}
