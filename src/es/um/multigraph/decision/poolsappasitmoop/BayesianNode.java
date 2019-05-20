/**
 * MULTIGRAPH
 *
 * Project for Mattia Zago Master Thesis
 *
 * (C) 2015 - Mattia Zago
 *
 */
package es.um.multigraph.decision.poolsappasitmoop;

import es.um.multigraph.decision.basegraph.Edge;
import es.um.multigraph.decision.basegraph.JDialogNode;
import es.um.multigraph.decision.basegraph.Node;
import es.um.multigraph.event.solution.Solution;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;
import javax.swing.JPanel;
import junit.framework.TestCase;
import org.junit.Test;

/**
 * According to the paper this class would represent the node in the attack
 * graph. It merges the attribute-template, state and graph navigation.
 */
public class BayesianNode extends Node {

    private boolean state = BayesianNode.STATE_FALSE;

    public void setState(boolean state) {
        this.state = state;
    }

    public boolean getState() {
        return this.state;
    }

    public static final boolean STATE_TRUE = true;
    public static final boolean STATE_FALSE = false;

    /**
     * FIXME
     *
     * @param ID
     * @param priorProbability Null iff state is not external.
     */
    public BayesianNode(String ID, Double priorProbability) {
        super(ID);
        this.fixedPriorPr = priorProbability;
    }

    public BayesianNode(String ID) {
        this(ID, Double.NaN);
    }

    @Override
    public String getFullRepresentationAsString(boolean printEdges) {
        String result = ""
                + "Bayesian Node (ID: " + this.ID + "):"
                + "\n\tClass:\t" + this.getClass()
                + "\n\tLabel:\t" + this.label
                + "\n\tPrior: " + String.format("%.2f", this.getPriorPr())
                + "\n\tPosterior: " + String.format("%.2f", this.getPosteriorPr())
                + "\n\tUnconditional: " + String.format("%.2f", this.getUnconditionalPr())
                + "\n\tExpected Loss: " + String.format("%.2f", this.getExpectedLoss())
                + "\n\tExpected Gain: " + String.format("%.2f", this.getExpectedGain())
                + "";

        if (printEdges) {
            result += "\n\tOut Edges:";
            for (Edge e : this.getOut()) {
                result += "\n\t\t" + e.getFullRepresentationAsString();
            }
            result += "\n\tIn Edges:";
            for (Edge e : this.getIn()) {
                result += "\n\t\t" + e.getFullRepresentationAsString();
            }
        }

        return result;
    }

    @Override
    public String getLabelGraph() {
        String l = this.getLabel();
        l += "\nPrior: " + String.format("%.2f", this.getPriorPr());
        l += "\nPosterior: " + String.format("%.2f", this.getPosteriorPr());
        l += "\nUnconditional: " + String.format("%.2f", this.getUnconditionalPr());
        return l;
    }

    //========================================================================
    // PROBABILITIES
    //========================================================================
    //==== UNCONDITIONAL =====================================================
    private Double unconditionalPr = Double.NaN;

    public Double getUnconditionalPr() {
        return this.unconditionalPr;
    }

    public void setUnconditionalPr(Double pr) {
        this.unconditionalPr = pr;
    }
    
    public void updateUnconditionalPr(Double pr) {
        this.unconditionalPr = getUnconditionalPr() * pr;
    }

    public boolean hasUnconditionalPr() {
        return this.unconditionalPr != Double.NaN;
    }

    //==== PRIOR =============================================================
    private Double fixedPriorPr = Double.NaN;

    public Double getPriorPr() {
        return this.fixedPriorPr;
    }

    public void setPriorPr(Double pr) {
        this.fixedPriorPr = pr;
    }

    public boolean hasFixedPriorPr() {
        return this.fixedPriorPr != Double.NaN;
    }

    @Test
    public void assertExternalNodeIfHasFixedPriorPr() {
        if (this.hasFixedPriorPr()) {
            TestCase.assertEquals(isExternal(), true);
        }
    }

    //==== POSTERIOR =========================================================
    Double posteriorPr = Double.NaN;

    public Double getPosteriorPr() {
        return this.posteriorPr;
    }

    public void setPosteriorPr(Double pr) {
        this.posteriorPr = pr;
    }

    public boolean hasPosteriorPr() {
        return this.posteriorPr != Double.NaN;
    }

