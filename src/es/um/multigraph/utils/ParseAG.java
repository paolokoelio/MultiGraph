package es.um.multigraph.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ParseAG {

	private NodeList edges;
	private NodeList nodes;
	private List<HashMap<String, String>> myNodes;
	private List<HashMap<String, String>> myEdges;

	public ParseAG(NodeList nodes, NodeList edges) {
		this.edges = edges;
		this.nodes = nodes;

	}

	public void parseAG() {

		this.myNodes = new ArrayList<HashMap<String, String>>();
		parseElements(nodes, this.myNodes);
		this.myEdges = new ArrayList<HashMap<String, String>>();
		parseElements(edges, this.myEdges);
//		debug
//		System.out.println(this.myNodes);
//		System.out.println(this.myEdges);
	}

	public List<HashMap<String, String>> getMyNodes() {
		return myNodes;
	}

	public List<HashMap<String, String>> getMyEdges() {
		return myEdges;
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
//						debug
//						System.out.println(eElement.getNodeName() + ": " + eElement.getTextContent());
				}

			}

			myElements.add(myNode);
		}

	}

}
