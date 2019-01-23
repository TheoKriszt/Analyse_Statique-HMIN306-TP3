package fr.kriszt.theo.spoon.main;

import fr.kriszt.theo.GraphX.SpoonMethodsGrapher;
import fr.kriszt.theo.NodeEntities.TypeEntity;

public class CallGraphBySpoon {

    public static String PROJECT_PATH = "lib/SimpleSample";

    public static void main( String[] args) {

        if (args.length > 0){
            PROJECT_PATH = args[0];
        }

        SpoonExample<Void> spoonInstance = new SpoonExample<>(PROJECT_PATH);
        spoonInstance.runScan();

        System.out.println("Types déclarés : " + TypeEntity.getDeclaredTypes());

        System.out.println("Entrées : " + spoonInstance.methodsCalls.keySet());
//        System.out.println("Tailles : " + spoonInstance.methodsCalls.values());

//        System.out.println(spoonInstance.classes);
        new SpoonMethodsGrapher( spoonInstance.classes, spoonInstance.methodsCalls);


    }
}