    @Test
    public void assertStateTrueIfHasPosteriorPr() {
        if (this.hasPosteriorPr()) {
            TestCase.assertEquals(state, true);
        }
    }

    //========================================================================
    // NAVIGATION AND GRAPH QUERIES
    //========================================================================
    /**
     * If this state is compromised, and the input edges are in AND
     * decomposition then all nodes in its parent set must me also compromised.
     * Similarly, if the edges are in OR decomposition signifies that at least
     * one parent node is in the true state. With this method we will force all
     * parents to be compromised if the in-edges are in AND-decomposition.
     */
    public void forceParentsState() {
        if (this.parentDecomposition == BayesianEdge.DECOMPOSITION_AND) {
            in.stream().forEach((e) -> {
                ((BayesianNode) e.getFrom()).setState(true);
            });
        }
    }

    //========================================================================
    // STRUCTURAL PROP.
    //========================================================================
    private Set<BayesianEdge> out = new HashSet<>();
    private Set<BayesianEdge> in = new HashSet<>();
    private Set<BayesianNode> cachedParent = new HashSet<>();

    private boolean parentDecomposition = BayesianEdge.DECOMPOSITION_OR;

    public boolean getParentDecomposition() {
        return parentDecomposition;
    }

    public void setParentDecomposition(boolean parentDecomposition) {
        this.parentDecomposition = parentDecomposition;
    }

    public Set<BayesianNode> getAllAncestor() {
        Set<BayesianNode> result = new HashSet<>();
        for (Iterator<Edge> it = this.getIn().iterator(); it.hasNext();) {
            BayesianNode tmp = (BayesianNode) it.next().getFrom();
            result.add(tmp);
            result.addAll(tmp.getAllAncestor());
        }

        return result;
    }
    
    //TODO decide if to keep
    public Set<BayesianNode> getAllDescendants() {
        Set<BayesianNode> result = new LinkedHashSet<>();
        for (Iterator<Edge> it = this.getOut().iterator(); it.hasNext();) {
            BayesianNode tmp = (BayesianNode) it.next().getTo();
            result.add(tmp);
            result.addAll(tmp.getAllDescendants());
        }

        return result;
    }
    
    public Set<BayesianNode> getAllParents() {
        Set<BayesianNode> result = new HashSet<>();
        for (Iterator<Edge> it = this.getIn().iterator(); it.hasNext();) {
            BayesianNode tmp = (BayesianNode) it.next().getFrom();
            result.add(tmp);
            //result.addAll(tmp.getAllAncestor());
        }

        return result;
    }

    public static JPanel getJPanel(BayesianNode node) {
        JPanel result = new JPanel();
        Field[] ff = node.getClass().getFields();
        result.setLayout(new GridLayout(ff.length, 2));

        result.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;

        int i = 0;
        for (Field f : ff) {
            if (String.class.isAssignableFrom(f.getType()) || Double.class.isAssignableFrom(f.getType())) {
                c.gridx = i;
                c.gridy = 0;
                result.add(new JLabel(f.getName()), c);
                c.gridx = i++;
                c.gridy = 1;
                try {
                    result.add(new JLabel(f.get(node).toString()), c);
                } catch (IllegalArgumentException | IllegalAccessException ex) {
                    result.add(new JLabel(""), c);
                    Logger.getLogger(Node.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return result;
    }

    //========================================================================
    // COUNTERMEASURE
    //========================================================================    
    Set<Solution> countermeasures = new HashSet<>();

    public Set<Solution> getCountermeasures() {
        return countermeasures;
    }

    public void setCountermeasures(Set<Solution> countermeasures) {
        this.countermeasures = countermeasures;
    }

    private Double expectedLoss = 1d;
    private Double expectedGain = 0d;

    public Double getExpectedLoss() {
        return expectedLoss;
    }

    public void setExpectedLoss(Double expectedLoss) {
        this.expectedLoss = expectedLoss;
    }

    public Double getExpectedGain() {
        return expectedGain;
    }

    public void setExpectedGain(Double expectedGain) {
        this.expectedGain = expectedGain;
    }

    public Double getExpectedLossGain() {
        if (this.isExternal()) {
            return 0d;
        }
        
        Double GainFact = (1 - this.getUnconditionalPr()) * this.getExpectedGain();
        Double LossFact = this.getUnconditionalPr() * this.getExpectedLoss();
        return GainFact - LossFact;
    }

    public static JDialogNode getJDialog() {
        return new BayesianJDialogNode(null, true);
    }

}
