/**
 * 
 */
package de.nrw.hbz.json.helper.json2java;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.TreeMap;

/**
 * @author aquast
 *
 */
public class FundingReference {

	// a representation for Elements with a cardinality of 0-1
	private TreeMap<String, String> simpleElement = new TreeMap<>();
	// a representation for Elements with a cardinality of 0-n
	private TreeMap<String, ArrayList<String>> arrayElement = new TreeMap<>();
	// a representation for repeatable complex elements
	private ArrayList<TreeMap> elementList = new ArrayList();

	private String awardNumber = null;
	private String awardURI = null;
	private String awardTitle = null;

	public void setElement(String key, String value) {
		if (!simpleElement.containsKey(key)) {
			simpleElement.put(key, value);
		}
	}

	public void setFunderName(String funderName) {
		if (!simpleElement.containsKey("funderName")) {
			simpleElement.put("funderName", funderName);
		}
	}

	public void setFunderIdentifier(String funderIdentifier) {
		if (!simpleElement.containsKey("funderIdentifier")) {
			simpleElement.put("funderIdentifier", funderIdentifier);
		}
	}

	public void setFundingIdemtifier(String fundingStream) {
		if (!simpleElement.containsKey("fundingStream")) {
			simpleElement.put("fundingStream", fundingStream);
		}
	}

	public TreeMap<String, String> getSimpleElements() {
		return this.simpleElement;
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