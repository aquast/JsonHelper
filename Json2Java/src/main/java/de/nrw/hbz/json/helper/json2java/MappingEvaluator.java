/**
 * 
 */
package de.nrw.hbz.json.helper.json2java;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * @author aquast
 *
 */
public class MappingEvaluator {

	private static FundingReference fRefer = new FundingReference(); 
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		
		// TODO Auto-generated method stub
		JsonNode node = JsonFileReader.getJsonNodeFromFile("src/main/resources/frl-example.json");
		System.out.println(node);
		List<JsonNode> fNode = node.findValues("transformer");
		System.out.println(fNode);
		
		GenericJsonReader gjr = new GenericJsonReader();
		
		gjr.listFields(node);
		
		
		/*
		for (int i=0; i < fNode.size(); i++) {
			Iterator<String> it = fNode.get(i).fieldNames();
			
			while (it.hasNext()) {
				String key = it.next();
				JsonNode nodePart = fNode.get(i).findPath(key);
				System.out.println(nodePart);
				if(nodePart.isValueNode()) {
					fRefer.setElement(key, nodePart.textValue());
					System.out.println(key + " : " + nodePart.textValue());
				}
				
			
			}
			
		}
		*/
		TreeMap<String, String> simpleElementList = fRefer.getSimpleElements();
		
		
		for (String value : simpleElementList.values()) {
			System.out.println(value);
		}
		
	}

}
