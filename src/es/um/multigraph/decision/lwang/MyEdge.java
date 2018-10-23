/**
 * MULTIGRAPH
 *
 * Project for Mattia Zago Master Thesis
 *
 * (C) 2015 - Mattia Zago
 *
 */
package es.um.multigraph.decision.lwang;

import es.um.multigraph.decision.basegraph.Edge;

/**
 *
 * @author Mattia Zago <a href="mailto:dev@zagomattia.it">dev@zagomattia.it</a>
 * @param <N> Node type
 */
public class MyEdge extends Edge {

	private boolean decomposition;
	public static final boolean DECOMPOSITION_AND = true;
	public static final boolean DECOMPOSITION_OR = false;

	
	public MyEdge(String ID, MyNode from, MyNode to) {
		super(ID, from, to);

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
