package es.um.multigraph.decision.almohri.Graph;

/*
 * Decompiled with CFR 0.139.
 * 
 * Could not load the following classes:
 *  Graph.Arc
 *  Graph.Graph
 *  Graph.Initial
 *  Graph.Optimizer
 *  Graph.Vertex
 *  Utilities.FileAccess
 */
// package Graph;

//import Graph.Arc;
//import Graph.Graph;
//import Graph.Initial;
//import Graph.Optimizer;
//import Graph.Vertex;
import es.um.multigraph.decision.almohri.Graph.*;
import Utilities.FileAccess;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

public class GraphReader {
    public Graph readGraph(String path, boolean optimize) {
        List arcs = this.readArcsFile(path + "ARCS.CSV");
        if (arcs == null) {
            return null;
        }
        List vertices = this.readVertexFile(path + "VERTICES.CSV");
        if (vertices == null) {
            return null;
        }
        List initial = this.readInitialValuesFile(path + "initial.in");
        Graph g = this.updateGraphWithInitialValues(initial, new Graph(arcs, vertices));
        if (optimize) {
            Optimizer o = new Optimizer();
            o.optimize(g);
        }
        return g;
    }

    public Graph readGraph(String path) {
        return this.readGraph(path, false);
    }

    public Graph readGraphOptimize(String path) {
        return this.readGraph(path, true);
    }

    private BufferedReader openFile(String filename) {
        BufferedReader bf = null;
        try {
            bf = new BufferedReader(new FileReader(filename));
        }
        catch (FileNotFoundException ex) {
            // empty catch block
        }
        return bf;
    }

    private List readArcsFile(String file) {
        ArrayList<Arc> arcList = new ArrayList<Arc>();
        BufferedReader bf = this.openFile(file);
        if (bf == null) {
            return null;
        }
        String buffer = "";
        while (buffer != null) {
            try {
                buffer = bf.readLine();
                if (buffer == null) break;
                String[] buffersplit = buffer.split(",");
                Arc newArc = new Arc();
                newArc.v = Integer.parseInt(buffersplit[0]);
                newArc.u = Integer.parseInt(buffersplit[1]);
                newArc.weight = Integer.parseInt(buffersplit[2]);
                arcList.add(newArc);
            }
            catch (IOException exp) {
                System.out.println("Could not read ARCS.CSV.");
            }
        }
        return arcList;
    }

    private List readVertexFile(String file) {
        ArrayList<Vertex> vertList = new ArrayList<Vertex>();
        BufferedReader bf = this.openFile(file);
        if (bf == null) {
            return null;
        }
        String buffer = "";
        while (buffer != null) {
            try {
                buffer = bf.readLine();
                if (buffer == null) break;
                String[] buffersplit = buffer.split(",");
                Vertex newVertex = new Vertex();
                newVertex.node = Integer.parseInt(buffersplit[0]);
                newVertex.mulvaln = Double.parseDouble(buffersplit[buffersplit.length - 1]);
                newVertex.attackNode = newVertex.mulvaln != 0.0 && newVertex.mulvaln != 1.0;
                switch (buffersplit[buffersplit.length - 2]) {
                    case "\"AND\"": {
                        newVertex.type = 0;
                        break;
                    }
                    case "\"OR\"": {
                        newVertex.type = 1;
                        break;
                    }
                    default: {
                        newVertex.type = 2;
                    }
                }
                newVertex.body = "";
                for (int i = 1; i < buffersplit.length - 2; ++i) {
                    newVertex.body = i < buffersplit.length - 2 ? newVertex.body + buffersplit[i] + "," : newVertex.body + buffersplit[i];
                }
                newVertex.bodyPlain = newVertex.body.substring(1, newVertex.body.length() - 2);
                vertList.add(newVertex);
            }
            catch (IOException exp) {
                System.out.println("Could not read ARCS.CSV.");
            }
        }
        return vertList;
    }

    private List readInitialValuesFile(String file) {
        ArrayList<Initial> initialValues = new ArrayList<Initial>();
        FileAccess fa = new FileAccess();
        BufferedReader bf = fa.createBufferedReader(file);
        if (bf == null) {
            return null;
        }
        String buffer = "";
        while (buffer != null && (buffer = fa.readFromFile(bf)) != null && buffer.length() != 0) {
            String[] bsplit = buffer.split(",");
            Initial p = new Initial();
            p.name = bsplit[0].replaceAll(" ", "").toLowerCase();
            p.value = Double.parseDouble(bsplit[1].replaceAll(" ", ""));
            initialValues.add(p);
        }
        return initialValues;
    }

    private double uniformInitialValue(Vertex v) {
        if (v.isAvailability()) {
            return 0.75;
        }
        if (v.isHostAccess()) {
            return 0.8500000238418579;
        }
        if (v.isNetAccess()) {
            return 0.9900000095367432;
        }
        if (v.isIPTable()) {
            return 0.9900000095367432;
        }
        if (v.isAttacker()) {
            return 0.949999988079071;
        }
        return 0.800000011920929;
    }

    private Graph updateGraphWithInitialValues(List initialValues, Graph g) {
        Graph gp = new Graph(g.arcs, new ArrayList());
        gp.size = g.vertices.size();
        int appliedTo = 0;
        int uniformvalues = 0;
        boolean skipInitial = false;
        if (initialValues == null) {
            skipInitial = true;
        }
        for (int i = 0; i < g.vertices.size(); ++i) {
            Vertex v = (Vertex)g.vertices.get(i);
            if (v.type != 2) {
                gp.vertices.add(v);
                continue;
            }
            if (!skipInitial) {
                for (int j = 0; j < initialValues.size(); ++j) {
                    Initial p = (Initial)initialValues.get(j);
                    if (!v.body.contains(p.name)) continue;
                    ++appliedTo;
                    v.initialProbability = p.value;
                    break;
                }
            }
            if (v.initialProbability == 0.0) {
                v.initialProbability = this.uniformInitialValue(v);
                ++uniformvalues;
            }
            gp.vertices.add(v);
        }
        System.out.println("Initial values applied to " + appliedTo);
        System.out.println("Initial uniform values " + uniformvalues);
        return gp;
    }
}
