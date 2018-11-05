package fr.kriszt.theo.NodeEntities;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class MethodEntity  extends NodeEntity{

    public ArrayList<String> params = new ArrayList<>();
    public int countLines = 0;

    public HashSet<MethodEntity> calledMethods = new HashSet<>();
    private String returnType = "void";

    public MethodEntity(String n) {
        super(n);
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
}