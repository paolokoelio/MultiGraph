package es.um.multigraph.decision.lwang;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import es.um.multigraph.conf.MulVALPrimitives;
import es.um.multigraph.decision.basegraph.Node;

public class Adapter implements es.um.multigraph.decision.basegraph.Adapter {

	private Map<Integer, MyNode> myNodes;
	private Map<String, MyEdge> myEdges;
//	All edges that point to an AND node
	private Map<String, MyEdge> myToAndEdges;

	public Map<Integer, MyNode> getMyNodes() {
		return myNodes;
	}

	public Map<String, MyEdge> getMyEdges() {
		return myEdges;
	}

	private List<HashMap<String, String>> nodes;
	private List<HashMap<String, String>> edges;

	public void setMyEdges(List<HashMap<String, String>> myEdges) {
		this.edges = myEdges;
	}

	public void setMyNodes(List<HashMap<String, String>> myNodes) {
		this.nodes = myNodes;
	}

	public static final boolean DECOMPOSITION_AND = true;
	public static final boolean DECOMPOSITION_OR = false;
	public static final String TYPE_AND = "AND";
	public static final String TYPE_OR = "OR";
	public static final String PREFIX_ID = "n";

	public Adapter() {
		this.myNodes = new HashMap<Integer, MyNode>();
		this.myEdges = new HashMap<String, MyEdge>();
		this.myToAndEdges = new HashMap<String, MyEdge>();
	}

	@Override
	public void convertAG() {
		convertNodes();
		convertEdges();
		adaptAG();
	}

	public void convertNodes() {
		for (Iterator<HashMap<String, String>> iter = this.nodes.iterator(); iter.hasNext();) {
			HashMap<String, String> tmpNode = iter.next();

			ArrayList<String> facts = extractFacts(tmpNode.get("fact"));

			
			MyNode node = new MyNode(prependPrefix(PREFIX_ID, tmpNode.get("id")));
			node.setTypeMulval(tmpNode.get("type"));
			node.setLabel(tmpNode.get("fact"));
			
			if (facts.get(0).equals(MulVALPrimitives.VULN.getValue()))
				node.setType(true);
			else
				node.setType(false);
			
			//default state for an active node => true
			node.setState(true);

			this.myNodes.put(atoi(tmpNode.get("id")), node);
		}
	}

	public void convertEdges() {
		for (Iterator<HashMap<String, String>> iter = this.edges.iterator(); iter.hasNext();) {
			HashMap<String, String> tmpEdge = iter.next();

			// here src and dst are inverted, because in mulVAL output they seem actually
			// inverted
			MyNode dst = this.myNodes.get(atoi(tmpEdge.get("src")));
			MyNode src = this.myNodes.get(atoi(tmpEdge.get("dst")));

			String edgeId = src.getID() + "." + dst.getID();

			// decomposition OR is by default
			boolean flagDecomp = DECOMPOSITION_OR;

			MyEdge edge = new MyEdge(edgeId, src, dst);

			if (dst.getTypeMulval().equals(TYPE_AND)) {
				flagDecomp = DECOMPOSITION_AND;
				this.myToAndEdges.put(edge.getID(), edge);
			}

			edge.setDecomposition(flagDecomp);

			this.myEdges.put(edge.getID(), edge);
		}
	}

	/**
	 * Remove the AND nodes from MulVAL rules, transfer this logic to the edges and
	 * implement the condition and exploit nodes as per L.Wang et al. (similar to
	 * Poolsapapsit et al.)
	 */
	private void adaptAG() {
		List<String> purgeEdgeList = new ArrayList<String>();
		List<String> purgeNodeList = new ArrayList<String>();
		Map<String, MyEdge> bufferUpdatedEdges = new HashMap<String, MyEdge>();
		Node exploitNode = null;

		for (Iterator<String> iter = this.myToAndEdges.keySet().iterator(); iter.hasNext();) {
			MyEdge toAndEdge = this.myToAndEdges.get(iter.next());

			ArrayList<String> facts = extractFacts(toAndEdge.getFrom().getLabel());

			if (facts.get(0).equals(MulVALPrimitives.VULN.getValue()))
				exploitNode = toAndEdge.getFrom();

			// if toAnd is a vulExist node => point the other toAnds to this vulExist and
			// point vulExist to the dst of the other remaining edges
			if (exploitNode != null) {

//				First, re-point all siblings of expolitNode to him
				for (Iterator<String> iter2 = this.myEdges.keySet().iterator(); iter2.hasNext();) {
					MyEdge edge = this.myEdges.get(iter2.next());

//					if the edges toAnd and edge match the AND node and the toAnd.getFrom()
//					is an exploitNode pointing to that AND node and the edge.getFrom() _is not_ the exploitNode
					if (toAndEdge.getTo().getID().equals(edge.getTo().getID())
							& toAndEdge.getFrom().getID().equals(exploitNode.getID())
							& !(edge.getFrom().getID().equals(exploitNode.getID()))) {

//						then point all the other edges pointing to that common AND to the exploitNode
						edge.setTo(exploitNode);

						purgeEdgeList.add(edge.getID());
						edge.setID(edge.getFrom().getID() + "." + exploitNode.getID());
						bufferUpdatedEdges.put(edge.getID(), edge);
					}

				} // end of first for over myToAndEdges

//				Second, re-point the expolitNode to what its dst AND node is pointing to
				for (Iterator<String> iter2 = this.myEdges.keySet().iterator(); iter2.hasNext();) {
					MyEdge edge = this.myEdges.get(iter2.next());

//					if the edges toAnd and edge match the AND node and the toAnd.getFrom()
//					is an exploitNode pointing to that AND node
					if (toAndEdge.getTo().getID().equals(edge.getFrom().getID())
							& toAndEdge.getFrom().getID().equals(exploitNode.getID())) {
//						System.out.println("Repointing exploitEdge " + edge.getID());
//						Third, remove that AND node
						purgeNodeList.add(toAndEdge.getTo().getID());
						toAndEdge.setTo(edge.getTo());

						// set the decomposition to OR for terminal incoming edges/different exploit
						// nodes
						toAndEdge.setDecomposition(DECOMPOSITION_OR);

						purgeEdgeList.add(toAndEdge.getID());
						purgeEdgeList.add(edge.getID());
						toAndEdge.setID(toAndEdge.getFrom().getID() + "." + edge.getTo().getID());
						bufferUpdatedEdges.put(toAndEdge.getID(), toAndEdge);
					}

				} // end of second for over bayesianToAndEdges

			} // end if(exploitNode != null)
		}

		for (Iterator<String> iter = purgeEdgeList.iterator(); iter.hasNext();)
			this.myEdges.remove(iter.next());

		for (Iterator<String> iter = purgeNodeList.iterator(); iter.hasNext();)
			this.myNodes.remove(atoi(removePrefix(iter.next())));

		this.myEdges.putAll(bufferUpdatedEdges);
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
	 * Prepends the a string identifier "node" to the ID (because of consistency
	 * with Poolsappasit method) Not needed for real.
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

	private int atoi(String tmp) {
		int result = 0;
		for (int i = 0; i < tmp.length(); i++) {
			char digit = (char) (tmp.charAt(i) - '0');
			result += (digit * Math.pow(10, (tmp.length() - i - 1)));

		}
		return result;
	}

}
