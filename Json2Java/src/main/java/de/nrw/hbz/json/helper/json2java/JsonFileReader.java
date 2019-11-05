/**
 * 
 */
package de.nrw.hbz.json.helper.json2java;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;


/**
 * @author aquast
 *
 */
public class JsonFileReader {

	private static JsonNode node = null;
	
	public static void readJsonNode(String fileName) {
		ObjectMapper om = new ObjectMapper();
		try {
			node = om.readTree(new File(fileName));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static JsonNode getJsonNodeFromFile(String fileName) {
		readJsonNode(fileName);
		return node;
	}

}
