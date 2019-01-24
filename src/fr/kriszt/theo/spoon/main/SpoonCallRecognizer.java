package fr.kriszt.theo.spoon.main;

import spoon.Launcher;
import spoon.reflect.code.CtConstructorCall;
import spoon.reflect.code.CtInvocation;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.CtType;
import spoon.reflect.visitor.Query;
import spoon.reflect.visitor.filter.TypeFilter;

import java.util.*;

public class SpoonCallRecognizer<T>
{
	Map<String, Set<String>> methodsCalls = new HashMap<>();
	public Map<String, Set<String>> classes = new HashMap<>();
	private Map<CtType, List<CtMethod>> classesMethods = new HashMap<>();
	private Map<CtMethod, List<String>> methodsInvocations = new HashMap<>();

	private Launcher launcher = new Launcher();

	SpoonCallRecognizer(String sourcePath)
	{
		launcher.getEnvironment().setNoClasspath(true);
		launcher.addInputResource(sourcePath);
		launcher.buildModel();
	}

	void runScan()
	{
		for(CtType<?> ctType : launcher.getFactory().Type().getAll()) {
			scanType(ctType);
		}
		bindMethods();
	}

	@SuppressWarnings("Duplicates")
	private void bindMethods() {

		for ( CtType<?> classe : classesMethods.keySet() ){
			System.out.println("Classe : " + classe.getSimpleName());

			for (CtMethod<?> method : classesMethods.get(classe)){
				String signature = classe.getQualifiedName() + "." + method.getSignature();
				System.out.println("\tMéthode " + signature);
				for(CtInvocation<?> methodInvocation : Query.getElements(method, new TypeFilter<>(CtInvocation.class))) {
					String targetSignature = methodInvocation.getTarget().getType().getQualifiedName() + "." + methodInvocation.getExecutable().getSignature();
					System.out.println("\t\tInvocation : " + targetSignature);
					if (classesMethods.keySet().contains(methodInvocation.getTarget().getType().getTypeDeclaration()) && !targetSignature.equals(signature)){
						System.out.println("\t\t\tEXISTE : " );

						String k = signature;
						String v = targetSignature;
						System.out.println("\t\t\t==>" + k + " ==> " + v);

						methodsInvocations.get(method).add(targetSignature);

						if (!k.equals(v)){
							methodsCalls.putIfAbsent(k, new HashSet<>());
							methodsCalls.get(k).add(v);
						}

					}
				}

				for (CtConstructorCall elem : method.getElements(new TypeFilter<>(CtConstructorCall.class))) {
					System.out.println("Constructor call : " + elem);

					String targetSignature = elem.getExecutable().toString();
					System.out.println("\t\tInvocation : " + targetSignature); // en rechercher la declaration
					if (classesMethods.keySet().contains(elem.getType().getTypeDeclaration()) && !targetSignature.equals(signature)){
						System.out.println("\t\t\tEXISTE : " );
						System.out.println("\t\t\t==>" + signature + " ==> " + targetSignature);

						methodsInvocations.get(method).add(targetSignature);


						if (!signature.equals(targetSignature)){
							methodsCalls.putIfAbsent(signature, new HashSet<>());
							methodsCalls.get(signature).add(targetSignature);
						}

					}
				}


			}

		}
	}

	private void scanType(CtType<?> ctType)
	{
		Collection<CtMethod<?>> methods = ctType.getMethods();

		classes.putIfAbsent(ctType.getQualifiedName(), new HashSet<>());
		classesMethods.putIfAbsent(ctType, new ArrayList<>());

		for(CtMethod<?> method : methods) {
			methods.add(method);
			classes.get(ctType.getQualifiedName()).add(ctType.getQualifiedName() + "." + method.getSignature());
			methodsInvocations.putIfAbsent(method, new ArrayList<>());
			classesMethods.get(ctType).add(method);
		}

	}
}
