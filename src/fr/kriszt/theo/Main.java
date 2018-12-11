package fr.kriszt.theo;

import fr.kriszt.theo.NodeEntities.ApplicationEntity;
import fr.kriszt.theo.visitors.SourceCodeVisitor;
import org.apache.commons.io.FileUtils;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;

import java.io.*;
import java.util.List;
import java.util.Map;

public class Main {

    private static final String DEFAULT_SOURCE_PATH = "lib/sourceProject";
    private static final String PARSEABLE_EXTENSION = "java";
    private static final ASTParser parser = ASTParser.newParser(AST.JLS10);
    private static CompilationUnit cu ;
    private static SourceCodeVisitor visitor;

    private static void initParser() {
        parser.setKind(ASTParser.K_COMPILATION_UNIT);
        parser.setUnitName("parserUnit");

        Map<String, String> options = JavaCore.getOptions();
        options.put(JavaCore.COMPILER_COMPLIANCE, JavaCore.VERSION_1_6);
        options.put(JavaCore.COMPILER_CODEGEN_TARGET_PLATFORM, JavaCore.VERSION_1_6);
        options.put(JavaCore.COMPILER_SOURCE, JavaCore.VERSION_1_6);
        parser.setCompilerOptions(options);

        parser.setResolveBindings(true);
        parser.setBindingsRecovery(true);
        parser.setStatementsRecovery(true);

        List<File> srcFileCollection = (List<File>) FileUtils.listFiles(new File(DEFAULT_SOURCE_PATH), new String[]{PARSEABLE_EXTENSION}, true);

        String[] sources = new String[srcFileCollection.size()];

        for (int i = 0; i < sources.length; i++){
            sources[i] = srcFileCollection.get(i).getAbsolutePath();
        }

        System.out.println("Setting source path to " + DEFAULT_SOURCE_PATH);
//        for (String s : sources){
//            System.out.println("\t" + s);
//        }


        parser.setEnvironment(
                new String[]{},
                new String[]{new File(DEFAULT_SOURCE_PATH).getAbsolutePath()},
                null,
                false);
//        parser.setEnvironment(new String[]{}, sources, null, true);


    }

    @SuppressWarnings("Duplicates")
    public static void parse(File f, ApplicationEntity application) throws IOException {
        String str = readFileToString(f.getAbsolutePath());
        parser.setSource(str.toCharArray());

        cu = (CompilationUnit) parser.createAST(null);

        visitor = new SourceCodeVisitor(cu, str, application);
        cu.accept(visitor);



    }

    //read file content into a string
    private static String readFileToString(String filePath) throws IOException {
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
//                    System.err.println("parsing file " + f.getName());
//                    parse(f, application); // TODO : virer, déplacer, createASTs
                    application.addSourceFile( f );
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
        List<File> srcFiles = application.getSrcFiles();


        initParser();

        for (File f : srcFiles){
            System.out.println("Reading " + f);
            System.out.println("-----------------------------------------");
            parse(f, application);
            System.out.flush();
            System.err.flush();
        }


//        application.printResume( 5 );

//        MethodInvocationEntity.bind(application);


    }


}
