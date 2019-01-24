package fr.kriszt.theo.spoon.main;

import fr.kriszt.theo.GraphX.SpoonMethodsGrapher;

import java.util.Set;

public class CallGraphBySpoon {

    public static String PROJECT_PATH = "lib/SimpleSample/company/src/com/company";

    public static void main( String[] args) {

        if (args.length > 0){
            PROJECT_PATH = args[0];
        }

        SpoonCallRecognizer<Void> spoonInstance = new SpoonCallRecognizer<>(PROJECT_PATH);
        spoonInstance.runScan();

        System.out.println("Types déclarés : " + spoonInstance.classes);

        System.out.println("Appels : \n");

        for (String k : spoonInstance.methodsCalls.keySet()){
            Set<String> callees = spoonInstance.methodsCalls.get(k);
            System.out.println("\t" + k);
            for (String v : callees){
                System.out.println("\t\t" + v);
            }
        }

        new SpoonMethodsGrapher( spoonInstance.classes, spoonInstance.methodsCalls);


    }
}
