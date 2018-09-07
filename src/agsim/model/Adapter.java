/**
 * 
 */
package agsim.model;

import org.w3c.dom.NodeList;

/**
 * @author julien
 *
 */
public interface Adapter {
	public void  convertAG(NodeList nodes, NodeList arcs);
}
