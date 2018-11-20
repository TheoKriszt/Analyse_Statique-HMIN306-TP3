package fr.kriszt.theo;

import fr.kriszt.theo.NodeEntities.ApplicationEntity;
import fr.kriszt.theo.NodeEntities.MethodInvocationEntity;
import fr.kriszt.theo.visitors.SourceCodeVisitor;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.internal.core.JavaProject;

import java.io.*;
import java.util.Map;

public class Main {

    public static final String DEFAULT_SOURCE_PATH = "lib/sourceProject";
//    public static final String DEFAULT_SOURCE_PATH = "/auto_home/tkriszt/IdeaProjects/Evolution_restructuration/AnalyseStatique/lib/sourceProject/";
//    public static final String DEFAULT_SOURCE_PATH = "/auto_home/tkriszt/workspace/Resolution/";
//    public static final String DEFAULT_SOURCE_PATH = "lib/sourceProjet/";
    public static final String PARSEABLE_EXTENSION = "java";
    private static final ASTParser parser = ASTParser.newParser(AST.JLS10);
    private static CompilationUnit cu ;
    private static SourceCodeVisitor visitor;

    public static void createParser(String path){

    }

    public static void parse(File f, ApplicationEntity application) throws IOException {
        String str = readFileToString(f.getAbsolutePath());
        parser.setSource(str.toCharArray());
        parser.setKind(ASTParser.K_COMPILATION_UNIT);
//        parser.setEnvironment(null, null, null, true);

        System.out.println("DEFAULT SOURCE : " + DEFAULT_SOURCE_PATH);
        System.out.println("current source : " + f.toString());

        Map<String, String> options = JavaCore.getOptions();
        options.put(JavaCore.COMPILER_COMPLIANCE, JavaCore.VERSION_1_6);
        options.put(JavaCore.COMPILER_CODEGEN_TARGET_PLATFORM, JavaCore.VERSION_1_6);
        options.put(JavaCore.COMPILER_SOURCE, JavaCore.VERSION_1_6);
        parser.setCompilerOptions(options);
        parser.setEnvironment(null, new String[]{f.toString()}, null, false);
        parser.setUnitName("C1.java");
        parser.setResolveBindings(true);
        parser.setBindingsRecovery(true);

        cu = (CompilationUnit) parser.createAST(null);

//        if (cu.getAST().hasBindingsRecovery()){
//            System.out.println("Binding recovery activated");
//        }else {
//            System.out.println("Binding recovery is not activated.");
//        }
//
//        if (cu.getAST().hasResolvedBindings()){
//            System.out.println("Binding activated");
//        }else {
//            System.out.println("Binding is not activated.");
//        }

        visitor = new SourceCodeVisitor(cu, str, application);

//        System.err.println(linesNumber + " lines found");

//        visitor.setApplication(application);
        cu.accept(visitor);

//        System.err.println(visitor);

    }

    //read file content into a string
    public static String readFileToString(String filePath) throws IOException {
        StringBuilder fileData = new StringBuilder(1024);
        BufferedReader reader = new BufferedReader(new FileReader(filePath));

        char[] buf = new char[10];
        int numRead = 0;
        while ((numRead = reader.read(buf)) != -1) {
//            System.out.println(numRead);
            String readData = String.valueOf(buf, 0, numRead);
            fileData.append(readData);
            buf = new char[1024];
        }

        reader.close();

        return  fileData.toString();
    }

    /**
     * Recursively explore and parse files with PARSEABLE_EXTENSION extension
     * @param path
     * @param application
     * @throws IOException
     */
    public static void readDirectory(String path, ApplicationEntity application) throws IOException{

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
//                    System.err.println("parsing file " + f.getName());
                    parse(f, application);
//                    return;
                }
            }

    }

    public static void main(String[] args) throws IOException {
        String path = DEFAULT_SOURCE_PATH;
        if (args.length > 0){
            path = args[0];
        }

        ApplicationEntity application = new ApplicationEntity("Application");
        readDirectory(path, application);



//        application.printResume( 5 );

        MethodInvocationEntity.bind(application);


    }
}
