/**
 * 
 */
package de.nrw.hbz.json.helper.json2java;

import java.util.ArrayList;
import java.util.Hashtable;

/**
 * @author aquast
 * A Class representing an json-DOM within different Types of ArrayList
 * Model aims to unify methods to access the json-DOM Elements
 * All object elements will be broken down into key:value pairs where key is 
 * the canonical Path of each object-element. literal elements will be treated similar
 * Both kind of key value pairs will be stored in an ArrayList of Hashtable<String, String>
 * 
 * In contrast Arrays will be stored in an ArrayList of Values 
 *  
 */
public class JsonElementModelRefactored {

	private String fieldName = null;
	private String path = null;
	private Hashtable<String, String> kvPair = new Hashtable<String, String>();
	private ArrayList<String> valueList = null;

	public JsonElementModelRefactored(String fieldName) {
		this.fieldName = fieldName;
	}
	
	public String getPath() {
		return this.path;
	}

	public void setPath(String path) {
		this.path = path;;
	}
	
	/**
	 * @param valueElements
	 */
	public void setValueElements(Hashtable<String, String> vElement) {
		this.kvPair = new Hashtable<>();
		this.kvPair = vElement;
	}

	/**
	 * @return
	 */
	public Hashtable<String, String> getValueElements() {
		return kvPair;
	}

	/**
	 * @param valueList
	 */
	public void setArrayElement(ArrayList<String> valueList) {
		this.valueList = new ArrayList<>();
		this.valueList = valueList;
	}

	/**
	 * @param valueList
	 */
	public void addArrayElement(String value) {
		if(this.valueList==null) {
			this.valueList = new ArrayList<>();
		}
		this.valueList.add(value);
	}

	/**
	 * @return
	 */
	public ArrayList<String> getArrayList() {
		return valueList;
	}

	/**
	 * @param jEM
	 * @return
	 */
	public boolean isArray() {
		if (this.valueList != null) {
			return true;
		}
		return false;
	}

	/**
	 * @param jEM
	 * @return
	 */
	public boolean isEmpty() {
		if (kvPair == null) {
			return true;
		}
		return false;
	}

}
