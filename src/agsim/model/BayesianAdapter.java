package agsim.model;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import es.um.multigraph.decision.basegraph.Edge;
import es.um.multigraph.decision.basegraph.Node;
import es.um.multigraph.decision.model.BayesianEdge;

public class BayesianAdapter implements Adapter {

	private Map<Integer, BayesianNodeAdapted> bayesianNodes;
	private Map<String, BayesianEdgeAdapted> bayesianEdges;
	private Map<String, BayesianEdgeAdapted> bayesianToAndEdges;

	private List<HashMap<String, String>> nodes;
	private List<HashMap<String, String>> edges;
	public static final boolean DECOMPOSITION_AND = true;
	public static final boolean DECOMPOSITION_OR = false;
	public static final double DUMMY_EXPECTED_GAIN = 0.5;
	public static final double DUMMY_EDGE_PROB = 0.6;
	public static final String TYPE_AND = "AND";
	public static final String TYPE_OR = "OR";
	// types AND, OR and LEAF

	public BayesianAdapter() {
		this.bayesianNodes = new HashMap<Integer, BayesianNodeAdapted>();
		this.bayesianEdges = new HashMap<String, BayesianEdgeAdapted>();
		this.bayesianToAndEdges = new HashMap<String, BayesianEdgeAdapted>();
	}

	@Override
	public void convertAG() {
//		System.out.println("Converting to Poolsappasit et al. form!\n");

		convertNodes();
		convertEdges();
		removeAnds();

//		debug
//		System.out.print("\n Bayesian Nodes: " + this.bayesianNodes);
//		System.out.print("\n Bayesian Edges: " + this.bayesianEdges);
	}

	/**
	 * Prepends the a string identifier "node" to the ID (because of column creation
	 * error in SQL instruction
	 */
	private String prependPrefix(String s, String n) {
		return s + n;
	}

	/**
	 * Rmoves the a string identifier "node" to the ID
	 */
	private String removePrefix(String s) {
		return s.replaceFirst("n", "");
	}

	/**
	 * Instantiate and populate BayesianNodesAdapted (see doc
	 * {@link} BayesianNodesAdapted}.
	 */
	public void convertNodes() {
		for (Iterator<HashMap<String, String>> iter = this.nodes.iterator(); iter.hasNext();) {
			HashMap<String, String> tmpNode = iter.next();

			// TODO set prioProb to metric somehow in the future
			// the Node ID is added a string to relief SQL problem in adding new columns
			// named with an integer (TODO integrate uuids)
			BayesianNodeAdapted bsNode = new BayesianNodeAdapted(prependPrefix("n", tmpNode.get("id")), Double.NaN);
			bsNode.setType(tmpNode.get("type"));
			bsNode.setLabel(tmpNode.get("type") + " " + tmpNode.get("fact"));
			bsNode.setExpectedGain(DUMMY_EXPECTED_GAIN);
			this.bayesianNodes.put(atoi(tmpNode.get("id")), bsNode);
			// iter.remove();
		}
	}

	/**
	 * Instantiate and populate BayseianEdges
	 */
	public void convertEdges() {
		for (Iterator<HashMap<String, String>> iter = this.edges.iterator(); iter.hasNext();) {
			HashMap<String, String> tmpEdge = iter.next();

			// here src and dst are inverted, because in mulVAL output they seem actually
			// inverted
			BayesianNodeAdapted dst = this.bayesianNodes.get(atoi(tmpEdge.get("src")));
			BayesianNodeAdapted src = this.bayesianNodes.get(atoi(tmpEdge.get("dst")));

			String edgeLabel = src.getID() + "." + dst.getID();

			// decomposition OR is by default
			boolean flagDecomp = DECOMPOSITION_OR;
			if (dst.getType().equals(TYPE_AND)) {
				// se e' AND allora cerco il dst dell'AND, lo conservo
				// e lo faccio diventare il dst di quello che aveva l' AND come dst

				BayesianEdgeAdapted bsEdge = new BayesianEdgeAdapted(edgeLabel, src, dst);

//				BayesianNodeAdapted andDst = findDests(dst);
				// TOFIX what to do with the exploit label?
				// edgeLabel = dst.getLabel();
				// System.out.println(i++ + " " + andDst.getID());
				// TODO fix removal of AND nodes
//				dst = andDst;

				flagDecomp = DECOMPOSITION_AND;// set decomp AND
				bsEdge.setDecomposition(flagDecomp);
				bsEdge.setOverridePrActivable(DUMMY_EDGE_PROB);
				this.bayesianToAndEdges.put(bsEdge.getID(), bsEdge);
//				continue;
			}

			BayesianEdgeAdapted bsEdge = new BayesianEdgeAdapted(edgeLabel, src, dst);
			bsEdge.setDecomposition(flagDecomp);

			// TODO fix this with real metrics
			bsEdge.setOverridePrActivable(DUMMY_EDGE_PROB);

			this.bayesianEdges.put(bsEdge.getID(), bsEdge);
//			iter.remove();

		}
	}

