package es.um.multigraph.decision.almohri.Graph;

/*
 * Decompiled with CFR 0.139.
 * 
 * Could not load the following classes:
 *  Graph.Graph
 *  Graph.Vertex
 *  Utilities.FileAccess
 */

import es.um.multigraph.decision.almohri.Graph.*;
import Utilities.FileAccess;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.List;

public class SolutionReader {
    public List readSolFileUtility(String path, Graph g, boolean interior) {
        String buffer;
        FileAccess fa = new FileAccess();
        BufferedReader bf = fa.createBufferedReader(path);
        if (bf == null) {
            return null;
        }
        this.skipHeaders(bf, fa);
        while ((buffer = fa.readFromFile(bf)) != null) {
            String[] bsplit = buffer.split(" ");
            if (bsplit.length > 1) {
                this.extractVariableUtility(bsplit, g, interior);
            }
            if (buffer.length() != 0) continue;
        }
        return g.vertices;
    }

    public void writeSolutionFile(String path, Graph g) {
        FileAccess fa = new FileAccess();
        BufferedWriter bf = fa.createBufferedWriter(path + "results.txt");
        for (int i = 0; i < g.vertices.size(); ++i) {
            Vertex v = (Vertex)g.vertices.get(i);
            if (!v.isFact()) {
                fa.writeToFile(bf, v.node + "," + v.bodyPlain + "," + v.computedProbability);
            } else {
                fa.writeToFile(bf, v.node + "," + v.bodyPlain + "," + v.initialProbability);
            }
            fa.writeToFile(bf, "\n");
        }
        fa.closeBufferedWriter(bf);
    }

    private void skipHeaders(BufferedReader bf, FileAccess fa) {
        int c = 0;
        String buffer = " ";
        while (buffer != null && (buffer = fa.readFromFile(bf)) != null) {
            if (buffer.toLowerCase().contains("------")) {
                ++c;
            }
            if (c != 2) continue;
            break;
        }
    }

    private List removeSpaces(String[] line) {
        ArrayList<String> nline = new ArrayList<String>();
        for (int i = 0; i < line.length; ++i) {
            if (line[i].length() == 0) continue;
            nline.add(line[i]);
        }
        return nline;
    }

    private void extractVariableUtility(String[] line, Graph g, boolean interior) {
        String var = "";
        int index = 0;
        boolean xindex = false;
        List nline = this.removeSpaces(line);
        var = (String)nline.get(1);
        if (var.charAt(0) == 'y') {
            return;
        }
        try { //some times parseInt throws an ex 
        	index = Integer.parseInt(var.substring(1));
        	--index;}
        catch (Exception ex) {
                System.err.println("ParseInt failed: " + ex);
            }
        try {    
        double value = interior ? Double.parseDouble((String)nline.get(2)) : Double.parseDouble((String)nline.get(3));

            Vertex v = (Vertex)g.vertices.get(index);
            v.computedProbability = value;
            g.vertices.set(index, v);
        }
        catch (Exception ex) {
            // empty catch block
        }
    }
}
