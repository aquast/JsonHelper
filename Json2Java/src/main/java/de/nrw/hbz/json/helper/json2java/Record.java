package de.nrw.hbz.json.helper.json2java;

public abstract class Record {

	public abstract String getRecordClassName();
	
	
	public String getMyClassName() {
		return Record.class.getCanonicalName();
	};
}
