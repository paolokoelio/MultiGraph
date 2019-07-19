package es.um.multigraph.decision.almohri.Success;

import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.ListMultimap;
import com.google.common.collect.MultimapBuilder;
import com.google.common.collect.Sets;

//import es.um.multigraph.decision.almohri.Graph.*;
import Graph.Arc;
import Graph.Graph;
import Graph.Vertex;
//import Success.SLP;
import es.um.multigraph.utils.FileUtils;

public class Improvement {

	// default probability given to improvement nodes when generated
	private static final double DEFAULT_PROB = 1.0;
	// probability of improvement nodes when activated in a combination
	private static final double IMP_PROB = 0.1;

	private Graph g;
	private String path;
	private SLP slp;
	private Set<Vertex> allPreds;
	private Set<String> goalNodes;
	private Map<String, Double> goalInitProbs = new HashMap<String, Double>();
//	private Map<String, ArrayListMultimap<Double, Set<Integer>>> goalFinalProbs = new HashMap<String, ArrayListMultimap<Double, Set<Integer>>>();
	// contains goal nodes, indexes of added improvement nodes and respective
	// probabilities
	private Map<String, ListMultimap<Double, Set<Integer>>> goalFinalProbs = new HashMap<String, ListMultimap<Double, Set<Integer>>>();
	private Map<String, Set<Integer>> goalsAndNodes = new HashMap<String, Set<Integer>>(); // final result, contains
																							// <goal node, Set<improved
																							// nodes>>

	public Improvement(String path, Graph g) {
		this.g = g;
		this.path = path;
		this.goalNodes = new HashSet<String>();
		this.goalNodes.add("1");
		this.slp = new SLP();
	}

	public Improvement(String path, Graph g, Set<String> goals) {
		this.g = g;
		this.path = path;
		this.goalNodes = goals;
		this.slp = new SLP();
	}

	public void solve() {

		List<Integer> improvableNodesInd;
		Set<Integer> optsInd = null;

		// compute ECSAs w/out improvements
		this.slp.solve(path, g); // FIX ME check if avoidable

		// give me goal ECSAs before improvements
		for (Iterator<String> iterator = goalNodes.iterator(); iterator.hasNext();) {
			String goal = (String) iterator.next();
			this.goalInitProbs.put(goal, ((Vertex) g.vertices.get(Integer.parseInt(goal))).computedProbability);
		}

		// for every goal, get improvable descendants
		for (Iterator<String> gi = goalNodes.iterator(); gi.hasNext();) {
			String sg = (String) gi.next();
			Integer goal = Integer.parseInt(sg);

			improvableNodesInd = this.getNar(g, goal);

			optsInd = this.addOptions(g, improvableNodesInd);

			// n of improvable nodes
			int AInd = optsInd.size();
			// n of available improvements

			System.out.println("Admissible rule nodes size: " + AInd);
			System.out.println("Goal Node Init Probs: " + this.goalInitProbs.get(sg));

			// we need to keep original graph g, we don't like shallow copies
			Graph gTmp = copyG(this.g);

			// get all possible combinations of improvable nodes w/out duplicates
			Set<Set<Integer>> allCombInd = Sets.powerSet(optsInd);
			int allCombSize = allCombInd.size();
			System.out.println("N of possible combination of improvemetns: " + allCombSize);

			// having Guava MultiMap implementation saves us some mental health problems
//			ArrayListMultimap<Double, Set<Integer>> ECSAs = ArrayListMultimap.create();

			ListMultimap<Double, Set<Integer>> ECSAs = MultimapBuilder.treeKeys().arrayListValues().build();
			double p;

			for (Set<Integer> comb : allCombInd) {
//			System.out.println(s);

				// update probabilities for improvable nodes
				this.improveNodes(gTmp, comb);

				// find new ECSA
				this.slp.solve(this.path, gTmp);

				System.out.println("Prob for " + goal +": " + ((Vertex) gTmp.vertices.get(goal)).computedProbability);

				// update list of goal ECSAs, indexes in gTmp
				p = ((Vertex) gTmp.vertices.get(goal)).computedProbability;

				// the comb is the set of indices of improvement nodes
				ECSAs.put(p, comb);
//				reccs.put(comb,p);

				// reset gTmp
				this.resetNodes(gTmp, comb);
			}

			goalFinalProbs.put(sg, ECSAs);
		}

		for (Iterator iterator = goalFinalProbs.keySet().iterator(); iterator.hasNext();) {
			String goalKey = (String) iterator.next();

			String goal = goalKey;
			Set<Integer> impNodes = new HashSet<Integer>();

			for (Iterator iterator2 = goalFinalProbs.get(goalKey).values().iterator(); iterator2.hasNext();) {
				Set<Integer> setInt = (Set<Integer>) iterator2.next();

				for (Iterator iterator4 = setInt.iterator(); iterator4.hasNext();) {
					Integer impNode = (Integer) iterator4.next();

					for (Iterator iterator3 = this.g.arcs.iterator(); iterator3.hasNext();) {
						Arc arc = (Arc) iterator3.next();

						// this is nightmare
						if (arc.u == ((Vertex) this.g.vertices.get(impNode.intValue())).node) {
//							System.out.println("u: " + arc.u);
//							System.out.println("v:" + arc.v);
							impNodes.add(arc.v); // to be improved node
						}
					}
				}
			}

			goalsAndNodes.put(goalKey, impNodes);

		}

		// TODO make test with scen 3
		// {1={0.034816=[[73]], 0.34816=[[]]}, 68={0.0160989=[[74]], 0.160989=[[]]},
		// 38={0.034816=[[75]], 0.34816=[[]]}}

		System.out.println("Final security plan: " + goalsAndNodes);
//		System.out.println(goalFinalProbs);

		// write everything into CSV
		String line = "";
		for (String goal : goalsAndNodes.keySet())
			for (Integer n : goalsAndNodes.get(goal))
				line = line + n + "," + FIXED_COST + NEW_LINE_SEPARATOR;
		
		String[] scenDir = this.path.split("/");
		writeCSV(scenDir[scenDir.length - 1], line);

//		Map<String, ArrayListMultimap<Double, Set<Integer>>> 
//		for (String goal : goalFinalProbs.keySet()) {
//			ListMultimap<Double, Set<Integer>> reccs = goalFinalProbs.get(goal);
//
//			line = "";
//			for (Double k : reccs.keySet()) {
//
//				for (Iterator<Set<Integer>> iterator = reccs.get(k).iterator(); iterator.hasNext();) {
//					Set<Integer> recc = (Set<Integer>) iterator.next();
//					for (Integer id : recc) {
//						line = line + id + ",";
//					}
//					if (recc.isEmpty())
//						line = line + "[],";
//					int ind = line.lastIndexOf(",");
//					line = line.substring(0, ind) + NEW_LINE_SEPARATOR;
//				}
//
//			}
//			// test
////			System.out.println(goal + ": \n" + line);
////			writeCSV(goal, line);
//		}

	}

