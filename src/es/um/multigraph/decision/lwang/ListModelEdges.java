/**
 * MULTIGRAPH
 *
 * Project for Mattia Zago Master Thesis
 *
 * (C) 2015 - Mattia Zago
 *
 */
package es.um.multigraph.decision.lwang;

import java.util.Collection;
import javax.swing.DefaultListModel;

/**
 *
 * @author Mattia Zago <a href="mailto:dev@zagomattia.it">dev@zagomattia.it</a>
 */
public class ListModelEdges extends DefaultListModel<MyEdge>{
    public ListModelEdges(Collection<? extends MyEdge> edges) {
        super.clear();
        if(edges != null) {
            edges.stream().forEach((x) -> {
                super.addElement(x);
            });
        }
    }
}
