package fr.kriszt.theo.NodeEntities;

import org.eclipse.jdt.core.dom.Type;

import java.util.ArrayList;
import java.util.HashSet;

public abstract class TypeEntity extends NodeEntity {

    private static HashSet<TypeEntity> declaredTypes = new HashSet<>();

    ArrayList<MethodEntity> methods = new ArrayList<>();
    private String superType = null;
    ArrayList<String> attributes = new ArrayList<>();

    TypeEntity(String n) {
        super(n);
        declaredTypes.add(this);
    }

    public void addMethod(MethodEntity m){
        methods.add(m);
    }

    public ArrayList<MethodEntity> getMethods() {
        return methods;
    }

    public void setSuperType(Type superclassType) {
        if (superclassType != null){
            superType = superclassType.getNodeType() + "";
        }

    }

    public void addAttribute(String attr){
        attributes.add(attr);
    }

}