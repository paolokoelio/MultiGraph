
package es.um.multigraph.gui;

import com.mxgraph.util.mxConstants;
import es.um.multigraph.decision.basegraph.Edge;
import es.um.multigraph.decision.basegraph.Node;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Juanjo Andreu
 */
public class GraphTest {
    public static void main(String[] args) {
        GraphView graphw = new GraphView(null, "Test graph");
        graphw.setVisible(true);
        //graphw.populate();
        Node n = new Node("N1");
        n.setLabel(n.getID());
        graphw.insertNode(n);
        Node n2 = new Node("N2");
        n2.setLabel(n2.getID());
        graphw.insertNode(n2);
        Edge e = new Edge("E12", n, n2);
        
        graphw.insertEdge(e);
        
        Node n3 = new Node("N3");
        n3.setLabel(n3.getID());
        
        graphw.insertNode(n3);
        
        Edge e2 = new Edge("E12", n2, n3);
        graphw.insertEdge(e2);
        
        graphw.setLabel(e, "0.1");
        
        graphw.setLabel(n);
        

    }
}
