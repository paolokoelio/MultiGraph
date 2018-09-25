/**
 * MULTIGRAPH
 *
 * Project for Mattia Zago Master Thesis
 *
 * (C) 2015 - Mattia Zago
 *
 */
package es.um.multigraph.decision.model.adapt;

import es.um.multigraph.decision.model.BayesianEdge;
import es.um.multigraph.decision.model.adapt.BayesianNodeAdapted;

/**
 * An edge is a representation of an exploit between two different nodes/states.
 */
public class BayesianEdgeAdapted extends BayesianEdge {

	private BayesianNodeAdapted to;
	
	public BayesianEdgeAdapted(String ID, BayesianNodeAdapted from, BayesianNodeAdapted to) {
		super(ID, from, to);

	}
	
//	public void setTo(BayesianNodeAdapted to) {
//        this.to = to;
//    }

}
