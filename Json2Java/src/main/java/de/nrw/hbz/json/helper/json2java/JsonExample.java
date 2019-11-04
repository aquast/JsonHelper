/**
 * 
 */
package de.nrw.hbz.json.helper.json2java;

import java.io.IOException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author aquast
 *
 */
public class JsonExample {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		JsonNode node = null; 
		String jsonExample1 = new String();
		jsonExample1 = "{\"vorname\" : \"Andres\", \"nachname\" : \"Quast\" , \"alter\" : 53, \"wohnorte\" : [\"Köln\", \"Bochum\", \"Göttingen\" ] }";
		ObjectMapper mapper = new ObjectMapper();
		try {
			node = mapper.readTree(jsonExample1);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		StructureFinder sf = new StructureFinder();
		sf.mapStructure(node);
	}

}
