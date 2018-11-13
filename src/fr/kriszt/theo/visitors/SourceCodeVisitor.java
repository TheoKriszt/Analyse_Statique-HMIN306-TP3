package fr.kriszt.theo.visitors;

import fr.kriszt.theo.NodeEntities.*;
import org.eclipse.jdt.core.dom.*;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Explore l'AST d'un fichier
 */
public class SourceCodeVisitor extends ASTVisitor{

    public static final String METHODS_KEY = "methods";
    public static final String LINES_NB_KEY = "lines number";
    public static final String ATTRIBUTES_KEY = "methods";
    public static final String PARAMETERS_KEY = "methods";

    private ApplicationEntity application;
    private Set declaredNames = new HashSet();
    private Set declaredPackages = new HashSet();
    private TreeMap<String, HashMap<String, Integer>> declaredClasses = new TreeMap<>();
    private static int totalLines = 0;
    private static int nonBlankLines = 0;

//    private static NodeEntity application = new NodeEntity("");

    private PackageEntity currentPackage;

    // METHOD


    /**
     *  1. Nombre de classes dans l'application : 0  => DECLAREDcLASSES
     *  2. Nombre de lignes de code dans l'application : 105 => totalLines
     *  3. Nombre total de méthodes de l'application : 0 => classes.methods
     *  4. Nombre total de packages dans l'application : packagesCount
     *  5. Nombre moyen de méthodes par classe : ---
     *  6. Nombre moyen de lignes de codes par méthode : methods.lines
     *  7. Nombre moyen d'attributs par classe : classes.attributes
     *  8. Les 10% des classes qui possèdent le plus grand nombre de méthodes :
     *  9. Les 10% des classes qui possèdent le plus grand nombre d'attributs :
     * 10. Les classes qui font partie en même temps des deux catégories précédentes :
     * 11. Les classes qui possèdent plus de X méthodes :
     * 12. Les 10% de méthodes qui possèdent le plus grand nombre de lignes de code (par classe) :
     * 13. Le nombre maximal de paramètres par rapport à toutes les méthodes de l'application :
     */


    private CompilationUnit cu;
    private TypeEntity currentType;


    public SourceCodeVisitor(CompilationUnit compilationUnit, String source, ApplicationEntity application){
        this.cu = compilationUnit;
        totalLines += countLines(source, false);
        nonBlankLines += countLines(source, true);
        this.application = application;

        application.addLines( countLines(source, false) );
        application.addNonBlankLines( countLines(source, true) );
//        totalLines += countLines(source, false);
    }

    public boolean visit(VariableDeclarationFragment node) {
        SimpleName name = node.getName();
        this.declaredNames.add(name.getIdentifier());
//        System.out.println("------------------------");
//        System.out.println("Declaration of '" + name + "' at line"
//                + cu.getLineNumber(name.getStartPosition()));
        return false; // do not continue
    }


    @Override
    public boolean visit(PackageDeclaration node) {
//        System.err.println("Package trouvé : " + node.getName());
        declaredPackages.add(node.getName());

        currentPackage = new PackageEntity(node.getName().toString());


        application.addPackage( currentPackage );

        return super.visit(node);
    }

    public boolean visit(SimpleName node) {
        if (this.declaredNames.contains(node.getIdentifier())) {
//            System.out.println("------------------------");
//            System.out.println("Usage of '" + node + "' at line "
//                    + cu.getLineNumber(node.getStartPosition()));
        }
        return true;
    }

    // TODO : Javadoc; lineComment, BlockCOmment


    public boolean visit(TypeDeclaration node) {



        String text ="";
        if (node.isInterface()) {
            text +="Nom de l'interface";
            InterfaceEntity  interfaceEntity = new InterfaceEntity(node.getName().toString());
            currentPackage.addInterface( interfaceEntity );
            currentType = interfaceEntity;
//            interfacesCount++;
        }
        else {
            text +="Nom de la classe";
            ClassEntity classEntity = new ClassEntity(node.getName().toString());
            currentPackage.addClass( classEntity );
            currentType = classEntity;

//            classesCount++;
            declaredClasses.put(node.getName().toString(), new HashMap<>());
        }

        text+=" : " +node.getName()+ "\n" + "Nom de la super classe: ";
//        Type Superclass = node.getSuperclassType();
//        System.out.println("Setting superType of " + node.getName() + " to " +  node.getSuperclassType());
        currentType.setSuperType( node.getSuperclassType() );
        text += node.getSuperclassType();
        text+="\n";
        text+="Liste des attributs : \n";
        for (FieldDeclaration f : node.getFields()) {
            // Object o = f.fragments().get(0);

            text += "\t Nom de l'attribut : " + ((VariableDeclarationFragment)f.fragments().get(0)).getName()+"\n";
            text += "\t Visibilité de l'attribut : " + f.modifiers() +"\n";
            text += "\t Type de l'attribut : " +f.getType()+"\n \n";

            currentType.addAttribute( f.modifiers() + " " + f.getType() + " " +  ((VariableDeclarationFragment)f.fragments().get(0)).getName());
        }

        text +="Liste des méthodes : \n";
        for (MethodDeclaration m : node.getMethods()) {
            text+= "\t Nom de la méthode : "+ m.getName() +"\n" +
                    "\t Type de retour : " +m.getReturnType2() +
                    "\n" +"\t Liste des paramètres"+m.parameters()+"\n \n";

            MethodEntity methodEntity = new MethodEntity(m.getName().toString(), m, currentType);
            methodEntity.addParams(m.parameters());

            String returnType = m.getReturnType2() == null ? "void" : m.getReturnType2().toString();
            methodEntity.setReturnType(returnType);

//            System.out.println("Nom de la methode : " + m.getName());
//            System.out.println("TEST TS : " + m.toString());

            String methodBody = "";
            if (m.getBody() != null){
                methodBody = m.getBody().toString();
            }

            methodEntity.setLinesCount( countLines(methodBody, false ) );

            currentType.addMethod( methodEntity );


        }

//        System.out.println(text);
        return true;
    }

