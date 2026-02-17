package com.inn.dsa.practice;
import java.util.*;
import java.util.stream.Collectors;

class Animal {
    int id;
    String name;

    public Animal(int id, String name) {
        this.id =id;
        this.name=name;
    }

    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    @Override
        public String toString() {
            return "Animal{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    '}';
        }
    public static void main(String[] args) {
        Animal a = new Animal(1,"Lion");
        Animal b = new Animal(2,"tiger");
        Animal c = new Animal(3,"dog");
        Animal d = new Animal(4,"cat");
        Animal e = new Animal(5,"leopard");

        List<Animal> animals = new ArrayList<>();
        animals.add(a);
        animals.add(b);
        animals.add(c);
        animals.add(d);
        animals.add(e);
        
        Map<Integer, Animal> map = animals.stream().collect(Collectors.toMap(Animal::getId, animal->animal));

        System.out.println(map);

    }
}