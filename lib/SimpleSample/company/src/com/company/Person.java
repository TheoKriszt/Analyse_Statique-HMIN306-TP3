package com.company;

import java.io.IOException;
import java.util.HashSet;

public class Person {

    private static HashSet<Person> allPersons = new HashSet<>();

    private final String name;

    private HashSet<Car> myCars = new HashSet<>();

    public Person(String name){
        this.name = name;
        allPersons.add(this);
    }

    public static HashSet<Person> getAllPersons() {
        return allPersons;
    }

    public void addCar(Car c){
        try {
            myCars.add(c);
            c.setOwner(this);
        }catch (Exception e ){
//            System.err.println("This try-catch should be useless");
        }

    }

    @Override
    public String toString(){
        String ret = name + " : \n";
        for (Car c : myCars){
            ret += "\t" + c + "\n";
        }

        return ret;
    }

    public String getName() {
        return name;
    }

    public void addCar(Car car) {
        myCars.add(car);
    }

    public void removeCar(Car car) {
        myCars.remove(car);
    }
}
