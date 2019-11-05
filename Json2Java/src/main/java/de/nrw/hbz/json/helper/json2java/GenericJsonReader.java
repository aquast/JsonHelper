/**
 * 
 */
package de.nrw.hbz.json.helper.json2java;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * @author aquast
 *
 */
public class GenericJsonReader {

	// a representation for Elements with a cardinality of 0-1
	private TreeMap<String, String> simpleElement = new TreeMap<>();
	
	// a representation for Elements with a cardinality of 0-n
	private TreeMap<String, List<String>> arrayElement = new TreeMap<>();
	
	// a representation for repeatable complex elements
	private ArrayList<TreeMap> elementList = new ArrayList();

	public void setElement(String key, String value) {
		if (!simpleElement.containsKey(key)) {
			simpleElement.put(key, value);
		}
	}


	public void setArrayElement(String key, List<String> valueList) {
		if (!arrayElement.containsKey(key)) {
			arrayElement.put(key, valueList);
		}
	}
	
	
	public TreeMap<String, String> getSimpleElements() {
		return this.simpleElement;
	}
	
	public ArrayList<String> listFields(JsonNode node) {
		ArrayList<String> fieldList = new ArrayList();

		Iterator<String> it = node.fieldNames();
			
			while (it.hasNext()) {
				String key = it.next();
				JsonNode nodePart = node.findPath(key);
				//System.out.println(nodePart.);
				if(nodePart.isValueNode()) {
					System.out.println("Elementkey: " + key);
				}			
			}

		return fieldList;
	}
	

}
/**
 * 
 * <oaire:fundingReferences> <oaire:fundingReference> <oaire:funderName>European
 * Commission</datacite:funderName>
 * <oaire:funderIdentifier funderIdentifierType="Crossref Funder ID">
 * http://doi.org/10.13039/100010661 </oaire:funderIdentifier>
 * <oaire:fundingStream>Horizon 2020 Framework Programme</oaire:fundingStream>
 * <oaire:awardNumber awardURI=
 * "http://cordis.europa.eu/project/rcn/194062_en.html">643410</oaire:awardNumber>
 * <oaire:awardTitle>Open Access Infrastructure for Research in Europe
 * 2020</oaire:awardTitle> </oaire:fundingReference> </oaire:fundingReferences>
 * 
 **/