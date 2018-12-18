package fr.kriszt.theo.GraphX;

import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxConstants;
import com.mxgraph.view.mxGraph;
import fr.kriszt.theo.NodeEntities.MethodEntity;
import fr.kriszt.theo.NodeEntities.NodeEntity;
import fr.kriszt.theo.NodeEntities.TypeEntity;
import fr.kriszt.theo.Relation;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class MethodsGrapher extends JFrame {


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

    public MethodsGrapher(){

        super("Methods internal calls");


        graph = new mxGraph();
        parent = graph.getDefaultParent();

        graph.getModel().beginUpdate();
        placeTypes();

//        placeRelations(relations);
//        for (TypeEntity te : declaredTypes){
//
//            Object node = graph.insertVertex(parent, null, te.toString(), new Random().nextInt(DEFAULT_WINDOW_WIDTH), new Random().nextInt(DEFAULT_WINDOW_HEIGHT), minWidth(te), DEFAULT_HEIGHT);
//
//            typesNodes.put(te.toString(), node);
//        }

//        for (Relation r : relations){
//            Object source = typesNodes.get(r.getInputType());
//            Object dest = typesNodes.get(r.getOutputType());
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

//        endInit(); // TODO

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



    private void placeTypes() {

        int typesCount = 0;
        for (TypeEntity te : TypeEntity.getDeclaredTypes()){
            System.out.println("Reading declared type " + te);
            typesCount++;

            for (MethodEntity me : te.getMethods()){
                System.out.println("\t found method " + me);
                for (MethodEntity invocation : me.calledMethods){
                    System.out.println("\t\t calls method " + invocation);
                }
            }
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
