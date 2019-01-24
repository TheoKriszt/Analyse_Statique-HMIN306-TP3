package fr.kriszt.theo.relations;

import fr.kriszt.theo.NodeEntities.TypeEntity;

import java.util.HashSet;
import java.util.Set;

public class Relation {

    private String inputType;
    private String outputType;
    private Set<String> inMethodsNames = new HashSet<>();
    private Set<String> outMethodsNames = new HashSet<>();
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
            inMethodsNames.add(mn);
        }else outMethodsNames.add(mn);
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

    int getCount() {
        return count;
    }


    public Set<String> getIncomingMethods() {
        return inMethodsNames;
    }

    public Set<String> getOutcomingMethods() {
        return outMethodsNames;
    }

    void setInType(String next) {
        if (outputType.equals(next))
            throw new IllegalArgumentException("Reference cyclique");
        this.inputType = next;
    }

    void setOutType(String next) {
        if (inputType.equals(next))
            throw new IllegalArgumentException("Reference cyclique");
        this.outputType = next;
    }

    @Override
    public boolean equals(Object o) {
        boolean res = o instanceof Relation &&
                inputType.equals(((Relation) o).inputType) &&
                outputType.equals(((Relation) o).outputType);
        return res;
    }
}
