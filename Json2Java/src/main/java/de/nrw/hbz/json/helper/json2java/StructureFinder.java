/**
 * 
 */
package de.nrw.hbz.json.helper.json2java;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map.Entry;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * @author aquast
 *
 */
public class StructureFinder {

	Hashtable<String,String> simpleElements = new Hashtable<>();
	// Hashtable<String, Hashtable<String,String>> complexElement = new Hashtable<>(); 
	ArrayList<Hashtable<String,String>> complexElement = new ArrayList<>();
	StringBuffer pathBuffer = new StringBuffer("");

	public void mapStructure(JsonNode node) {
		mapStructure(node, new StringBuffer("root"));
		printElements();
	}
	
	public void mapStructure(JsonNode node, StringBuffer pBuffer) {

		Iterator<String> it = node.fieldNames();
		while(it.hasNext()) {
			int l = pBuffer.length(); 
			
			String key = it.next();
			if (node.get(key).isValueNode()){
				Hashtable<String, String> iE = new Hashtable<>();
				iE.put(pBuffer + "." + key, node.get(key).asText());
				complexElement.add(iE);
			}
			
			if(node.get(key).isObject()){
				//System.out.println(key + " : " + node.get(key).size() );
				pBuffer.append("." + key);
				JsonNode complexNode = node.get(key);
				mapStructure(complexNode, pBuffer);
			}
			
			if(node.get(key).isArray()){
				pBuffer.append("." + key);
				JsonNode complexNode = node.get(key);
				Iterator<JsonNode> nIt = complexNode.elements();
				while(nIt.hasNext()) {
					JsonNode arrayNode = nIt.next();
					if(arrayNode.isObject()) {
						//System.out.println(arrayNode.toString());					
						mapStructure(arrayNode, pBuffer);
					}else {
						Hashtable<String, String> iE = new Hashtable<>();
						//System.out.println("nur Text " + arrayNode.toString());
						iE.put(pBuffer.toString(), arrayNode.asText());
						complexElement.add(iE);
					}
					
				}
			}
			pBuffer.setLength(l);

		}
	}
	
	public void printElements() {
		Iterator<Hashtable<String,String>> it = complexElement.iterator();
		while (it.hasNext()) {
			Hashtable<String,String> simpleLiterals = it.next();
			Enumeration<String> sEnum = simpleLiterals.keys();
			while (sEnum.hasMoreElements()){
				String key = sEnum.nextElement();
				System.out.println(key + " : " + simpleLiterals.get(key));
			}
		}
	}

}
