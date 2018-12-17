package fr.kriszt.theo;

import fr.kriszt.theo.NodeEntities.ApplicationEntity;
import fr.kriszt.theo.visitors.ParserHelper;
import fr.kriszt.theo.visitors.SourceCodeVisitor;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;

import java.io.*;
import java.util.List;

public class Main {

    private static final String DEFAULT_SOURCE_PATH = "lib/sourceProject";
    private static ASTParser parser;


    @SuppressWarnings("Duplicates")
    private static void parse(File f, ApplicationEntity application) throws IOException {
        String str = ParserHelper.readFileToString(f.getAbsolutePath());
        parser.setSource(str.toCharArray());

        CompilationUnit cu = (CompilationUnit) parser.createAST(null);

        SourceCodeVisitor visitor = new SourceCodeVisitor(str, application);
        cu.accept(visitor);
    }

    public static void main(String[] args) throws IOException {
        String path = DEFAULT_SOURCE_PATH;
        if (args.length > 0){
            path = args[0];
        }

        ApplicationEntity application = new ApplicationEntity("Application");
        ParserHelper.readDirectory(path, application);
        List<File> srcFiles = application.getSrcFiles();
        ParserHelper.initParser(path, parser);


        for (File f : srcFiles){
            System.out.flush();
            System.err.flush();
            System.out.println("Reading " + f);
            for (int i = 0; i < ("Reading " + f).length(); i++){
                System.out.print("-");
            }
            System.out.println();

            parse(f, application);
            System.out.flush();
            System.err.flush();
        }

//        application.printResume( 5 );

//        MethodInvocationEntity.bind(application);


    }


}
