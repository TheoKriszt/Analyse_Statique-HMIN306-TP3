package fr.kriszt.theo;

import fr.kriszt.theo.GraphX.Dendrogram;
import fr.kriszt.theo.GraphX.Grapher;
import fr.kriszt.theo.GraphX.MethodsGrapher;
import fr.kriszt.theo.NodeEntities.ApplicationEntity;
import fr.kriszt.theo.NodeEntities.TypeEntity;
import fr.kriszt.theo.relations.Relation;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;

import java.io.*;
import java.util.List;

public class Main {

    public static final String DEFAULT_SOURCE_PATH = "lib/SimpleSample/company/src";
    private static final String PARSEABLE_EXTENSION = "java";
    private static ASTParser parser;

    private static void initParser(String path) {

        parser = ASTParser.newParser(AST.JLS10);
        parser.setResolveBindings(true);
        parser.setBindingsRecovery(true);
        parser.setStatementsRecovery(true);
        parser.setKind(ASTParser.K_COMPILATION_UNIT);
        parser.setUnitName("parserUnit");


        parser.setEnvironment(
                new String[]{new File(path).getAbsolutePath()},
                new String[]{new File(path).getAbsolutePath()},
                new String[]{ "UTF-8" },
                true);

    }

    @SuppressWarnings("Duplicates")
    private static void parse(File f, ApplicationEntity application) throws IOException {
        String str = readFileToString(f.getAbsolutePath());
        parser.setSource(str.toCharArray());

        CompilationUnit cu = (CompilationUnit) parser.createAST(null);

        SourceCodeVisitor visitor = new SourceCodeVisitor(str, application);
        cu.accept(visitor);
    }

    //read file content into a string
    private static String readFileToString(String filePath) throws IOException {
        StringBuilder fileData = new StringBuilder(1024);
        BufferedReader reader = new BufferedReader(new FileReader(filePath));

        char[] buf = new char[10];
        int numRead;
        while ((numRead = reader.read(buf)) != -1) {
            String readData = String.valueOf(buf, 0, numRead);
            fileData.append(readData);
            buf = new char[1024];
        }

        reader.close();

        return  fileData.toString();
    }

    /**
     * Recursively explore and parse files with PARSEABLE_EXTENSION extension
     */
    private static void readDirectory(String path, ApplicationEntity application) throws IOException{

        File dirs = new File(path);

        File root = new File(dirs.getCanonicalPath());

        File[] files = root.listFiles ( );

        if (files == null){
            throw new FileNotFoundException("Le dossier spécifié suivant n'existe pas : " + path);
        }
            for (File f : files ) {
                if (f.isDirectory()){
                    readDirectory(f.getCanonicalPath(), application);
                } else if(f.isFile() && f.getName().endsWith(PARSEABLE_EXTENSION)){
                    application.addSourceFile( f );
                }
            }

    }

    public static void main(String[] args) throws IOException {
        String path = DEFAULT_SOURCE_PATH;
        if (args.length > 0){
            path = args[0];
        }
        int MIN_X = 3;
        if (args.length > 1){
            try {
                MIN_X = Integer.parseInt(args[1]);
            } catch (NumberFormatException e){
                System.err.println("Impossible de lire l'argument 'min-X'");
            }
        }

        System.out.println("========================================");
        if (args.length == 0) {
            System.out.println("Aucun argument détecté : utilisation des paramètres par défaut");
            System.out.println("Usage : java -jar monJar.jar <chemin_vers_les_sources> <min-X>");
        }
            System.out.println("Chemin vers le projet : " + path);
            System.out.println("Min-X : " + MIN_X);
            System.out.println("========================================");
        System.out.println("\n\n\n");


        ApplicationEntity application = new ApplicationEntity("Application");
        readDirectory(path, application);
        List<File> srcFiles = application.getSrcFiles();

        for (File f : srcFiles){
            System.out.println("Reading " + f);
            System.out.println("-----------------------------------------");
            initParser(path);
            parse(f, application);
            System.out.flush();
            System.err.flush();
        }

        Relation.filterOutsideRelations();

        System.out.println("\n==============================================================\n");

        application.printResume( MIN_X );


        new Grapher(TypeEntity.getDeclaredTypes(), Relation.getAllRelations());
        new MethodsGrapher();

        new Dendrogram(false, false); // réel
    }


}
