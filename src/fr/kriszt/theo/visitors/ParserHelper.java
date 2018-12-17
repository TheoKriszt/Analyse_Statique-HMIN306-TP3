package fr.kriszt.theo.visitors;

import fr.kriszt.theo.NodeEntities.ApplicationEntity;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;

import java.io.*;

public class ParserHelper {

    private static final String PARSEABLE_EXTENSION = "java";

    public static void initParser(String path, ASTParser parser) {

        parser = ASTParser.newParser(AST.JLS10);
        parser.setResolveBindings(true);
        parser.setBindingsRecovery(true);
        parser.setStatementsRecovery(true);
        parser.setKind(ASTParser.K_COMPILATION_UNIT);
        parser.setUnitName("parserUnit");

//        List<File> srcFileCollection = (List<File>) FileUtils.listFiles(new File(path), new String[]{PARSEABLE_EXTENSION}, true);
//        String[] sources = new String[srcFileCollection.size()];
//
//        for (int i = 0; i < sources.length; i++){
//            sources[i] = srcFileCollection.get(i).getAbsolutePath();
//        }


        parser.setEnvironment(
                new String[]{new File(path).getAbsolutePath()},
                new String[]{new File(path).getAbsolutePath()},
                new String[]{ "UTF-8" },
                true);

//        Map<String, String> options = JavaCore.getOptions();
//        options.put(JavaCore.COMPILER_COMPLIANCE, JavaCore.VERSION_1_6);
//        options.put(JavaCore.COMPILER_CODEGEN_TARGET_PLATFORM, JavaCore.VERSION_1_6);
//        options.put(JavaCore.COMPILER_SOURCE, JavaCore.VERSION_1_6);
//        parser.setCompilerOptions(options);

    }

    /**
     * Recursively explore and parse files with PARSEABLE_EXTENSION extension
     */
    public static void readDirectory(String path, ApplicationEntity application) throws IOException {

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

    /**
     * reads a file content into a string
     */
    public static String readFileToString(String filePath) throws IOException {
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
}
