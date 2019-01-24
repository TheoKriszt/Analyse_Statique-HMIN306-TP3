package fr.kriszt.theo.NodeEntities;

import org.eclipse.jdt.core.dom.TypeDeclaration;

import java.util.ArrayList;

public class ClassEntity extends TypeEntity {

    private TypeDeclaration typeDeclaration;

    private PackageEntity packageDeclaration;


    public ClassEntity(String n, PackageEntity currentPackage, TypeDeclaration node) {
        super(n);
        packageDeclaration = currentPackage;
        typeDeclaration = node;
    }

    public boolean equals(Object o){
        return o instanceof ClassEntity && ((ClassEntity)o).getName().equals(getName());
    }

}