	private void removeAnds() {

//		System.out.println(this.edges);
		System.out.println(this.bayesianEdges);

		for (Iterator<String> iter = this.bayesianToAndEdges.keySet().iterator(); iter.hasNext();) {
			BayesianEdgeAdapted toAnd = this.bayesianToAndEdges.get(iter.next());

			for (Iterator<String> iter2 = this.bayesianEdges.keySet().iterator(); iter2.hasNext();) {
				BayesianEdgeAdapted fromAnd = this.bayesianEdges.get(iter2.next());

				if (toAnd.getTo().getID().equals(fromAnd.getFrom().getID())) {
//					System.out.println(toAnd + " - " + fromAnd);
					toAnd.setTo(fromAnd.getTo()); //TODO FIX
				}
			}

//			// here src and dst are inverted, because in mulVAL they're actually inverted
//			BayesianNodeAdapted dst = this.bayesianNodes.get(atoi(tmpEdge.get("src")));
//			BayesianNodeAdapted src = this.bayesianNodes.get(atoi(tmpEdge.get("dst")));
//
//			String edgeLabel = src.getID() + "->" + dst.getID();
//
//			// decomposition OR is by default
//			boolean flagDecomp = DECOMPOSITION_OR;
//			if (dst.getType().equals(TYPE_AND)) {
//				// se e' AND allora cerco il dst dell'AND, lo conservo
//				// e lo faccio diventare il dst di quello che aveva ;' AND come dst
//				BayesianNodeAdapted andDst = findDests(dst);
//				// TOFIX what to do with the exploit label?
//				// edgeLabel = dst.getLabel();
//				// System.out.println(i++ + " " + andDst.getID());
//				dst = andDst;
//				flagDecomp = DECOMPOSITION_AND;// set decomp AND
//			}
//
//			BayesianEdge bsEdge = new BayesianEdge(edgeLabel, src, dst);
//			bsEdge.setDecomposition(flagDecomp);
//
//			// TODO fix this with real metrics
//			bsEdge.setOverridePrActivable(DUMMY_EDGE_PROB);
//
//			this.bayesianEdges.put(bsEdge.getID(), bsEdge);
//			iter.remove();

		}
//		System.out.println(this.bayesianToAndEdges);
	}

	// TODO FIX
	private BayesianNodeAdapted findDests(BayesianNodeAdapted dst) {
		String id = removePrefix(dst.getID());
		// System.out.println(id);
		String tmp = "";
		// HashMap<String, String> n = this.nodes.get(atoi(removePrefix(dst.getID())));

		for (Iterator<HashMap<String, String>> iter = this.nodes.iterator(); iter.hasNext();) {
			HashMap<String, String> tmpNode = iter.next();
			// System.out.println(tmpNode.get("id"));
			if (tmpNode.get("id").equals(id)) {
				// System.out.println("Found node in parsed nodes: " + tmpNode.get("fact"));
				tmpNode.get("");
			}
			tmp = "yo";
		}

		return this.bayesianNodes.get(atoi(tmp));
	}

	public Map<Integer, BayesianNodeAdapted> getBAG() {
		return bayesianNodes;
	}

	public void setBAG(Map<Integer, BayesianNodeAdapted> bAG) {
		bayesianNodes = bAG;
	}

	public Map<Integer, BayesianNodeAdapted> getMyBayesianNodes() {
		return this.bayesianNodes;
	}

	public void setMyNodes(List<HashMap<String, String>> myNodes) {
		this.nodes = myNodes;
	}

	public Map<String, BayesianEdgeAdapted> getMyBayesianEdges() {
		return this.bayesianEdges;
	}

	public void setMyEdges(List<HashMap<String, String>> myEdges) {
		this.edges = myEdges;
	}

	private int atoi(String tmp) {
		int result = 0;
		for (int i = 0; i < tmp.length(); i++) {
			char digit = (char) (tmp.charAt(i) - '0');
			result += (digit * Math.pow(10, (tmp.length() - i - 1)));

		}
		return result;
	}

}
