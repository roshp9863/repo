package com.inn.LLD.DesignPen;

public class TwistStrategy implements ClosingMechanism {

    @Override
    public void open() {
        System.out.println("Twist to open the pen");
    }

    @Override
    public void close() {
        System.out.println("Twist to close the pen");
    }

}
