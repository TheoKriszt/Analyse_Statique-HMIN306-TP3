package sourceProject;

import java.util.HashSet;
import java.util.Set;

public class Car {

    public static Set<Car> allCars = new HashSet<>();

    private Person owner = null;

    public Car(){
        allCars.add(this);
    }

    public Car(Person p){
        this();
        setOwner(p);
    }

    public void setOwner(Person o){
        owner = o;
        owner.addOwnedCar(this);
    }

}
