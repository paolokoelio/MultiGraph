package es.um.multigraph.decision.almohri.Success;

/*
 * Decompiled with CFR 0.139.
 * 
 * Could not load the following classes:
 *  Graph.Vertex
 *  org.lsmp.djep.djep.DJep
 *  org.lsmp.djep.xjep.PrintVisitor
 *  org.nfunk.jep.Node
 *  org.nfunk.jep.ParseException
 */

//import Graph.Vertex;
import es.um.multigraph.decision.almohri.Graph.*;
import java.io.PrintStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Pattern;
import org.lsmp.djep.djep.DJep;
import org.lsmp.djep.xjep.PrintVisitor;
import org.nfunk.jep.Node;
import org.nfunk.jep.ParseException;

public class TaylorExpansion {
    private DecimalFormat dfSmall = new DecimalFormat("#.######");

    public String taylorExpansion(Vertex n) {
        String diff;
        String diffFor;
        String exp;
        int i;
        if (n.predecessors == null) {
            return "";
        }
        double[] weights = n.randWeights;
        String initial = this.goalNeighborsInitial(n.node, n.predecessors, weights, 0);
        String initialEvaluated = this.evaluate(initial);
        if (!initialEvaluated.equals("")) {
            initial = initialEvaluated;
        }
        List y = this.yVariables(n);
        String constraint = initial;
        for (i = 0; i < n.predecessors.size(); ++i) {
            Vertex v = (Vertex)n.predecessors.get(i);
            constraint = constraint + "+";
            constraint = constraint + "(x" + v.node + "-" + this.dfSmall.format(v.computedProbability) + ")*";
            List X = n.predecessors;
            this.setVariableBodies(X, y, weights, i, -1, n.node);
            exp = "(" + this.goalNeighborsInitialOneVariable(n.node, X, y, weights, 0) + ")";
            exp = this.evaluate(exp);
            exp = this.resolvePlusMinus(exp);
            diffFor = this.xSymbol((Vertex)X.get(i));
            diff = this.differentiate(exp, diffFor);
            diff = this.resolvePlusMinus(diff);
            constraint = constraint + "(" + diff + ")";
        }
        for (i = 0; i < weights.length - 1; ++i) {
            constraint = constraint + "+";
            constraint = constraint + "(" + this.yVariable(n.node, i) + "-" + this.dfSmall.format(weights[i]) + ")*";
            List X = n.predecessors;
            this.setVariableBodies(X, y, weights, -1, i, n.node);
            String init = this.goalNeighborsInitialOneVariable(n.node, X, y, weights, 0);
            exp = "(" + init + ")";
            exp = this.resolvePlusMinus(exp);
            diffFor = ((Vertex)y.get((int)i)).body;
            diff = this.differentiate(exp, diffFor);
            diff = this.resolvePlusMinus(diff);
            constraint = constraint + "(" + diff + ")";
        }
        constraint = this.resolvePlusMinus(this.evaluate(constraint));
        return constraint;
    }

    private String resolvePlusMinus(String exp) {
        return exp.replaceAll(Pattern.quote("+-"), "-");
    }

    public String goalNeighborsInitial(int index, List neighbors, double[] weights, int start) {
        double computedProbability1 = ((Vertex)neighbors.get((int)start)).computedProbability;
        double computedProbability2 = ((Vertex)neighbors.get((int)(start + 1))).computedProbability;
        if (neighbors.size() - start == 2) {
            return "(" + this.dfSmall.format(weights[start]) + "*" + this.dfSmall.format(computedProbability1) + ")" + "+" + "(" + this.dfSmall.format(1.0 - weights[start]) + "*" + this.dfSmall.format(computedProbability2) + ")";
        }
        return "(" + this.dfSmall.format(weights[start]) + "*" + this.dfSmall.format(computedProbability1) + ")" + "+" + this.dfSmall.format(1.0 - weights[start]) + "*(" + this.goalNeighborsInitial(index, neighbors, weights, start + 1) + ")";
    }

