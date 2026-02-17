package com.inn.LLD.DesignPen;

public class Client {
    public static void main(String[] args) {
        Pen pen = new FountainPen("Fountain Pen", "Parker", new ClickStrategy());
        pen.write();
        pen.getClosingType().open();
        pen.getClosingType().close();

        RefillPens pen1 = new BallPen("Ball Point Pen", "Reynolds", new TwistStrategy());
        pen1.write();
        pen1.refill();
        pen1.getClosingType().open();
        pen1.getClosingType().close();

        pen1 = new GelPen("Gel Pen", "Pilot", new CapStrategy());
        pen1.write();
        pen1.refill(); 
        pen1.getClosingType().open();
        pen1.getClosingType().close();
    }

}
