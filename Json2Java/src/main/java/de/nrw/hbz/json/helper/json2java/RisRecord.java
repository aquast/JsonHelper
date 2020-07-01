package de.nrw.hbz.json.helper.json2java;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Hashtable;

public class RisRecord extends ModsRecord  implements java.io.Serializable {
	
	public RisRecord(JsonLDMapper jMapper) {
		super(jMapper);
	}
	
	public String metadata() {
		return xml2any("ris");
	}
}
