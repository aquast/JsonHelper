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
public class JsonLDMapper {

	private ArrayList<Hashtable<String, String>> complexElement =
			new ArrayList<>();
	private ArrayList<JsonElementModel> jemElement = new ArrayList<>();
	private JsonElementModel jEM = null;
	private Hashtable<String, ArrayList<Integer>> index = new Hashtable<>();

	StringBuffer pathBuffer = new StringBuffer("");

	/**
	 * Constructor that integrates the indexing
	 * 
	 * @param node
	 */
	public JsonLDMapper(JsonNode node) {
		jemElement = mapToJsonElementModel(node, new StringBuffer("root"));
		createIndex();
	}
	
	/**
	 * 
	 * @param node
	 * @param pBuffer
	 * @return
	 */
	public ArrayList<JsonElementModel> mapToJsonElementModel(JsonNode node,
			StringBuffer pBuffer) {

		Iterator<String> it = node.fieldNames();
		while (it.hasNext()) {
			int l = pBuffer.length();

			String key = it.next();
			if (node.get(key).isValueNode()) {

				Hashtable<String, String> iE = new Hashtable<>();
				complexElement.add(iE);

				if (jEM != null && !jEM.isEmpty()) {
					// if we have an jEM already, we will use the ElementList form this 
					Hashtable<String, String> ha = jEM.getComplexElementList();
					ha.put(key, node.get(key).asText());
					jEM.setComplexElement(ha);
				} else {
					// This block is used for the first method call only,
					// as there is no jEM to provide 
					iE.put(pBuffer + "." + key, node.get(key).asText());
					iE.put(key, node.get(key).asText());
					jEM = new JsonElementModel(pBuffer.toString());
					jEM.setComplexElement(iE);
					jemElement.add(jEM);
				}
			}

			// check if current field is of type array, if so, we have to make 
			// a recursive call to mapToJsonModel
			if (node.get(key).isObject()) {
				pBuffer.append("." + key);
				JsonNode complexNode = node.get(key);
				jEM = new JsonElementModel(pBuffer.toString());
				jEM.setComplexElement(new Hashtable<String, String>());
				mapToJsonElementModel(complexNode, pBuffer);
				jemElement.add(jEM);
			}

			// check if current field is of type array 
			if (node.get(key).isArray()) {
				pBuffer.append("." + key);

				JsonNode complexNode = node.get(key);
				jEM = new JsonElementModel(pBuffer.toString());

				Iterator<JsonNode> nIt = complexNode.elements();
				while (nIt.hasNext()) {
					JsonNode arrayNode = nIt.next();
					
					if (arrayNode.isObject()) {
						// If the field of array is an object, make recursive call to mapToJsonModel
						// to find all Fields with Literals 
						jEM = new JsonElementModel(pBuffer.toString());
						jEM.setComplexElement(new Hashtable<String, String>());
						mapToJsonElementModel(arrayNode, pBuffer);
						jemElement.add(jEM);

					} else {
						// We have a Literal as Field of Array, so put this into Hashtable 
						Hashtable<String, String> iE = new Hashtable<>();
						iE.put(pBuffer.toString(), arrayNode.asText());
						jEM.addArrayElement(arrayNode.asText());
					}
				}
				if (jEM.isArray()) {
					jemElement.add(jEM);
				}
			}
			pBuffer.setLength(l);
			if (pBuffer.toString().equals("root")){
				jEM = new JsonElementModel(pBuffer.toString());
			}


		}
		return jemElement;
	}

	/**
	 * method for examine the classes result
	 * 
	 */
	// TODO: move into separate testing Class
	public void printElements() {
		Iterator<Hashtable<String, String>> it = complexElement.iterator();
		for (int i = 0; i < jemElement.size(); i++) {
			if (jemElement.get(i).isArray()) {
				Iterator<String> jit = jemElement.get(i).getArrayList().iterator();
				while (jit.hasNext()) {
					System.out.print(jit.next().toString() + "\t");
				}
			} else if (!jemElement.get(i).isEmpty()) {
				System.out.println(jemElement.get(i).getPath());
				Enumeration<String> jEnum = jemElement.get(i).getComplexElementList().keys();
				while (jEnum.hasMoreElements()) {
					String key = jEnum.nextElement().toString();
					System.out.print("\t" + "\t" + key + "\t");
					System.out
							.print(jemElement.get(i).getComplexElementList().get(key) + "\n");
				}

			}
			System.out.println("" + jemElement.size());
		}
	}

	public void createIndex() {

		Iterator<JsonElementModel> jemIt = jemElement.iterator();
		ArrayList<Integer> position = new ArrayList<>();
		int i = 0;
		while (jemIt.hasNext()) {
			JsonElementModel jEM = jemIt.next();
			if (index.containsKey(jEM.getPath())) {
				position = index.get(jEM.getPath());
			} else {
				position = new ArrayList<>();
			}

			int pos = i;
			position.add(Integer.valueOf(pos));
			index.put(jEM.getPath(), position);
			i++;
		}
	}

	public ArrayList<Hashtable<String,String>> getElement(String path) {

		ArrayList<Hashtable<String,String>> result = new ArrayList<>();
		if(index.containsKey(path)) {
			ArrayList<Integer> fieldIndex = index.get(path);
			for (int i = 0; i < fieldIndex.size(); i++) {
				JsonElementModel sJem = jemElement.get(fieldIndex.get(i));
				if(!sJem.isEmpty()) {
					Hashtable<String,String> element = sJem.getComplexElementList();
					result.add(element);
				}else{
					for (int j = 0; j < sJem.getArrayList().size();j++) {
						Hashtable<String,String> element = new Hashtable<>();
						element.put(path, sJem.getArrayList().get(j));
						result.add(element);
					}
				}
			}
			return result;			
		}
		return new ArrayList<Hashtable<String,String>>(); // return empty hashtable to prevent caller from null
	}

	public ArrayList<JsonElementModel> getElementModel(String path) {

		ArrayList<JsonElementModel> result = new ArrayList<>();
		if(index.containsKey(path)) {
			ArrayList<Integer> fieldIndex = index.get(path);
			for (int i = 0; i < fieldIndex.size(); i++) {
				JsonElementModel sJem = jemElement.get(fieldIndex.get(i));
				result.add(sJem);
				}
			return result;	
		}
		return new ArrayList<JsonElementModel>();
	}

	public boolean elementExists(String element) {
		if (index.containsKey(element)){
			return true;
		}else if(!element.startsWith("root.")) {
			ArrayList<Hashtable<String, String>> jemList = getElement("root");
			for(int i = 0; i < jemList.size(); i++) {
				if(jemList.get(i).containsKey(element)) {
					return true;
				}
			}
		}
		return false;
	}
}
