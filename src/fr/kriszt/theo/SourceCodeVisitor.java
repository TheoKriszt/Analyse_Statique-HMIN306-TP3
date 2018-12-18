package fr.kriszt.theo;

import fr.kriszt.theo.NodeEntities.*;
import org.eclipse.jdt.core.dom.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Explore l'AST d'un fichier
 */
public class SourceCodeVisitor extends ASTVisitor{

    private ApplicationEntity application;

    private PackageEntity currentPackage;
    private TypeEntity currentType;



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

        currentType.setSuperType( node.getSuperclassType() );
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

    public boolean visit(MethodInvocation methodInvocation) {
        System.out.println("Method invocation : " + methodInvocation);

        Expression expression = methodInvocation.getExpression();



        if (expression == null) {
            return true;
        }

        ITypeBinding typeBinding = expression.resolveTypeBinding();
        IMethodBinding methodBinding = methodInvocation.resolveMethodBinding();
//        System.out.println("Binding : " + methodBinding);

        if (methodBinding == null){
//            System.err.println("methodBinding for " + methodInvocation + " is null");
            return true;
        }

        if (typeBinding != null) {
            System.out.println("Appel : " + currentType + " ==> " + typeBinding.getQualifiedName());



            Relation rel = Relation.addRelation(currentType.toString(), typeBinding.getQualifiedName());
            rel.addMethod(methodBinding.toString(), currentType.toString());

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
