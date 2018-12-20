package com.company;

import java.util.HashSet;

public class Person {

    private static HashSet<Person> allPersons = new HashSet<>();

    private final String name;

    private HashSet<com.company.Car> myCars = new HashSet<>();

    public Person(String name){
        this.name = name;
        allPersons.add(this);
    }

    public static HashSet<Person> getAllPersons() {
        return allPersons;
    }

    public void adddCar(com.company.Car c){
        myCars.add(c);
        c.setOwner(this);
    }

    @Override
    public String toString(){
        String ret = name + " : \n";
        for (com.company.Car c : myCars){
            ret += "\t" + c + "\n";
        }

        return ret;
    }

    public String getName() {
        return name;
    }

    public void addCar(com.company.Car car) {
        myCars.add(car);
    }

    public void removeCar(com.company.Car car) {
        myCars.remove(car);
    }
}