    public String goalNeighborsInitialOneVariable(int index, List x, List y, double[] weights, int start) {
        if (x.size() - start == 2) {
            return ((Vertex)y.get((int)start)).body + "*" + ((Vertex)x.get((int)start)).body + "+((1-" + ((Vertex)y.get((int)start)).body + ")*" + ((Vertex)x.get((int)(start + 1))).body + ")";
        }
        return ((Vertex)y.get((int)start)).body + "*" + ((Vertex)x.get((int)start)).body + "+(1-" + ((Vertex)y.get((int)start)).body + ")*(" + this.goalNeighborsInitialOneVariable(index, x, y, weights, start + 1) + ")";
    }

    private void setRandomInitial(List neighbors) {
        for (int i = 0; i < neighbors.size(); ++i) {
            Vertex v = (Vertex)neighbors.get(i);
            if (v.computedProbability != 0.0) continue;
            v.computedProbability = this.randomProb();
            neighbors.set(i, v);
        }
    }

    private void setVariableBodies(List X, List y, double[] w, int skipx, int skipy, int index) {
        for (int i = 0; i < X.size(); ++i) {
            Vertex x;
            Vertex v;
            if (skipy == i) {
                v = new Vertex();
                v.body = this.yVariable(index, i);
                y.set(i, v);
            } else if (i < y.size()) {
                v = new Vertex();
                v.body = this.dfSmall.format(w[i]);
                y.set(i, v);
            }
            if (skipx == i) {
                x = (Vertex)X.get(i);
                x.body = "x" + x.node;
                X.set(i, x);
                continue;
            }
            x = (Vertex)X.get(i);
            x.body = this.dfSmall.format(x.computedProbability);
            X.set(i, x);
        }
    }

    private String evaluate(String exp) {
        DJep j = new DJep();
        j.addStandardConstants();
        j.addStandardFunctions();
        j.addComplex();
        j.setAllowUndeclared(true);
        j.setAllowAssignment(true);
        j.setImplicitMul(true);
        try {
            Node node = j.parse(exp);
            Node simp = j.simplify(node);
            PrintVisitor p = j.getPrintVisitor();
            try {
                return this.dfSmall.format(p.toString(simp));
            }
            catch (Exception ex2) {
                return p.toString(simp);
            }
        }
        catch (ParseException ex) {
            System.out.println("Error: " + exp);
            return "";
        }
    }

    private String differentiate(String exp, String var) {
        DJep j = new DJep();
        j.addStandardConstants();
        j.addStandardFunctions();
        j.addComplex();
        j.setAllowUndeclared(true);
        j.setAllowAssignment(true);
        j.setImplicitMul(true);
        try {
            Node diff;
            Node node = j.parse(exp);
            Node simp = diff = j.differentiate(node, var);
            PrintVisitor p = j.getPrintVisitor();
            try {
                return this.dfSmall.format(p.toString(simp));
            }
            catch (Exception ex2) {
                return p.toString(simp);
            }
        }
        catch (ParseException ex) {
            System.out.println("Error: " + exp);
            return "";
        }
    }

    private String yVariable(int xi, int yi) {
        return "y" + yi + "x" + xi;
    }

    private String xSymbol(Vertex v) {
        return "x" + v.node;
    }

    private double randomProb() {
        Random generator = new Random();
        return generator.nextDouble() * 1.0;
    }

    private List yVariables(Vertex v) {
        int numberOfYVariables = v.predecessors.size() - 1;
        if (numberOfYVariables < 1) {
            return null;
        }
        ArrayList<Vertex> Y = new ArrayList<Vertex>();
        int i = 0;
        while (i < numberOfYVariables) {
            Vertex y = new Vertex();
            y.body = this.yVariable(v.xindex + 1, i);
            y.node = i++;
            Y.add(y);
        }
        return Y;
    }
}