	/**
	 * Do a value copy of graph g.
	 * 
	 * @param g
	 * @return new Graph with same values of g
	 */
	private Graph copyG(Graph g) {

		List<Arc> tmpA = new ArrayList<>();
		for (@SuppressWarnings("unchecked")
		Iterator<Arc> iterator = g.arcs.iterator(); iterator.hasNext();) {
			Arc a = (Arc) iterator.next();
			Arc na = new Arc();
			na.u = a.u;
			na.v = a.v;
			na.weight = a.weight;
			tmpA.add(na);
		}
		List<Vertex> tmpV = new ArrayList<>();
		for (@SuppressWarnings("unchecked")
		Iterator<Vertex> iterator = g.vertices.iterator(); iterator.hasNext();) {
			Vertex v = (Vertex) iterator.next();
			Vertex nv = new Vertex();
			nv.attackNode = v.attackNode;
			nv.body = v.body;
			nv.bodyPlain = v.bodyPlain;
			nv.constraint = v.constraint;
			nv.computedProbability = v.computedProbability;
			nv.initialProbability = v.initialProbability;
			nv.constraintCount = v.constraintCount;
			nv.mulvaln = v.mulvaln;
			nv.ninode = v.ninode;
			nv.node = v.node;
			nv.predecessors = v.predecessors;
			nv.prior = v.prior;
			nv.randWeights = v.randWeights;
			nv.rank = v.rank;
			nv.steps = v.steps;
			nv.type = v.type;
			nv.xindex = v.xindex;
			tmpV.add(nv);
		}

		return (new Graph(tmpA, tmpV));
	}

	@SuppressWarnings("unchecked")
	private Set<Integer> addOptions(Graph g2, List<Integer> s) {
		Set<Integer> optsInd = new HashSet<Integer>();
		List<Integer> opts = new ArrayList<Integer>();
		int gSize;
		for (Integer i : s) {
			gSize = g2.vertices.size();
//			System.out.println(i);
			Vertex n = (Vertex) g2.vertices.get(i);
//			System.out.println("Impr: " + n.node + " indx: " + i + " pr: " + n.computedProbability);
			Vertex nv = new Vertex();
			nv.bodyPlain = "vulPatch()";
			nv.initialProbability = DEFAULT_PROB;
			nv.mulvaln = 1;
			nv.node = gSize + 1;
			nv.type = 2;
//			nv.type = 4;	//by theory it should be an optNode, but ok
			Arc na = new Arc();
			na.u = nv.node;
			na.v = n.node;
			na.weight = -1f;
//			System.out.println("Impr: " + n.node + " indx: " + i + " pr: " + n.computedProbability);
			g2.arcs.add(na);
			g2.vertices.add(nv);
			opts.add(Integer.valueOf(nv.node));
			optsInd.add(nv.node - 1);
		}
		System.out.println("Added " + s.size() + " improvement nodes");
		return optsInd;
	}

