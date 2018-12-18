package fr.kriszt.theo.GraphX;

import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxConstants;
import com.mxgraph.view.mxGraph;
import fr.kriszt.theo.NodeEntities.NodeEntity;
import fr.kriszt.theo.NodeEntities.TypeEntity;
import fr.kriszt.theo.Relation;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class Grapher extends JFrame {

    private static final int	ROOT_X	= 200;
    private static final int	ROOT_Y	= 20;

    private static final int	DEFAULT_WIDTH_SMALL	= 50;
    private static final int	DEFAULT_WIDTH		= 100;
    private static final int DEFAULT_HEIGHT = 20;
    private static final int	DEFAULT_SPACE	= 90;
    private static final int DEFAULT_WINDOW_WIDTH = 900;
    private static final int DEFAULT_WINDOW_HEIGHT = 750;

    String roundEdgesStyle = mxConstants.STYLE_EDGE + "=" + mxConstants.EDGESTYLE_ENTITY_RELATION + ";"
            +  mxConstants.STYLE_ROUNDED+"=1";
    String styleCallNode = mxConstants.STYLE_FILLCOLOR + "=#00ffff";


    private mxGraph graph;
    private Object	parent;

    public Grapher(Set<TypeEntity> declaredTypes, Set<Relation> relations){

        super("Methods calls");

        Map<String, Object> typesNodes =new HashMap<>();


        graph = new mxGraph();
        parent = graph.getDefaultParent();

        graph.getModel().beginUpdate();

//        Object rootNode = graph.insertVertex(parent, null, "Hello", ROOT_X, ROOT_Y, DEFAULT_WIDTH,
//                DEFAULT_HEIGHT, "fillColor=#ff8080;");

        for (TypeEntity te : declaredTypes){

            Object node = graph.insertVertex(parent, null, te.toString(), new Random().nextInt(DEFAULT_WINDOW_WIDTH), new Random().nextInt(DEFAULT_WINDOW_HEIGHT), minWidth(te), DEFAULT_HEIGHT);

            typesNodes.put(te.toString(), node);
        }

        for (Relation r : relations){
            Object source = typesNodes.get(r.getInputType());
            Object dest = typesNodes.get(r.getOutputType());


            int width = 0;
            int height = 0;
            String label = "";//"[" + r.getCount() + " call" + (r.getCount()>1?"s":"") + "]\n";

            for (String methodCall : r.getIncomingMethods()){
                label += methodCall + "\n";
                width = Math.max(width, methodCall.length());
                height += 20;
            }
            Object call = graph.insertVertex(parent, null, label, new Random().nextInt(DEFAULT_WINDOW_WIDTH), new Random().nextInt(DEFAULT_WINDOW_HEIGHT), width*7, height, styleCallNode);
            graph.insertEdge(parent, null, "", source, call, roundEdgesStyle);
            graph.insertEdge(parent, null, "", call, dest, roundEdgesStyle);

            label = "";
            width = 0;
            height = 0;
            for (String methodCall : r.getOutcomingMethods()){
                label += methodCall + "\n";
            }

            graph.insertEdge(parent, null, label, dest, source, roundEdgesStyle);
            call = graph.insertVertex(parent, null, label, new Random().nextInt(DEFAULT_WINDOW_WIDTH), new Random().nextInt(DEFAULT_WINDOW_HEIGHT), width*7, height, styleCallNode);
            graph.insertEdge(parent, null, "", dest, call, roundEdgesStyle);
            graph.insertEdge(parent, null, "", call, source, roundEdgesStyle);


        }

        endInit();

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

//        graphComponent.setMinimumSize(new Dimension(750, 600));
        graphComponent.setPreferredSize(new Dimension(DEFAULT_WINDOW_WIDTH, DEFAULT_WINDOW_HEIGHT));
        getContentPane().add(graphComponent);

        pack();
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);


    }

    private int minWidth(NodeEntity nodeEntity){
        return Math.max(DEFAULT_WIDTH, nodeEntity.toString().length() * 5);
    }


}
