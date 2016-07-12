/**
 * MULTIGRAPH
 *
 * Project for Mattia Zago Master Thesis
 *
 * (C) 2015 - Mattia Zago
 *
 */
package es.um.multigraph.decision.basegraph;

import es.um.multigraph.decision.model.BayesianJDialogNode;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author Mattia Zago <a href="mailto:dev@zagomattia.it">dev@zagomattia.it</a>
 */
public class Node {
    
    public Node(String ID) {
        this.ID = ID;
    }
    
    public String ID;
    public String label;
    
    public Set<Edge> out = new HashSet<>();
    public Set<Edge> in = new HashSet<>();
    public Set<Node> cachedParent;

    public Set<Edge> getOut() {
        return out;
    }
    public void setOut(Set<Edge> out) {
        this.out = out;
    }
    public void addOutEdge(Edge edge) {
        this.out.add(edge);
    }
    public Set<Edge> getIn() {
        return in;
    }
    public void setIn(Set<Edge> in) {
        this.in = in;
    }
    public void addInEdge(Edge edge) {
        this.in.add(edge);
    }
    public Set<? extends Node> getCachedParent() {
        return cachedParent;
    }
    public String getID() {
        return this.ID;
    }
    public void setId(String ID) {
        this.ID = ID;
    }
    public String getLabel() {
        return this.label;
    }
    public void setLabel(String label) {
        this.label = label;
    }
    public void setCachedParent(Set<Node> cachedParent) {
        this.cachedParent = cachedParent;
    }
  
    /**
     * A parent node is a node with an edge directed to this node.
     *
     * @param recompute
     * @return
     */
    public Set<Node> getParents(boolean recompute) {

        if (!recompute) {
            return this.cachedParent;
        }

        Set<Node> parents = new TreeSet<>();
        in.stream().forEach((e) -> {
            
            parents.add(e.getFrom());
        });
        this.setCachedParent(parents);
        return parents;
    }
    
    public Set<String> getParentsIDs() {
        Set<String> parents = new TreeSet<>();
        in.stream().forEach((e) -> {
            
            parents.add(e.getFrom().getID());
        });
        return parents;
    }
    
    
    /**
     * A state node 'n' is external if not exists a state 'a' such that
     * n=post(a).
     *
     * @return True if the node has empty input list.
     */
    public boolean isExternal() {
        return in.isEmpty();
    }

    /**
     * A state node 'n' is internal if exist two states 'a1,a2' such that
     * n=pre(a1) and n=pre(a2).
     *
     * @return True if the node has not empty input list and not empty output
     * list.
     */
    public boolean isInternal() {
        return !in.isEmpty() && !out.isEmpty();
    }

    /**
     * A state node 'n' is terminal if not exists a state 'a' such that
     * n=pre(a).
     *
     * @return True if the node has empty output list.
     */
    public boolean isTerminal() {
        return out.isEmpty();
    }
    
    
    /**
     * Return true if:
     * <ol><li>One of the exiting edges target is the argument state</li>
     * <li>One of the exiting edges can reach the argument state</li>
     * </ol>
     *
     * @param x State desired
     * @return True if can be reached, false otherwise
     */
    public boolean canReach(Node x) {
        
        if(x == null)
            return false;
        
        if(this.equals(x))
            return true;
        
        if(out == null)
            return false;
        
        for(Iterator<Edge> it = out.iterator(); it.hasNext(); ) {
            Node tmp = it.next().getTo();
            if(tmp.equals(x) || tmp.canReach(x))
                return true;
        }
        
        return false;
    }

    /**
     * Return the path from this state node to the desired state node.
     *
     * @param x Desired state
     * @return List of edges
     */
    public List<Edge> pathTo(Node x) {
        List<Edge> chain = new LinkedList<>();
        
        for (Edge e : out) {
            if (e.getTo().equals(x)) {
                chain.add(e);
                return chain;
            }
            if (e.getTo().canReach(x)) {
                chain.add(e);
                chain.addAll(e.getTo().pathTo(x));
                return chain;
            }
        }
        
        return null;
    }
    
    
    public static JDialogNode getJDialog(Class<? extends Node> nodeClass) {
        return new JDialogNode(null, true, nodeClass, nodeClass.getFields());
    }
    
    public static JPanel getJPanel(Class<? extends Node> node) {
        JPanel result = new JPanel();
        Field[] ff = node.getClass().getFields();
        result.setLayout(new GridLayout(ff.length, 2));
        
        
        result.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;

        int i = 0;
        for(Field f : ff) {
            if(String.class.isAssignableFrom(f.getType())) {
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
    
    @Override
    public String toString() {
        return "ID: "+this.getID()+" - Label: "+this.getLabel();
    }
    
    public String getFullRepresentationAsString(boolean printEdges) {
        String result =  this.ID + ":\n"
                + "\tClass:\t"+this.getClass()+"\n"
                + "\tLabel:\t"+this.label;
        if(printEdges) {
            result += "\n\tOut Edges:";
            for(Edge e : this.getOut())
                result += "\n\t\t"+e.getFullRepresentationAsString();
            result += "\n\tIn Edges:";
            for(Edge e : this.getIn())
                result += "\n\t\t"+e.getFullRepresentationAsString();
            
        }
        return result;
    }

    public String getLabelGraph() {
        return this.getLabel();
    }
}
