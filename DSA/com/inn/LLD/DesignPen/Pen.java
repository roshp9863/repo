package com.inn.LLD.DesignPen;

public abstract class Pen {

    private String name;
    private int price;
    private String brand;
    private PenType penType;
    private ClosingMechanism closingType;
    public Pen(String name, String brand, PenType penType, ClosingMechanism closingType) {
        this.name = name;
        this.brand = brand;
        this.penType = penType;
        this.closingType = closingType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public PenType getPenType() {
        return penType;
    }

    public void setPenType(PenType penType) {
        this.penType = penType;
    }

    public ClosingMechanism getClosingType() {
        return closingType;
    }

    public void setClosingType(ClosingMechanism closingType) {
        this.closingType = closingType;
    }

    public abstract void write();


}
