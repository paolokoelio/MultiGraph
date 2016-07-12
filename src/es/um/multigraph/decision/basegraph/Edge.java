/**
 * MULTIGRAPH
 *
 * Project for Mattia Zago Master Thesis
 *
 * (C) 2015 - Mattia Zago
 *
 */
package es.um.multigraph.decision.basegraph;

/**
 *
 * @author Mattia Zago <a href="mailto:dev@zagomattia.it">dev@zagomattia.it</a>
 * @param <N> Node type
 */
public class Edge<N extends Node> {

    private String ID;
    private N from;
    private N to;

    public Edge(String ID, N from, N to) {
        this.from = from;
        this.to = to;
        this.ID = ID;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public N getFrom() {
        return from;
    }

    public void setFrom(N from) {
        this.from = from;
    }

    public N getTo() {
        return to;
    }

    public void setTo(N to) {
        this.to = to;
    }

    public void addThisToNodes() {
        this.to.addInEdge(this);
        this.from.addOutEdge(this);
    }

    @Override
    public String toString() {
        return "(" + this.getClass().getSimpleName().replace("]", "").replace("[", "") + ") From: " + this.getFrom().getID() + " - To: " + this.getTo().getID();
    }

    public String getFullRepresentationAsString() {
        return this.toString() + " (" + this.getClass() + ")";
    }

    public String getLabelGraph() {
        return this.toString();
    }
}
