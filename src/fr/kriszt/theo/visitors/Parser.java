package fr.kriszt.theo.visitors;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class Parser {
    private static Parser ourInstance = new Parser();

    private ASTParser parser = ASTParser.newParser(AST.JLS10);
    private CompilationUnit cu ;
    private SourceCodeVisitor visitor;

    public static Parser getInstance() {
        return ourInstance;
    }

    private Parser() {
        parser.setKind(ASTParser.K_COMPILATION_UNIT);
//        Map<String, String> options = JavaCore.getOptions();
//        options.put(JavaCore.COMPILER_COMPLIANCE, JavaCore.VERSION_1_6);
//        options.put(JavaCore.COMPILER_CODEGEN_TARGET_PLATFORM, JavaCore.VERSION_1_6);
//        options.put(JavaCore.COMPILER_SOURCE, JavaCore.VERSION_1_6);
//        parser.setCompilerOptions(options);
        parser.setResolveBindings(true);
        parser.setBindingsRecovery(true);
        parser.setStatementsRecovery(true);


        String[] srcFiles = new String[]{};

//        cu = (CompilationUnit) parser.createASTs();
//        cu = (CompilationUnit) parser.createAST(null);

//        visitor = new SourceCodeVisitor(cu, str, application);
//        cu.accept(visitor);
        throw new NotImplementedException();
    }
}
