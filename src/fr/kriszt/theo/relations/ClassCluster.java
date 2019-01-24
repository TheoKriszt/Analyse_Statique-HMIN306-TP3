package fr.kriszt.theo.relations;

import java.util.*;

public class ClassCluster {

    private Set<String> classes= new HashSet<>();
    private int cohesion = 0;
    private ClassCluster parent = null;

    public int getCohesion() {
        return cohesion;
    }

    public ClassCluster getParent() {
        return parent;
    }



    public ClassCluster( Set<String> classes ){
        this.classes.addAll(classes);
    }

    public ClassCluster(ClassCluster cc1, ClassCluster cc2) {
        classes.addAll(cc1.classes);
        classes.addAll(cc2.classes);

        int count = 0;

        for (Relation r : Relation.getAllRelations()){
            String in = r.getInputType();
            String out = r.getOutputType();

            if ( cc1.contains(in) && cc2.contains(out) ||
                    cc1.contains(out) && cc2.contains(in)
            ){
                count += r.getCount();
            }
        }

        cohesion = count;

        if (cohesion * 2 >= cc1.cohesion + cc2.cohesion){
            cc1.setParent(this);
            cc2.setParent(this);
        }




    }

    public static Set<Relation> removeRedundantRelations(ClassCluster cc, Set<Relation> relations) {
        String first = cc.getFirstClass();

//        Set<Relation> relations = Relation.getAllRelations();
        Set<Relation> relationsARefactorer = new HashSet<>();

        for (Relation r : relations){
            String in = r.getInputType();
            String out = r.getOutputType();
            if (!in.equals(out)){
                if ( (cc.contains(in) && !cc.contains(out)) || (cc.contains(out) && !cc.contains(in)) ) {


                    if (!cc.contains(in) && !out.equals(first)){
                        r.setOutType(first);

                    } else if (!cc.contains(out) && !in.equals(first)){
                        r.setInType(first);
                    }
                }
            }

        }

        Map<String, Integer> relationsCounts = new HashMap<>();

        for (Relation relation : relations){
            String key = relation.getInputType() + " ==> " + relation.getOutputType();
            if (!relationsCounts.containsKey(key)){
                relationsCounts.put(key, relation.getCount());
            } else {
                relationsCounts.put(key, relationsCounts.get(key) + relation.getCount());
            }
        }

        for (String k : relationsCounts.keySet()){
            String[] tokens = k.split(" ==> ");
            if (!tokens[0].equals(tokens[1]))
            relationsARefactorer.add(new Relation(tokens[0], tokens[1], relationsCounts.get(k)));
        }

        return relationsARefactorer;
    }

    private void setParent(ClassCluster classCluster) {
        parent = classCluster;
    }

    public Set<String> getClasses(){
        return classes;
    }



    public boolean contains(String className){
        return classes.contains(className);
    }

    public static ClassCluster getContainingCluster(String needle, Set<ClassCluster> haystack){
        for (ClassCluster cc : haystack){
            if (cc.contains(needle)) return cc;
        }
        return null;
    }

    public String getFirstClass(){
        String c = classes.iterator().next();

        for (String s : classes){
            if (s.compareTo(c) < 0){
                c = s;
            }
        }
        return c;
    }

    @Override
    public String toString(){
        String res = "Cluster (cohesion="+cohesion+") : ";
        res += classes;

        return res;
    }

    @Override
    public boolean equals(Object o){
        return o instanceof ClassCluster && o.toString().equals(this.toString());
    }
}
