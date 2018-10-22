/**
 * MULTIGRAPH
 *
 * Project for Mattia Zago Master Thesis
 *
 * (C) 2015 - Mattia Zago
 *
 */
package es.um.multigraph.decision.poolsappasitmoop;

import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Mattia Zago <a href="mailto:dev@zagomattia.it">dev@zagomattia.it</a>
 */
public class BayesianCountermeasureSet {

    public List<BayesianCMNode> CM;
    public String ID;

    public BayesianCountermeasureSet() {
        this.CM = new LinkedList<>();
        this.ID = "";
    }

    public List<BayesianCMNode> getCMS() {
        return CM;
    }

    public void add(BayesianCMNode n) {
        this.CM.add(n);
    }

    public void del(BayesianCMNode n) {
        this.CM.remove(n);
    }

    public void enable(BayesianCMNode n) {
        n.enable();
    }

    public void disable(BayesianCMNode n) {
        n.disable();
    }

    public void setCMS(List<BayesianCMNode> countermeasureList) {
        this.CM = countermeasureList;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public void setStatus(int p, boolean s) {
        setStatus(CM.get(p), s);
    }

    public void setStatus(BayesianCMNode n, boolean s) {
        if (s) {
            enable(n);
        } else {
            disable(n);
        }
    }

    public void invertStatus(int pos) {
        this.CM.get(pos).setState(!CM.get(pos).getState());
    }

    public void invertStatus(BayesianCMNode node) {
        invertStatus(CM.indexOf(node));
    }

    public String toString() {
        String result = "CMs Set: [";
        result = getCMS().stream().map((n) -> ", (" + n.getID() + "," + n.getState() + ")").reduce(result, String::concat);
        result = result.replaceFirst(",", "");
        result += " ]";
        return result;
    }

    public boolean equals(BayesianCountermeasureSet tmp) {
        boolean result = false;

        List<BayesianCMNode> me = new LinkedList<>(this.getCMS());
        List<BayesianCMNode> other = new LinkedList<>(tmp.getCMS());

        if (me.size() != other.size()) {
            return false;
        }

        for (BayesianCMNode meNode : me) {
            BayesianCMNode choosen = null;

            for (BayesianCMNode otherNode : other) {
                if (meNode.equals(otherNode)) {
                    choosen = otherNode;
                    break;
                }
            }

            if (choosen == null) {
                return false;
            }
        }

        return true;
    }
}
