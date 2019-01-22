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


    String roundEdgesStyle = mxConstants.STYLE_EDGE + "=" + mxConstants.EDGESTYLE_ENTITY_RELATION + ";"
            +  mxConstants.STYLE_ROUNDED+"=1";
    String styleCallNode = mxConstants.STYLE_FILLCOLOR + "=#00ffff";


    private mxGraph graph;
    private Object	parent;

    public Grapher(Set<TypeEntity> declaredTypes, Set<Relation> relations){

        super("Methods calls");


        graph = new mxGraph();
        parent = graph.getDefaultParent();

        graph.getModel().beginUpdate();
        placeTypes(declaredTypes);

        placeRelations(relations);
//        for (TypeEntity te : declaredTypes){
//
//            Object node = graph.insertVertex(parent, null, te.toString(), new Random().nextInt(DEFAULT_WINDOW_WIDTH), new Random().nextInt(DEFAULT_WINDOW_HEIGHT), minWidth(te), DEFAULT_HEIGHT);
//
//            typesNodes.put(te.toString(), node);
//        }

//        for (Relation r : relations){
//            Object source = typesNodes.get(r.getCallingMethod());
//            Object dest = typesNodes.get(r.getCalledMethod());
//
//
//            int width = 0;
//            int height = 0;
//            String label = "";//"[" + r.getCount() + " call" + (r.getCount()>1?"s":"") + "]\n";
//
//            for (String methodCall : r.getIncomingMethods()){
//                label += methodCall + "\n";
//                width = Math.max(width, methodCall.length());
//                height += 20;
//            }
//            Object call = graph.insertVertex(parent, null, label, new Random().nextInt(DEFAULT_WINDOW_WIDTH), new Random().nextInt(DEFAULT_WINDOW_HEIGHT), width*7, height, styleCallNode);
//            graph.insertEdge(parent, null, "", source, call);
//            graph.insertEdge(parent, null, "", call, dest);
//
//            label = "";
//            width = 0;
//            height = 0;
//            for (String methodCall : r.getOutcomingMethods()){
//                label += methodCall + "\n";
//            }
//
////            graph.insertEdge(parent, null, label, dest, source);
//            call = graph.insertVertex(parent, null, label, new Random().nextInt(DEFAULT_WINDOW_WIDTH), new Random().nextInt(DEFAULT_WINDOW_HEIGHT), width*7, height, styleCallNode);
//            graph.insertEdge(parent, null, "", dest, call);
//            graph.insertEdge(parent, null, "", call, source);
//
//
//        }

        endInit();

    }

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

    private int getNodeWidth(TypeEntity te) {
        int width = te.toString().length();

        for (MethodEntity me : te.getMethods()){
            width = Math.max(width, me.toString().length());
        }

        return width * LETTER_WIDTH;
    }

    private double getNodeWidth(Set<String> methods) {
        int width = 0;

        for (String s : methods){
            width = Math.max(width, s.length());
        }
        return width * LETTER_WIDTH;
    }

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
