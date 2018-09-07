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
		parseElements(nodes, this.myNodes);
		this.myEdges = new ArrayList<HashMap<String, String>>();
		parseElements(arcs, this.myEdges);
		System.out.println(this.myNodes);
		System.out.println(this.myEdges);
	}

	/**
	 * @param nodes
	 */
	public void parseElements(NodeList xmlNodes, List<HashMap<String, String>> myElements) {
		for (int temp = 0; temp < xmlNodes.getLength(); temp++) {
			Node nNode = xmlNodes.item(temp);

			HashMap<String, String> myNode = new HashMap<String, String>();

			NodeList subNodeList = nNode.getChildNodes();

			for (int i = 0; i < subNodeList.getLength(); i++) {
				Node currentNode = subNodeList.item(i);

				if (currentNode.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) currentNode;
					myNode.put(eElement.getNodeName(), eElement.getTextContent());
//					debug
//					System.out.println(eElement.getNodeName() + ": " + eElement.getTextContent());
				}

			}

			myElements.add(myNode);
		}

	}

}
