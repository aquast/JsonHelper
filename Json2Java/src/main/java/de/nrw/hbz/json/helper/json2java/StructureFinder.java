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
	ArrayList<Hashtable<String,String>> simpleElement = new ArrayList<>();
	ArrayList<Hashtable<String,ArrayList<String>>> arrayElement = new ArrayList<>();
	ArrayList<JsonElementModel> jemElement = new ArrayList<>();
	JsonElementModel jEM = null;
	Hashtable<String, ArrayList<Integer>> index = new Hashtable<>();
	
	StringBuffer pathBuffer = new StringBuffer("");

	public void mapStructure(JsonNode node) {
		mapStructure(node, new StringBuffer("root"));
		createIndex();
		printElements(jemElement);
		printElements(getElement("root"));
	}
	
	public void mapStructure(JsonNode node, StringBuffer pBuffer) {

		Iterator<String> it = node.fieldNames();
		while(it.hasNext()) {
			int l = pBuffer.length(); 
			
			String key = it.next();
			if (node.get(key).isValueNode()){
				Hashtable<String, String> iE = new Hashtable<>();
				//iE.put(pBuffer + "." + key, node.get(key).asText());
				//iE.put(key, node.get(key).asText());
				//complexElement.add(iE);
				
				if(jEM != null && jEM.isObject()) {
					Hashtable<String,String> ha = jEM.getComplexElementList();
					ha.put(key, node.get(key).asText());
					jEM.setComplexElement(ha);
				} else {
					iE.put(key, node.get(key).asText());
					jEM = new JsonElementModel(pBuffer.toString());
					jEM.setComplexElement(iE);
					jemElement.add(jEM);
				}
			}
			
			if(node.get(key).isObject()){
				//System.out.println(key + " : " + node.get(key).size() );
				pBuffer.append("." + key);
				JsonNode complexNode = node.get(key);
				jEM = new JsonElementModel(pBuffer.toString());
				jEM.setComplexElement(new Hashtable<String,String>());
				Hashtable<String,String> ha = jEM.getComplexElementList();
				mapStructure(complexNode, pBuffer);
				jemElement.add(jEM);
			}
			
			if(node.get(key).isArray()){
				pBuffer.append("." + key);
				JsonNode complexNode = node.get(key);
				Iterator<JsonNode> nIt = complexNode.elements();
				jEM = new JsonElementModel(pBuffer.toString());
				while(nIt.hasNext()) {
					JsonNode arrayNode = nIt.next();
					if(arrayNode.isObject()) {
						jEM = new JsonElementModel(pBuffer.toString());
						jEM.setComplexElement(new Hashtable<String,String>());
						//System.out.println(arrayNode.toString());					
						mapStructure(arrayNode, pBuffer);
						jemElement.add(jEM);

					}else {
						Hashtable<String, String> iE = new Hashtable<>();
						//System.out.println("nur Text " + arrayNode.toString());
						iE.put(pBuffer.toString(), arrayNode.asText());
						complexElement.add(iE);
						jEM.addArrayElement(arrayNode.asText());
					}
				}
				if(jEM.isArray()){
					jemElement.add(jEM);				
				}
			}
			pBuffer.setLength(l);

		}
	}
	
	public void printElements(ArrayList<JsonElementModel> jemElement) {

		if(jemElement != null && !jemElement.isEmpty()) {
			for (int i=0; i<jemElement.size(); i++) {
				if(jemElement.get(i).isArray()) {
					System.out.print(jemElement.get(i).getPath() + ":\t");
					Iterator jit = jemElement.get(i).getArrayList().iterator();
					while (jit.hasNext()) {
						System.out.print(jit.next().toString() + "\t");
					}
					System.out.println("\n");
				}else if (jemElement.get(i).isObject()) {
					System.out.println(jemElement.get(i).getPath());
					Enumeration jEnum = jemElement.get(i).getComplexElementList().keys();
					while (jEnum.hasMoreElements()) {
						String key = jEnum.nextElement().toString();
						System.out.print( "\t" + "\t" + key + "\t");
						System.out.print(jemElement.get(i).getComplexElementList().get(key) + "\n");
					}
					
				}  
				//System.out.println("" + jemElement.size());
			} 
			
		}
	}
	

	public void createIndex() {

		Iterator<JsonElementModel> jemIt = jemElement.iterator();
		ArrayList<Integer> position = new ArrayList<>();
		int i=0;
		while (jemIt.hasNext()) {
			JsonElementModel jEM = jemIt.next();
			//System.out.println(jEM.getPath());
			if (index.containsKey(jEM.getPath())) {
				position = index.get(jEM.getPath());
			}else {
				position = new ArrayList<>();
			}
			
			int pos = i;
			position.add(Integer.valueOf(pos));
			index.put(jEM.getPath(), position);
			i++;
		}
	}
	
	public ArrayList<JsonElementModel> getElement(String path) {

		ArrayList<JsonElementModel> result = new ArrayList<JsonElementModel>();
		if(index.containsKey(path)) {
			ArrayList<Integer> fieldIndex = index.get(path);
			//System.out.println(fieldIndex.size());
			for (int i = 0; i < fieldIndex.size(); i++) {
				JsonElementModel sJem = jemElement.get(fieldIndex.get(i));
				result.add(sJem);
			}
			return result;			
		}
		return null;
	}

}
