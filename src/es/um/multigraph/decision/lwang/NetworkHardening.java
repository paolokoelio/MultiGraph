package es.um.multigraph.decision.lwang;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import es.um.multigraph.decision.basegraph.Node;

/**
 * Hardening procedure implementation as described in L.Wang et al.
 * 
 * @author Pavlo Burda <a href="mailto:p.burda@tue.nl">p.burda@tue.nl</a>
 */

public class NetworkHardening {

	AttackGraph AG;

	//FIXME
	private Set<MyNode> exploits;
	private Set<MyNode> conditions;
	
	// Goal conditions to be protected
	private List<MyNode> goals;
	// Result of initials conditions to be negated
	private List<MyNode> L;
	// Temporary FIFO queue to store search results
	private Queue<MyNode> queue;
	// Sets of Pre(e) and Pre(c) predecessors for every e and c in the AG
	private Map<MyNode, List<MyNode>> preE;
	private Map<MyNode, List<MyNode>> preC;

	public NetworkHardening(AttackGraph AG, List<MyNode> goals) {
		this.AG = AG;
		this.goals = goals;
		this.queue = new LinkedList<MyNode>();
		this.exploits = new HashSet<MyNode>();
		this.conditions = new HashSet<MyNode>();
		// result L
		this.L = new ArrayList<MyNode>();
		this.preE = new HashMap<MyNode, List<MyNode>>();
		this.preC = new HashMap<MyNode, List<MyNode>>();

		/*
		 * Prepare the queues
		 */
		for (Iterator<MyNode> iterator = this.goals.iterator(); iterator.hasNext();) {
			MyNode myNode = (MyNode) iterator.next();
			this.queue.add(myNode);
//			this.L.add(myNode);//FIXME
		}

		for (Iterator<Node> iterator = (Iterator<Node>) this.AG.getNodes().iterator(); iterator.hasNext();) {
			MyNode myNode = (MyNode) iterator.next();

			if (myNode.getTypeBool() == MyNode.EXPLOIT) {
				this.exploits.add(myNode);//FIXME

				// for each e do Pre(e) = {e} etc
				preE.put(myNode, new ArrayList<MyNode>());
				preE.get(myNode).add(myNode);

			} else {
				this.conditions.add(myNode);//FIXME

				// for each c do Pre(c) = {c} etc
				preC.put(myNode, new ArrayList<MyNode>());
				preC.get(myNode).add(myNode);
			}
		}

		System.out.println("preC: " + preC);
		System.out.println("preE: " + preE);
		System.out.println("queue: " + queue);
		System.out.println("L: " + L);
	}

	public List<MyNode> harden() {

		/*
		 * while Q is not empty
		 */
		Iterator<MyNode> iterator = this.queue.iterator();

		while (iterator.hasNext()) {

			// dequeue from Q
			MyNode nodeQ = this.queue.poll();

			if (nodeQ.getTypeBool() == MyNode.CONDITION) {

				// Se = {e1, e2, .. en}
				Set<Node> parentExploits = null;

				/*
				 * No danger to be an exploit (it' s a MyNode.CONDITION!).
				 */
				parentExploits = nodeQ.getParents(true);

				if (parentExploits.isEmpty())
					continue;

				// T = (e1 v e2 v .. en)
				List<MyNode> T = new ArrayList<MyNode>();

//				System.out.println("parentExploits: " + parentExploits);

				for (Iterator<?> eIter = parentExploits.iterator(); eIter.hasNext();) {
					MyNode e = (MyNode) eIter.next();
					T.add(e);
				}

				/*
				 * for each ei belonging to Se intersected with Pre(c)
				 */
				Set<Node> intersection = new HashSet<Node>(parentExploits);
				intersection.retainAll(this.preC.get(nodeQ));

				for (Iterator<?> iterator2 = intersection.iterator(); iterator2.hasNext();) {
					MyNode e = (MyNode) iterator2.next();
					// set e with false in T
					if (T.remove(e)) {
						e.setState(false);
						T.add(e);
					}
				}

				// replace c with T in L (pay attention here) FIXME
				this.L.addAll(T);

				// for each ei belongs to Se - Pre(c)
				Set<Node> diff = new HashSet<Node>(parentExploits);
				diff.removeAll(this.preC.get(nodeQ));

//				System.out.println("Se - Pre(c): " + diff);

				// equivalent to the loop down here
				for (Node node : diff) {
					// equeue ei in Q
					this.queue.add((MyNode) node);

					// Let Pre(ei) = Pre(ei) union Pre(c)
					this.preE.get((MyNode) node).addAll(this.preC.get(nodeQ));
				}

				// check
				System.out.println("T: " + T);
				System.out.println("Q: " + this.queue);

				// end for each c dequeued from Q

			} else if (nodeQ.getTypeBool() == MyNode.EXPLOIT) {

				// Sc = {c1, c2, .. cm}
				Set<Node> parentConds = null;

				/*
				 * No danger to be a condition (it's a MyNode.EXPLOIT!).
				 */
				parentConds = nodeQ.getParents(true);
				if (parentConds.isEmpty())
					continue;

				// T = (c1 & c2 & .. cm)
				List<MyNode> T = new ArrayList<MyNode>();

				for (Iterator<?> cIter = parentConds.iterator(); cIter.hasNext();) {
					MyNode c = (MyNode) cIter.next();
					T.add(c);
				}

				/*
				 * for each ci belonging to Sc intersected with Pre(e)
				 */
				Set<Node> intersection = new HashSet<Node>(parentConds);
				intersection.retainAll(this.preE.get(nodeQ));

				for (Iterator<?> iterator2 = intersection.iterator(); iterator2.hasNext();) {
					MyNode c = (MyNode) iterator2.next();
					// set e with false in T
					if (T.remove(c)) {
						c.setState(false);
						T.add(c);
					}
				}

				// replace e with T in L (pay attention here) FIXME
				L.addAll(T);

				// for each ei belongs to Se - Pre(c)
				Set<Node> diff = new HashSet<Node>(parentConds);
				diff.removeAll(this.preE.get(nodeQ));

				// equivalent to the loop down here
				for (Node node : diff) {
					// equeue ei in Q
					this.queue.add((MyNode) node);

					// Let Pre(ei) = Pre(ei) union Pre(c)
					this.preC.get((MyNode) node).addAll(this.preE.get(nodeQ));
				}

//				for (Iterator<?> iterator3 = diff.iterator(); iterator3.hasNext();) {
//					MyNode c = (MyNode) iterator3.next();
//					// enqueue ei in Q
//					this.queue.add(c);
//
//					// Let Pre(ei) = Pre(ei) union Pre(c)
//					this.preC.get(c).addAll(this.preE.get(nodeQ));
//				}

				// check
				System.out.println("T: " + T);
				System.out.println("Q: " + this.queue);

			} // end for each e dequeued from Q\

			// remove c from Q
//			this.queue.remove();
			System.out.println("L: " + L);
		}

		/*
		 * debug print.out for case lines 9 and 17 of L.Wang et al. algorithm i.e. the
		 * state of the nodes in case of cycles ?
		 */
		for (Iterator<MyNode> iterator4 = this.L.iterator(); iterator4.hasNext();) {
			MyNode n = iterator4.next();
			System.out.println("Res: " + n + "-" + n.getState());
		}

		return null;
	}
}
