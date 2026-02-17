package com.inn.LLD.DesignPen;

public class ClickStrategy implements ClosingMechanism {

    @Override
    public void open() {
        System.out.println("Opening pen with click mechanism");
    }

    @Override
    public void close() {
        System.out.println("Closing pen with click mechanism");
    }

}