    public boolean visit(MethodInvocation methodInvocation) {

        try
        {
            ASTNode parent = methodInvocation.getParent();

            if(parent == null)
                return true;

            while(parent.getNodeType() != 31)
                parent = parent.getParent();

            MethodDeclaration methodDeclaration = (MethodDeclaration) parent;

//            System.out.println("Methode parente : " + currentType.getName() + "." + methodDeclaration.getName());

            parent = methodInvocation.getParent();

            if(parent == null)
                return true;

            while(parent.getNodeType() != 55)
                parent = parent.getParent();

            TypeDeclaration typeDeclaration = (TypeDeclaration) parent;

//            if(treeStructures.get(typeDeclaration.getName().toString()).declarationInvocations.get(methodDeclaration.getName().toString()) == null)
//                treeStructures.get(typeDeclaration.getName().toString()).declarationInvocations.put(methodDeclaration.getName().toString(), new TreeSet<String>());


//            System.out.println("Invocation depuis : " + currentType.getName() + "." + methodDeclaration.getName() + " ->  " + methodInvocation.getExpression() + "." +  methodInvocation.getName());
//            System.out.println("Signature totale : " + methodInvocation);
//            System.out.println("Appellant : " + methodInvocation.getExpression().resolveTypeBinding().getName() );
//            System.out.println("Classe Appelante : " + currentType.getName());

            String miName = currentType.getName() + "." + methodDeclaration.getName() + " ->  " + methodInvocation.getExpression() + "." +  methodInvocation.getName();
//            System.out.println(miName);


            MethodInvocationEntity mie = new MethodInvocationEntity(miName, currentType, methodDeclaration, methodInvocation); // <Full name>, Predicat . affichage , System.out

//            treeStructures.get(typeDeclaration.getName().toString()).declarationInvocations.get(methodDeclaration.getName().toString()).add(methodInvocation.getName().toString());
//
//            methodMethods.get(methodDeclaration.getName().toString()).add(methodInvocation.getName().toString());
        }
        catch(NullPointerException nullPointerException)
        {

        }

        return true;
    }


    public String toString(){

        return ""
                + " 1. Nombre de classes dans l'application : " + declaredClasses.keySet().size() + "\n"
                + " 2. Nombre de lignes de code dans l'application : " + totalLines + ", dont " + nonBlankLines + " non vides\n"
                + " 3. Nombre total de méthodes de l'application : " + countApplicationMethods() + "\n"
                + " 4. Nombre total de packages dans l'application : " + declaredPackages.size() + "\n"
                + " 5. Nombre moyen de méthodes par classe : " + "\n"
                + " 6. Nombre moyen de lignes de codes par méthode : " + "\n"
                + " 7. Nombre moyen d'attributs par classe : " + "\n"
                + " 8. Les 10% des classes qui possèdent le plus grand nombre de méthodes : " + "\n"
                + " 9. Les 10% des classes qui possèdent le plus grand nombre d'attributs : " + "\n"
                + "10. Les classes qui font partie en même temps des deux catégories précédentes : " + "\n"
                + "11. Les classes qui possèdent plus de X méthodes : " + "\n"
                + "12. Les 10% de méthodes qui possèdent le plus grand nombre de lignes de code (par classe) : " + "\n"
                + "13. Le nombre maximal de paramètres par rapport à toutes les méthodes de l'application : " + "\n";
    }

    private int countApplicationMethods() {
        return 0;
    }

    /**
     * Compte le nombre de lignes au total
     * @param str chaîne d'entrée
     * @param removeBlankLines, retire les lignes vides si True
     * @return
     */
    public static int countLines(String str, boolean removeBlankLines) {

//        Matcher lineMatcher = Pattern.compile("\r\n|\r|\n").matcher(str);
        Matcher lineMatcher = Pattern.compile("\n").matcher(str);

        int lines = 1;
        while (lineMatcher.find())
        {
            lines ++;
        }


        if (removeBlankLines){
            Matcher emptyMatcher = Pattern.compile("^$|^\\s$", Pattern.MULTILINE).matcher(str);
//            Matcher emptyMatcher = Pattern.compile("^\\s*$", Pattern.MULTILINE).matcher(str);
//            Matcher emptyMatcher = Pattern.compile("\\s*$", Pattern.MULTILINE).matcher(str);
            while (emptyMatcher.find())
            {
                lines --;
            }
        }
        return lines;
    }

}
