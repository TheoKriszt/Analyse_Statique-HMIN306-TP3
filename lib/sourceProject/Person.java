package fr.kriszt.theo;

import java.util.HashSet;

public class Person {

    public static HashSet<Person> allPersons = new HashSet<>();

    private HashSet<Car> ownedCars = new HashSet<Car>();

    public Person(){
        allPersons.add(this);
    }

    public void buyCar(Car c){
        c.setOwner(this);
        ownedCars.add(c);
    }

    public static Set<Person> getAllPersons(){
        return allPersons;

    }
}
