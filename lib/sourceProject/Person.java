package sourceProject;

import java.util.HashSet;

public class Person {

    private String  name;

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

    public void addOwnedCar(Car c){
        ownedCars.add(c);
    }

    @Override
    public String toString(){
        return name + " has "+ownedCars.size() + " cars";
    }

    public String getName(){
        return name;
    }
}
