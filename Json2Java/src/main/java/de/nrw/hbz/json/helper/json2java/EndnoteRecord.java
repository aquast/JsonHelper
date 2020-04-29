package de.nrw.hbz.json.helper.json2java;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Hashtable;

public class EndnoteRecord extends ModsRecord  implements java.io.Serializable {
	
	public EndnoteRecord(JsonLDMapper jMapper) {
		super(jMapper);
	}
	
	public String metadata() {
		return xml2any("end");
	}
}
