package com.inn.LLD.DesignPen;

public abstract class RefillPens extends Pen implements Refillable {

    public RefillPens(String name, String brand, PenType penType, ClosingMechanism closingType) {
        super(name, brand, penType, closingType);
    }

    @Override
    public void refill() {
        System.out.println("Refilling the pen");
    }

    
}
