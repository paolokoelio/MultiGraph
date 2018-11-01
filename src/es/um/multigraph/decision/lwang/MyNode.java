/**
 * MULTIGRAPH
 *
 * Project for Mattia Zago Master Thesis
 *
 * (C) 2015 - Mattia Zago
 *
 */
package es.um.multigraph.decision.lwang;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JLabel;
import javax.swing.JPanel;

import es.um.multigraph.decision.basegraph.Edge;
import es.um.multigraph.decision.basegraph.JDialogNode;
import es.um.multigraph.decision.basegraph.Node;
import es.um.multigraph.event.solution.Solution;

/**
 * According to the paper of L.Wang et al. this class represents a node in the
 * attack graph. It can be an EXPLOIT node or a security (pre/post) CONDITION
 * type node. A STATE variable says if it's enabled or not.
 * 
 * @author Pavlo Burda <a href="mailto:p.burda@tue.nl">p.burda@tue.nl</a>
 * @author Mattia Zago <a href="mailto:dev@zagomattia.it">dev@zagomattia.it</a>
 */
public class MyNode extends Node {

	// node is active or not?
	public boolean state = MyNode.STATE_TRUE;
	// node type L.Wang et al. definition: EXPLOIT or CONTIDION
	private boolean type = MyNode.EXPLOIT;
	// node type MulVAL definition: AND, OR, LEAF
	private String typeMulval;

	public void setState(boolean state) {
		this.state = state;
	}

	public boolean getState() {
		return this.state;
	}

	public String getTypeMulval() {
		return typeMulval;
	}

	public void setTypeMulval(String type) {
		this.typeMulval = type;
	}

	// TODO elaborate on this if it' s reallly needed
	private String sourceHost;
	private String intermediateHost;
	private String destHost;

	public String getSourceHost() {
		return sourceHost;
	}

	public void setSourceHost(String sourceHost) {
		this.sourceHost = sourceHost;
	}

	public String getIntermediateHost() {
		if (type)
			return intermediateHost;
		return null;
	}

	public void setIntermediateHost(String intermediateHost) {
		if (type)
			this.intermediateHost = intermediateHost;
	}

	public String getDestHost() {
		return destHost;
	}

	public void setDestHost(String destHost) {
		this.destHost = destHost;
	}

	public static final boolean STATE_TRUE = true;
	public static final boolean STATE_FALSE = false;
	public static final boolean EXPLOIT = true;
	public static final boolean CONDITION = false;

	public String getType() {
		if (this.type)
			return "exploit";
		else
			return "condition";
	}

	public void setType(boolean type) {
		this.type = type;
	}

	/**
	 *
	 * @param ID.
	 */
	public MyNode(String ID) {
		super(ID);
		this.sourceHost = null;
		this.destHost = null;
		this.intermediateHost = null;
	}

	/**
	 *
	 * @param ID.
	 */
	public MyNode(String ID, String sourceHost, String intermediateHost, String destHost) {
		super(ID);
		this.sourceHost = sourceHost;
		this.destHost = destHost;
		this.intermediateHost = intermediateHost;
	}

	@Override
	public String getFullRepresentationAsString(boolean printEdges) {
		String result = "" + "Node (ID: " + this.ID + "):" + "\n\tClass:\t" + this.getClass() + "\n\tLabel:\t"
				+ this.label + "\n\tType: " + this.type + "\n\tState: " + this.state + "\n\tSource host: "
				+ this.sourceHost + "\n\tIntermediate host: " + this.intermediateHost + "\n\tDest host: "
				+ this.destHost + "\n\tExpected Loss: " + String.format("%.2f", this.getExpectedLoss())
				+ "\n\tExpected Gain: " + String.format("%.2f", this.getExpectedGain()) + "";

		if (printEdges) {
			result += "\n\tOut Edges:";
			for (Edge e : this.getOut()) {
				result += "\n\t\t" + e.getFullRepresentationAsString();
			}
			result += "\n\tIn Edges:";
			for (Edge e : this.getIn()) {
				result += "\n\t\t" + e.getFullRepresentationAsString();
			}
		}

		return result;
	}

