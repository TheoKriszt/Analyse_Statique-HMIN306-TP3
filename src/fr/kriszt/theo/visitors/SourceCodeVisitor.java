package fr.kriszt.theo.visitors;

import fr.kriszt.theo.NodeEntities.NodeEntity;
import org.eclipse.jdt.core.dom.*;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Explore l'AST d'un fichier
 */gi
public class SourceCodeVisitor extends ASTVisitor{

    public static final String METHODS_KEY = "methods";
    public static final String LINES_NB_KEY = "lines number";
    public static final String ATTRIBUTES_KEY = "methods";
    public static final String PARAMETERS_KEY = "methods";

    private Set declaredNames = new HashSet();
    private Set declaredPackages = new HashSet();
    private TreeMap<String, HashMap<String, Integer>> declaredClasses = new TreeMap<>();
    private static int totalLines = 0;

    private static NodeEntity application = new NodeEntity("");

    private static NodeEntity currentPackage

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


    public SourceCodeVisitor(CompilationUnit compilationUnit, String source){
        this.cu = compilationUnit;
        totalLines += countLines(source, false);
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
        declaredPackages.add(node.getName());
        return super.visit(node);
    }

    public boolean visit(SimpleName node) {
        if (this.declaredNames.contains(node.getIdentifier())) {
            System.out.println("------------------------");
            System.out.println("Usage of '" + node + "' at line "
                    + cu.getLineNumber(node.getStartPosition()));
        }
        return true;
    }

    // TODO : Javadoc; lineComment, BlockCOmment


    public boolean visit(TypeDeclaration node) {
        String text ="";
        if (node.isInterface()) {
            text +="Nom de l'interface";
//            interfacesCount++;
        }
        else {
            text +="Nom de la classe";
//            classesCount++;
            declaredClasses.put(node.getName().toString(), 0);
        }

        text+=" : " +node.getName()+ "\n" + "Nom de la super classe: ";
        Type Superclass = node.getSuperclassType();
        text +=node.getSuperclassType();
        text+="\n";
        text+="Liste des attributs : \n";
        for (FieldDeclaration f : node.getFields()) {
            // Object o = f.fragments().get(0);

            text += "\t Nom de l'attribut : " + ((VariableDeclarationFragment)f.fragments().get(0)).getName()+"\n";
            text += "\t Visibilité de l'attribut : " + f.modifiers() +"\n";
            text += "\t Type de l'attribut : " +f.getType()+"\n \n";
        }

        text +="Liste des méthodes : \n";
        for (MethodDeclaration m : node.getMethods()) {
            text+= "\t Nom de la méthode : "+ m.getName() +"\n" +
                    "\t Type de retour : " +m.getReturnType2() +
                    "\n" +"\t Liste des paramètres"+m.parameters()+"\n \n";

        }

        System.out.println(text);
        return true;
    }


    public String toString(){
        String res = ""
                + " 1. Nombre de classes dans l'application : " + declaredClasses.keySet().size() + "\n"
                + " 2. Nombre de lignes de code dans l'application : " + totalLines + ", dont XXX executables, YYY de commentaires, ZZZ vides\n"
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
                + "13. Le nombre maximal de paramètres par rapport à toutes les méthodes de l'application : " + "\n"
                ;

        return res;
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

        Matcher lineMatcher = Pattern.compile("\r\n|\r|\n").matcher(str);

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
