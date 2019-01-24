package fr.kriszt.theo.spoon.main;

import spoon.Launcher;
import spoon.compiler.Environment;
import spoon.reflect.CtModel;
import spoon.reflect.code.*;
import spoon.reflect.declaration.*;
import spoon.reflect.factory.CodeFactory;
import spoon.reflect.factory.CoreFactory;
import spoon.reflect.factory.Factory;
import spoon.reflect.factory.TypeFactory;
import spoon.reflect.visitor.Query;
import spoon.reflect.visitor.filter.*;
import spoon.support.reflect.code.CtInvocationImpl;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * Inspiré de https://www.youtube.com/watch?v=ZZzdVTIu-OY
 *
 * Utilise le programme d'exemple dans  lib/SimpleSample
 * Le programme exemple reprend des classes simples : Person.java, Car.java et Main.java
 *
 * Scénario :
 *  Lors d'une code review, vous vous rendez-compte qu'une faille dans le code permet à des classes non autorisées de modifier librement des personnes.
 *  Après inspection, il s'avère que l'attribut statique allPersons de la classe Person a été laissé public, malgré le fait qu'un getter existe déjà.
 *  On se rend compte au passage que le getter encapsule mal le champ et renvoie directement l'objet pointé par Person.allPersons.
 *
 *      Objectifs :
 *          - Passer le champ Person.allPersons en private
 *          - Altérer Person.getAllPersons pour qu'il retourne une copie de la liste privée de Personnes
 *          - Modifier dans le programme les accès en lecture du champ Person.allPersons pour appeler à la place le getter dorénavant sûr
 *
 */
public class SpoonTutorial<T>
{
    private final Factory factory;
    private CtModel model;

    /**
     * @param classFilePath Path to project files
     */
    private SpoonTutorial(String classFilePath)
    {
        Launcher launcher = new Launcher();
        launcher.addInputResource(classFilePath);
        model = launcher.buildModel();

        Environment env = launcher.getEnvironment();
        env.setCommentEnabled(true);
        env.setAutoImports(true);

        factory = launcher.getFactory();
        CodeFactory codeFactory = factory.Code();


    }

    /**
     *
     * @param classFilePath le chemin où stocker les fichiers générés
     * @param packageName // le nom du package de destination
     * @param fileContent // l'instance CtClass à enregistrer
     * @throws IOException
     */
    private void saveRefactoredFiles(String classFilePath, String packageName, CtClass fileContent) throws IOException
    {
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(classFilePath));
        bufferedWriter.write("package " + packageName + ";");
        bufferedWriter.write("\n\n");
        bufferedWriter.write(fileContent.toString());

        bufferedWriter.close();


    }


    public static void main(String[] Args) throws IOException
    {
        String packagePath = "lib/SimpleSample/company/src";
        String personFile = packagePath + "/Person.java";
        String mainFile = packagePath + "/Main.java";

        Files.deleteIfExists(Paths.get(personFile)); // nettoyer après une exécution précédente
        Files.deleteIfExists(Paths.get(mainFile));

        // -------------------  Fin préparations  --------------------------------

        // On créé une instance de Spoon, on donne le chemin à scanner
        SpoonTutorial<Void> spoonInstance = new SpoonTutorial<>(packagePath);


        // On récupère les références vers les classes Main et Person, on peut modifier ces objets puis les sauvegarder plus tard
        CtClass<?> mainClass = spoonInstance.model.getElements(new NamedElementFilter<>(CtClass.class,"Main")).get(0);
        CtClass<?> personClass = spoonInstance.model.getElements(new NamedElementFilter<>(CtClass.class,"Person")).get(0);

        // Référence vers le champ allPersons
        CtField allPersons = personClass.getField("allPersons");
        allPersons.setVisibility(ModifierKind.PRIVATE); // Objectif rempli : passer le champ allPersons à Private

        // Référence vers la méthode-accesseur getAllPersons()
        CtMethod getAllPersons = personClass.getMethod("getAllPersons");
        String returnWithCopy = "return allPersons.clone();"; // On prépare le nouveau corps de la méthod
        CtCodeSnippetStatement statement = spoonInstance.factory.createCodeSnippetStatement(returnWithCopy);
        getAllPersons.setBody(statement); // le nouvel accesseur ne renvoie pluq que des compies et protège la liste originelle

        CtInvocationImpl getAllPersonsInvocation = (CtInvocationImpl) mainClass.filterChildren(new InvocationFilter(getAllPersons)).list().get(0);

        List<CtFieldAccess<?>> accesses =
            Query.getElements(spoonInstance.factory, new FieldAccessFilter(allPersons.getReference())); // On repère les accès au champ allPersons

        // Dernier objectif : remplacer l'accès au champ allRelations par l'appel du getter sûr
        for (CtFieldAccess access : accesses){
            if (access instanceof CtFieldRead){
                CtFieldRead ct = (CtFieldRead) access;
                ct.replace(getAllPersonsInvocation);
                ct.getParent().addComment( spoonInstance.factory.createInlineComment("TODO : check this auto-generated patch !") );
            }
        }

        // ---------------------  Fin des modifications : renommer et sauvegarder les résultats ----------------------------------

        spoonInstance.saveRefactoredFiles(personFile, "com.company", personClass);
        spoonInstance.saveRefactoredFiles(mainFile, "com.company", mainClass);
    }


}
