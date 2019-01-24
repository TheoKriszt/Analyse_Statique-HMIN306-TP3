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
import java.util.*;

public class Grapher extends JFrame {


    private static final int DEFAULT_WIDTH	= 100;
    private static final int NODE_PADDING = 20;
    private static final int LINE_HEIGHT = 20;
    private static final int LETTER_WIDTH = 6;
    private int GRAPH_WIDTH = 800;
    private int GRAPH_HEIGHT = 600;

    private Map<String, Integer> nodesAxis = new HashMap<>();
    private Map<String, Object> typesNodes = new HashMap<>();


    private String styleCallNode = mxConstants.STYLE_FILLCOLOR + "=#00ffff";


    private mxGraph graph;
    private Object	parent;

    public Grapher(Set<TypeEntity> declaredTypes, Set<Relation> relations){

        super("Methods calls");


        graph = new mxGraph();
        parent = graph.getDefaultParent();

        graph.getModel().beginUpdate();
        placeTypes(declaredTypes);

        placeRelations(relations);

        endInit();

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



    private void placeTypes(Set<TypeEntity> declaredTypes) {

        int totalWidth = NODE_PADDING * (declaredTypes.size() + 1) + 400;
        int y = (GRAPH_HEIGHT - LINE_HEIGHT) / 2;
        int x = NODE_PADDING;

        for (TypeEntity te : declaredTypes){
            totalWidth += getNodeWidth(te);
        }

        GRAPH_WIDTH = totalWidth;

        for (TypeEntity te : declaredTypes){
            int lines = 2;
            int width = getNodeWidth(te);


            Set<MethodEntity> internalMethods = te.getMethods() ;

            String label = te.toString() + "\n[ " + te.getLinesCount() + " lines]\n";
            for (MethodEntity method : internalMethods){
                lines++;
                label += method + "\n";
            }

            nodesAxis.put(te.toString(), x + width / 2);

            Object node = graph.insertVertex(parent, null, label, x, y, width, LINE_HEIGHT * lines, styleCallNode);
            typesNodes.put(te.toString(), node);


            x += width + NODE_PADDING;
        }



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


}
