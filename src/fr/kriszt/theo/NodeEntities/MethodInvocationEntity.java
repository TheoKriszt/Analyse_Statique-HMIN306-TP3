package fr.kriszt.theo.NodeEntities;

import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;

import java.util.ArrayList;

public class MethodInvocationEntity extends NodeEntity {

    private MethodInvocation methodInvocation;
    private TypeEntity callingClass;
    private MethodDeclaration callingMethod;

    private static ArrayList<MethodInvocationEntity> invocations = new ArrayList<>();


    public MethodInvocationEntity(String miName, TypeEntity currentType, MethodDeclaration methodDeclaration, MethodInvocation methodInvocation) {
        super(miName);
        this.methodInvocation = methodInvocation;
        this.callingMethod = methodDeclaration;
        this.callingClass = currentType;

        invocations.add(this);
    }



    public static boolean bind(){
        return false;
    }
}
