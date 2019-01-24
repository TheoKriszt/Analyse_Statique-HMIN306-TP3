package fr.kriszt.theo.relations;

import java.util.HashSet;
import java.util.Set;

public class MethodRelation {

    private String callingMethod;
    private String calledMethod;

    public String getCallingType() {
        return callingType;
    }

    public String getCalledType() {
        return calledType;
    }

    private String callingType;
    private String calledType;


    private static Set<MethodRelation> allRelations = new HashSet<>();


    private MethodRelation(String callingMethod, String callingClass, String calledMethod, String calledClass ){
        this.callingMethod = callingMethod;
        this.calledMethod = calledMethod;
        this.callingType = callingClass;
        this.calledType = calledClass;
    }

    public static MethodRelation addMethodRelation(String type1, String t1, String type2, String t2){


        MethodRelation targetRelation = null;
        for (MethodRelation r : allRelations){
            if (
                    (type1.equals(r.callingMethod) && type2.equals(r.calledMethod)) && r.callingType.equals(t1) && r.calledType.equals(t2)
            ){
                // Relation already exists
                targetRelation = r;
                break;
            }
        }

        if (targetRelation != null){
            return targetRelation;
        }else {
            targetRelation = new MethodRelation(type1, t1, type2, t2);
            allRelations.add(targetRelation);
            return targetRelation;
        }

    }

    public static Set<MethodRelation> getAllRelations() {
        return allRelations;
    }

    @Override
    public String toString(){
        return "(" + callingType + ") " + callingMethod + " ==> (" + calledType + ")" + calledMethod;// + " [" + count + " time" + (count > 1 ? "s" : "") + "]";
    }



    public String getCallingMethod() {
        return callingMethod;
    }

    public String getCalledMethod() {
        return calledMethod;
    }

}
