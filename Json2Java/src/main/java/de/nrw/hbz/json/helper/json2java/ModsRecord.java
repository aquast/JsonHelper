/*
 * Copyright 2019 hbz NRW (http://www.hbz-nrw.de/)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package de.nrw.hbz.json.helper.json2java;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import de.nrw.hbz.json.helper.json2java.model.CoarModel;
import de.nrw.hbz.json.helper.json2java.model.EmbargoModel;

/**
 * @author reimer@hbz-nrw.de
 *
 */
public class ModsRecord extends Record implements java.io.Serializable {

	Document doc = null;

	
	public ModsRecord(JsonLDMapper jMapper) {
		createRecord(jMapper);
	}
	
	/**
	 * remove \r\n (return, newline), probably originating from copy&paste
	 * @param value to be scrubbed
	 *
	 */

	public static String scrub(String value) {
		return value.replaceAll("\r\n", " ");
	}

	public void createRecord(JsonLDMapper jMapper) {
	
		DocumentBuilderFactory.newInstance();
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);
		factory.isValidating();
		DocumentBuilder docBuilder = null;
		try {
			docBuilder = factory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		doc = docBuilder.newDocument();

		Element modsCollection = doc.createElement("modsCollection");
		modsCollection.setAttribute("xmlns:xlink","http://www.w3.org/1999/xlink");
		modsCollection.setAttribute("xmlns:xsi","http://www.w3.org/2001/XMLSchema-instance");
		modsCollection.setAttribute("xmlns", "http://www.loc.gov/mods/v3");
		modsCollection.setAttribute("xsi:schemaLocation", "http://www.loc.gov/mods/v3 http://www.loc.gov/standards/mods/v3/mods-3-3.xsd");
	
		Element mods = doc.createElement("mods");
		mods.setAttribute("version", "3.3");
		modsCollection.appendChild(mods);
		
		Hashtable<String, String> jsonElementList = new Hashtable<>();

		
		// generate titleInfo
		ArrayList<Hashtable<String, String>> jemList = jMapper.getElement("root.title");
		for (int i=0; i < jemList.size(); i++) {
			Element titleInfo = doc.createElement("titleInfo");
			Element title = doc.createElement("title");
			title.appendChild(doc.createTextNode(scrub(jemList.get(i).get("root.title"))));
			titleInfo.appendChild(title);
			if(i>0) {
				titleInfo.setAttribute("type", "alternative");
			}
			mods.appendChild(titleInfo);
		}
		
		// generate name
		jemList = jMapper.getElement("root.creator");
		for (int i=0; i < jemList.size(); i++) {
			Element name = doc.createElement("name");
			name.setAttribute("type", "personal");
			Element namePart = doc.createElement("namePart");
			namePart.appendChild(doc.createTextNode(jemList.get(i).get("prefLabel")));
			name.appendChild(namePart);
			mods.appendChild(name);
		}
		
		// generate abstract
		jemList = jMapper.getElement("root.abstractText");
		for (int i=0; i < jemList.size(); i++) {
			Element abstracttext = doc.createElement("abstract"); // 'abstract' seems to be a reserved word in java
			abstracttext.appendChild(doc.createTextNode(scrub(jemList.get(i).get("root.abstractText"))));
			mods.appendChild(abstracttext);
		}
		// generate typeOfResource
		Element typeOfResource = doc.createElement("typeOfResource"); 
		typeOfResource.appendChild(doc.createTextNode("text"));
		mods.appendChild(typeOfResource);
		
		// generate genre
		// TODO: lookup of proper frl-to-mods mapping
		Element genre = doc.createElement("genre"); 
		//genre.appendChild(doc.createTextNode("academic journal"));
		//genre.appendChild(doc.createTextNode("conference publication"));
		genre.appendChild(doc.createTextNode("book"));
		mods.appendChild(genre);

		
		// generate language
		jemList = jMapper.getElement("root.language");
		for (int i = 0; i < jemList.size(); i++) {
			Element language = doc.createElement("language");
			language.appendChild(doc.createTextNode(jemList.get(i).get("@id").substring(38)));
			mods.appendChild(language);
		}
		
		// generate subjects
		// TODO: use numerical value instead of label
		jemList = jMapper.getElement("root.ddc");
		for (int i=0; i < jemList.size(); i++) {
			Element classification = doc.createElement("classification");
			classification.appendChild(doc.createTextNode(jemList.get(i).get("prefLabel")));
			classification.setAttribute("authority", "ddc");
			mods.appendChild(classification);
		}
		
		jemList = jMapper.getElement("root.subject");
		Element subject = doc.createElement("subject");
		for (int i=0; i < jemList.size(); i++) {
			Element topic = doc.createElement("topic");
			topic.appendChild(doc.createTextNode(jemList.get(i).get("prefLabel")));
			subject.appendChild(topic);
		}
		mods.appendChild(subject);

		// generate identifier
		jemList = jMapper.getElement("root");
		for (int i = 0; i < jemList.size(); i++) {
			if (jemList.get(i).containsKey("@id")) {
				Element purl = doc.createElement("identifier");
				purl.appendChild(doc.createTextNode("https://frl.publisso.de/" + jemList.get(i).get("@id")));
				purl.setAttribute("type", "purl");	
				mods.appendChild(purl);
				// citekey
				//Element identifier_citekey = doc.createElement("identifier");
				//identifier_citekey.appendChild(doc.createTextNode(jemList.get(i).get("@id")));
				//identifier_citekey.setAttribute("type", "citekey");	
				//mods.appendChild(identifier_citekey);
				mods.setAttribute("ID", jemList.get(i).get("@id"));
			}
			if (jemList.get(i).containsKey("urn")) {
				Element urn = doc.createElement("identifier");
				urn.appendChild(doc.createTextNode(jemList.get(i).get("urn")));
				urn.setAttribute("type", "urn");	
				mods.appendChild(urn);
			}
		}
		// generate DOI
		jemList = jMapper.getElement("root.bibo:doi");
		for (int i=0; i < jemList.size(); i++) {
			Element doi = doc.createElement("identifier");
			doi.appendChild(doc.createTextNode(jemList.get(i).get("root.bibo:doi")));
			doi.setAttribute("type", "doi");
			mods.appendChild(doi);
		}
					
		
		doc.appendChild(modsCollection);

		System.out.println(XmlUtils.docToString(doc));

	}	
	
	@Override
	public String toString() {
		
		
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		try {
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			//StreamResult result = new StreamResult(new File("C:\\file.xml"));

			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
			// Output to console for testing
			StreamResult result = new StreamResult(System.out);
			transformer.transform(source, result);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		return "Test beendet";

	}

	@Override
	public String getRecordClassName() {
		// TODO Auto-generated method stub
		return ModsRecord.class.getCanonicalName();
	}
}
