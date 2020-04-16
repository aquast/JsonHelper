/**
 * 
 */
package de.nrw.hbz.json.helper.json2java.model;

import java.util.Calendar;

/**
 * @author aquast
 *
 */
public class EmbargoModel {
	
	public Calendar setEmbargoDate(String value) {
		String[] valueSplit = value.split("-");
		
		Calendar embargoCal = Calendar.getInstance();
		embargoCal.clear();
		embargoCal.set(Calendar.YEAR, Integer.parseInt(valueSplit[0]));
		embargoCal.set(Calendar.MONTH, Integer.parseInt(valueSplit[1])-1);
		embargoCal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(valueSplit[2]));
		return embargoCal;
	}
	
	public boolean isActiveEmbargo(String embargoTime) {
		Calendar cal = setEmbargoDate(embargoTime);
		boolean test = cal.getTime().after(Calendar.getInstance().getTime());
		return test;
	}

	public String getAccessScheme(String embargoTime) {
		Calendar cal = setEmbargoDate(embargoTime);
		String schemeValue = "public";
		if(cal.getTime().after(Calendar.getInstance().getTime())) {
			schemeValue = "embargo";
		};
		
		return schemeValue;
	}

}
