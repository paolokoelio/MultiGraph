/**
 * First method from 
 */
package agsim.model;

import org.w3c.dom.*;

import agsim.utils.FileUtils;

import javax.xml.parsers.*;

//	package packageName;
//	import ClassNameToImport; 
//	accessSpecifier class ClassName {
//	  accessSpecifier dataType variableName [= initialValue];
//	  accessSpecifier ClassName([argumentList]) {
//	    constructorStatement(s)
//	  }
//	  accessSpecifier returnType methodName ([argumentList]) {
//	    methodStatement(s)
//	  }
//	  // This is a comment
//	  /* This is a comment too */
//	  /* This is a
//	     multiline
//	     comment */
//	}
// @author Pavlo

public class ParserAG {

	private DocumentBuilder builder;
	private FileUtils file;
	private NodeList aList;
	private NodeList nList;

	public ParserAG() throws ParserConfigurationException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		this.builder = factory.newDocumentBuilder();

	}

	public void parseAG() {

		System.out.println("Loaded " + this.file.getFile() + "\n");

		try {
			Document doc = this.builder.parse(this.file.getFile());
			doc.getDocumentElement().normalize();
			System.out.println("Root element: " + doc.getDocumentElement().getNodeName());

			this.parseEdges(doc);
			this.parseNodes(doc);
//			printArcs(doc);
//			printVertices(doc);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public NodeList getEdges() {
		return aList;
	}

	public NodeList getNodes() {
		return nList;
	}

	public void setFile(FileUtils file) {
		this.file = file;
	}

	/**
	 * 
	 * 
	 * @param doc
	 * @return NodeList node of all nodes
	 */
	public NodeList parseNodes(Document doc) {
		
		this.nList = doc.getElementsByTagName("vertex");
		return this.nList;
	}

	/**
	 * 
	 * 
	 * @param doc
	 * @return NodeList node of all edges
	 */
	public NodeList parseEdges(Document doc) {
		
		this.aList = doc.getElementsByTagName("arc");
		return this.aList;
	}

	/**
	 * Prints the edges of the AG
	 * 
	 * @param doc
	 * @param i
	 */
	public void printArcs(Document doc) {

		NodeList nList = doc.getElementsByTagName("arc");
		System.out.println("----------------------------");
		for (int temp = 0; temp < nList.getLength(); temp++) {
			Node nNode = nList.item(temp);
			System.out.println("\nCurrent Element :" + nNode.getNodeName());

			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				Element eElement = (Element) nNode;
//	              System.out.println("Arc attribute : " 
//	                 + eElement.getAttribute("id"));
				System.out.println("Source : " + eElement.getElementsByTagName("src").item(0).getTextContent());
				System.out.println("Dest : " + eElement.getElementsByTagName("dst").item(0).getTextContent());
			}
		}

	}

	/**
	 * Prints the vertices of the AG
	 * 
	 * @param doc
	 * @param i
	 */
	public void printVertices(Document doc) {

		NodeList nList = doc.getElementsByTagName("vertex");
		System.out.println("----------------------------");

		for (int temp = 0; temp < nList.getLength(); temp++) {
			Node nNode = nList.item(temp);
			System.out.println("\nCurrent Element: " + nNode.getNodeName());

			NodeList nodeList = nNode.getChildNodes();
			for (int i = 0; i < nodeList.getLength(); i++) {
				Node currentNode = nodeList.item(i);
				if (currentNode.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) currentNode;
					System.out.println(eElement.getNodeName() + ": " + eElement.getTextContent());
				}
			}

		}

	}

}
