package es.um.multigraph.decision.almohri.Graph;

/*
 * Decompiled with CFR 0.139.
 * 
 * Could not load the following classes:
 *  Graph.Arc
 *  Graph.Graph
 *  Graph.Vertex
 */
//package Graph;
//
//import Graph.Arc;
//import Graph.Graph;
//import Graph.Vertex;
import es.um.multigraph.decision.almohri.Graph.*;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

public class Optimizer {
    private int slices;

    public Optimizer() {
        this.slices = 12;
    }

    public Optimizer(int slices) {
        this.slices = slices;
    }

    public void optimize(Graph g) {
        for (int i = 0; i < g.vertices.size(); ++i) {
            Vertex u = (Vertex)g.vertices.get(i);
            if (!u.isGoal()) continue;
            this.sliceLargePredecessorSet(g, u, this.slices);
        }
    }

    public boolean sliceLargePredecessorSet(Graph g, Vertex u, int slices) {
        int i;
        if (u.predecessors != null) {
            return false;
        }
        u.predecessors = this.findPredecessorSet(u.node, g);
        if (!u.isGoal()) {
            return false;
        }
        int phiSize = u.predecessors.size();
        if (phiSize < slices || phiSize < 50) {
            return false;
        }
        System.out.println("Optimizing node: " + u.node + " (" + u.bodyPlain + ") with |\\phi(u)|=" + phiSize + " using slice factor: " + slices + " and original " + "graph size: " + g.vertices.size());
        int sizeOfEachSlice = phiSize / slices;
        int indexForDummyNode = g.vertices.size();
        int indexForDummyRule = g.vertices.size() + slices;
        ArrayList<Vertex> dummyNodes = new ArrayList<Vertex>();
        ArrayList<Vertex> dummyRules = new ArrayList<Vertex>();
        for (i = 0; i < slices; ++i) {
            Vertex v = new Vertex();
            v.bodyPlain = "DummyGoal";
            v.body = "DummyGoal";
            v.type = 1;
            v.node = ++indexForDummyNode;
            dummyNodes.add(v);
            Vertex r = new Vertex();
            r.bodyPlain = "DummyRule";
            r.body = "DummyRule";
            r.type = 0;
            r.node = ++indexForDummyRule;
            dummyRules.add(r);
            Arc arc = new Arc();
            arc.v = r.node;
            arc.u = v.node;
            g.vertices.add(v);
            g.arcs.add(arc);
            Arc arcToU = new Arc();
            arcToU.v = u.node;
            arcToU.u = r.node;
            g.arcs.add(arcToU);
        }
        for (i = 0; i < dummyRules.size(); ++i) {
            Vertex r = (Vertex)dummyRules.get(i);
            g.vertices.add(r);
        }
        int j = 0;
        int k = 0;
        for (int i2 = 0; i2 < slices; ++i2) {
            int max = sizeOfEachSlice > phiSize - j ? phiSize - j : sizeOfEachSlice;
            Vertex dummy = (Vertex)dummyNodes.get(i2);
            while (j < (max += j) && k < g.arcs.size()) {
                Arc arc = (Arc)g.arcs.get(k);
                if (arc.v == u.node) {
                    arc.v = dummy.node;
                    g.arcs.set(k, arc);
                    ++j;
                }
                ++k;
            }
        }
        System.out.println("Resulting graph size: " + g.vertices.size() + "" + " with " + j + " updated arcs");
        return true;
    }

    private List findPredecessorSet(int index, Graph g) {
        ArrayList<Vertex> neighbors = new ArrayList<Vertex>();
        for (int i = 0; i < g.arcs.size(); ++i) {
            Arc a = (Arc)g.arcs.get(i);
            if (a.v != index) continue;
            Vertex u = (Vertex)g.vertices.get(a.u - 1);
            neighbors.add(u);
        }
        return neighbors;
    }
}
