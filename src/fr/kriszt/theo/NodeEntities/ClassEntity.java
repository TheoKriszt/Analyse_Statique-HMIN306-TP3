package fr.kriszt.theo.NodeEntities;

import org.eclipse.jdt.core.dom.PackageDeclaration;

public class ClassEntity extends TypeEntity {
    public ClassEntity(String n) {
        super(n);
//        System.out.println("Classe " + n);
    }

    public ClassEntity(String n, PackageDeclaration p){
        this(n);
    }

    public boolean equals(Object o){
        return o instanceof ClassEntity && ((ClassEntity)o).getName().equals(getName());
    }
}
