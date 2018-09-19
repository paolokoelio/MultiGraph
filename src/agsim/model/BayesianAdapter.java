package agsim.model;

import java.util.ArrayList;
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
//	All edges that point to an AND node
	private Map<String, BayesianEdgeAdapted> bayesianToAndEdges;

	private List<HashMap<String, String>> nodes;
	private List<HashMap<String, String>> edges;
	public static final boolean DECOMPOSITION_AND = true;
	public static final boolean DECOMPOSITION_OR = false;
	public static final double DUMMY_EXPECTED_GAIN = 0.5;
	public static final double DUMMY_EDGE_PROB = 0.6;
	public static final String TYPE_AND = "AND";
	public static final String TYPE_OR = "OR";
	private static final Double DEFAULT_UNC_PR = 1.0;
	public static final String EXPLOIT_NODE = "vulExists";
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
			bsNode.setLabel(tmpNode.get("fact"));

//			System.out.print(tmpNode.get(""));

			bsNode.setExpectedGain(DUMMY_EXPECTED_GAIN);
//			bsNode.setUnconditionalPr(DEFAULT_UNC_PR);
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

				flagDecomp = DECOMPOSITION_AND;
				bsEdge.setDecomposition(flagDecomp);
				bsEdge.setOverridePrActivable(DUMMY_EDGE_PROB);
				this.bayesianToAndEdges.put(bsEdge.getID(), bsEdge);
				// continue; // TODO pay attention here
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
		System.out.println("Edges befor AND removal " + this.bayesianEdges);

		List<String> purgeEdgeList = new ArrayList<String>();
		List<String> purgeNodeList = new ArrayList<String>();
		Map<String, BayesianEdgeAdapted> bufferUpdatedEdges = new HashMap<String, BayesianEdgeAdapted>();
		Node exploitNode = null;
		Node andNode = null;

		for (Iterator<String> iter = this.bayesianToAndEdges.keySet().iterator(); iter.hasNext();) {
			BayesianEdgeAdapted toAnd = this.bayesianToAndEdges.get(iter.next());

			// if toAnd is a a vulExist node => point the other toAnds to this vulExist and
			// point vulExist to the fromAnd
			ArrayList<String> facts = extractFacts(toAnd.getFrom().getLabel());
			if (facts.get(0).equals(EXPLOIT_NODE)) {
				System.out.println(facts.get(0));
				exploitNode = toAnd.getFrom();
			}

			if (exploitNode != null) {
				System.out.println("Inside exploitEdge!" + exploitNode.getID());

//				First, re-point all siblings of expolitNode to him
				for (Iterator<String> iter2 = this.bayesianEdges.keySet().iterator(); iter2.hasNext();) {
					BayesianEdgeAdapted edge = this.bayesianEdges.get(iter2.next());

//					if the edges toAnd and edge match the AND node and the toAnd.getFrom()
//					is an exploitNode pointing to that AND node and the fromAnd.getFrom() _is not_ the exploitNode
					System.out.println("DEBUG: " + toAnd.getTo().getID() + "=" + edge.getTo().getID() + " AND "
							+ toAnd.getFrom().getID() + "=" + exploitNode.getID() + " AND not " + edge.getFrom().getID()
							+ "=" + exploitNode.getID());
					if (toAnd.getTo().getID().equals(edge.getTo().getID())
							& toAnd.getFrom().getID().equals(exploitNode.getID())
							& !(edge.getFrom().getID().equals(exploitNode.getID()))) {
//					then do point all the other edges pointing to that common AND to the exploitNode and the exploitNode to the AND's node dst
						System.out.println("Not an exploitEdge " + edge.getID());
						edge.setTo(exploitNode);
						purgeEdgeList.add(edge.getID());
						edge.setID(edge.getFrom().getID() + "." + exploitNode.getID());
						bufferUpdatedEdges.put(edge.getID(), edge);

					}

//					original impl. of AND removal
////				looking for matching destination and source edges
//					if (toAnd.getTo().getID().equals(fromAnd.getFrom().getID())) {
////					System.out.println(toAnd + " - " + fromAnd);
//						toAnd.setTo(fromAnd.getTo()); // TODO FIX
////					System.out.println(toAnd.getFrom().getID() + "." + fromAnd.getTo().getID());
//						// set new id for the Edge
//						purgeList.add(toAnd.getID());
//						toAnd.setID(toAnd.getFrom().getID() + "." + fromAnd.getTo().getID());
//						bufferUpdatedEdges.put(toAnd.getID(), toAnd);
//					}

				} // end of first for over bayesianToAndEdges

//				Second, re-point the expolitNode to what its dst AND node is pointing to
				for (Iterator<String> iter2 = this.bayesianEdges.keySet().iterator(); iter2.hasNext();) {
					BayesianEdgeAdapted edge = this.bayesianEdges.get(iter2.next());

//					if the edges toAnd and edge match the AND node and the toAnd.getFrom()
//					is an exploitNode pointing to that AND node and the fromAnd.getFrom() _is not_ the exploitNode
					if (toAnd.getTo().getID().equals(edge.getFrom().getID())
							& toAnd.getFrom().getID().equals(exploitNode.getID())) {
						System.out.println("Repointing exploitEdge " + edge.getID());
//						Third, remove that AND node
						purgeNodeList.add(toAnd.getTo().getID());
						toAnd.setTo(edge.getTo());
						purgeEdgeList.add(toAnd.getID());
						purgeEdgeList.add(edge.getID());
						toAnd.setID(toAnd.getFrom().getID() + "." + edge.getTo().getID());
						bufferUpdatedEdges.put(toAnd.getID(), toAnd);

					}
				} // end of second for over bayesianToAndEdges

//				 iter.remove();
//				exploitNode = null;

			} // end if(exploitNode != null)
		}

		for (Iterator<String> iter = purgeEdgeList.iterator(); iter.hasNext();) {
			String next = iter.next();
//			System.out.println("deleting this: " + next);
			this.bayesianEdges.remove(next);
		}
		for (Iterator<String> iter = purgeNodeList.iterator(); iter.hasNext();) {
			String next = iter.next();
			System.out.println("Purging node: " + next);
			this.bayesianNodes.remove(atoi(removePrefix(next)));
		}

//		System.out.println(this.bayesianToAndEdges);
//		System.out.println(bufferUpdatedEdges);
		this.bayesianEdges.putAll(bufferUpdatedEdges);
		System.out.println("Edges after AND removal " + this.bayesianEdges);
	}

	private ArrayList<String> extractFacts(String facts) {

		ArrayList<String> factList = new ArrayList<String>();
		String[] splitString = (facts.split("\\("));
		// the first element is the primitive/derivate, the rest are
		// variables/parameters of it
		factList.add(splitString[0]);
		String[] splitSubString = (splitString[1].split("\\)"));
		String[] factParams = (splitSubString[0].split(","));
		for (String string : factParams)
			factList.add(string);

		return factList;

	}

	// TODO FIX TO remove
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
