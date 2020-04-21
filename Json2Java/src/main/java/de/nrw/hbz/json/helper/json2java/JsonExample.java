/**
 * 
 */
package de.nrw.hbz.json.helper.json2java;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;

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
		jsonExample1 = "{ \"vorname\" : \"Andres\", \"nachname\" : \"Quast\" , \"alter\" : 53, \"wohnorte\" : [\"Hannover\", \"Kassel\" ], "
				+ "\"anstellungen\" : [{\"ort\" : \"Köln\", \"arbeitgeber\" : \"hbz\", \"von\" : 2008 , \"bis\" : \"laufend\" }"
				+ ", {\"ort\" : \"Göttingen\", \"arbeitgeber\" : \"SUB\", \"von\" : 2005 , \"bis\" : 2008 }"
				+ ", {\"ort\" : \"Göttingen\", \"arbeitgeber\" : \"MiniCar\", \"von\" : 2003 , \"bis\" : 2005 } "
				+ "]"
				+ ", \"akademischeTitel\" : [ \"Dipl.-Geol.\", \"Dr.\"]}";
		ObjectMapper mapper = new ObjectMapper();
		try {
			node = mapper.readTree(jsonExample1);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		JsonNode fileNode = JsonFileReader.getJsonNodeFromFile("src/main/resources/frl:6402506.json2.json");
		//node = JsonFileReader.getJsonNodeFromFile("src/main/resources/frl-example.json2.json");
		node = JsonFileReader.getJsonNodeFromFile("src/main/resources/frl-example.json");
		//node = JsonFileReader.getJsonNodeFromFile("src/main/resources/video-example.json");
		//node = JsonFileReader.getJsonNodeFromFile("src/main/resources/oer-example.json");
		//node = JsonFileReader.getJsonNodeFromFile("src/main/resources/frl:6402506.json2.json");

		
		StructureFinder sf = new StructureFinder();
		//sf.mapStructure(node);
		//sf.mapStructure(fileNode);
		//JsonLDMapper jFileMapper = new JsonLDMapper(fileNode);
		//jFileMapper.printElements();

		JsonLDMapper jMapper = new JsonLDMapper(node);
		jMapper.printElements();
		
		System.out.println("\n#####  XML Ausgabe #####\n");

		OpenAireRecord oar = new OpenAireRecord(jMapper);

		JsonLDMapper jMapper = new JsonLDMapper(node);
		//OpenAireRecord oar = new OpenAireRecord(jMapper);
		ModsRecord mods = new ModsRecord(jMapper);

		System.out.println(mods.xml2any("bib"));
		//System.out.println(mods.toString());
		//System.out.println(jMapper.getElement("root.creator").get(0).get("prefLabel"));
		
		//System.out.println(jMapper.getElement("root.title").get(0).get("title"));
		
		//System.out.println(new MapperTest().getData(node));
		
		//sf.printElements(jMapper.getElementModel("root.creator"));
	}

}
