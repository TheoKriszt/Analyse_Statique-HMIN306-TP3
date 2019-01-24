package com.company;

import java.io.IOException;
import java.util.HashSet;

public class Person {

    // TODO : accès interdit de l'exterieur : passer en private
    public static HashSet<Person> allPersons = new HashSet<>();

    private final String name;

    private HashSet<Car> myCars = new HashSet<>();

    public Person(String name){
        this.name = name;
        allPersons.add(this);
    }

    /**
     * Retourne la liste de toute sles personnes
     * @return allPersons la liste des personnes qui apparaissent dans le programme
     * TODO : empecher les effets de bord depuis une autre classe : ne renvoyer qu'une copie;
     */
    public static HashSet<Person> getAllPersons() {
        return allPersons;
    }

    public void addCar(Car c){
        try {
            myCars.add(c);
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

    public void removeCar(Car car) {
        myCars.remove(car);
    }
}
