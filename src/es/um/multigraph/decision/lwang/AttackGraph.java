/**
 * MULTIGRAPH
 *
 * Project for Mattia Zago Master Thesis
 *
 * (C) 2015 - Mattia Zago
 *
 */
package es.um.multigraph.decision.lwang;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
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

import es.um.multigraph.conf.DBManager;
import es.um.multigraph.conf.FeaturesEnum;
import es.um.multigraph.conf.RiskScale;
import es.um.multigraph.core.MainClass;
import es.um.multigraph.decision.DecisionInterface;
import es.um.multigraph.decision.basegraph.Edge;
import es.um.multigraph.decision.basegraph.Node;
import es.um.multigraph.decision.poolsappasitmoop.BayesianNode;
import es.um.multigraph.event.Event;
import es.um.multigraph.event.EventStream;
import es.um.multigraph.event.solution.Solution;
import es.um.multigraph.event.solution.dummy.DummySolution;
import es.um.multigraph.utils.FileUtils;
import es.um.multigraph.utils.ImportAG;
import es.um.multigraph.utils.ParseAG;

/**
 *
 * @author Mattia Zago <a href="mailto:dev@zagomattia.it">dev@zagomattia.it</a>
 * @author Pavlo Burda
 */
public class AttackGraph implements DecisionInterface {

	MainClass parent;
	private boolean stop = false;

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
	 * 
	 * @throws
	 */

	private void initAGsim() throws ParserConfigurationException {
		ImportAG bs;
		this.AG = new HashSet<>();

		bs = new ImportAG();

		FileUtils fl = new FileUtils();
		fl.readFile("files/AttackGraph.xml");

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

		// debug
//		for (Iterator<? extends Node> iterator = this.getNodes().iterator(); iterator.hasNext();) {
//			MyNode myNode = (MyNode) iterator.next();
//			System.out.println(myNode.getID() + "(" + myNode.getSourceHost() + "," + myNode.getDestHost()
//					+ ") - " + myNode.getLabel() + " - state: " + myNode.getState() + " - type: " + myNode.getType());
//		}
		
		List<MyNode> goals = new ArrayList<MyNode>();
		goals.add(this.getNodeByID("n11"));
		
//		System.out.println(goals);
		
		NetworkHardening nh = new NetworkHardening(this, goals);
		nh.harden();


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
	// PAPER IMPL.
	// ========================================================================

	private Set<MyNode> AG = new HashSet<>();

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
	 * @param id
	 * @return Node
	 */
	private MyNode getNodeByID(String id) {
		for (Iterator iterator = this.getNodes().iterator(); iterator.hasNext();) {
			MyNode node = (MyNode) iterator.next();
			if(node.getID().equals(id))
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
