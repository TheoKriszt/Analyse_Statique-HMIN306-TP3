package fr.kriszt.theo;

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

    private Relation( String in, String out ){
        inputType = in;
        outputType = out;
        count = 1;
    }

    public static boolean typeHasRelations(TypeEntity te) {

        for (Relation r : allRelations){
            if (te.toString().equals(r.inputType) || te.toString().equals(r.outputType)){
                return true;
            }
        }

        return false;
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

    static void filterOutsideRelations() {
        Set<TypeEntity> declaredTypes = TypeEntity.getDeclaredTypes();
        Set<Relation> internalRelations = new HashSet<>();

        for (Relation r : allRelations){
            if (! r.inputType.equals(r.outputType)) {
                for (TypeEntity te : declaredTypes){

                    if (te.toString().equals(r.outputType)){
                        internalRelations.add(r);
                    }
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

    public Set<String> getIncomingMethods() {
        return inmethodsNames;
    }

    public Set<String> getOutcomingMethods() {
        return outmethodsNames;
    }
}
