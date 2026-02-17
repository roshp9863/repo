package com.inn.LLD.DesignPen;

public class FountainPen extends Pen {

    public FountainPen(String name, String brand, ClosingMechanism closingType) {
        super(name, brand, PenType.FOUNTAIN, closingType);
        super.setPrice(100);
    }

    @Override
    public void write() {
        System.out.println("Writing with fountain pen with price : " + getPrice());
    }

}
