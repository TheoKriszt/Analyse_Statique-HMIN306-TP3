package com.company;

public class Main {

    @SuppressWarnings("Duplicates")
    public static void main(String[] args) {

        Person antho = new Person("Arthur");
        Person bob = new Person("Bob");
        Person charles = new Person("Charles");

        System.out.println("Avant les achats de voitures :");
        for (Person p :  Person.allPersons){
            System.out.println(p);
        }

        Car c1 = new Car(antho);
        Car c2 = new Car(antho);
        Car c3 = new Car(bob);
        Car c4 = new Car(charles);

        System.out.println("Après les achats de voitures :");
        for (Person p :  Person.allPersons){
            System.out.println(p);
        }

        c2.setOwner(charles);
        c4.setOwner(bob);

        System.out.println("Après les transferts de voitures :");
        for (Person p :  Person.allPersons){
            System.out.println(p);
        }

        System.out.println(Person.getAllPersons());
    }
}