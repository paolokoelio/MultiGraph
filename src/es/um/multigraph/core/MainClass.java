/**
 * MULTIGRAPH
 *
 * Project for Mattia Zago Master Thesis
 *
 * (C) 2015 - Mattia Zago
 *
 */
package es.um.multigraph.core;

import es.um.multigraph.decision.DecisionInterface;
import es.um.multigraph.decision.DecisionInterfaceImpl;
import es.um.multigraph.decision.basegraph.Edge;
import es.um.multigraph.decision.basegraph.Node;
import es.um.multigraph.decision.model.BayesianAttackGraph;
import es.um.multigraph.decision.poolsappasitmoop.BayesianAttackGraphAdapted;
import es.um.multigraph.decision.lwang.AttackGraph;
import es.um.multigraph.decision.almohri.Main.ECSA;
import es.um.multigraph.event.Event;
import es.um.multigraph.event.EventListener;
import es.um.multigraph.event.EventStream;
import es.um.multigraph.gui.GUI;
import es.um.multigraph.gui.GraphView;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author Mattia Zago <a href="mailto:dev@zagomattia.it">dev@zagomattia.it</a>
 */
public class MainClass implements Runnable {
    
    /*======================================================================
     * EVENT HANDLER SECTION
     *====================================================================*/
    /**
     * Listers list, to register please refer to MainClass.addListener method.
     */
    private List<EventListener> listeners = new LinkedList<>();

    /**
     * Add lister to the list.
     * @param toAdd EventListener
     */
    public void addListener(EventListener toAdd) {
        listeners.add(toAdd);
    }
    
    /**
     * Remove the passed listener from the list. 
     * @param toDel EventListener
     * @return True if listener is removed, false otherwise
     */
    public boolean delListner(EventListener toDel) {
        return this.listeners.remove(toDel);
    }
   
    public void log(String txt, Object sender) {
        // move to logger class
        // Notify everybody that may be interested.
        listeners.stream().forEach((l) -> {
            l.log("["+sender.toString()+"] "+txt);
        });
    }
    
    
    /*======================================================================
     * EVENT SOURCE SECTION
     *====================================================================*/
    private final EventStream eventStream;
   
    private void configEventStream() {
        // TODO: do we need some extra config?
    }
    
    public void startEventSource() {
        eventThread = new Thread(eventStream);
        eventThread.setName("Event Source");
        eventThread.start();
    }
    
    public void stopEventSource() {
        eventStream.stopStream();
    }
    
    public void setEventPrivateCounter(Integer counter) {
        this.gui.setEventPrivateCounter(counter);
    }
    
    public void fireEvent() {
        Event e = eventStream.getNextQueuedEvent();
        this.react(e);
    }
    
    
    /*======================================================================
     * GUI CONFIGURATION SECTION
     *====================================================================*/
    private final GUI gui;
    private GraphView graph;
    
    private void configGUI() {
        this.addListener(gui);
    }
    
    public void startGUI() {
        this.guiThread.start();
    }
    public void stopGUI() {
        this.gui.dispose();
    }
    
    public void createEmptyGraph(String label) {
        if(label == null)
            label = "Multigraph Simulator Graph Viewer";
        this.graph = new GraphView(this, label);
    }
    
    public GraphView getGraph() {
        if(this.graph == null)
            createEmptyGraph(null);
        return this.graph;
    }
    
    public void setGraph(GraphView graph) {
        this.graph = graph;
    }
    
    public void showGraph() {
        if(this.graph != null)
            this.graph.setVisible(true);
    }
    
    public void repaintGraph() {
        for(Iterator<Node> it = this.activeDecisionModule.getNodes().iterator(); it.hasNext(); ) {
            setGraphLabel(it.next());
        }
        for(Iterator<Edge> it = this.activeDecisionModule.getEdges().iterator(); it.hasNext(); ) {
            setGraphLabel(it.next());
        }
        this.graph.repaint(false);
    }
    
    public void setGraphLabel(Node n) {
        setGraphLabel(n, n.getLabelGraph());
    }
    public void setGraphLabel(Node n, String label) {
        this.graph.setLabel(n, label);
    }
    public void setGraphLabel(Edge e) {
        setGraphLabel(e, e.getLabelGraph());
    }
    public void setGraphLabel(Edge e, String label) {
        this.graph.setLabel(e, label);
    }
    
