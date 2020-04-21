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
		boolean testeMich = true;
		
	}
	
	public void mapStructure(JsonNode node, StringBuffer pBuffer) {

		Iterator<String> it = node.fieldNames();
		while(it.hasNext()) {
			int l = pBuffer.length(); 
			
			String key = it.next();
			if (node.get(key).isValueNode() || node.get(key).isNumber()){
				Hashtable<String, String> iE = new Hashtable<>();
				//System.out.println("HIER 1, ValueNode: " + key);

				
				if(jEM != null && !jEM.isEmpty()) {
					Hashtable<String,String> ha = jEM.getComplexElementList();
					ha.put(key, node.get(key).asText());
					jEM.setComplexElement(ha);
					//System.out.println("HIER 2, Zufügen von Inhalt zu bestehender Elementliste: " + key);
					//System.out.println("HIER 2, pBuffer: " + pBuffer.toString());

				} else {
					iE.put(key, node.get(key).asText());
					jEM = new JsonElementModel(pBuffer.toString());
					jEM.setComplexElement(iE);
					jemElement.add(jEM);
					//System.out.println("HIER 3, Erstellen von neuer Elementliste und Zufügen von Inhalt : " + key);
					//System.out.println("HIER 3, pBuffer: " + pBuffer.toString());
				}
			}
			
			if(node.get(key).isObject()){
				pBuffer.append("." + key);
				//System.out.println("HIER 4, Inhalt ist Objekt, Methode wird rekursiv aufgerufen: " + key);
				//System.out.println("HIER 4, pBuffer: " + pBuffer.toString());
				/*
				pBuffer.append("." + key);
				JsonNode complexNode = node.get(key);
				jEM = new JsonElementModel(pBuffer.toString());
				jEM.setComplexElement(new Hashtable<String,String>());
				Hashtable<String,String> ha = jEM.getComplexElementList();
				mapStructure(complexNode, pBuffer);
				jemElement.add(jEM);
				*/
				JsonNode complexNode = node.get(key);
				Iterator<String> nIt = complexNode.fieldNames();
				ArrayList<String> objKeyList = new ArrayList<>();
				while (nIt.hasNext()) {
					String objKey = nIt.next();
					
					if(complexNode.get(objKey).isValueNode() || complexNode.get(objKey).isNumber()) {

						Hashtable<String, String> iE = new Hashtable<>();
						if (jEM != null && !jEM.isEmpty()) {
							Hashtable<String, String> ha = jEM.getComplexElementList();
							ha.put(objKey, complexNode.get(objKey).asText());
							jEM.setComplexElement(ha);
						} else {
							iE.put(objKey, complexNode.get(objKey).asText());
							jEM = new JsonElementModel(pBuffer.toString());
							jEM.setComplexElement(iE);
							jemElement.add(jEM);
						}
						
					} else {
						objKeyList.add(objKey);
					}
				}

				Iterator<String> objKeyIt = objKeyList.iterator();
				while (objKeyIt.hasNext()) {
				    String objectItemKey = objKeyIt.next();
					JsonNode realObjNode = complexNode.get(objectItemKey);
				    pBuffer.append("." + objectItemKey);
				    
				    jEM = new JsonElementModel(pBuffer.toString());
					jEM.setComplexElement(new Hashtable<String,String>());
					mapStructure(realObjNode, pBuffer);
					jemElement.add(jEM);			
				}

			}
			
			if(node.get(key).isArray()){
				//System.out.println("HIER 5, Inhalt ist Array, Array-Verarbeitung: " + key);
				pBuffer.append("." + key);
				//System.out.println("HIER 5, pBuffer: " + pBuffer.toString());
				JsonNode complexNode = node.get(key);
				Iterator<JsonNode> nIt = complexNode.elements();
				jEM = new JsonElementModel(pBuffer.toString());
				
				while(nIt.hasNext()) {
					JsonNode arrayNode = nIt.next();
					//System.out.println("HIER 6, pBuffer: " + pBuffer.toString());
					if(arrayNode.isObject()) {
						//System.out.println("HIER 6, Array-Feld enthält Objekt, rekursives Aufrufen der Methode: " + key);
						jEM = new JsonElementModel(pBuffer.toString());
						jEM.setComplexElement(new Hashtable<String,String>());
						mapStructure(arrayNode, pBuffer);
						jemElement.add(jEM);

					}else {
						//System.out.println("HIER 7, Array-Feld enthält Literal, Schreiben des Literals in Elementliste: " + key);
						Hashtable<String, String> iE = new Hashtable<>();
						iE.put(pBuffer.toString(), arrayNode.asText());
						//complexElement.add(iE);
						jEM.addArrayElement(arrayNode.asText());
					}
				}
				if(jEM.isArray()){
					//System.out.println("HIER 8");
					jemElement.add(jEM);
				}
			} 
			pBuffer.setLength(l);
			if (pBuffer.toString().equals("root")){
				jEM = new JsonElementModel(pBuffer.toString());
			}
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
				}else if (!jemElement.get(i).isEmpty()) {
					System.out.println(jemElement.get(i).getPath());
					Enumeration jEnum = jemElement.get(i).getComplexElementList().keys();
					while (jEnum.hasMoreElements()) {
						String key = jEnum.nextElement().toString();
						System.out.print( "\t" + "\t" + key + "\t");
						//System.out.print(jemElement.get(i).getPath() + "." + key + ": " + jemElement.get(i).getComplexElementList().get(key) + "\n");
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
