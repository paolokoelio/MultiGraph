/**
 * MULTIGRAPH
 *
 * Project for Mattia Zago Master Thesis
 *
 * (C) 2015 - Mattia Zago
 *
 */
package es.um.multigraph.decision.poolsappasitmoop;


/**
 *
 * @author Mattia Zago <a href="mailto:dev@zagomattia.it">dev@zagomattia.it</a>
 */
public class BayesianCMEdge extends BayesianEdge {
    public BayesianCMEdge(String ID, BayesianNode from, BayesianNode to) {
        super(ID, from, to);
    }
    
    private final Double prActivable = 0d;
    
    public Double getPrActivable() {
        return 0d;
    }
    
}
