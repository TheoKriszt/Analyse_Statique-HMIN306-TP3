package com.company;

public class Main {

    public static void main(String[] args) {

        Person antho = new Person("Arthur");
        Person bob = new Person("Bob");
        Person charles = new Person("Charles");

        Car c1 = new Car(antho);
        Car c2 = new Car(antho);
        Car c3 = new Car(bob);
        Car c4 = new Car(charles);
        c2.setOwner(charles);
        c4.setOwner(bob);

//        HashSet<Person> everyone = Person.getAllPersons();

        for (Person p : Person.getAllPersons()){
            System.out.println(p);
        }
    }
}