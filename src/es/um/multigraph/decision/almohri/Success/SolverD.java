package es.um.multigraph.decision.almohri.Success;

/*
 * Decompiled with CFR 0.139.
 * 
 * Could not load the following classes:
 *  Graph.Arc
 *  Graph.Vertex
 *  Utilities.FastIO
 *  Utilities.FileAccess
 */
//package Success;

import Graph.Arc;
import Graph.Vertex;
import Utilities.FastIO;
import Utilities.FileAccess;
import java.io.BufferedWriter;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

public class SolverD {
	protected String variablesCached = null;

	public Boolean runSolverInterior(String path) {
		FileAccess fa = new FileAccess();
		ArrayList<String> cmd = new ArrayList<String>();
		cmd.add("/usr/local/bin/glpsol");
		cmd.add("--interior");
		cmd.add("-m");
		cmd.add(path);
		cmd.add("-o");
		cmd.add(path + ".sol");
		if (!fa.processCommand(cmd)) {
			return false;
		}
		return true;
	}

	public Boolean runSolverSimplex(String path) {
		FileAccess fa = new FileAccess();
		ArrayList<String> cmd = new ArrayList<String>();
		cmd.add("/usr/local/bin/glpsol");
		cmd.add("-m");
		cmd.add(path);
		cmd.add("-o");
		cmd.add(path + ".sol");
		if (!fa.processCommand(cmd)) {
			return false;
		}
		return true;
	}

	public Boolean runSolver(String path, String infile, String outfile) {
		FileAccess fa = new FileAccess();
		ArrayList<String> cmd = new ArrayList<String>();
		cmd.add("/usr/local/bin/glpsol");
		cmd.add("-m");
		cmd.add(path + infile);
		cmd.add("-o");
		cmd.add(path + outfile);
		if (!fa.processCommand(cmd)) {
			return false;
		}
		return true;
	}

	protected void addVariables(FileAccess fa, BufferedWriter bf, List vertices) {
		if (this.variablesCached == null) {
			this.variablesCached = "";
			for (int i = 0; i < vertices.size(); ++i) {
				Vertex v = (Vertex) vertices.get(i);
				if (v.isFact())
					continue;
				this.variablesCached = this.variablesCached + "var x" + (i + 1) + "<=1; \n";
			}
		}
		if (!fa.writeToFile(bf, this.variablesCached)) {
			System.out.println("I/O error");
		}
	}

	protected void addVariables(String file, List vertices) {
		if (this.variablesCached == null) {
			this.variablesCached = "";
			for (int i = 0; i < vertices.size(); ++i) {
				Vertex v = (Vertex) vertices.get(i);
				if (v.isFact())
					continue;
				this.variablesCached = this.variablesCached + "var x" + (i + 1) + "<=1; \n";
			}
		}
		FastIO fast = new FastIO();
		fast.write(file, this.variablesCached);
	}

	protected void writeObjectiveFunctionSimple(String file) {
		FastIO fast = new FastIO();
		fast.writeAppend(file, "\nmaximize z: x1;\n");
	}

	protected List findNeighbors(int index, List arcs, List vertices) {
		ArrayList<Vertex> neighbors = new ArrayList<Vertex>();
		for (int i = 0; i < arcs.size(); ++i) {
			Arc a = (Arc) arcs.get(i);
			if (a.v != index)
				continue;
			Vertex u = (Vertex) vertices.get(a.u - 1);
			neighbors.add(u);
		}
		return neighbors;
	}

	protected boolean areAllFacts(List neighbors) {
		for (int i = 0; i < neighbors.size(); ++i) {
			if (((Vertex) neighbors.get(i)).isFact())
				continue;
			return false;
		}
		return true;
	}

	protected double[] generateUniformGoalSelectors(int count) {
		double[] value = new double[count];
		if (count == 1) {
			value[0] = 1.0;
			return value;
		}
		double uniformWeight = 1.0 / (double) count;
		for (int i = 0; i < count; ++i) {
			value[i] = uniformWeight;
		}
		return value;
	}
}
