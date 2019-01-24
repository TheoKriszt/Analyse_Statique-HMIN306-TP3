package fr.kriszt.theo;

import fr.kriszt.theo.NodeEntities.*;
import fr.kriszt.theo.relations.MethodRelation;
import fr.kriszt.theo.relations.Relation;
import org.eclipse.jdt.core.dom.*;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Explore l'AST d'un fichier
 */
public class SourceCodeVisitor extends ASTVisitor{

    private ApplicationEntity application;

    private PackageEntity currentPackage;
    private TypeEntity currentType;
    private MethodEntity currentMethod;


    SourceCodeVisitor(String source, ApplicationEntity application){
        this.application = application;

        application.addLines( countLines(source, false) );
        application.addNonBlankLines( countLines(source, true) );
    }


    @Override
    public boolean visit(PackageDeclaration node) {
        currentPackage = new PackageEntity(node.getName().toString());
        application.addPackage( currentPackage );

        return super.visit(node);
    }


    public boolean visit(TypeDeclaration node) {

        String typeName = currentPackage.getName() + "." + node.getName().toString();
        System.out.println("Visiting type " + typeName);


        if (node.isInterface()) {
            InterfaceEntity  interfaceEntity = new InterfaceEntity(typeName);
            currentPackage.addInterface( interfaceEntity );
            currentType = interfaceEntity;
        }
        else {
            ClassEntity classEntity = new ClassEntity(typeName, currentPackage, node);
            currentPackage.addClass( classEntity );
            currentType = classEntity;

        }

        for (FieldDeclaration f : node.getFields()) {
            currentType.addAttribute( f.modifiers() + " " + f.getType() + " " +  ((VariableDeclarationFragment)f.fragments().get(0)).getName());
        }

        currentType.setLinesCount( countLines(node.toString(), false) );

        for (MethodDeclaration m : node.getMethods()) {

            MethodEntity methodEntity = new MethodEntity(m.getName().toString(), m, currentType);
            methodEntity.addParams(m.parameters());

            String returnType;
            if (m.isConstructor()){
                returnType = m.getName().toString();
            }else if ( m.getReturnType2() != null){
                returnType = m.getReturnType2().toString();
            } else returnType = "void";

            methodEntity.setReturnType(returnType);

            String methodBody = "";
            if (m.getBody() != null){
                methodBody = m.getBody().toString();
            }

            methodEntity.setLinesCount( countLines(methodBody, false ) );

            currentType.addMethod( methodEntity );

        }
        return true;
    }

    @Override
    public boolean visit(MethodDeclaration methodDeclaration) {
        ArrayList<MethodEntity> methods = new ArrayList<>(currentType.getMethods());
        currentMethod = null;

        MethodEntity methodEntity = new MethodEntity(methodDeclaration.getName().toString(), methodDeclaration, currentType);
        methodEntity.addParams(methodDeclaration.parameters());

        String returnType = "void";
        if (methodDeclaration.isConstructor()){
            returnType = methodDeclaration.getName().toString();
        }else if ( methodDeclaration.getReturnType2() != null){
            returnType = methodDeclaration.getReturnType2().toString();
        }

        methodEntity.setReturnType(returnType);

//        System.out.println("\t trying to fing method called " + methodEntity);

        for ( MethodEntity me : methods ){
//            System.out.println("\t\t comparing with " + me);
            if (me.toString().equals(methodEntity.toString())){
                currentMethod = me;
                break;

            }
        }
        return true;
    }

    public boolean visit(MethodInvocation methodInvocation) {
        System.out.println("-----------------------------------");
        System.out.println("Method invocation : " + methodInvocation);
        System.out.println("Current class : " + currentType);
        System.out.println("Current method : " + currentMethod);


        Expression expression = methodInvocation.getExpression();



        if (expression == null) {
            return true;
        }

        ITypeBinding typeBinding = expression.resolveTypeBinding();
        IMethodBinding methodBinding = methodInvocation.resolveMethodBinding();

        if (methodBinding == null){
            return true;
        }

        if (typeBinding != null && currentMethod != null) {
            if ( !currentType.toString().equals(typeBinding.getQualifiedName()) ){
                Relation rel = Relation.addRelation(currentType.toString(), typeBinding.getQualifiedName());
                rel.addMethod(methodBinding.toString(), currentType.toString());

                MethodRelation.addMethodRelation(currentMethod.toString(), currentType.toString(), methodBinding.toString(), typeBinding.getQualifiedName());
            }



        }

        return true;
    }

    /**
     * Compte le nombre de lignes au total
     * @param str chaîne d'entrée
     * @param removeBlankLines, retire les lignes vides si True
     */
    private static int countLines(String str, boolean removeBlankLines) {
        Matcher lineMatcher = Pattern.compile("\n").matcher(str);

        int lines = 1;
        while (lineMatcher.find())
        {
            lines ++;
        }


        if (removeBlankLines){
            Matcher emptyMatcher = Pattern.compile("^$|^\\s$", Pattern.MULTILINE).matcher(str);
            while (emptyMatcher.find())
            {
                lines --;
            }
        }


        return lines;
    }

}
