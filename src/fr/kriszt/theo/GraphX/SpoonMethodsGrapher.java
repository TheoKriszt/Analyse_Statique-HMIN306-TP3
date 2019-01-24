package fr.kriszt.theo.GraphX;

import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxConstants;
import com.mxgraph.view.mxGraph;
import fr.kriszt.theo.NodeEntities.MethodEntity;
import fr.kriszt.theo.NodeEntities.NodeEntity;
import fr.kriszt.theo.NodeEntities.TypeEntity;
import fr.kriszt.theo.relations.Relation;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class SpoonMethodsGrapher extends JFrame {


    private static final int	DEFAULT_WIDTH		= 100;
    private static final int LINE_HEIGHT = 20;
    private static final int LETTER_WIDTH = 6;
    private int GRAPH_WIDTH = 800;
    private int GRAPH_HEIGHT = 600;

    private Map<String, Integer> nodesAxis = new HashMap<>();
    private Map<String, Object> typesNodes = new HashMap<>();

    private mxGraph graph;
    private Object	parent;

    public SpoonMethodsGrapher(Map<String, Set<String>> classes, Map<String, Set<String>> calls){

        super("Methods internal calls");


        graph = new mxGraph();
        parent = graph.getDefaultParent();

        graph.getModel().beginUpdate();
        placeTypes(classes, calls);

        endInit(); // TODO

    }

    private void placeTypes(Map<String, Set<String>> classes, Map<String, Set<String>> calls) {

        Map<String, Object> classesReferences = new HashMap<>(); // Nom de classe vers objet Vertex conteneur de la classe
        Map<String, Object> methodsReferences = new HashMap<>(); // Nom de la méthode vars objet Vertex avec son nom
        Map<String, Object> methodToClassReferences = new HashMap<>(); //Nom d'une méthode vers le vertex de sa classe contenante

        for (String c : classes.keySet()){
            Object classVertex = graph.insertVertex(parent, null, c, 100, 100, c.length() * LETTER_WIDTH, classes.get(c).size() * LINE_HEIGHT);
            classesReferences.putIfAbsent(c, classVertex);

            for (String subMethod : classes.get(c)){
                methodToClassReferences.put(subMethod, classVertex);
            }
        }

        for ( String callerName : calls.keySet()){
            for (String calleeName : calls.get(callerName)){
                Object callerClass = methodToClassReferences.get(callerName);
                Object calleeClass = methodToClassReferences.get(calleeName);

                Object callerVertex = graph.insertVertex(callerClass, null, callerName, 300, 100, callerName.length() * LETTER_WIDTH, LINE_HEIGHT);
                methodsReferences.put( callerName, callerVertex);

                Object calleeVertex = graph.insertVertex(calleeClass, null, calleeName, 300, 100, calleeName.length() * LETTER_WIDTH, LINE_HEIGHT);
                methodsReferences.put( calleeName, calleeVertex);

                graph.insertEdge(parent, null, "", callerVertex, calleeVertex);


            }
        }







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
