package fr.kriszt.theo.NodeEntities;

import org.eclipse.jdt.core.dom.MethodDeclaration;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class MethodEntity  extends NodeEntity{

    public ArrayList<String> params = new ArrayList<>();
    private MethodDeclaration methodDeclaration = null;
    private TypeEntity typeEntity = null;
    public int countLines = 0;

    public HashSet<MethodEntity> calledMethods = new HashSet<>();
    private String returnType = "void";

    public MethodEntity(String n) {
        super(n);

    }

    public MethodEntity(String toString, TypeEntity currentType) {
        super(toString);
        this.typeEntity = currentType;
//        System.out.println("Methode " + this);

    }

    public MethodEntity(String toString, MethodDeclaration m, TypeEntity currentType) {
        this(toString, currentType);
        this.methodDeclaration = m;
    }

    public void setReturnType(String returnType2) {
        this.returnType = returnType2;

    }

    public void addParams(List parameters) {

        for (Object o : parameters){
            params.add(o.toString());
        }

    }

    public void setLinesCount(int countLines) {
        this.countLines = countLines;

    }

    @Override
    public String toString(){
        return typeEntity + "." +  super.toString();
    }
}