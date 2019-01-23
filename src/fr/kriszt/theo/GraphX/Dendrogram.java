package fr.kriszt.theo.GraphX;

import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;
import fr.kriszt.theo.relations.DendroLevel;
import fr.kriszt.theo.relations.Relation;
import fr.kriszt.theo.relations.ClassCluster;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

        public class Dendrogram extends JFrame {

            private static final int H_SPAN = 100;
            private static final int V_SPAN = 100;
            private static final int LINE_HEIGHT = 20;
            private static final int LETTER_WIDTH = 6;
            private mxGraph graph;
            private Object	parent;

            private int GRAPH_WIDTH = 800;
            private int GRAPH_HEIGHT = 600;

            public Dendrogram(boolean testMode, boolean complet){
                super("Arbre de couplage");

                graph = new mxGraph();
                parent = graph.getDefaultParent();

                graph.getModel().beginUpdate();

                if (testMode){
                    createTestModel(complet);

                } else {
                    createModel();
                }

                createModel();

        endInit();
    }

    @SuppressWarnings("Duplicates")
    private void createModel() {

        Set<String> classes = new HashSet<>();


        for (Relation r : Relation.getAllRelations()){
//            System.out.println(r);
            classes.add(r.getInputType());
            classes.add(r.getOutputType());
        }

//        System.out.println("Classes : " + classes);
        Set<ClassCluster> baseClusters = new HashSet<>();
        for (String c : classes){
            baseClusters.add(new ClassCluster(Collections.singleton(c)));
        }

//        for (ClassCluster cc : baseClusters){
//            System.out.println(cc);
//        }

        DendroLevel currentLevel = new DendroLevel(baseClusters);
        DendroLevel baseLevel = currentLevel;
//        System.out.println("Base : " + baseClusters);
//
        ArrayList<DendroLevel> levels = new ArrayList<>();
//
        while (currentLevel.genNextLevel()){
            levels.add(currentLevel);
            currentLevel = currentLevel.getParent();
        }
//
//        System.out.println("Levels :: " + levels);

        renderLevels(levels);



    }

    @SuppressWarnings("Duplicates")
    private void createTestModel(boolean complet) {

        Set<String> classes = new HashSet<>();
        classes.add("A");
        classes.add("B");
        classes.add("C");
        classes.add("D");

        Set<ClassCluster> baseClusters = new HashSet<>();

        ClassCluster a = new ClassCluster(Collections.singleton("A"));
        ClassCluster b = new ClassCluster(Collections.singleton("B"));
        ClassCluster c = new ClassCluster(Collections.singleton("C"));
        ClassCluster d = new ClassCluster(Collections.singleton("D"));
        baseClusters.add(a);
        baseClusters.add(b);
        baseClusters.add(c);
        baseClusters.add(d);

        DendroLevel currentLevel = new DendroLevel(baseClusters);

        ArrayList<DendroLevel> levels = new ArrayList<>();
//        levels.add(currentLevel);
//        System.out.println(currentLevel);

        Set<Relation> relations = Relation.getAllRelations();
        relations.clear();

        if (complet){

            relations.add(new Relation("A", "B", 5));
            relations.add(new Relation("B", "C", 3));
            relations.add(new Relation("C", "D", 4));
            relations.add(new Relation("D", "A", 2));

        } else {
            relations.add(new Relation("A", "B", 5));
            relations.add(new Relation("B", "C", 2));
            relations.add(new Relation("C", "D", 4));
        }



        while (currentLevel.genNextLevel()){
            levels.add(currentLevel);
//            System.err.println("Ajout du level " + currentLevel);
            currentLevel = currentLevel.getParent();
        }

        if (currentLevel != null){
            levels.add(currentLevel);
        }
//
//        System.out.println("Levels trouvés :: " + levels);

        renderLevels(levels);



    }

    private void renderLevels(List<DendroLevel> levels) {

        Map<ClassCluster, Object> classesToVertices = new HashMap<>();
        Set<ClassCluster> wantedParents = new HashSet<>();

        int x = H_SPAN;
        int y = V_SPAN;

        for (DendroLevel dl : levels){
            int levelNumber = levels.indexOf(dl);
//            System.out.println("Rendu du level " + levelNumber);
            int levelHeight = 0;
            for (ClassCluster cc : dl.getClusters()){
                levelHeight = Math.max(levelHeight, cc.getClasses().size());
                String label = "S = " + cc.getCohesion() + "\n";
                for (String s : cc.getClasses()){
                    label += s + "\n";
                }

                if (!classesToVertices.containsKey(cc) && (levelNumber == 0 || wantedParents.contains(cc))) {
//                    System.out.println("\tRendering cluster " + cc);
                    wantedParents.add(cc.getParent());
                    Object vertex = graph.insertVertex(parent, null, label, x, y, getVertexWidth(label),  getVertexHeight(label));
                    classesToVertices.put(cc, vertex);
                }


                x += getVertexWidth(label) + H_SPAN;
            }

            x = H_SPAN;

            y += levelHeight * LINE_HEIGHT + V_SPAN;


        }

        linkClusters(classesToVertices);

    }

    private void linkClusters(Map<ClassCluster, Object> classesToVertices) {

        for (ClassCluster cc : classesToVertices.keySet()){
            if (cc.getParent() != null){
                Object from = classesToVertices.get(cc);
                Object to = classesToVertices.get(cc.getParent());
                graph.insertEdge(parent, null, "", from, to);
            }
        }

    }

    private int getVertexHeight(String label) {
        return label.split("\\n").length * LINE_HEIGHT;
    }

//    private int countLabelLines(String label, int lineHeight) {
//        int max = 0;
//
//        for (String token : label.split("\\n")){
//            max = Math.max(max, token.length());
//        }
//
//        return max * lineHeight;
//    }

    private int getVertexWidth(String label) {

        int max = 0;

        for (String token : label.split("\\n")){
            max = Math.max(max, token.length());
        }

        return max * LETTER_WIDTH;
    }

    @SuppressWarnings("Duplicates")
    private void endInit() {

        graph.setAllowDanglingEdges(false);
        graph.setEdgeLabelsMovable(true);
        graph.setConnectableEdges(false);

        graph.setCellsDeletable(false);
        graph.setCellsCloneable(false);
        graph.setCellsDisconnectable(false);
        graph.setDropEnabled(false);
        graph.setSplitEnabled(false);
        graph.setDisconnectOnMove(false);

        graph.setCellsBendable(true);
        graph.getModel().endUpdate();
        mxGraphComponent graphComponent = new mxGraphComponent(graph);
        graphComponent.setPreferredSize(new Dimension(GRAPH_WIDTH, GRAPH_HEIGHT));
        getContentPane().add(graphComponent);

        pack();
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);

    }

}
