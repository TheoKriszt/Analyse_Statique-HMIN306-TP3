package fr.kriszt.theo.NodeEntities;

import org.eclipse.jdt.core.dom.Type;

import java.util.ArrayList;

public abstract class TypeEntity extends NodeEntity {

    protected ArrayList<MethodEntity> methods = new ArrayList<>();
    protected String superType = null;
    protected ArrayList<String> attributes = new ArrayList<>();

    public TypeEntity(String n) {
        super(n);
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