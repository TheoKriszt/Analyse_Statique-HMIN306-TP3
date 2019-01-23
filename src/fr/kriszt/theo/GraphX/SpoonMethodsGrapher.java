package fr.kriszt.theo.GraphX;

import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxConstants;
import com.mxgraph.view.mxGraph;
import fr.kriszt.theo.NodeEntities.MethodEntity;
import fr.kriszt.theo.NodeEntities.NodeEntity;
import fr.kriszt.theo.NodeEntities.TypeEntity;
import fr.kriszt.theo.relations.MethodRelation;
import fr.kriszt.theo.relations.Relation;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.*;

public class SpoonMethodsGrapher extends JFrame {


    private static final int	DEFAULT_WIDTH		= 100;
//    private static final int DEFAULT_WINDOW_WIDTH = 900;
//    private static final int DEFAULT_WINDOW_HEIGHT = 750;
    private static final int NODE_PADDING = 20;
    private static final int LINE_HEIGHT = 20;
    private static final int LETTER_WIDTH = 6;
    private int GRAPH_WIDTH = 800;
    private int GRAPH_HEIGHT = 600;

    private Map<String, Integer> nodesAxis = new HashMap<>();
    private Map<String, Object> typesNodes = new HashMap<>();

    String styleCallNode = mxConstants.STYLE_FILLCOLOR + "=#00ffff";


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

    @SuppressWarnings("Duplicates")
    private void placeRelations(Set<Relation> relations) {


        for (Relation r : relations){

            String inType = r.getInputType();
            String outType = r.getOutputType();

            int x = (nodesAxis.get(inType) + nodesAxis.get(outType)) / 2 + (outType.length() + inType.length()) * LETTER_WIDTH / 2;
            int inY = GRAPH_HEIGHT / 2;
            int outY = GRAPH_HEIGHT / 2;

            int labelHeight = 0;
            String inLabel = "";

            if (r.getIncomingMethods().size() > 0){

                for (String in : r.getIncomingMethods()){
                    labelHeight += LINE_HEIGHT;
                    inLabel += in + "\n";
                }

                inY -= (labelHeight + LINE_HEIGHT * 2);

                Object interNode = graph.insertVertex(parent, null, inLabel, x - getNodeWidth(r.getIncomingMethods())/2, inY, getNodeWidth(r.getIncomingMethods()), labelHeight);
                graph.insertEdge(parent, null, "", typesNodes.get(inType), interNode);
                graph.insertEdge(parent, null, "", interNode, typesNodes.get(outType));
            }


            if (r.getOutcomingMethods().size() > 0){
                labelHeight = 0;
                String outLabel = "";
                for (String out : r.getOutcomingMethods()){
                    labelHeight += LINE_HEIGHT;
                    outLabel += out + "\n";
                }

                outY += labelHeight + LINE_HEIGHT;

                Object interNode = graph.insertVertex(parent, null, outLabel, x, outY, getNodeWidth(r.getOutcomingMethods()), labelHeight);
                graph.insertEdge(parent, null, "", interNode, typesNodes.get(inType));
                graph.insertEdge(parent, null, "", typesNodes.get(outType), interNode);


            }



        }
    }



    private void placeTypes(Map<String, Set<String>> classes, Map<String, Set<String>> calls) {

        Map<String, Object> classesReferences = new HashMap<>(); // Nom de classe vers objet Vertex conteneur de la classe
        Map<String, Object> methodsReferences = new HashMap<>(); // Nom de la méthode vars objet Vertex avec son nom
        Map<String, Object> methodToClassReferences = new HashMap<>(); //Nom d'une méthode vers le vertex de sa classe contenante

        for (String c : classes.keySet()){
            Object classVertex = graph.insertVertex(parent, null, c, 100, 100, c.length() * LETTER_WIDTH, classes.get(c).size() * LINE_HEIGHT);
            classesReferences.putIfAbsent(c, classVertex);
//            System.out.println("\nInscription de la classe " + c);

            for (String subMethod : classes.get(c)){
//                System.out.println("La méthode " + subMethod + " appartient a la classe " + c);
                methodToClassReferences.put(subMethod, classVertex);
            }
        }

        for ( String callerName : calls.keySet()){
            for (String calleeName : calls.get(callerName)){
                Object callerClass = methodToClassReferences.get(callerName);
                Object calleeClass = methodToClassReferences.get(calleeName);

//                System.out.println("Recherche de la référence de la classe contenant " + callerName);

                Object callerVertex = graph.insertVertex(callerClass, null, callerName, 300, 100, callerName.length() * LETTER_WIDTH, LINE_HEIGHT);
                methodsReferences.put( callerName, callerVertex);

                Object calleeVertex = graph.insertVertex(calleeClass, null, calleeName, 300, 100, calleeName.length() * LETTER_WIDTH, LINE_HEIGHT);
                methodsReferences.put( calleeName, calleeVertex);

                graph.insertEdge(parent, null, "", callerVertex, calleeVertex);


            }
        }







    }

    private double getNodeWidth(String called) {
        return LETTER_WIDTH * called.length();
    }

    @SuppressWarnings("Duplicates")
    private int getNodeWidth(TypeEntity te) {
        int width = te.toString().length();

        for (MethodEntity me : te.getMethods()){
            width = Math.max(width, me.toString().length());
        }

        return width * LETTER_WIDTH;
    }

    @SuppressWarnings("Duplicates")
    private double getNodeWidth(Set<String> methods) {
        int width = 0;

        for (String s : methods){
            width = Math.max(width, s.length());
        }
        return width * LETTER_WIDTH;
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

    private int minWidth(NodeEntity nodeEntity){
        return Math.max(DEFAULT_WIDTH, nodeEntity.toString().length() * LETTER_WIDTH);
    }


}
