package de.nrw.hbz.json.helper.json2java;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Hashtable;

public class BibtexRecord extends ModsRecord  implements java.io.Serializable {
	
	public BibtexRecord(JsonLDMapper jMapper) {
		super(jMapper);
	}
	
	public String metadata() {
		return xml2any("bib");
	}
}
