package de.uni.due.paluno.se.edt;

import java.util.HashMap;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;

import weka.core.Attribute;
import weka.core.Instance;

public class Utils {

	
	public static Graph graph(Instance instance){
		
		Graph graph = new SingleGraph("tutorial 1");
		
		HashMap<String, Node> nodes = new HashMap<String, Node>();
		
		HashMap<String, Node> referencableNodes = new HashMap<String, Node>();
		
		HashMap<String, Boolean> edges = new HashMap<String, Boolean>();
		
		Node rootNode = graph.addNode("Root");
		rootNode.addAttribute("ui.label", "Root");
		
		for(int i=0;i<instance.numAttributes();i++){
			Attribute attribute = instance.attribute(i);
			String stringValue = getAttributeValue(instance, attribute);
			String attrNameCompleteKey = attribute.name();
			String[] split = attrNameCompleteKey.split(Constants.keySeparator);
			if(split.length>1){
				
				String currentAttrName = concat(split,split.length-2);
				Node node = nodes.get(currentAttrName);

				String nextAttributeName = concat(split,split.length-1);
				Node node2;
				if(nodes.containsKey(nextAttributeName)){
					node2 = nodes.get(nextAttributeName);
				}
				else if(referencableNodes.containsKey(stringValue)){
					node2 = referencableNodes.get(stringValue);
				}
				else{
					node2 = graph.addNode(nextAttributeName);
					if(stringValue.startsWith("http")){
						referencableNodes.put(stringValue, node2);
					}
					node2.addAttribute("ui.label", stringValue);
					nodes.put(nextAttributeName, node2);
				}

				String relID = node.getId()+"-->"+node2.getId();

				System.out.println(relID);
				
				if(!edges.containsKey(relID)){
					edges.put(relID, true);
					Edge addEdge = graph.addEdge("Edge"+attrNameCompleteKey, node, node2,true);
					addEdge.addAttribute("ui.label", split[split.length-1]);
				}

			}
			else{
				Node node = graph.addNode(attrNameCompleteKey);
				if(stringValue.startsWith("http")){
					referencableNodes.put(stringValue, node);
				}
				node.addAttribute("ui.label", stringValue);
				nodes.put(attrNameCompleteKey,node);
				
				Edge edge = graph.addEdge("Edge"+attrNameCompleteKey, rootNode, node,true);
				edge.addAttribute("ui.label", "E:"+attrNameCompleteKey);
			}
		}
		
		return graph;
	}
	
	/**
	 * Concats string parts up to index i
	 * 
	 * @param string
	 * @param i
	 * @return
	 */
	public static String concat(String [] string, int i){
	
		
		String result = new String();
		for (int j = 0; j <= i; j++) {
			if(i==j){
				result = result.concat(string[j]);
			}
			else{
				result = result.concat(string[j]+Constants.keySeparator);
			}
		}
		return result;
	}
	
	public static String getAttributeValue(Instance instance, Attribute attr){
		String attributeValue = null;
		try{				
			attributeValue = instance.stringValue(attr);
			return attributeValue;
		}
		catch(Throwable t){
			//TODO fix that for other types;
			return "NaN";
		}
	}
	
}
