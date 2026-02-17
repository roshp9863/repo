package com.inn.LLD.DesignPen;


public class GelPen extends RefillPens  {


    public GelPen(String name, String brand, ClosingMechanism closingMechanism) {
        super(name, brand, PenType.GEL, closingMechanism);
        super.setPrice(70);
    }


    @Override
    public void write() {
        System.out.println("Writing with gel pen with price : " + getPrice() );
    }





}
