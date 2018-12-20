package fr.kriszt.theo.spoon.model.modified;

public class Person {
    private String city = "";

    private int age;

    private String name;

    public Person(int age, String name, String city) {
        super();
        this.age = age;
        this.name = name;
        this.city = city;;
    }

    @Override
    public String toString() {
        return "Person : "
        + "city : " + city
        + "age : " + age
        + "name : " + name
        ;
    }

    public void newMethod() {
        System.out.println("New method");
    }
}