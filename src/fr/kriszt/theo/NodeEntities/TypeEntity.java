package fr.kriszt.theo.NodeEntities;

import fr.kriszt.theo.visitors.TypeSpecifier;

import java.util.ArrayList;

public class TypeEntity extends NodeEntity {

    public ArrayList<MethodEntity> methods= new ArrayList<>();

    public TypeEntity(String n, TypeSpecifier t) {
        super(n);
    }
}