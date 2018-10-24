/**
 * 
 */
package es.um.multigraph.decision.basegraph;

/**
 * A generic interface to convert AGs from imported format.
 * @author Pavlo Burda - p.burda@tue.nl
 *
 */
public interface Adapter {
	public void convertAG();

	public void convertNodes();

	public void convertEdges();

}
