package es.um.multigraph.decision.poolsappasitmoop.adapt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import es.um.multigraph.conf.MulVALPrimitives;
import es.um.multigraph.decision.basegraph.Adapter;
import es.um.multigraph.decision.basegraph.Node;
import es.um.multigraph.decision.poolsappasitmoop.BayesianNode;

public class BayesianAdapter implements Adapter {

	private Map<Integer, BayesianNodeAdapted> bayesianNodes;
	private Map<String, BayesianEdgeAdapted> bayesianEdges;
//	All edges that point to an AND node
	private Map<String, BayesianEdgeAdapted> bayesianToAndEdges;

	private List<HashMap<String, String>> nodes;
	private List<HashMap<String, String>> edges;

	public static final boolean DECOMPOSITION_AND = true;
	public static final boolean DECOMPOSITION_OR = false;
	public static final String TYPE_AND = "AND";
	public static final String TYPE_OR = "OR";

	public static final double DUMMY_EXPECTED_GAIN = 1;
	public static final double DUMMY_EXPECTED_LOSS = 1;
	public static final double DUMMY_EDGE_PROB = 0.6;

//	private static final Double DEFAULT_UNC_PR = 1.0;
	private static final Double DEFAULT_PRIOR_PR = 1.0;

	public static final String PREFIX_ID = "n";
	// types AND, OR and LEAF

	public BayesianAdapter() {
		this.bayesianNodes = new HashMap<Integer, BayesianNodeAdapted>();
		this.bayesianEdges = new HashMap<String, BayesianEdgeAdapted>();
		this.bayesianToAndEdges = new HashMap<String, BayesianEdgeAdapted>();
	}

