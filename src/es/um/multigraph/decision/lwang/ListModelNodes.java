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
import es.um.multigraph.decision.basegraph.Node;

/**
 *
 * @author Mattia Zago <a href="mailto:dev@zagomattia.it">dev@zagomattia.it</a>
 */
public class ListModelNodes extends DefaultListModel<Node>{
    public ListModelNodes(Collection<Node> nodes) {
        super.clear();
        if(nodes != null) {
            nodes.stream().forEach((x) -> {
                super.addElement(x);
            });
        }
    }

}
