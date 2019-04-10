/**
 * MULTIGRAPH - FIXME
 *
 * Attack Graph Simulation and Network Hardening
 * 
 * Pavlo Burda
 *
 */
package es.um.multigraph.decision.lwang;

import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JFrame;
import javax.xml.parsers.ParserConfigurationException;

import com.bpodgursky.jbool_expressions.And;
import com.bpodgursky.jbool_expressions.Expression;
import com.bpodgursky.jbool_expressions.Not;
import com.bpodgursky.jbool_expressions.Or;
import com.bpodgursky.jbool_expressions.Variable;
import com.bpodgursky.jbool_expressions.rules.RuleSet;

import es.um.multigraph.conf.DBManager;
import es.um.multigraph.conf.FeaturesEnum;
import es.um.multigraph.conf.RiskScale;
import es.um.multigraph.core.MainClass;
import es.um.multigraph.decision.DecisionInterface;
import es.um.multigraph.decision.basegraph.Edge;
import es.um.multigraph.decision.basegraph.Node;
import es.um.multigraph.event.Event;
import es.um.multigraph.event.EventStream;
import es.um.multigraph.event.solution.Solution;
import es.um.multigraph.event.solution.dummy.DummySolution;
import es.um.multigraph.utils.FileUtils;
import es.um.multigraph.utils.ImportAG;
import es.um.multigraph.utils.ParseAG;

/**
 *
 * @author Pavlo Burda - p.burda@tue.nl
 * @author Mattia Zago <a href="mailto:dev@zagomattia.it">dev@zagomattia.it</a>
 *
 */
public class AttackGraph implements DecisionInterface {

	MainClass parent;
	private boolean stop = false;
	private static final String NEW_LINE_SEPARATOR = "\n";
	private static final String FILE_HEADER = "cost,nodeIds";
	private static final String PAPER_PREFIX = "Wang_NetHard";
//	private static final String INPUT_AG_PATH = "files/AttackGraph2.xml";
	private static final String INPUT_AG_PATH = "files/AttackGraph_2vul.xml";
	private static final String SOL_BASE_PATH = "files/solutions/";
//	private static final String GOAL_NODE = "n34"; // FIXME
	private static final String GOAL_NODE = "n1"; // FIXME

	public AttackGraph() {
		this.nodes = new LinkedList<>();
	}

	@Override
	public void init(MainClass main) {
		this.parent = main;
		if (main != null)
			main.getGraph().cleanGraph();

//		defaultInit();

		try {
			initAGsim();
		} catch (ParserConfigurationException e) {
			// AG generation from file
			e.printStackTrace();
		}

		if (main != null)
			this.parent.getGraph().repaint(true);
	}

	/**
	 * @throws ParserConfigurationException Start AG simulation from MulVAL import.
	 * Implements the procedures described in L. Wang et al., including Network
	 * Hardening and cost function (TODO make the same one of Poolsappasit).
	 */

	private void initAGsim() throws ParserConfigurationException {
		ImportAG bs;
		this.AG = new HashSet<>();

		bs = new ImportAG();

		FileUtils fl = new FileUtils();
		fl.readFile(INPUT_AG_PATH);

		bs.setFile(fl);

		bs.importAG();
		ParseAG ps = new ParseAG(bs.getNodes(), bs.getEdges());
		ps.parseAG();
		Adapter adapter = new Adapter();
		adapter.setMyEdges(ps.getMyEdges());
		adapter.setMyNodes(ps.getMyNodes());
		adapter.convertAG();

		Map<Integer, MyNode> myNodes = adapter.getMyNodes();
		Map<String, MyEdge> myEdges = adapter.getMyEdges();

		for (Map.Entry<Integer, MyNode> entry : myNodes.entrySet())
			this.addNode(entry.getValue());

		for (Entry<String, MyEdge> entry : myEdges.entrySet())
			this.addEdge(entry.getValue());

		List<MyNode> goals = new ArrayList<MyNode>();
		goals.add(this.getNodeByID(GOAL_NODE));

		NetworkHardening nh = new NetworkHardening(this, goals);
		nh.harden();
//		nh.hardenApprox(1); //TODO check if needed approx. alg. (ForwardSearch)
//		List<Object> L = nh.getL();
		Expression<String> L = nh.getLexpr();
		log("Result - L:\n");
		System.out.println(L);
		
//		Expression dnfL = RuleSet.toDNF(this.toExpression(L));
		Expression dnfL = RuleSet.toDNF(Not.of(L));
		log(dnfL.toLexicographicString() + "\n");

		
		/* Write and log CSVs with plans */
		List<String> parsedStringSol = new ArrayList<String>();
		parsedStringSol = this.dnfToList(dnfL);
		
		List<List<MyNode>> listSol = this.toList(parsedStringSol);

		//test things
//		this.getNodeByID("n12").setCost(0.2);
//		this.getNodeByID("n41").setCost(0.6);
//		this.getNodeByID("n8").setCost(0.2);
		
		Collections.sort(listSol, new SolComparator<>());

		// for testing
		for (Iterator<List<MyNode>> iterator = listSol.iterator(); iterator.hasNext();) {
			List<MyNode> sol = iterator.next();
			String row = ""; double cost = 0d;
			for (Iterator<MyNode> iterator2 = sol.iterator(); iterator2.hasNext();) {
				MyNode node = iterator2.next();
				row = row + "," + node.getID();
				cost += node.getCost();
			}
			System.out.println(cost + row);
		}
		
//		System.out.println(listSol);
		this.writeCSV(listSol);
		
	}

