package es.um.multigraph.decision.poolsappasitmoop.adapt;

import es.um.multigraph.decision.poolsappasitmoop.BayesianNode;

/**
 * Adding a type attribute to handle decomposition conversion from MulVAL
 * representation. The types are: AND - this kind of node is deleted and its src
 * nodes linked to its dst node, the label gets lost for now. OR - equivalent to
 * OR decomposition LEAF - initial conditions
 */
public class BayesianNodeAdapted extends BayesianNode {

	private String type;

	public BayesianNodeAdapted(String ID, Double priorProbability) {
		super(ID, priorProbability);

	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
