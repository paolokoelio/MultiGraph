package es.um.multigraph.decision.almohri.Success;

/*
 * Decompiled with CFR 0.139.
 * 
 * Could not load the following classes:
 *  Graph.Graph
 *  Graph.SolutionReader
 *  Graph.Vertex
 *  Success.Solver
 *  Success.TaylorExpansion
 *  Utilities.FastIO
 */
//package Success;

//import Graph.Graph;
//import Graph.SolutionReader;
//import Graph.Vertex;
import es.um.multigraph.decision.almohri.Graph.*;
//import Success.Solver;

import es.um.multigraph.decision.almohri.Success.*;
//import Success.TaylorExpansion;
import Utilities.FastIO;
import java.io.PrintStream;
import java.nio.channels.FileChannel;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Random;

public class SLPD extends Solver {
	private double epsilon = 0.2;
	private double epsilonChangeFactor = 0.95;
	private double differenceFactor = 1.0E-4;

	public Graph solve(String path, Graph g) {
		if (((Vertex) g.vertices.get((int) 0)).computedProbability <= 0.0) {
			return g;
		}
		this.setYSelectors(g);
		double difference = 1.0;
		double sol = ((Vertex) g.vertices.get((int) 0)).computedProbability;
		int i = 0;
		while (difference > this.differenceFactor) {
			this.createLinearizedModelFileSLP(path, g);
			this.runSolverSimplex(path + "slp.m");
			SolutionReader reader = new SolutionReader();
			reader.readSolFileUtility(path + "slp.m.sol", g, false);
			difference = ((Vertex) g.vertices.get((int) 0)).computedProbability - sol;
			difference = difference > 0.0 ? difference : -difference;
			sol = ((Vertex) g.vertices.get((int) 0)).computedProbability;
			System.out.println("Current solution: x1=" + sol + " with difference: " + difference);
			this.differenceFactor = this.differenceFactor * 0.5 + this.differenceFactor;
			System.out.println("Move limit changed to " + this.differenceFactor);
			++i;
		}
		System.out.println("Iterations: " + i);
		return g;
	}

	private void createLinearizedModelFileSLP(String path, Graph g) {
		String file = path + "slp.m";
		this.addVariables(file, g.vertices);
		this.writeObjectiveFunctionSimple(file);
		this.writeConstraintsNonLinear(file, g);
	}

	private void writeConstraintsNonLinear(String file, Graph g) {
		FastIO fast = new FastIO();
		FileChannel channel = fast.openChannel(file);
		for (int i = 0; i < g.vertices.size(); ++i) {
			Vertex n = (Vertex) g.vertices.get(i);
			if (n.isFact() || n.predecessors == null)
				continue;
			String constraint = this.neighborsSLP(n.predecessors, n, i);
			fast.writeAppend(channel, constraint);
		}
		fast.writeAppend(channel, "\n");
	}

	private String neighborsSLP(List neighbors, Vertex n, int index) {
		TaylorExpansion texp = new TaylorExpansion();
		DecimalFormat df2 = new DecimalFormat("#.#######");
		String constraint = "";
		if (n.isGoal() && neighbors.size() == 1) {
			constraint = "s.t. c" + n.node + ": x" + n.node + " = x" + ((Vertex) neighbors.get((int) 0)).node + ";\n";
		} else if (n.isGoal() && neighbors.size() > 1) {
			constraint = "";
			for (int i = 0; i < neighbors.size() - 1; ++i) {
				constraint = constraint + "var " + this.yVariable(n.node, i) + "<=1;\n";
				constraint = constraint + "s.t. c" + this.yVariable(n.node, i) + ": " + this.yVariable(n.node, i)
						+ ">=0;\n";
			}
			constraint = constraint + "s.t. c" + n.node + ": x" + n.node + " = " + texp.taylorExpansion(n);
			constraint = constraint + ";\n";
			double max = ((Vertex) neighbors.get((int) 0)).computedProbability;
			for (int c = 0; c < neighbors.size(); ++c) {
				if (!(max < ((Vertex) neighbors.get((int) c)).computedProbability))
					continue;
				max = ((Vertex) neighbors.get((int) c)).computedProbability;
			}
		} else if (n.isRule()) {
			if (this.areAllFacts(neighbors)) {
				double a = 1.0;
				for (int i = 0; i < neighbors.size(); ++i) {
					a *= ((Vertex) neighbors.get((int) i)).initialProbability;
				}
				constraint = "s.t. c" + n.node + ": x" + n.node + "=" + df2.format(a) + ";\n";
			} else {
				double a = 1.0;
				String nonfact = "x";
				for (int i = 0; i < neighbors.size(); ++i) {
					if (((Vertex) neighbors.get(i)).isFact()) {
						a *= ((Vertex) neighbors.get((int) i)).initialProbability;
						continue;
					}
					nonfact = nonfact + ((Vertex) neighbors.get((int) i)).node;
				}
				constraint = "s.t. c" + n.node + ": x" + n.node + "=" + df2.format(a) + "*" + nonfact + ";\n";
			}
		}
		constraint = constraint + "s.t. l" + n.node + ": x" + n.node + ">=" + "0.0001;\n";
		return constraint;
	}

	public void setYSelectors(Graph g) {
		for (int i = 0; i < g.vertices.size(); ++i) {
			Vertex v = (Vertex) g.vertices.get(i);
			if (v.isRule()) {
				v.predecessors = this.findNeighbors(v.node, g.arcs, g.vertices);
			}
			if (!v.isGoal())
				continue;
			v.predecessors = this.findNeighbors(v.node, g.arcs, g.vertices);
			v.randWeights = this.ySelectorWeights(v.predecessors.size() - 1);
			g.vertices.set(i, v);
		}
	}

	private double[] ySelectorWeights(int numberOfSelectors) {
		int i;
		double weightsSum = 0.0;
		double[] weights = new double[numberOfSelectors];
		Random generator = new Random();
		double rangeMin = 5.0;
		double rangeMax = 10.0;
		for (i = 0; i < numberOfSelectors; ++i) {
			weights[i] = rangeMin + (rangeMax - rangeMin) * generator.nextDouble();
			weightsSum += weights[i];
		}
		for (i = 0; i < numberOfSelectors; ++i) {
			weights[i] = weights[i] / weightsSum;
		}
		return weights;
	}

	public String yVariable(int xi, int yi) {
		return "y" + yi + "x" + xi;
	}
}