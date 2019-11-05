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
		mapStructure(node, new StringBuffer("."));
		printElements();
	}
	
	public void mapStructure(JsonNode node, StringBuffer pBuffer) {

		Iterator<String> it = node.fieldNames();
		while(it.hasNext()) {
			
			String key = it.next();
			if (node.get(key).isValueNode()){
				Hashtable<String, String> iE = new Hashtable<>();
				iE.put(pBuffer + key, node.get(key).asText());
				complexElement.add(iE);
			}
			
			if(node.get(key).isObject()){
				System.out.println(key + " : " + node.get(key).size() );
				Iterator<Entry<String, JsonNode>> nIt = node.get(key).fields();
				//while(nIt.hasNext()) {
				//	System.out.println("key : " + nIt.next().getKey());
				//}
				//if (subNode.size() > 1) {
					//for (int i=0; i < subNode.size(); i++) {
						//pBuffer.append(key + ".");
					//	System.out.println(subNode.toString());
						// JsonNode complexNode = subNode.get(i);
						//mapStructure(subNode.get(i), pBuffer);
					//}
				//}
				pBuffer.append(key + ".");
				JsonNode complexNode = node.get(key);
				mapStructure(complexNode, pBuffer);
			}
			if(node.get(key).isArray()){
				System.out.println(key + " : " + node.get(key).size() );
			}
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
