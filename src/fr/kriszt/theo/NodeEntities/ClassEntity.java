package fr.kriszt.theo.NodeEntities;

import org.eclipse.jdt.core.dom.TypeDeclaration;

import java.util.ArrayList;

public class ClassEntity extends TypeEntity {

    private TypeDeclaration typeDeclaration;

    private PackageEntity packageDeclaration;

//    @Override
//    public ArrayList<MethodEntity> getMethods() {
//        return methods;
//    }

    private ArrayList<MethodEntity> methods = new ArrayList<>();

    public ClassEntity(String n, PackageEntity currentPackage, TypeDeclaration node) {
        super(n);
        packageDeclaration = currentPackage;
        typeDeclaration = node;
    }

    public boolean matchDeclaration(TypeDeclaration declaration){
        return declaration.equals(typeDeclaration);
    }

//    public void addMethod(MethodEntity me){
//        methods.add(me);
//    }

    public boolean equals(Object o){
        return o instanceof ClassEntity && ((ClassEntity)o).getName().equals(getName());
    }

    public PackageEntity getPackageDeclaration() {
        return packageDeclaration;
    }
}