	@Override
	public String getLabelGraph() {
		String l = "";
		l += this.getID() + "\n" + this.getLabel();
		return l;
	}

//    @Test
//    public void assertExternalNodeIfHasFixedPriorPr() {
//        if (this.hasFixedPriorPr()) {
//            TestCase.assertEquals(isExternal(), true);
//        }
//    }

//    @Test
//    public void assertStateTrueIfHasPosteriorPr() {
//        if (this.hasPosteriorPr()) {
//            TestCase.assertEquals(state, true);
//        }
//    }

	// ========================================================================
	// STRUCTURAL PROP.
	// ========================================================================
	private Set<MyEdge> out = new HashSet<>();
	private Set<Edge> in = new HashSet<>();
	private Set<Node> cachedParent = new HashSet<>();

	private boolean parentDecomposition = MyEdge.DECOMPOSITION_OR;

	public boolean getParentDecomposition() {
		return parentDecomposition;
	}

	public void setParentDecomposition(boolean parentDecomposition) {
		this.parentDecomposition = parentDecomposition;
	}

	public Set<MyNode> getAllAncestor() {
		Set<MyNode> result = new HashSet<>();
		for (Iterator<Edge> it = this.getIn().iterator(); it.hasNext();) {
			MyNode tmp = (MyNode) it.next().getFrom();
			result.add(tmp);
			result.addAll(tmp.getAllAncestor());
		}

		return result;
	}

	public static JPanel getJPanel(MyNode node) {
		JPanel result = new JPanel();
		Field[] ff = node.getClass().getFields();
		result.setLayout(new GridLayout(ff.length, 2));

		result.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;

		int i = 0;
		for (Field f : ff) {
			if (String.class.isAssignableFrom(f.getType()) || Double.class.isAssignableFrom(f.getType())) {
				c.gridx = i;
				c.gridy = 0;
				result.add(new JLabel(f.getName()), c);
				c.gridx = i++;
				c.gridy = 1;
				try {
					result.add(new JLabel(f.get(node).toString()), c);
				} catch (IllegalArgumentException | IllegalAccessException ex) {
					result.add(new JLabel(""), c);
					Logger.getLogger(Node.class.getName()).log(Level.SEVERE, null, ex);
				}
			}
		}
		return result;
	}

	// ========================================================================
	// COUNTERMEASURE TODO
	// ========================================================================
	Set<Solution> countermeasures = new HashSet<>();

	public Set<Solution> getCountermeasures() {
		return countermeasures;
	}

	public void setCountermeasures(Set<Solution> countermeasures) {
		this.countermeasures = countermeasures;
	}

	private Double expectedLoss = 1d;
	private Double expectedGain = 0d;

	public Double getExpectedLoss() {
		return expectedLoss;
	}

	public void setExpectedLoss(Double expectedLoss) {
		this.expectedLoss = expectedLoss;
	}

	public Double getExpectedGain() {
		return expectedGain;
	}

	public void setExpectedGain(Double expectedGain) {
		this.expectedGain = expectedGain;
	}

//    public Double getExpectedLossGain() {
//        if (this.isExternal()) {
//            return 0d;
//        }
//        
//        Double GainFact = (1 - this.getUnconditionalPr()) * this.getExpectedGain();
//        Double LossFact = this.getUnconditionalPr() * this.getExpectedLoss();
//        return GainFact - LossFact;
//    }

	public static JDialogNode getJDialog() {
		return new JDialogNode(null, true, null, null);
	}

	public boolean getTypeBool() {
		return this.type;
	}

	@Override
	public Set<Node> getParents(boolean recompute) {

		if (!recompute) {
			return this.cachedParent;
		}

		in = this.getIn();

		Set<Node> parents = new HashSet<>();
		in.stream().forEach((e) -> {

			parents.add(e.getFrom());

		});
		this.setCachedParent(parents);
		return parents;
	}
}
