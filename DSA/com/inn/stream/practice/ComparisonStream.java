package com.inn.stream.practice;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ComparisonStream {

    public static void main(String[] args) {

        List<Car> cars = List.of(
                new Car("BMW", 50000, 250),
                new Car("Audi", 50000, 240),
                new Car("Tata", 20000, 180),
                new Car("Hyundai", 20000, 190));

        List<Car> sorted = cars.stream()
                .sorted(Comparator
                        .comparingInt((Car c) -> c.price) // first by price
                        .thenComparing(Comparator.comparingInt(c -> -c.speed)) // then by speed descending
                )
                .collect(Collectors.toList());

        sorted.forEach(System.out::println);

    }

}

class Car {
    String name;
    int price;
    int speed;

    Car(String name, int price, int speed) {
        this.name = name;
        this.price = price;
        this.speed = speed;
    }

    @Override
    public String toString() {
        return name + " - ₹" + price + ", " + speed + " km/h";
    }
}
