package es.um.multigraph.decision.lwang;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import es.um.multigraph.decision.basegraph.Node;

/**
 * Hardening procedure implementation as described in L.Wang et al.
 * 
 * @author Pavlo Burda <a href="mailto:p.burda@tue.nl">p.burda@tue.nl</a>
 */

public class NetworkHardening {

	AttackGraph AG;
	private List<MyNode> nodes;
	private Set<MyNode> exploits;
	private Set<MyNode> conditions;

	private List<MyEdge> edges;
	private List<MyNode> goals;
	// result
	private List<MyNode> L;
	// temporary queue
	private List<MyNode> queue;
	// Sets of Pre(e) and Pre(c) for every e and c in the AG
	private Map<MyNode, List<MyNode>> preE;
	private Map<MyNode, List<MyNode>> preC;

	public NetworkHardening(AttackGraph AG, List<MyNode> goals) {
		this.AG = AG;
		this.goals = goals;
		this.queue = new ArrayList<MyNode>();
		this.exploits = new HashSet<MyNode>();
		this.conditions = new HashSet<MyNode>();
		this.L = new ArrayList<MyNode>();
		this.preE = new HashMap<MyNode, List<MyNode>>();
		this.preC = new HashMap<MyNode, List<MyNode>>();

		/*
		 * Prepare the queues
		 */
		for (Iterator<MyNode> iterator = goals.iterator(); iterator.hasNext();) {
			MyNode myNode = (MyNode) iterator.next();
			this.queue.add(myNode);
			this.L.add(myNode);
		}

		for (Iterator<Node> iterator = (Iterator<Node>) this.AG.getNodes().iterator(); iterator.hasNext();) {
			MyNode myNode = (MyNode) iterator.next();

//			System.out.println(myNode.getID() + "(" + myNode.getSourceHost() + "," + myNode.getDestHost() + ") - "
//					+ myNode.getLabel() + " - state: " + myNode.getState() + " - type: " + myNode.getType());

			if (myNode.getTypeBool() == MyNode.EXPLOIT) {
				this.exploits.add(myNode);

				// for each e do Pre(e) = {e} etc
				preE.put(myNode, new ArrayList<MyNode>());
				preE.get(myNode).add(myNode);

			} else {
				this.conditions.add(myNode);

				// for each c do Pre(c) = {c} etc
				preC.put(myNode, new ArrayList<MyNode>());
				preC.get(myNode).add(myNode);
			}
		}

	}

	public List<MyNode> harden() {

		/*
		 * while q is not empty
		 */
		Iterator<MyNode> iterator = this.queue.iterator();
		while (iterator.hasNext()) {

			MyNode goal = iterator.next();
			// Se = {e1, e2, .. en}
			Set<Node> parentExploits = null;

			/*
			 * No danger to be a condition (by definition).
			 */
			parentExploits = goal.getParents(true);

			// T = (e1 v e2 v .. en)
			List<MyNode> T = new ArrayList<MyNode>();

			for (Iterator eIter = parentExploits.iterator(); eIter.hasNext();) {
				MyNode e = (MyNode) eIter.next();
				T.add(e);
			}

			/*
			 * for each ei belonging to Se intersected with Pre(c)
			 */

			Set<Node> intersection = new HashSet<Node>(parentExploits);
			intersection.retainAll(this.preC.get(goal));

			for (Iterator<?> iterator2 = intersection.iterator(); iterator2.hasNext();) {
				MyNode e = (MyNode) iterator2.next();
				// set e with false in T
				if (T.remove(e)) {
					e.setState(false);
					T.add(e);
				}
			}

			// replace c with T in L ?
			this.L = T;

			// TODO for each ei belongs to Se - Pre(c)
				// equeue ei in Q
				// Let Pre(ei) = Pre(ei) union Pre(c)

			// TODO repeat the same with Sc = {c1, c2, .. cm}

			System.out.println("L" + this.L);

			// remove c from Q
		}

		for (Iterator<MyNode> iterator4 = this.exploits.iterator(); iterator4.hasNext();) {
			iterator4.next();
			System.out.println();
		}

		/*
		 * While Q is not empty
		 */
		while (!this.queue.isEmpty()) {

			/*
			 * For each condition from Q let Se be the exploits pointing to c
			 */
			for (Iterator<MyNode> iterator3 = this.conditions.iterator(); iterator3.hasNext();) {
				MyNode c = iterator3.next();

			}

		}

		return null;
	}
}
