/**
 * MULTIGRAPH
 *
 * Project for Mattia Zago Master Thesis
 *
 * (C) 2015 - Mattia Zago
 *
 */
package es.um.multigraph.decision.model;

import es.um.multigraph.decision.basegraph.Edge;
import es.um.multigraph.decision.basegraph.JDialogNode;
import es.um.multigraph.event.solution.*;
import java.util.HashSet;
import java.util.Set;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;

/**
 *
 * @author Mattia Zago <a href="mailto:dev@zagomattia.it">dev@zagomattia.it</a>
 * @param <S>
 */
public class BayesianCMNode<S extends Solution> extends BayesianNode {

    private Set<BayesianCMEdge> connectedEdges = new HashSet<>();

    public Set<BayesianCMEdge> getConnectedEdges() {
        return connectedEdges;
    }

    public void setConnectedEdges(Set<BayesianCMEdge> connectedEdges) {
        this.connectedEdges = connectedEdges;
    }
    
    public void addOutEdge(Edge edge) {
        super.addOutEdge(edge);
        if(edge instanceof BayesianCMEdge)
            connectedEdges.add((BayesianCMEdge) edge);
    }
    
    private S countermeasure;

    public S getCountermeasure() {
        return countermeasure;
    }

    public void setCountermeasure(S countermeasure) {
        this.countermeasure = countermeasure;
    }

    @Deprecated
    public BayesianCMNode(String ID, Double priorProbability) {
        super(ID, priorProbability);
    }

    public BayesianCMNode(String ID, S cm) {
        super(ID);
        this.label = "Countermeasure " + ID + ": "+cm.toString();
        this.countermeasure = cm;
        
    }
    
    @Override
    public String getFullRepresentationAsString(boolean printEdges) {
        return "Countermeasure\n\tSolution: " + this.countermeasure + "\n\t" + super.getFullRepresentationAsString(printEdges);
    }

    public void enable() {
        this.setState(STATE_TRUE);
        this.setUnconditionalPr(1d);
        this.setPosteriorPr(1d);
        this.setPriorPr(1d); 
    }

    public void disable() {
        this.setState(STATE_FALSE);
        this.setUnconditionalPr(0d);
        this.setPosteriorPr(0d);
        this.setPriorPr(0d);
    }

    private Double getAnyPr() {
        return this.getState() == STATE_TRUE ? 1d : 0d;
    }

    //==== UNCONDITIONAL =====================================================
    @Override
    public Double getUnconditionalPr() {
        return getAnyPr();
    }

    @Override
    public boolean hasUnconditionalPr() {
        return true;
    }

    //==== PRIOR =============================================================
    @Override
    public Double getPriorPr() throws UnsupportedClassVersionError {
        return getAnyPr();
    }


    @Override
    public boolean hasFixedPriorPr() throws UnsupportedClassVersionError {
        return true;
    }

    //==== POSTERIOR =========================================================
    @Override
    public Double getPosteriorPr() throws UnsupportedClassVersionError {
        return getAnyPr();
    }

    @Override
    public String toString() {
        return "Bayesian CM " + getID() + " " + (this.isEnabled() ? "ENABLED ("+getOut().size()+")" : "DISABLED");
    }

    @Override
    public String getLabelGraph() {
        return "CounterMeasure\n"
                + this.getID() + "\n"
                + (this.isEnabled() ? "ENABLED ("+getOut().size()+")" : "DISABLED") + "\n"
                + "Pr: " + String.format("%.2f", getAnyPr());
    }

    public static JDialogNode getJDialog() {
        return new BayesianJDialogCMNode(null, true);
    }

    public static ComboBoxModel getListModel() {
        DefaultComboBoxModel<Class<? extends Solution>> result = new DefaultComboBoxModel<>();

        result.addElement(AC_AccessControl.class);
        result.addElement(AT_AwarenessAndTraining.class);

        return result;
    }
    
    public boolean equals(BayesianCMNode tmp) {
        if(this.getID().equals(tmp.getID()))
            if(this.getCountermeasure().equals(tmp.getCountermeasure()))
                if(this.getState() == tmp.getState())
                    return true;
        
        return false;
    }

    boolean isEnabled() {
        return this.getState() == STATE_TRUE && getOut().size()>0;
    }
}