    public void addNodeToGraph(Node n) {
        addNodeToGraph(n, n.getLabelGraph());
    }
    public void addNodeToGraph(Node n, String label) {
        if(this.graph == null)
            createEmptyGraph("Graph");
        
        this.graph.insertNode(n);
        this.graph.setLabel(n, label);
    }
    
    public void addEdgeToGraph(Edge e) {
        addEdgeToGraph(e, e.getLabelGraph());
    }
    public void addEdgeToGraph(Edge e, String label) {
        if(this.graph == null)
            createEmptyGraph("Graph");
        
        this.graph.insertEdge(e);
        this.graph.setLabel(e, label);
    }
    
    public void delNodeFromGraph(Node n) {
        for(Edge e : n.getIn())
            delEdgeFromGraph(e);
        
        for(Edge e : n.getOut())
            delEdgeFromGraph(e);
        
        this.graph.removeNode(n);
    }
    
    public void delEdgeFromGraph(Edge e) {
        this.graph.removeEdge(e);
    }

    /*======================================================================
     * DECISION MODULE CONFIGURATION SECTION
     *====================================================================*/
    private DecisionInterface activeDecisionModule;
    
    private void configDecisionModule() {
        //addDecisionModuleToSwitcher(DecisionInterfaceImpl.class);        
        addDecisionModuleToSwitcher(BayesianAttackGraph.class);
        addDecisionModuleToSwitcher(BayesianAttackGraphAdapted.class);
        addDecisionModuleToSwitcher(AttackGraph.class);
        addDecisionModuleToSwitcher(ECSA.class);
        forceDecisionModule(1);
//        this.activeDecisionModule.init(this);
    }
    public void updateSelectedDecisionModule(boolean init) {
        try {
            this.activeDecisionModule = this.gui.getSelectedDecisionModule().newInstance();
            if(this.activeDecisionModule != null)
                if(init)
//                    this.activeDecisionModule.init(this);
            forceDecisionModule(this.gui.getSelectedDecisionModulePos());
        } catch (InstantiationException | IllegalAccessException ex) {
            Logger.getLogger(MainClass.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public DecisionInterface getActiveDecisionModule() {
        return this.activeDecisionModule;
    }
    
    public void forceDecisionModule(int f) throws NullPointerException {
        this.gui.setSelectedDecisionModule(f);
    }
    public void addDecisionModuleToSwitcher(Class<? extends DecisionInterface> toAdd) {
        this.gui.addDecisionModule(toAdd);
    }
    public void delDecisionModuleToSwitcher(int toDel) {
        this.gui.delDecisionModule(toDel);
    }
    public void startDecisionModule() {
        this.activeDecisionModule.init(this); //FIXME I run the init() only pressing Start Decision Module
        this.decisionThread = new Thread(this.activeDecisionModule);
        this.decisionThread.setName("Decision Module");
        this.decisionThread.start();

    }
    public void stopDecisionModule() {
        activeDecisionModule.stop();
    }
    
    public void updateNumberOfNodes(int i) {
        listeners.stream().forEach((hl) -> {
            hl.log("Number of nodes: "+i+"\n");
        });
    }
    
    public void react(Event e) {
        this.activeDecisionModule.react(e);
    }

    /*======================================================================
     * MAIN CLASS AND RUNNABLE SECTION
     *====================================================================*/
    private final Thread mainThread;
    private final Thread guiThread;
    private Thread eventThread;
    private Thread decisionThread;
    
    public boolean isDecisionThreadRunning() {
        return decisionThread!=null && decisionThread.isAlive();
    }
    
    public static void main(String[] args) throws InterruptedException {
        MainClass mainClassInstance = new MainClass();
        mainClassInstance.startMainClass();
    }
    
    public MainClass() {
        // initial configuration
        this.mainThread = new Thread(this);
        this.mainThread.setName("Main Class");
        
        // config EVENTSOURCE and relative thread
        eventStream = new EventStream(this);
        this.configEventStream();
        
        // config GUI and relative thread
        gui = new GUI(this);
        this.configGUI();
        this.guiThread = new Thread(gui);
        this.guiThread.setName("GUI");
        
        // config default decision module TODO check this out
        this.activeDecisionModule = new BayesianAttackGraph();
        this.configDecisionModule();
        
    }
    
    public void startMainClass() {
        this.mainThread.start();
    }
    
    @Override
    public void run() {
        this.startGUI();
        // the best option is to launch this from the GUI
        // this.startEventSource(); 
    }

    


    

    
    
}
