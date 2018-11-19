package fr.kriszt.theo.NodeEntities;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.internal.compiler.batch.ModuleFinder;

import java.util.ArrayList;
import java.util.List;

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

//        System.out.println("Method Invocation Entity : " + this);
//        System.out.println("\t Method Invocation : " + methodInvocation);

        invocations.add(this);
    }



    public static boolean bind(ApplicationEntity application){

        List<ClassEntity> classes = application.getClasses();

        for (MethodInvocationEntity mie : invocations){
            if (! mie.bindClass(classes)){
                System.err.println("Impossible de binder ");
                return false;
            }
        }

        return true;
    }

    private boolean bindClass(List<ClassEntity> classes){

        System.out.println("Binding de l'appel de méthode " + name);
        System.out.println("\tInvocation : " + methodInvocation);

        IMethodBinding methodBinding = methodInvocation.resolveMethodBinding();
        System.out.println("methodBinding : " + methodBinding);
        int isStatic  = Modifier.STATIC & methodBinding.getModifiers();
        if (isStatic > 0) {
            // method is static method
        } else {
            // method is not static
        }

//        System.out.println("Methode interne appellante : " + callingMethod.getName());
//        System.out.println("Recherche de la méthode " + signature() + " dans " );
        for (MethodEntity me : callingClass.getMethods()){

//            System.out.println("Méthode " + me);


            /*
            if (me.toString().equals(signature())){
                Expression expression = methodInvocation.getExpression();
//                System.out.println("\tInscription que " + signature() + " invoque " + methodInvocation);
//                System.out.println("\tExpression : " + expression + " --> objet ou classe appellante");

//                System.out.println("Method Invocation: " + methodInvocation.getName());

                ITypeBinding binding = expression.resolveTypeBinding();
                System.out.println("Method binding : " + methodInvocation.resolveMethodBinding());
                System.out.println("Method type binding : " + methodInvocation.resolveTypeBinding());

                if ( binding != null) {
                    System.out.println("TODO : binding trouvé sur " + binding);
//                    System.out.println(binding.getDeclaringClass());
                    me.addInvocation(this);
                } else {
                    System.err.println("Pas de binding pour " + methodInvocation);
                }

                System.out.flush();
                System.err.flush();


                return true;
            }
            /**/
            return true;
        }
//        if (callingClass.methods.contains()){
//
//        }

        // find parent class
//        ClassEntity caller = null;
//        for (ClassEntity ce : classes){
//            if (ce.equals(callingClass)){
//                return true;
//            }
//        }

        return false;

    }

    public String signature(){
        String returnType;
        if (callingMethod.isConstructor()){
            returnType = callingClass.name;
        }else if (callingMethod.getReturnType2() != null){
            returnType = callingMethod.getReturnType2().toString();
        }else returnType = "void";


        return returnType + " " +
                callingClass.name + "." +
                callingMethod.getName() + "(" +
                callingMethod.parameters().toString().replaceAll("\\[|]", "") +
                ")";
    }
}
