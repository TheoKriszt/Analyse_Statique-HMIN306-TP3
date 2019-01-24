package fr.kriszt.theo.NodeEntities;

import java.io.File;
import java.util.*;

public class ApplicationEntity extends NodeEntity {

    private Set<PackageEntity> packages = new HashSet<>();
    private List<File> srcFiles = new ArrayList<>();
    private int totalLines = 0;
    private int nonBlankLines = 0;

    public ApplicationEntity(String n) {
        super(n);
    }

    public void addPackage(PackageEntity packageEntity) {
        packages.add(packageEntity);
    }

    public void addSourceFile(File srcPath){
        srcFiles.add(srcPath);
    }

    public List<File> getSrcFiles(){
        return srcFiles;
    }

    public void printResume(int minMethods) {
        String res = ""
                + " 1. Nombre de classes dans l'application : " + countClasses() + "\n"
                + " 2. Nombre de lignes de code dans l'application : " + totalLines + ", dont " + nonBlankLines + " non vides\n"
                + " 3. Nombre total de méthodes de l'application : " + getMethods().size() + "\n" //countApplicationMethods() + "\n"
                + " 4. Nombre total de packages dans l'application : " + countPackages(packages) + "\n"
                + " 5. Nombre moyen de méthodes par classe : " + String.format("%.2f", getAverageMethodsPerClass()) + "\n"
                + " 6. Nombre moyen de lignes de codes par méthode : " + String.format("%.2f", avgLinesPerMethod())+ "\n"
                + " 7. Nombre moyen d'attributs par classe : " + String.format("%.2f",getAverageAttributesPerClass()) + "\n"
                + " 8. Les 10% des classes qui possèdent le plus grand nombre de méthodes : " + top10Methods() + "\n"
                + " 9. Les 10% des classes qui possèdent le plus grand nombre d'attributs : " + top10Attributes() + "\n"
                + "10. Les classes qui font partie en même temps des deux catégories précédentes : " + top10Overall() + "\n"
                + "11. Les classes qui possèdent plus de "+minMethods+" méthodes : " + getClassesWithMinMethods(minMethods) + "\n"
                + "12. Les 10% de méthodes qui possèdent le plus grand nombre de lignes de code (par classe) : " + top10LinesCount() + "\n"
                + "13. Le nombre maximal de paramètres par rapport à toutes les méthodes de l'application : " + getMaxParams() + "\n";
        System.out.println(res);
    }

    private float avgLinesPerMethod() {
        float count = 0;
        for (MethodEntity me : getMethods()){
            count += me.countLines;
        }
        return count / getMethods().size();
    }

    private int countPackages(Set<PackageEntity> packages) {

        List<String> seen = new ArrayList<>();

        for (PackageEntity pe : packages){
            if (!seen.contains(pe.toString())){
                seen.add(pe.toString());
            }
        }

        return seen.size();
    }

    private int getMaxParams() {
        int max = 0;
        for (MethodEntity m : getMethods()){
            if (max < m.params.size()){
                max = m.params.size();
            }
        }

        return max;
    }

    private ArrayList<ClassEntity> getClassesWithMinMethods(int minMethods) {
        ArrayList<ClassEntity> classes = new ArrayList<>();
        for (ClassEntity c : getClasses()){
            if (c.getMethods().size() >= minMethods){
                classes.add(c);
            }
        }
        return classes;
    }

    private int countClasses(){
        return getClasses().size();
    }

    public void addLines(int countLines) {
        totalLines += countLines;
    }

    public void addNonBlankLines(int countLines) {
        nonBlankLines += countLines;
    }

    List<ClassEntity> getClasses(){
        List<ClassEntity> classes = new ArrayList<>();

        for (PackageEntity myPackage : packages){
            classes.addAll(myPackage.classes);
        }
        return classes;
    }

    private List<MethodEntity> getMethods(){
        List<MethodEntity> methods = new ArrayList<>();
        for (ClassEntity c : getClasses()){
            methods.addAll(c.getMethods());
        }
        return methods;
    }

    private float getAverageMethodsPerClass(){
        float avg = 0;

        for (ClassEntity c : getClasses()){
            avg += c.getMethods().size();
        }

        avg /= (float) countClasses();
        return avg;
    }

    private float getAverageAttributesPerClass(){
        float avg = 0;

        for (ClassEntity c : getClasses()){
            avg += c.attributes.size();
        }
        avg /= (float) countClasses();
        return avg;
    }

    @SuppressWarnings("Duplicates")
    private List<ClassEntity> getClassTopTier(List<ClassEntity> classes){
        ArrayList<ClassEntity> top = new ArrayList<>();
        if (!classes.isEmpty()) {
            if (classes.size() <= 10){
                top.add( classes.get(0) );
            }else {
                int limit = (int) Math.floor( classes.size() / 10f );
                return classes.subList(0, limit);
            }
        }
        return top;
    }

    @SuppressWarnings("Duplicates")
    private List<MethodEntity> getMethodTopTier(List<MethodEntity> classes){
        ArrayList<MethodEntity> top = new ArrayList<>();
        if (!classes.isEmpty()) {
            if (classes.size() <= 10){
                top.add( classes.get(0) );
            }else {
                int limit = (int) Math.floor( classes.size() / 10f );
                return classes.subList(0, limit);
            }
        }
        return top;
    }

    private List<ClassEntity> top10Attributes(){
        List<ClassEntity> classes = getClasses();
        classes.sort((Comparator<TypeEntity>) (o1, o2) -> o1.attributes.size() > o2.attributes.size() ? -1 : 1);

        return getClassTopTier(classes);


    }

    private List<ClassEntity> top10Methods(){
        List<ClassEntity> classes = getClasses();
        classes.sort((Comparator<TypeEntity>) (o1, o2) -> o1.methods.size() > o2.methods.size() ? -1 : 1);
        return getClassTopTier(classes);

    }

    private List<MethodEntity> top10LinesCount(){

        List<ClassEntity> classes = getClasses();
        List<MethodEntity> methods = new ArrayList<>();

        for ( ClassEntity ce : classes ){
            Set<MethodEntity> subMethods = ce.getMethods();
            List<MethodEntity> methodsList = new ArrayList<>(subMethods);
            methodsList.sort((o1, o2) -> {
                if (o1.countLines == o2.countLines) return 0;
                return o1.countLines > o2.countLines ? -1 : 1;
            });
            methods.addAll(getMethodTopTier(methodsList));
        }


        return methods;

    }

    private List<ClassEntity> top10Overall(){
        List<ClassEntity> topMethods = top10Methods();
        List<ClassEntity> topAttributes = top10Attributes();
        List<ClassEntity> both = new ArrayList<>();

        for (ClassEntity m : topMethods){
            if (topAttributes.contains(m)){
                both.add(m);
            }
        }

        return both;
    }


}
