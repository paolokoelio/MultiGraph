/**
 * 
 */
package es.um.multigraph.decision.model.adapt;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import es.um.multigraph.decision.basegraph.Node;
import es.um.multigraph.decision.model.BayesianCMEdge;
import es.um.multigraph.decision.model.BayesianCMNode;
import es.um.multigraph.decision.model.BayesianNode;
import es.um.multigraph.event.solution.SI_SystemInformationIntegrity;
import es.um.multigraph.event.solution.Solution;

/**
 * @author Pavlo Burda - p.burda@tue.nl
 *
 */
public class BayesianCMGenerator {

	private Collection<? extends Node> bag;
	private static final String EXPLOIT_NODE = "vulExists";
	private static final String CM_NODE = "pn";
	private static final double DEFAULT_COST = 0.5;
	private static final double DEFAULT_prFalse = 1.0;
	private static final double DEFAULT_prTrue = 0.0;

	private Set<BayesianCMNode<Solution>> myCMNodes;
	private ArrayList<BayesianCMEdge> myCMEdges;

	public BayesianCMGenerator(Collection<? extends Node> bag) {
		this.bag = bag;
		this.myCMNodes = new HashSet<>();
		this.myCMEdges = new ArrayList<BayesianCMEdge>();
	}

	public void generateCMs() {

		for (Iterator<? extends Node> iterator = this.bag.iterator(); iterator.hasNext();) {

			BayesianNode node = (BayesianNode) iterator.next();

			ArrayList<String> facts = extractFacts(node.getLabel());
			if (facts.get(0).equals(EXPLOIT_NODE)) {

//				System.out.println("nodes Ids: " + node.getID());	//debug
				String nodeId = "p" + node.getID();

				BayesianCMNode<Solution> cm = new BayesianCMNode<>(nodeId, SI_SystemInformationIntegrity.SI_02);
				cm.getCountermeasure().setCost(DEFAULT_COST);

				String edgeId = nodeId + "." + node.getID();
				BayesianCMEdge cmEdge = new BayesianCMEdge(edgeId, cm, node);
				cmEdge.setDecomposition(BayesianCMEdge.DECOMPOSITION_AND);

				this.myCMNodes.add(cm);
				this.myCMEdges.add(cmEdge);
//				bag.enableCM(cm);

			}
		}

//		System.out.println("CM nodes: " + this.myCMNodes);	//debug
//		

	}

	/**
	 * TODO FIX code duplication in BayesianAdapter Extracts the facts as per MulVAL
	 * predicates and predicate name in the first position e.g. vulExist(facts...,
	 * vulExists, fileServer, 'CVE-7777', 40, nfs) => [vulExists, fileServer,
	 * 'CVE-7777', 40, nfs]
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
	 * Or function for char arrays and 
	 * @param nodesStates
	 * @param str
	 * @return
	 */
	public ArrayList<Boolean> or(ArrayList<Boolean> nodesStates, char[] str) {
		for(int j=0; j < str.length; j++) 
	       if(str[j] == '1' )
	        	nodesStates.set(j, true);
		return nodesStates;
     }
	
	public Set<BayesianCMNode<Solution>> getMyCMNodes() {
		return this.myCMNodes;
	}

	public ArrayList<BayesianCMEdge> getMyCMEdges() {
		return this.myCMEdges;
	}

	public static String getExploitNode() {
		return EXPLOIT_NODE;
	}

	public static String getCmNode() {
		return CM_NODE;
	}

	public static double getDefaultCost() {
		return DEFAULT_COST;
	}

	public static double getDefaultPrfalse() {
		return DEFAULT_prFalse;
	}

	public static double getDefaultPrtrue() {
		return DEFAULT_prTrue;
	}

}
