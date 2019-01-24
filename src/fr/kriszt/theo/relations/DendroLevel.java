package fr.kriszt.theo.relations;

import com.sun.istack.internal.NotNull;

import java.util.HashSet;
import java.util.Set;

import static fr.kriszt.theo.relations.ClassCluster.removeRedundantRelations;

public class DendroLevel {

    private DendroLevel parent = null;

    public Set<ClassCluster> getClusters() {
        return clusters;
    }

    private Set<ClassCluster> clusters = new HashSet<>();

    public DendroLevel(@NotNull Set<ClassCluster> clusters){
        this.clusters.addAll(clusters);
    }


    public boolean genNextLevel(){
        Set<Relation> relations = Relation.getAllRelations();
        if (relations.isEmpty()) return false;
        Relation strongestRelation = null;
        int minCoupling = 0;

        for (Relation relation : relations){
            if (relation.getCount() >= minCoupling){
                minCoupling = relation.getCount();
                strongestRelation = relation;
            }
        }

        if (strongestRelation != null){
            String inputType = strongestRelation.getInputType();
            String outputType = strongestRelation.getOutputType();

            ClassCluster cc1 = ClassCluster.getContainingCluster(inputType, clusters);
            ClassCluster cc2 = ClassCluster.getContainingCluster(outputType, clusters);



            ClassCluster merged = new ClassCluster(cc1, cc2);

            if (merged.getCohesion() == 15){
                System.err.println("****************************************************");
                System.err.println("Merging " + cc1);
                System.err.println("   With " + cc2);
                System.err.println();
                System.err.println("  Makes " + merged);
                System.err.println("****************************************************");
            }


            Set<ClassCluster> parentClusters = new HashSet<>();
            parentClusters.add(merged);

            for (ClassCluster cc : clusters){
                if (!cc.contains(inputType) && !cc.contains(outputType)){
                    parentClusters.add(cc);
                }
            }

            DendroLevel parent = new DendroLevel(parentClusters);
            this.parent = parent;

            relations.remove(strongestRelation);
            Set<Relation> newRelations = removeRedundantRelations(merged, relations);
            newRelations.remove(strongestRelation);
            Relation.setAllRelations(newRelations);
            return true;
        }

        return false;
    }

    public String toString(){
        String res = "DendroLevel : [\n";
        for (ClassCluster cluster : clusters){
            res += "\t" + cluster + "\n";
        }
        res += "]";
        return res;
    }

    public DendroLevel getParent() {
        return parent;
    }
}
