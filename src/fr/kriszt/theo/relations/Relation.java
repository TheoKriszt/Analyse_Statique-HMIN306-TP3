package fr.kriszt.theo.relations;

import fr.kriszt.theo.NodeEntities.TypeEntity;

import java.util.HashSet;
import java.util.Set;

public class Relation {

    private String inputType;
    private String outputType;
    private Set<String> inmethodsNames = new HashSet<>();
    private Set<String> outmethodsNames = new HashSet<>();
    private int count;

    private static Set<Relation> allRelations = new HashSet<>();

    public Relation( String in, String out ){
        if (in.compareTo(out) < 0){
            inputType = in;
            outputType = out;
        } else {
            inputType = out;
            outputType = in;
        }

        count = 1;
    }

    public Relation(String a, String b, int i) {
        this(a, b);
        count = i;
    }

    public static void setAllRelations(Set<Relation> aGarder) {
        allRelations = aGarder;
    }

    public void addMethod(String mn, String callingType){
        if (callingType.equals(inputType)){
            inmethodsNames.add(mn);
        }else outmethodsNames.add(mn);
    }

    public static Relation addRelation(String type1, String type2 ){

        Relation targetRelation = null;
        for (Relation r : allRelations){
            if (
                    (type1.equals(r.inputType) && type2.equals(r.outputType)) ||
                    (type1.equals(r.outputType) && type2.equals(r.inputType))
            ){
                // Relation already exists
                targetRelation = r;
                break;
            }
        }

        if (targetRelation != null){
            targetRelation.count++;
            return targetRelation;
        }else {
            targetRelation = new Relation(type1, type2);
            allRelations.add(targetRelation);
            return targetRelation;
        }

    }

    public static Set<Relation> getAllRelations() {
        return allRelations;
    }

    @Override
    public String toString(){
        return inputType + " ==> " + outputType + " [" + count + " time" + (count > 1 ? "s" : "") + "]";
    }

    public static void filterOutsideRelations() {
        Set<TypeEntity> declaredTypes = TypeEntity.getDeclaredTypes();
        Set<Relation> internalRelations = new HashSet<>();

        for (Relation r : allRelations){
            for (TypeEntity te : declaredTypes){
                if (te.toString().equals(r.outputType)){
                    internalRelations.add(r);
                }
            }
        }

        allRelations = internalRelations;
    }


    public String getInputType() {
        return inputType;
    }

    public String getOutputType() {
        return outputType;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int c) {
        count = c;
    }

    public Set<String> getIncomingMethods() {
        return inmethodsNames;
    }

    public Set<String> getOutcomingMethods() {
        return outmethodsNames;
    }

    public void setInType(String next) {
        this.inputType = next;
    }

    public void setOutType(String next) {
        this.outputType = next;
    }

    @Override
    public boolean equals(Object o) {
//        System.err.println("is " + this + " equal to " + o + " ? ");
        boolean res = o instanceof Relation &&
                inputType.equals(((Relation) o).inputType) &&
                outputType.equals(((Relation) o).outputType);
//        System.err.println(res ? "Yes" : "No");
        return res;
    }
}
