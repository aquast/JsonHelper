/**
 * 
 */
package de.nrw.hbz.json.helper.json2java;

import java.util.Hashtable;
import java.util.Iterator;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * @author aquast
 *
 */
public class StructureFinder {

	Hashtable<String,String> simpleElements = new Hashtable<>();

	public void mapStructure(JsonNode node) {
		StringBuffer sb = new StringBuffer("");
		Iterator<String> it = node.fieldNames();
		while(it.hasNext()) {
			String key = it.next();
			if (node.get(key).isValueNode()){
				System.out.println(key + " : " + node.get(key).asText() );
				simpleElements.put(key, node.get(key).asText());
			}else if(node.get(key).isArray()){
				System.out.println(key + " : " + node.get(key).asText() );
				//node.get(key).isArray();
				sb.append(key + ".");
				JsonNode complexNode = node.findPath(key);
				mapStructure(complexNode);
			}
		}
	};
}