	/**
	 * Default initialization.
	 */
	private void defaultInit() {
		MyNode A = new MyNode("A");

		MyNode B = new MyNode("B");

		MyNode C = new MyNode("C");

		MyNode D = new MyNode("D");

		MyEdge DB = new MyEdge("DB", D, B);
		DB.addThisToNodes();

		MyEdge DC = new MyEdge("DC", D, C);
		DC.addThisToNodes();

		MyEdge BA = new MyEdge("BA", B, A);
		BA.addThisToNodes();

		MyEdge CA = new MyEdge("CA", C, A);
		CA.addThisToNodes();

		this.addNode(A);
		this.addNode(B);
		this.addNode(C);
		this.addNode(D);

		this.addEdge(DB);
		this.addEdge(DC);
		this.addEdge(BA);
		this.addEdge(CA);

		System.out.println("Nodes: ");
		System.out.println(A.getFullRepresentationAsString(true));
		System.out.println(B.getFullRepresentationAsString(true));
		System.out.println(C.getFullRepresentationAsString(true));
		System.out.println(D.getFullRepresentationAsString(true));
	}

	public static void main(String[] args) {
		AttackGraph a = new AttackGraph();
		a.init(null);
	}

	// ========================================================================
	// UTILS
	// ========================================================================

	/**
	 * Get a L in jbool library format (Expression)
	 * @param L List<Object> solutions
	 * @return Expression logical expression
	 */
	@SuppressWarnings("unchecked")
	public Expression toExpression(List<Object> L) {
		Expression<String> trueL;
		Expression[] ors = new Expression[L.size()];

		Iterator<Object> iterator = L.iterator();
		for (int i = 0; iterator.hasNext(); i++) {
			ArrayList<MyNode> opt = (ArrayList<MyNode>) iterator.next();
			Expression[] ands = new Expression[opt.size()];

			Iterator<MyNode> iter2 = opt.iterator();
			for (int j = 0; iter2.hasNext(); j++) {
				MyNode node = iter2.next();
				ands[j] = Variable.of(node.getID());
			}
			ors[i] = And.of(ands);
		}

		trueL = Not.of(Or.of(ors));

		return trueL;
	}

	/**
	 * Get a List<String> representation of the DNF form solution
	 * @param L, solutions in DNF logical expression
	 * @return parsed expression
	 */
	public List<String> dnfToList(Expression L) {

		// remove first and last chars: ( and )
		String parsedL = L.toLexicographicString().replaceAll("^.|.$", "");
		// replace ORs with \n
		parsedL = parsedL.replaceAll("( \\| )", "\n");
		// replace ANDs with commas
		parsedL = parsedL.replaceAll("( \\& )", ",");
		// (?m) multi-line, remove all brackets
		parsedL = parsedL.replaceAll("(?m)^\\(|\\)$", "");
		// remove NOTs
		parsedL = parsedL.replaceAll("!", "");

		ArrayList<String> sentence = new ArrayList<String>();

		String lines[] = parsedL.split("\\n");

		for (String line : lines)
			sentence.add(line);

		return sentence;
	}
	
	/**
	 * Get (un-parse) a List object of the DNF form solution (blame it on the jbool_expressons library)
	 * @param String list
	 * @return List<List<>> of instances of solutions
	 */
	private List<List<MyNode>> toList(List<String> solutions) {
		List<List<MyNode>> orderedSolutions = new LinkedList<>();
		
		for (String solution : solutions) {
			String[] sols = solution.split(",");
			List<MyNode> tmpList = new LinkedList<>();
			for (String str : sols) {
					MyNode node = this.getNodeByID(str);
					tmpList.add(node);
			}
			orderedSolutions.add(tmpList);
		}
		return orderedSolutions;
	}

