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
import es.um.multigraph.decision.basegraph.Node;
import it.zagomattia.cvss.base.CVSSBase;

/**
 * An edge is a representation of an exploit between two different nodes/states.
 */
public class BayesianEdge extends Edge {

	private CVSSBase cvssBaseScore;
	private Double prActivable = Double.NaN;
	private boolean decomposition;
	public static final boolean DECOMPOSITION_AND = true;
	public static final boolean DECOMPOSITION_OR = false;

	public BayesianEdge(String ID, BayesianNode from, BayesianNode to) {
		super(ID, from, to);

	}

	/**
	 * Given the vulnerability exposure information (CVSS attribute) the probability
	 * of success while executing a given vulnerability exploitation is computed
	 * from CVSS Exploitability sub-score as: 2 * B_AV * B_AC * B_AU
	 *
	 * @see CVSSBase
	 * @return 2 * B_AV * B_AC * B_AU
	 */
	public Double getPrActivable() {
		if (this.prActivable == Double.NaN)
			return 2 * cvssBaseScore.getAccessVector().getValue() * cvssBaseScore.getAccessComplexity().getValue()
					* cvssBaseScore.getAuthentication().getValue();
		else
			return this.prActivable;
	}

	public void setPrActivable(CVSSBase cvssVector) {
		this.cvssBaseScore = cvssVector;
		this.prActivable = 2 * cvssBaseScore.getAccessVector().getValue()
				* cvssBaseScore.getAccessComplexity().getValue() * cvssBaseScore.getAuthentication().getValue();
	}

	public void setOverridePrActivable(Double d) {
		this.prActivable = d;
	}

	/*
	 * GETTER AND SETTER
	 */
	public boolean isDecompositionAND() {
		return decomposition;
	}

	public boolean isDecompositionOR() {
		return !decomposition;
	}

	public void setDecomposition(boolean decomposition) {
		this.decomposition = decomposition;
	}

	void propagateEdge() {
		this.getFrom().getOut().add(this);
		this.getTo().getIn().add(this);

	}

	@Override
	public String getLabelGraph() {
		return "";
	}

}
