package es.um.multigraph.decision;

import es.um.multigraph.conf.DBManager;
import es.um.multigraph.conf.FeaturesEnum;
import es.um.multigraph.conf.RiskScale;
import es.um.multigraph.core.MainClass;
import es.um.multigraph.decision.basegraph.Edge;
import es.um.multigraph.decision.basegraph.JDialogEdge;
import es.um.multigraph.decision.basegraph.Node;
import es.um.multigraph.event.Event;
import es.um.multigraph.event.EventListener;
import es.um.multigraph.event.solution.Solution;
import java.awt.Component;
import java.awt.Frame;
import java.net.URI;
import java.util.Collection;
import java.util.List;
import javax.swing.JFrame;

/**
 * Each theory model must implement this interface.
 * @author Mattia Zago <a href="mailto:dev@zagomattia.it">dev@zagomattia.it</a>
 * @param <N>
 * @param <E>
 */
public interface DecisionInterface<N extends Node,E extends Edge> extends EventListener, Runnable {
    /**
     * Called after the constructor, this method is designed for filling the 
     * configuration variables
     * @param main We need to pass the main class for creating a backward channel
     */
    public void init(MainClass main);
    
    /**
     * Must be called after init() method. Is intended as a list of event's 
     * family that the implemented model can recognize
     * @return List of enabled events.
     */
    public List<Class<? extends Event>> getRecognizedEvent();
    
    /**
     * Must be called after init() method. Is intended as a list of features 
     * that the implemented model can recognize;
     * @return List of enabled features
     */
    public List<FeaturesEnum> getEnabledFeatures();
    
    /**
     * Must be called after init() method. Start the decision process for the 
     * event passed as argument.
     * @param e Event
     * @return Specific solution instance
     */
    public Solution react(Event e);
    
    /**
     * 
     * @return 
     */
    public RiskScale getOverallRisk();
    
    /**
     * Load data from the local storage. Connection params must be specified in
     * the DBMS object.
     * @param database Connection class
     */
    public void loadState(DBManager database);
    
    /**
     * Return Paper's title
     * @return Paper's title
     */
    public String getPaperName();
    /**
     * Return Paper's authors list as string
     * @return Paper's authors list as string
     */
    public String getPaperAuthors();
    /**
     * Return Paper's DOI URL
     * @return Paper's DOI URL
     */
    public URI getPaperDOI();
    
    /**
     * Stop the current execution and terminate the thread
     */
    public void stop();

    public JFrame getModelConfigurationFrame();
    
    public Class<E> getEdgesClass();
    public Class<N> getNodesClass();

    public void addNode(N node);
    public void addEdge(E edge);

    public Collection<N> getNodes();
    public Collection<E> getEdges();

    public void delNode(N n);
    public void delEdge(E e);
}
