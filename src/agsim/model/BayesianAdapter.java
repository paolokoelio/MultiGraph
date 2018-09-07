package agsim.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import es.um.multigraph.decision.model.BayesianNode;

public class BayesianAdapter implements Adapter {

	private Set<BayesianNode> BAG;
	private List<HashMap<String, String>> myNodes;
	private List<HashMap<String, String>> myEdges;

	@Override
	public void convertAG(NodeList nodes, NodeList arcs) {

		// this.BAG = new HashSet<BayesianNode>();
		this.myNodes = new ArrayList<HashMap<String, String>>();
		importNodes(nodes);
		this.myEdges = new ArrayList<HashMap<String, String>>();
		importNodes(arcs);

	}

	/**
	 * @param nodes
	 */
	public void importNodes(NodeList nodes) {
		for (int temp = 0; temp < nodes.getLength(); temp++) {
			Node nNode = nodes.item(temp);

			HashMap<String, String> myNode = new HashMap<String, String>();

			NodeList subNodeList = nNode.getChildNodes();

			for (int i = 0; i < subNodeList.getLength(); i++) {
				Node currentNode = subNodeList.item(i);

				if (currentNode.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) currentNode;
					myNode.put(eElement.getNodeName(), eElement.getTextContent());
					// debug
//					System.out.println(eElement.getNodeName() + ": " + eElement.getTextContent());
				}

			}

			this.myNodes.add(myNode);
		}

//		System.out.println(this.myNodes);
	}

}
