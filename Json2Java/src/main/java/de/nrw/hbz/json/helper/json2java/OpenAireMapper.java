/**
 * 
 */
package de.nrw.hbz.json.helper.json2java;

import java.util.ArrayList;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * @author aquast
 *
 */
public class OpenAireMapper {
	
	private JsonNode node = null;
	
	private ArrayList<String> creator = new ArrayList();
	
	public void setNode(JsonNode node) {
		this.node = node;
	}
	
	public void setCreator() {
		
	}
}
