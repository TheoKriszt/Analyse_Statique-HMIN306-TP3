package sourceProject;

public class ExampleMain {

    public static void main(String[] args) {
	    Person alain = new Person("Alain");
	    Person bob = new Person("Bob");
	    Person charles = new Person("Charles");

        Car c1 = new Car(alain);
        Car c2 = new Car(bob);
        Car c3 = new Car(charles);
        Car c4 = new Car(alain);
        Car c5 = new Car();

	    charles.buyCar(c1);
	    bob.buyCar(c4);

	    c5.setOwner(charles);

	    for (Person p : Person.getAllPersons()){
            System.out.println("Personne : " + p.toString());
        }
    }
}
