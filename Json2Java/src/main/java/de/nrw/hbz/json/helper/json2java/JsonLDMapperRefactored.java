/**
 * 
 */
package de.nrw.hbz.json.helper.json2java;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * Maps a JsonNode into the regal-api JsonElementModel. 
 * Concept of JsonElementModel is to flatten any complex Json 
 * Representation into a List of key-value pairs. Keys within the 
 * ElementLists of the JsonElementModel are representing object 
 * structures as point separated paths. 
 * @author aquast
 * 
 */
public class JsonLDMapperRefactored {

	JsonNode node = null;
	String pathBase = "json";
	ArrayList<JsonElementModelRefactored> jNodeList = new ArrayList<>();

	public JsonLDMapperRefactored(JsonNode node) {
		this.node = node;
	}
	
	public ArrayList<JsonElementModelRefactored> mapToElementModel() {
		ArrayList<JsonElementModelRefactored> jEL = new ArrayList<>();
		JsonElementModelRefactored baseJEM = new JsonElementModelRefactored(pathBase);
		baseJEM.setPath(pathBase);
		jEL.add(baseJEM);
		jEL = mapToElementModel(node , jEL.get(jEL.size()-1)); 
		return jEL;
	}
	
	
	public ArrayList<JsonElementModelRefactored> mapToElementModel(JsonNode node, JsonElementModelRefactored jEM) {

		// Fetch all ArrayNodes with JsonNode-Iterator because they have no fieldNames
		/*
		Iterator<JsonNode> JnIt = node.iterator();
		while(JnIt.hasNext()) {
			JsonNode jNode = JnIt.next();
			if(jNode.isArray()) {
				JsonElementModelRefactored jEM = new JsonElementModelRefactored(elementPath);
				jEM.setPath(elementPath);
				System.out.println("ArrayNode: " + jEM.getPath());
				Iterator<String> it = jNode.fieldNames();
				while(it.hasNext()) {
					System.out.println(it.next());
				}
			}
		
		} */
		
		// fetch all Nodes. We need to know the fieldNames in order to create pathes as node identifiers    
		Iterator<String> it = node.fieldNames();
		while (it.hasNext()) {
			String fieldName = it.next();
			JsonNode jNode= node.get(fieldName);
			String path = jEM.getPath();
			jEM.setPath(path + "." + fieldName);
			
			if (jNode.isArray()) {
				System.out.println(jEM.getPath() + " = ArrayNode");
				jEM.setPath(path);
			}
			if (jNode.isObject() && !jNode.isTextual() && !jNode.isNumber()) {
				System.out.println(jEM.getPath() + " = ObjectNode");
				mapToElementModel(jNode, jEM);
				jEM.setPath(path);
			}
			if(jNode.isValueNode() || jNode.isNumber()) {
				Hashtable<String, String> kvPair = jEM.getValueElements();
				kvPair.put(jEM.getPath(), jNode.asText());
				jEM.setValueElements(kvPair);
				System.out.println(jEM.getPath() + ": " + jNode.asText());
				jEM.setPath(path);
			}
			
			
			
		}
	return jNodeList;
	}
}
