package fr.kriszt.theo.NodeEntities;

import org.eclipse.jdt.core.dom.MethodDeclaration;

import java.util.*;

public class MethodEntity  extends NodeEntity{

    ArrayList<String> params = new ArrayList<>();
    private MethodDeclaration methodDeclaration = null;
    private TypeEntity typeEntity;
    int countLines = 0;

    public HashSet<MethodEntity> calledMethods = new HashSet<>();
    private String returnType = "void";
    private HashMap<MethodInvocationEntity, Integer> invocations = new HashMap<>();


    private MethodEntity(String toString, TypeEntity currentType) {
        super(toString);
        this.typeEntity = currentType;
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
        return returnType + " "  + typeEntity + "." +  super.toString() + "(" + getParams() + ")";
    }

    private String getParams() {
        if (methodDeclaration == null) return "";
//        System.out.println("Params : " + methodDeclaration.parameters().toString().replace("\\[|]", ""));
        return methodDeclaration.parameters().toString().replaceAll("\\[|]", "");

    }

    void addInvocation(MethodInvocationEntity methodInvocationEntity) {
        if ( ! invocations.containsKey(methodInvocationEntity) ){
            invocations.put(methodInvocationEntity, 0);
        }else {
            invocations.put(methodInvocationEntity,
                    invocations.get(methodInvocationEntity) + 1
                    );
        }
    }
}