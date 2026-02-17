package com.inn.LLD.DesignPen;

public class BallPen extends RefillPens  {


    public BallPen(String name, String brand, ClosingMechanism closingType) {
        super(name, brand, PenType.BALL_POINT, closingType);
        super.setPrice(50);
    }


    @Override
    public void write() {
        System.out.println("Writing with ball pen with price : " + getPrice());
    }


}
