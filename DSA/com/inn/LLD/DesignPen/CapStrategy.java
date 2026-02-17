package com.inn.LLD.DesignPen;

public class CapStrategy implements ClosingMechanism {

    @Override
    public void open() {
        System.out.println("Opening the cap");
    }

    @Override
    public void close() {
        System.out.println("Closing the cap");
    }

}
