package fr.kriszt.theo.NodeEntities;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public abstract class TypeEntity extends NodeEntity {

    private static HashSet<TypeEntity> declaredTypes = new HashSet<>();

    HashSet<MethodEntity> methods = new HashSet<>();
    ArrayList<String> attributes = new ArrayList<>();
    private int linesCount = -1;

    TypeEntity(String n) {
        super(n);
        declaredTypes.add(this);
    }

    public static Set<TypeEntity> getDeclaredTypes() {
        return declaredTypes;
    }

    public void addMethod(MethodEntity m){
        methods.add(m);
    }

    public HashSet<MethodEntity> getMethods() {
        return methods;
    }

    public void addAttribute(String attr){
        attributes.add(attr);
    }

    public void setLinesCount(int countLines) {
        linesCount = countLines;
    }

    public int getLinesCount() {
        return linesCount;
    }
}