	/**
	 * Reset the probabilities of s nodes of gTmp.
	 * 
	 * @param gTmp temporary graph
	 * @param s    set of nodes
	 */

	private void resetNodes(Graph gTmp, Set<Integer> s) {
		for (Integer i : s) {
//			System.out.println(i);
			Vertex n = (Vertex) gTmp.vertices.get(i);
			Vertex on = (Vertex) this.g.vertices.get(i);
			n.computedProbability = on.computedProbability;
			n.initialProbability = on.initialProbability;
		}
	}

	private Graph improveNodes(Graph gTmp, Set<Integer> sInd) {
//		String s = "";
		for (Integer i : sInd) {
			Vertex n = (Vertex) gTmp.vertices.get(i);
			n.initialProbability = IMP_PROB;
//			s = s + (n.node - 1) + ", ";
		}
//		System.out.println("Set nodes: " + s);
		return gTmp;
	}

	/**
	 * Get all admissible Rule Nodes in ancestors of goal node in g. (E.g. exploit
	 * rules)
	 * 
	 * @param g
	 * @param goal
	 * @return List of admissible rule nodes ancestors of goal
	 */

	public List<Integer> getNar(Graph g, Integer goal) {

		Set<Vertex> s = new HashSet<Vertex>();
		Vertex goalV = (Vertex) g.vertices.get(goal - 1);
		this.allPreds = new HashSet<Vertex>();
		s = this.getAllPredecessors(goalV);

		List<Integer> indexes = new ArrayList<Integer>();

		for (Vertex v : s) {
			if (v.isRule())
				if (v.isExploitRule()) { // eg vulnExists as a predecessor, needs to be customizable
					// now add its index
					indexes.add(v.node - 1);
				}
		}

		return indexes;
//		return l;
	}

	/**
	 * Get all ancestors of a node.
	 * 
	 * @param v
	 * @return All ancestors of v
	 */
	private Set<Vertex> getAllPredecessors(Vertex v) { // TODO check alternative to recursion

//		Set<Vertex> allPreds = new HashSet<Vertex>();

		if (v.predecessors != null)
			for (Iterator<?> iterator = v.predecessors.iterator(); iterator.hasNext();) {
				Vertex pred = (Vertex) iterator.next();
				this.allPreds.add(pred);
				getAllPredecessors(pred);

			}
		return this.allPreds;
	}

	/**
	 * Get preds of a node.
	 * 
	 * @param v
	 * @return All ancestors of v
	 */
	private Vertex getPredecessor(Vertex v) {

//		Set<Vertex> allPreds = new HashSet<Vertex>();

		if (v.predecessors != null)
			for (Iterator<?> iterator = v.predecessors.iterator(); iterator.hasNext();) {
				Vertex pred = (Vertex) iterator.next();
				this.allPreds.add(pred);
				getPredecessor(pred);
			}
		return null;
	}

	/**
	 * Get all admissible Rule Nodes in g (e.g. exploit rules).
	 * 
	 * @param g
	 * @return List of all admissible rule nodes
	 */

	public List<Integer> getNar(Graph g) { // now useless
//		List<Vertex> l = new ArrayList<Vertex>();
		List<Integer> indexes = new ArrayList<Integer>();
		for (int i = 0; i < g.vertices.size(); i++) {
			Vertex n = (Vertex) g.vertices.get(i);
			if (n.isRule())
				if (n.isExploitRule()) { // eg vulnExists as a predecessor
//					l.add(n);
					indexes.add(i);
				}
		}
		return indexes;
	}

	private static final String NEW_LINE_SEPARATOR = "\n";
	private static final String FILE_HEADER = "nodeIds,cost";
	private static final String PAPER_PREFIX = "Almohri";
	private static final String SOL_BASE_PATH = "files/solutions/";
	private static final Double FIXED_COST = 1.0;

	public void writeCSV(String scenario, String list) {
		String path = PAPER_PREFIX;
		Date date = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
		path = SOL_BASE_PATH + path + "_" + scenario + "_" + dateFormat.format(date) + ".csv";

		FileUtils fileUtils = new FileUtils();
		FileWriter writer = fileUtils.getWriter(path);

		try {
			writer.append(FILE_HEADER.toString());
			writer.append(NEW_LINE_SEPARATOR);

			writer.append(list);

		} catch (Exception e) {
			System.out.println("Error in Writer");
			e.printStackTrace();
		} finally {

			try {
				writer.flush();
				writer.close();
			} catch (IOException e) {
				System.out.println("Error while flushing/closing Writer");
				e.printStackTrace();
			}

		}
	}

}
