/**
 * MULTIGRAPH
 *
 * Project for Mattia Zago Master Thesis
 *
 * (C) 2015 - Mattia Zago
 *
 */
package es.um.multigraph.decision;
import es.um.multigraph.conf.DBManager;
import es.um.multigraph.conf.FeaturesEnum;
import es.um.multigraph.conf.RiskScale;
import es.um.multigraph.core.MainClass;
import es.um.multigraph.decision.basegraph.Edge;
import es.um.multigraph.decision.basegraph.JDialogEdge;
import es.um.multigraph.decision.basegraph.ListModelNodes;
import es.um.multigraph.decision.basegraph.Node;
import es.um.multigraph.event.Event;
import es.um.multigraph.event.EventStream;
import es.um.multigraph.event.solution.Solution;
import es.um.multigraph.event.solution.dummy.DummySolution;
import es.um.multigraph.decision.basegraph.ModelConfigurationDefaultImplementation;
import java.awt.Frame;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.ListModel;

/**
 *
 * @author Mattia Zago <a href="mailto:dev@zagomattia.it">dev@zagomattia.it</a>
 */
public class DecisionInterfaceImpl implements DecisionInterface {

    MainClass parent;
    private boolean stop = false;
    
    public DecisionInterfaceImpl() {
        this.nodes = new LinkedList<>();
    }
    
    @Override
    public void init(MainClass main) {
        this.parent = main;
        
        /*
        Node A = new Node("A");
        
        Node B = new Node("B");
        
        Node C = new Node("C");
        
        Node D = new Node("D");
        
        Edge DB = new Edge(D, B);
        DB.addThisToNodes();
        
        Edge DC = new Edge(D, C);
        DC.addThisToNodes();
        
        Edge BA = new Edge(B, A);
        BA.addThisToNodes();
        
        Edge CA = new Edge(C, A);
        CA.addThisToNodes();
        
        this.nodes.add(A);
        this.nodes.add(B);
        this.nodes.add(C);
        this.nodes.add(D);
        
        System.out.println("Nodes: ");
        System.out.println(A.getFullRepresentationAsString(true));
        System.out.println(B.getFullRepresentationAsString(true));
        System.out.println(C.getFullRepresentationAsString(true));
        System.out.println(D.getFullRepresentationAsString(true));
        */
    }

    @Override
    public List<Class<? extends Event>> getRecognizedEvent() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<FeaturesEnum> getEnabledFeatures() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Solution react(Event e) {
        parent.log("React to Event "+e.toString()+"\n", this);
        parent.log("Creating and configuring new solution\n", this);
        
        return DummySolution.GENERIC;
    }

    @Override
    public RiskScale getOverallRisk() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void loadState(DBManager database) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void log(String txt) {
        if(this.parent != null)
            this.parent.log(txt, this);
        else
            System.out.println(txt);
    }
    
    @Override
    public String toString() {
        return getPaperName();
    }

    @Override
    public String getPaperName() {
        return "Default empty decision module";
    }

    @Override
    public String getPaperAuthors() {
        return "John Doe <john@example.com>";
    }

    @Override
    public URI getPaperDOI() {
        try {
            return new URI("http://www.example.org");
        } catch (URISyntaxException ex) {
            Logger.getLogger(DecisionInterfaceImpl.class.getName()).log(Level.SEVERE, null, ex);
        } 
        return null;
    }

    public boolean isStopped() {
        return stop;
    }

    @Override
    public void stop() {
        parent.log("Stop requested.\n", this);
        this.stop = true;
    }

    @Override
    public void run() {
        this.stop = false;
        parent.log("Started.\n", this);
        try {
            while(!this.isStopped()) {
                Thread.sleep(1000);
                //parent.log(toString()+" still active\n");
                //parent.updateNumberOfNodes(0);
            }
        } catch (InterruptedException ex) {
            Logger.getLogger(EventStream.class.getName()).log(Level.SEVERE, null, ex);
        }
        parent.log("Stopped.\n", this);
    }

    @Override
    public JFrame getModelConfigurationFrame() {
        return new ModelConfigurationDefaultImplementation(parent, this);
    }

    @Override
    public Class<? extends Edge> getEdgesClass() {
        return Edge.class;
    }

    @Override
    public Class<? extends Node> getNodesClass() {
        return Node.class;
    }
    
    private List<Node> nodes;

    @Override
    public void addNode(Node node) {
        this.nodes.add(node);
    }

    @Override
    public void addEdge(Edge edge) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Collection<? extends Node> getNodes() {
        return this.nodes;
    }

    @Override
    public void delNode(Node selected) {
        this.nodes.remove(selected);
    }

    @Override
    public void delEdge(Edge e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Collection getEdges() {
       Set<Edge> result = new HashSet();
        for(Iterator<Node> it = nodes.iterator(); it.hasNext(); ) {
            Node n = it.next();
            result.addAll(n.getIn());
            result.addAll(n.getOut());
        }
        
        return result;
    }
    
    

}
