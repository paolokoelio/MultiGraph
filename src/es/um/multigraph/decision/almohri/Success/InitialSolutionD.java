package es.um.multigraph.decision.almohri.Success;



/*
 * Decompiled with CFR 0.139.
 * 
 * Could not load the following classes:
 *  Graph.Graph
 *  Graph.SolutionReader
 *  Graph.Vertex
 *  Utilities.FastIO
 */
//package Success;

//import Graph.Graph;
//import Graph.SolutionReader;
//import Graph.Vertex;
import es.um.multigraph.decision.almohri.Graph.*;
import es.um.multigraph.decision.almohri.Success.Solver;
//import Success.Solver;
import Utilities.FastIO;
import java.io.PrintStream;
import java.nio.channels.FileChannel;
import java.text.DecimalFormat;
import java.util.List;

public class InitialSolutionD
extends Solver {
    public Graph findInitialSolution(Graph g, String path) {
        this.createModelFileUniform(path, g);
        System.out.println("Initial model created.");
        this.runSolverInterior(path + "linear.m");
        SolutionReader reader = new SolutionReader();
        reader.readSolFileUtility(path + "linear.m.sol", g, true);
        System.out.println("Initial model computed.");
        return g;
    }

    public void createModelFileUniform(String path, Graph g) {
        String file = path + "linear.m";
        this.addVariables(file, g.vertices);
        this.writeObjectiveFunctionSimple(file);
        this.writeConstraints(file, g);
    }

    private String neighborsNonRoot(List neighbors, Vertex n, int index, List vertices) {
        DecimalFormat df2 = new DecimalFormat("#.#######");
        String constraint = "";
        if (n.isGoal() && neighbors.size() == 1) {
            constraint = "s.t. c" + n.node + ": x" + n.node + " = x" + ((Vertex)neighbors.get((int)0)).node + ";\n";
        } else if (n.isGoal() && neighbors.size() > 1) {
            double[] weights = this.generateUniformGoalSelectors(neighbors.size());
            n.randWeights = weights;
            vertices.set(index, n);
            constraint = "s.t. c" + n.node + ": x" + n.node + " = " + df2.format(weights[0]) + "*x" + ((Vertex)neighbors.get((int)0)).node;
            for (int i = 1; i < neighbors.size(); ++i) {
                constraint = constraint + "+" + df2.format(weights[i]) + "*x" + ((Vertex)neighbors.get((int)i)).node;
            }
            constraint = constraint + ";\n";
        } else if (n.isRule()) {
            if (this.areAllFacts(neighbors)) {
                double a = 1.0;
                for (int i = 0; i < neighbors.size(); ++i) {
                    a *= ((Vertex)neighbors.get((int)i)).initialProbability;
                }
                constraint = "s.t. c" + n.node + ": x" + n.node + "=" + df2.format(a) + ";\n";
            } else {
                double a = 1.0;
                String nonfact = "x";
                for (int i = 0; i < neighbors.size(); ++i) {
                    if (((Vertex)neighbors.get(i)).isFact()) {
                        a *= ((Vertex)neighbors.get((int)i)).initialProbability;
                        continue;
                    }
                    nonfact = nonfact + ((Vertex)neighbors.get((int)i)).node;
                }
                constraint = "s.t. c" + n.node + ": x" + n.node + "=" + df2.format(a) + "*" + nonfact + ";\n";
            }
        }
        constraint = constraint + "s.t. l" + n.node + ": x" + n.node + ">=" + "0;\n";
        return constraint;
    }

    private void writeConstraints(String file, Graph g) {
        DecimalFormat df = new DecimalFormat("#.######");
        FastIO fast = new FastIO();
        FileChannel channel = fast.openChannel(file);
        fast.writeAppend(channel, "\n\n/* constraints */\n\n");
        fast.writeAppend(channel, "\n\n/* constraints */\n\n");
        for (int i = 0; i < g.vertices.size(); ++i) {
            List neighbors;
            Vertex n = (Vertex)g.vertices.get(i);
            if (n.isFact() || (neighbors = n.predecessors != null ? n.predecessors : this.findNeighbors(i + 1, g.arcs, g.vertices)).isEmpty()) continue;
            String constraint = "";
            constraint = this.neighborsNonRoot(neighbors, n, i, g.vertices);
            fast.writeAppend(file, constraint);
        }
        fast.writeAppend(channel, "\n");
        fast.closeChannel(channel);
    }
}