	@Override
	public void convertAG() {
		
		convertNodes();
		convertEdges();
		adaptAG();
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
			BayesianNodeAdapted bsNode = new BayesianNodeAdapted(prependPrefix(PREFIX_ID, tmpNode.get("id")),
					Double.NaN);
			bsNode.setType(tmpNode.get("type"));
			bsNode.setLabel(tmpNode.get("fact"));

			bsNode.setExpectedGain(DUMMY_EXPECTED_GAIN);
			bsNode.setExpectedLoss(DUMMY_EXPECTED_LOSS);
//			if (tmpNode.get("type").equals("LEAF")) // TODO set static
//				bsNode.setPriorPr(DEFAULT_PRIOR_PR);

//			bsNode.setUnconditionalPr(DEFAULT_UNC_PR);
			this.bayesianNodes.put(atoi(tmpNode.get("id")), bsNode);
		}
	}

	/**
	 * Instantiate and populate BayseianEdges
	 */
	public void convertEdges() {
		for (Iterator<HashMap<String, String>> iter = this.edges.iterator(); iter.hasNext();) {
			HashMap<String, String> tmpEdge = iter.next();

			// here src and dst are inverted, because in mulVAL output they seem actually
			// inveretd
			BayesianNodeAdapted dst = this.bayesianNodes.get(atoi(tmpEdge.get("src")));
			BayesianNodeAdapted src = this.bayesianNodes.get(atoi(tmpEdge.get("dst")));

			String edgeId = src.getID() + "." + dst.getID();

			// decomposition OR is by default
			boolean flagDecomp = DECOMPOSITION_OR;

			BayesianEdgeAdapted bsEdge = new BayesianEdgeAdapted(edgeId, src, dst);

			if (dst.getType().equals(TYPE_AND)) {
				flagDecomp = DECOMPOSITION_AND;
				this.bayesianToAndEdges.put(bsEdge.getID(), bsEdge);
			}

			bsEdge.setDecomposition(flagDecomp);

			this.bayesianEdges.put(bsEdge.getID(), bsEdge);
		}
	}

	/**
	 * Remove the AND nodes from MulVAL rules, transfer this logic to the edges and
	 * implement the pre-condition, vulnerability and post-condition attributes as
	 * per Poolsapapsit et al.
	 */
	private void adaptAG() {
		List<String> purgeEdgeList = new ArrayList<String>();
		List<String> purgeNodeList = new ArrayList<String>();
		Map<String, BayesianEdgeAdapted> bufferUpdatedEdges = new HashMap<String, BayesianEdgeAdapted>();
		Node exploitNode = null;

		for (Iterator<String> iter = this.bayesianToAndEdges.keySet().iterator(); iter.hasNext();) {
			BayesianEdgeAdapted toAndEdge = this.bayesianToAndEdges.get(iter.next());

			ArrayList<String> facts = extractFacts(toAndEdge.getFrom().getLabel());

			if (facts.get(0).equals(MulVALPrimitives.VULN.getValue()))
				exploitNode = toAndEdge.getFrom();

			// if toAnd is a vulExist node => point the other toAnds to this vulExist and
			// point vulExist to the dst of the other remaining edges
			if (exploitNode != null) {

//				First, re-point all siblings of expolitNode to him
				for (Iterator<String> iter2 = this.bayesianEdges.keySet().iterator(); iter2.hasNext();) {
					BayesianEdgeAdapted edge = this.bayesianEdges.get(iter2.next());

//					if the edges toAnd and edge match the AND node and the toAnd.getFrom()
//					is an exploitNode pointing to that AND node and the edge.getFrom() _is not_ the exploitNode
					if (toAndEdge.getTo().getID().equals(edge.getTo().getID())
							& toAndEdge.getFrom().getID().equals(exploitNode.getID())
							& !(edge.getFrom().getID().equals(exploitNode.getID()))) {

//						then point all the other edges pointing to that common AND to the exploitNode
						edge.setTo(exploitNode);

						// set the edge CVSS-based probability, we set the prob. to both in and out
						// edges of the vulnerability attribute
						// because we don't know exactly what is the edge prob of an edge pointing to
						// the vulnerability attribute
						edge.setOverridePrActivable(0.01 * atoi(facts.get(3)));

						purgeEdgeList.add(edge.getID());
						edge.setID(edge.getFrom().getID() + "." + exploitNode.getID());
						bufferUpdatedEdges.put(edge.getID(), edge);
					}

				} // end of first for over bayesianToAndEdges

//				Second, re-point the expolitNode to what its dst AND node is pointing to
				for (Iterator<String> iter2 = this.bayesianEdges.keySet().iterator(); iter2.hasNext();) {
					BayesianEdgeAdapted edge = this.bayesianEdges.get(iter2.next());

//					if the edges toAnd and edge match the AND node and the toAnd.getFrom()
//					is an exploitNode pointing to that AND node
					if (toAndEdge.getTo().getID().equals(edge.getFrom().getID())
							& toAndEdge.getFrom().getID().equals(exploitNode.getID())) {
//						System.out.println("Repointing exploitEdge " + edge.getID());
//						Third, remove that AND node
						purgeNodeList.add(toAndEdge.getTo().getID());
						toAndEdge.setTo(edge.getTo());

						// set the edge CVSS-based probability, we set the prob. to both in and out
						// edges of the vulnerability attribute
						// because we don't know exactly what is the edge prob of an edge pointing to
						// the vulnerability attribute
						toAndEdge.setOverridePrActivable(0.01 * atoi(facts.get(3)));

						// set the decomposition to OR for terminal incoming edges/different exploit
						// nodes
						toAndEdge.setDecomposition(DECOMPOSITION_OR);

						purgeEdgeList.add(toAndEdge.getID());
						purgeEdgeList.add(edge.getID());
						toAndEdge.setID(toAndEdge.getFrom().getID() + "." + edge.getTo().getID());
						bufferUpdatedEdges.put(toAndEdge.getID(), toAndEdge);
					}

					// set priorPr to leaf nodes explicitly excluding the exploit nodes (vulExists
					// type)
					if (edge.getFrom().getIn().isEmpty()
							& !(extractFacts(edge.getFrom().getLabel()).get(0).equals(MulVALPrimitives.VULN.getValue())))
						((BayesianNode) edge.getFrom()).setPriorPr(DEFAULT_PRIOR_PR);

				} // end of second for over bayesianToAndEdges

			} // end if(exploitNode != null)
		}

		for (Iterator<String> iter = purgeEdgeList.iterator(); iter.hasNext();)
			this.bayesianEdges.remove(iter.next());

		for (Iterator<String> iter = purgeNodeList.iterator(); iter.hasNext();)
			this.bayesianNodes.remove(atoi(removePrefix(iter.next())));

		this.bayesianEdges.putAll(bufferUpdatedEdges);
	}

	/**
	 * Extracts the facts as per MulVAL predicates and predicate name in the first
	 * position e.g. vulExist(facts..., vulExists, fileServer, 'CVE-7777', 40, nfs)
	 * => [vulExists, fileServer, 'CVE-7777', 40, nfs]
	 */
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

	/**
	 * Prepends the a string identifier "node" to the ID (because of column creation
	 * error in SQL instruction
	 */
	private String prependPrefix(String s, String n) {
		return s + n;
	}

	/**
	 * Removes the a string identifier, PREFIX_ID, from the ID
	 */
	private String removePrefix(String s) {
		return s.replaceFirst(PREFIX_ID, "");
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
