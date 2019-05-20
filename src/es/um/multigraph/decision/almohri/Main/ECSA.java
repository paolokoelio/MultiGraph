package es.um.multigraph.decision.almohri.Main;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JFrame;

import Graph.Graph;
import Graph.GraphReader;
import Success.InitialSolution;
import es.um.multigraph.conf.DBManager;
import es.um.multigraph.conf.FeaturesEnum;
import es.um.multigraph.conf.RiskScale;
import es.um.multigraph.core.MainClass;
import es.um.multigraph.decision.DecisionInterface;
import es.um.multigraph.decision.almohri.Success.Improvement;
import es.um.multigraph.event.Event;
import es.um.multigraph.event.solution.Solution;
import es.um.multigraph.utils.GoalReader;
import es.um.multigraph.decision.basegraph.Edge;
import es.um.multigraph.decision.basegraph.JDialogEdge;
import es.um.multigraph.decision.basegraph.Node;
import es.um.multigraph.decision.lwang.AttackGraph;

public class ECSA implements DecisionInterface {

	@Override
	public void log(String txt) {
		// TODO Auto-generated method stub

	}

	@Override
	public void run() {
		// TODO Auto-generated method stub

	}

	@Override
	public void init(MainClass main) {

		GraphReader reader = new GraphReader();
		Graph g;
		GoalReader goalReader = new GoalReader();
		Set<String> goalNodes = new HashSet<String>();

		String path = "";

//		try {
//			path = args[0];
//			
//		} catch (Exception e) {
//			System.out.println("Please enter the path to attack graph source files (generated by MulVAL).");
//		}

		path = "files/ags/scenario/";

		goalNodes = goalReader.readGoals(path);

		g = reader.readGraph(path, false);
//
		System.out.println("Read a total of " + g.vertices.size() + " vertices.");
		System.out.println("Read a total of " + g.totalFactNodes() + " fact vertices.");

		InitialSolution init = new InitialSolution();
		init.findInitialSolution(g, path);

		Improvement imp = new Improvement(path, g, goalNodes);

		imp.solve();

	}

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
		throw new UnsupportedOperationException("Not supported yet.");
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
	public String getPaperName() {
		return "H. M. J. Almohri, L. T. Watson, D. Yao and X. Ou, \"Security Optimization of Dynamic Networks with Probabilistic Graph Modeling and Linear Programming,\" in IEEE Trans. on Dep. & Secure Computing, vol. 13, no. 4, pp. 474-487, 1 July-Aug. 2016";

	}

	@Override
	public String getPaperAuthors() {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public URI getPaperDOI() {
		try {
			return new URI("https://www.doi.org/10.1109/TDSC.2015.2411264");
		} catch (URISyntaxException ex) {
			Logger.getLogger(AttackGraph.class.getName()).log(Level.SEVERE, null, ex);
		}
		return null;
	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub

	}

	@Override
	public JFrame getModelConfigurationFrame() {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public Class<Edge> getEdgesClass() {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public Class<Node> getNodesClass() {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public Collection<Node> getNodes() {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public Collection<Edge> getEdges() {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void addNode(Node node) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void addEdge(Edge edge) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void delNode(Node n) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void delEdge(Edge e) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

}
