package fr.kriszt.theo;

import java.util.HashSet;
import java.util.Set;

public class Relation {

    private String inputType;
    private String outputType;
    private int count;

    private static Set<Relation> allRelations = new HashSet<>();

    private Relation( String in, String out ){
        inputType = in;
        outputType = out;
        count = 1;
    }

    public static void addRelation( String type1, String type2 ){

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
        }else {
            allRelations.add(new Relation(type1, type2));
        }
    }

}
