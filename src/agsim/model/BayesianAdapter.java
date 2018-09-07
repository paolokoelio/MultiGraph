package agsim.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import es.um.multigraph.decision.model.BayesianNode;

public class BayesianAdapter implements Adapter {

	private Set<BayesianNode> BAG;
	private List<HashMap<String, String>> nodes;
	private List<HashMap<String, String>> edges;

	public BayesianAdapter() {
		this.BAG = new HashSet<BayesianNode>();
	}

	@Override
	public void convertAG() {
		
	}

	public Set<BayesianNode> getBAG() {
		return BAG;
	}

	public void setBAG(Set<BayesianNode> bAG) {
		BAG = bAG;
	}

	public List<HashMap<String, String>> getMyNodes() {
		return nodes;
	}

	public void setMyNodes(List<HashMap<String, String>> myNodes) {
		this.nodes = myNodes;
	}

	public List<HashMap<String, String>> getMyEdges() {
		return edges;
	}

	public void setMyEdges(List<HashMap<String, String>> myEdges) {
		this.edges = myEdges;
	}

}
