package fr.kriszt.theo.NodeEntities;

public class ClassEntity extends TypeEntity {
    public ClassEntity(String n) {
        super(n);
    }

    public boolean equals(Object o){
        return o instanceof ClassEntity && ((ClassEntity)o).getName().equals(getName());
    }
}
