package fr.kriszt.theo.GraphX;

import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxConstants;
import com.mxgraph.view.mxGraph;
import fr.kriszt.theo.relations.MethodRelation;
import fr.kriszt.theo.NodeEntities.MethodEntity;
import fr.kriszt.theo.NodeEntities.NodeEntity;
import fr.kriszt.theo.NodeEntities.TypeEntity;
import fr.kriszt.theo.relations.Relation;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class MethodsGrapher extends JFrame {

    private static final int LINE_HEIGHT = 20;
    private static final int LETTER_WIDTH = 6;
    private int GRAPH_WIDTH = 800;
    private int GRAPH_HEIGHT = 600;

    private mxGraph graph;
    private Object	parent;

    public MethodsGrapher(){

        super("Methods internal calls");


        graph = new mxGraph();
        parent = graph.getDefaultParent();

        graph.getModel().beginUpdate();
        placeTypes();

        endInit(); // TODO

    }


    private void placeTypes() {

        HashMap<String, Object> classesNodes = new HashMap<>();
        HashMap<String, Object> methodsNodes = new HashMap<>();
        List<TypeEntity> declaredTypes = new ArrayList<>(TypeEntity.getDeclaredTypes());
        List<String> declaredTypesNames = new ArrayList<>();
        List<String> displayedTypes = new ArrayList<>();
        List<String> displayedMethods = new ArrayList<>();
        for (TypeEntity declaredType : declaredTypes){
            declaredTypesNames.add(declaredType.toString());
        }


        // créer les classes classes
        for (MethodRelation mr : MethodRelation.getAllRelations()){
            String calledType = mr.getCalledType(), callingType = mr.getCallingType();

            if (declaredTypesNames.contains(calledType) && declaredTypesNames.contains(callingType)){

                if (!displayedTypes.contains(calledType)) {
                    classesNodes.put(calledType, graph.insertVertex(parent, null, calledType, GRAPH_WIDTH / 2, GRAPH_HEIGHT / 2, 100, 200));

                    displayedTypes.add(calledType);
                }

                if (!displayedTypes.contains(callingType)) {
                    classesNodes.put(callingType, graph.insertVertex(parent, null, callingType, 200, GRAPH_HEIGHT / 2 + 200, 100, 200));
                    displayedTypes.add(callingType);
                }

            }
        }

        for (MethodRelation mr : MethodRelation.getAllRelations()){

            String calling = mr.getCallingMethod();
            String called = mr.getCalledMethod();
            Object callingClassNode = classesNodes.get(mr.getCallingType());
            Object calledClassNode = classesNodes.get(mr.getCalledType());

            if (!displayedMethods.contains( calling ) && declaredTypesNames.contains(mr.getCallingType())){
                displayedMethods.add(calling);
                methodsNodes.put(calling, graph.insertVertex(callingClassNode, null, calling, new Random().nextInt(200), new Random().nextInt(200), getNodeWidth(calling), LINE_HEIGHT));

            }

            if (!displayedMethods.contains( called ) && declaredTypesNames.contains(mr.getCalledType())){
                displayedMethods.add(called);
                methodsNodes.put(called, graph.insertVertex(calledClassNode, null, called, (called.length() * LETTER_WIDTH), LINE_HEIGHT, getNodeWidth(called), LINE_HEIGHT));
            }

        }

        for (MethodRelation mr : MethodRelation.getAllRelations()){
            graph.insertEdge(parent, null, "",  methodsNodes.get(mr.getCallingMethod()), methodsNodes.get(mr.getCalledMethod()) );
        }

    }

    private double getNodeWidth(String called) {
        return LETTER_WIDTH * called.length();
    }


    @SuppressWarnings("Duplicates")
    private void endInit(){

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
        graphComponent.setConnectable(false);

        graphComponent.setPreferredSize(new Dimension(GRAPH_WIDTH, GRAPH_HEIGHT));
        getContentPane().add(graphComponent);

        pack();
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);


    }


}
