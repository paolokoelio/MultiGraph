
package es.um.multigraph.gui;

import com.mxgraph.layout.mxCompactTreeLayout;
import com.mxgraph.layout.mxIGraphLayout;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxConstants;
import com.mxgraph.view.mxGraph;
import es.um.multigraph.core.MainClass;
import es.um.multigraph.decision.basegraph.Edge;
import es.um.multigraph.decision.basegraph.Node;
import java.awt.Component;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JFrame;

/**
 *
 * @author Juanjo Andreu
 * @param <N>
 * @param <E>
 */
public class GraphView<N extends Node, E extends Edge<N>> {
    
    private mxGraph graph;
    private HashMap<N, Object> nodes;
    private HashMap<E, Object> edges;
    private mxIGraphLayout layout;
    private JFrame frame;

    public GraphView(MainClass parent, String title, Component parentWindow) {
        frame = new JFrame(title);
        frame.setAlwaysOnTop(true);

        graph = new mxGraph();
        nodes = new HashMap<>();
        edges = new HashMap<>();

        //Add style
        Map<String, Object> style = new HashMap<>();
        style.put(mxConstants.STYLE_SHAPE, mxConstants.SHAPE_RECTANGLE);
        style.put(mxConstants.STYLE_OPACITY, 85);
        style.put(mxConstants.STYLE_FONTCOLOR, "#000400");
        graph.getStylesheet().putCellStyle(mxConstants.STYLE_ROUNDED, style);
            
        layout = new mxCompactTreeLayout(graph);
        mxGraphComponent graphComponent = new mxGraphComponent(graph);
        
        //Disable editing of graph
        graphComponent.setEnabled(true);
        frame.getContentPane().add(graphComponent);
        
        frame.setLocationRelativeTo(parentWindow);
        //JOptionPane.showMessageDialog(parentWindow, style);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(600, 400);
        
        
    }
    
    public GraphView(MainClass parent, String title) {
        this(parent, title, null);
    }
    
    public void setVisible(boolean v) {
        
        frame.setVisible(true);
    }
    
    public GraphView(MainClass parent){
        this(parent, "Multigraph Graph Viewer");
    }
    
    
    
    public synchronized void insertNode(N node) {
        Object parent = graph.getDefaultParent();
        graph.getModel().beginUpdate();
        try {
            Object cell = graph.insertVertex(graph.getDefaultParent(), node.getID(), node.getLabelGraph(), 0, 0, 40, 40);
            nodes.put(node, cell);
            layout.execute(parent);
        } finally {
            graph.getModel().endUpdate();
        }
    }

    public synchronized void insertEdge(E edge) {
        Object parent = graph.getDefaultParent();
        graph.getModel().beginUpdate();
        try {
            Object source = nodes.get(edge.getFrom());
            Object dest = nodes.get(edge.getTo());
            Object cell = graph.insertEdge(parent, edge.getID(), edge.getLabelGraph(), source, dest);
            edges.put(edge, cell);
            layout.execute(parent);
        } finally {
            graph.getModel().endUpdate();
        }
    }
    
    public synchronized void setLabel(E edge, String label) {
        Object parent = graph.getDefaultParent();
        graph.getModel().beginUpdate();
        try {
            Object cell = edges.get(edge);
            graph.cellLabelChanged(cell, label, true);
            layout.execute(parent);
        } finally {
            graph.getModel().endUpdate();
        }
    }
    
    public synchronized void setLabel(N node) {
        setLabel(node, node.getLabelGraph());
    }
    
    public synchronized void setLabel(N node, String label) {
        Object parent = graph.getDefaultParent();
        graph.getModel().beginUpdate();
        try {
            Object cell = nodes.get(node);
            graph.cellLabelChanged(cell, label, true);
            layout.execute(parent);
        } finally {
            graph.getModel().endUpdate();
        }
    }
    
    public synchronized void setColor(N node, java.awt.Color color) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public synchronized void repaint(boolean relayout) {
        
        
        Object parent = graph.getDefaultParent();
        graph.getModel().beginUpdate();
        try {
            
            layout.execute(parent);
            graph.refresh();
            graph.repaint();
        } finally {
            graph.getModel().endUpdate();
        }
        
    }
    
    public void removeNode(N node) {
        Object[] cells = new Object[1];
        cells[0] = nodes.get(node);
        if (cells[0] != null ) {
            //Removes the cells and the edges attached to it
            graph.removeCells(cells, true);
        }
    }

    public void removeEdge(E e) {
        Object[] cells = new Object[1];
        cells[0] = edges.get(e);
        if (cells[0] != null ) {
            //Removes the edge
            graph.removeCells(cells);
        }
    }

}
