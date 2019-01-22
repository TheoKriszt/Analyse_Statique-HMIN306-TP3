package fr.kriszt.theo.spoon.main;

import spoon.Launcher;
import spoon.reflect.code.CtBlock;
import spoon.reflect.code.CtStatement;
import spoon.reflect.declaration.*;
import spoon.reflect.factory.CodeFactory;
import spoon.reflect.factory.CoreFactory;
import spoon.reflect.factory.TypeFactory;
import spoon.reflect.reference.CtFieldReference;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class SpoonExample <T>
{
	public final static String LINE_SEPARATOR = System.getProperty("line.separator");
	
	private CoreFactory coreFactory;
	private CodeFactory codeFactory;
	private TypeFactory typeFactory;
	
	private CtClass<T> generatedClass;
	
	/**
	 * Constructor
	 * @param classFilePath Path where found .java file
	 * @param className Name of the search class into classFilePath
	 */
	public SpoonExample(String classFilePath, String className)
	{	
		Launcher launcher = new Launcher();
		launcher.addInputResource(classFilePath);
		launcher.buildModel();

		coreFactory = launcher.getFactory().Core();
		codeFactory = launcher.getFactory().Code();
		typeFactory = launcher.getFactory().Type();
		
		generatedClass = (CtClass<T>) launcher.getFactory().Type().get(className);
	}
	
	/**
	 * Add an new field into this.generatedClass
	 * 
	 * @param fieldName Name of the new field
	 * @param fieldClass Type/Class of the new field
	 */
	public void addField(String fieldName, Class<?> fieldClass)
	{
		CtField<?> ctField = codeFactory.createCtField(fieldName, typeFactory.createReference(fieldClass),
			"\"\"", ModifierKind.PRIVATE);

		generatedClass.addFieldAtTop(ctField);
		
		alterConstructor(fieldName, fieldClass);
		
		alterToString();
	}
	
	/**
	 * Add the new field in the constructor (parameter + affectation)
	 * 
	 * This method is called in addField
	 * 
	 * @param fieldName Name of the new field
	 * @param fieldClass Type/Class of the new field
	 */
	@SuppressWarnings({"rawtypes", "unchecked"})
	private void alterConstructor(String fieldName, Class<?> fieldClass)
	{		
		CtConstructor<T> ctConstructor = (CtConstructor<T>) generatedClass.getConstructors().toArray()[0];
		fieldClass.getTypeName();
		CtParameter ctParameterCity = coreFactory.createParameter();
		ctParameterCity.setType(typeFactory.createReference(fieldClass));
		ctParameterCity.setSimpleName(fieldName);

		ctConstructor.addParameter(ctParameterCity);

		CtBlock<?> ctBlockConstructorBody = ctConstructor.getBody();

		ctBlockConstructorBody.addStatement(codeFactory.createCodeSnippetStatement("this.city = city;"));
		ctConstructor.setBody(ctBlockConstructorBody);
		
		ctBlockConstructorBody = ctConstructor.getBody();
	}
	
	/**
	 * Add the new field in the method toString
	 * 
	 * This method is called in addField
	 * 
	 */
	private void alterToString()
	{
		CtMethod<?> ctMethod = generatedClass.getMethod("toString");

		StringBuilder toString = new StringBuilder();
		
		toString.append("return ");
		toString.append("\"" + generatedClass.getSimpleName() + " : \"");
		toString.append(LINE_SEPARATOR);

		for(CtFieldReference<?> ctFieldReference : generatedClass.getAllFields())
		{
			if (!ctFieldReference.isStatic()) {
				toString.append("+ \"");
				toString.append(ctFieldReference.getSimpleName());
				toString.append(" : \" + ");
				toString.append(ctFieldReference.getSimpleName());
				toString.append(LINE_SEPARATOR);
			}
		}
		
		List<CtStatement> statements = new ArrayList<>();
		statements.add(codeFactory.createCodeSnippetStatement(toString.toString()));
		
		ctMethod.getBody().setStatements(statements);
	}
	
	/**
	 * Add an new method in this.generatedClass
	 * 
	 * (Simplified, no parameter, no return type)
	 * 
	 * @param methodName Name of the method
	 * @param methodBody Body of the method
	 */
	public void addMethod(String methodName, String methodBody)
	{
		CtMethod<Void> ctMethod = coreFactory.createMethod();
		
		ctMethod.setSimpleName(methodName);
		ctMethod.setVisibility(ModifierKind.PUBLIC);
		ctMethod.setType(typeFactory.createReference(void.class));
		ctMethod.setBody(codeFactory.createCodeSnippetStatement(methodBody));
		
		generatedClass.addMethod(ctMethod);
	}
	
	/**
	 * Saved the modified Class (this.generatedClass) into an file
	 * 
	 * @param classFilePath File where save this.generatedClass
	 * @param packageName Package name of the modified class
	 * 
	 * @throws IOException bufferedWriter.write, bufferedWriter.close
	 */
	public void createFile(String classFilePath, String packageName) throws IOException
	{
		BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(classFilePath));
		
		bufferedWriter.write("package " + packageName + ";");
		bufferedWriter.write(LINE_SEPARATOR + LINE_SEPARATOR);
		bufferedWriter.write(generatedClass.toString());
	     
		bufferedWriter.close();
	}
	
	public static void main(String[] Args) throws IOException
	{
		String packagePath = "lib/SimpleSample/company/src";
		String outputFile = packagePath + "/AlteredPerson.java";

		Files.deleteIfExists(Paths.get(outputFile));
		SpoonExample<Void> addAgeField = new SpoonExample<>(packagePath, "com.company.AlteredPerson");
		
		addAgeField.addField("age", Integer.class);
		
		addAgeField.addMethod("getAge", "return this.age;");
		
		addAgeField.createFile(outputFile, "com.company.AlteredPerson");
	}
}