	public void writeCSV(List<List<MyNode>> conds) {
		String path = PAPER_PREFIX;
		Date date = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
		path = SOL_BASE_PATH + path + "_" + dateFormat.format(date) + ".csv";

		FileUtils fileUtils = new FileUtils();
		FileWriter writer = fileUtils.getWriter(path);

		try {
			// Write the CSV file header
			writer.append(FILE_HEADER.toString());
			// Add a new line separator after the header
			writer.append(NEW_LINE_SEPARATOR);

			for (Iterator<List<MyNode>> iterator = conds.iterator(); iterator.hasNext();) {
				List<MyNode> sol = iterator.next();
				String row = ""; double cost = 0d;
				for (Iterator<MyNode> iterator2 = sol.iterator(); iterator2.hasNext();) {
					MyNode node = iterator2.next();
					row = row + "," + node.getID();
					cost += node.getCost();
				}
//				System.out.println(cost + row);
				writer.append(cost + row + NEW_LINE_SEPARATOR);
			}

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

	// ========================================================================
	// PAPER IMPL.
	// ========================================================================

	private Set<MyNode> AG = new HashSet<>();// FIXME

	// ========================================================================
	// INHEREDITED PROP.
	// ========================================================================

	@Override
	public List<Class<? extends Event>> getRecognizedEvent() {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public List<FeaturesEnum> getEnabledFeatures() {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public Solution react(Event e) {
		parent.log("React to Event " + e.toString() + "\n", this);
		parent.log("Creating and configuring new solution\n", this);

		return DummySolution.GENERIC;
	}

	@Override
	public RiskScale getOverallRisk() {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void loadState(DBManager database) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void log(String txt) {
		if (this.parent != null)
			this.parent.log(txt, this);
		else
			System.out.println(txt);
	}

	@Override
	public String toString() {
		return "AG&NetHard";
	}

	@Override
	public String getPaperName() {
		return "Network Hardening " + "An Automated Approach to Improving Network Security";
	}

	@Override
	public String getPaperAuthors() {
		return "Wang, " + "Albanese," + " Jajodia";
	}

	@Override
	public URI getPaperDOI() {
		try {
			return new URI("https://doi.org/10.1007/978-3-319-04612-9_3");
		} catch (URISyntaxException ex) {
			Logger.getLogger(AttackGraph.class.getName()).log(Level.SEVERE, null, ex);
		}
		return null;
	}

	public boolean isStopped() {
		return stop;
	}

	@Override
	public void stop() {
		this.log("Stop requested.\n");
		this.stop = true;
	}

	@Override
	public void run() {
		this.stop = false;
		parent.log("Started.\n", this);
		System.out.println(parent.toString());
		if (parent != null)
			parent.showGraph();
		try {
			while (!this.isStopped()) {
				Thread.sleep(1000);
				// parent.log(toString()+" still active\n");
				// parent.updateNumberOfNodes(0);
			}
		} catch (InterruptedException ex) {
			Logger.getLogger(EventStream.class.getName()).log(Level.SEVERE, null, ex);
		}
		this.log("Stopped.\n");
	}

	@Override
	public JFrame getModelConfigurationFrame() {
		return new ModelConfigurationDefaultImplementation(parent, this);
	}

	@Override
	public Class<? extends Edge> getEdgesClass() {
		return Edge.class;
	}

	@Override
	public Class<? extends Node> getNodesClass() {
		return Node.class;
	}

	/**
	 * Retrieve a node by its ID.
	 * 
	 * @param id
	 * @return Node
	 */
	private MyNode getNodeByID(String id) {
		for (Iterator iterator = this.getNodes().iterator(); iterator.hasNext();) {
			MyNode node = (MyNode) iterator.next();
			if (node.getID().equals(id))
				return node;
		}
		return null;
	}

	private List<Node> nodes;

	@Override
	public void addNode(Node node) {
		log("Add node (ID: " + node.getID() + ", L: " + node.getLabel() + ")\n");
//		node.setLabel("");
//		this.AG.add((MyNode) node);// FIXME merge the lists into AG

		if (this.parent != null)
			this.parent.addNodeToGraph(node);

		this.nodes.add(node);
	}

	@Override
	public void addEdge(Edge edge) {
		log("Add edge (ID: " + edge.getID() + ")\n");
		((MyEdge) edge).addThisToNodes();
		if (this.parent != null) {
			this.parent.addEdgeToGraph(edge);
		}
	}

	@Override
	public Collection<? extends Node> getNodes() {
		return this.nodes;
	}

	@Override
	public void delNode(Node selected) {
		this.nodes.remove(selected);
	}

	@Override
	public void delEdge(Edge e) {
		throw new UnsupportedOperationException("Not supported yet."); // To change body of generated methods, choose
																		// Tools | Templates.
	}

	@Override
	public Collection getEdges() {
		Set<Edge> result = new HashSet();
		for (Iterator<Node> it = nodes.iterator(); it.hasNext();) {
			Node n = it.next();
			result.addAll(n.getIn());
			result.addAll(n.getOut());
		}

		return result;
	}

}
