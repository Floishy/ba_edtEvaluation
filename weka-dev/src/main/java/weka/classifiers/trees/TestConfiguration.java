package weka.classifiers.trees;

import java.io.Serializable;

public class TestConfiguration implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4725693478617090226L;

	private String linkedData;
	
	private String ontModel;
	
	private String ontologyNS;
	
	private String csvSource;
	
	private int classIndex;
	
//	private int maxHops;
//	
//	
//	public int getMaxHops() {
//		return maxHops;
//	}
//	
//	public void setMaxHops(int maxHops) {
//		this.maxHops = maxHops;
//	}
	
	public String getLinkedData() {
		return linkedData;
	}
	
	public String getOntModel() {
		return ontModel;
	}
	
	public String getOntologyNS() {
		return ontologyNS;
	}
	
	public void setLinkedData(String linkedData) {
		this.linkedData = linkedData;
	}
	
	public void setOntModel(String ontModel) {
		this.ontModel = ontModel;
	}
	
	public void setOntologyNS(String ontologyNS) {
		this.ontologyNS = ontologyNS;
	}
	
	public String getCsvSource() {
		return csvSource;
	}
	
	public void setCsvSource(String csvSource) {
		this.csvSource = csvSource;
	}
	
	public int getClassIndex() {
		return classIndex;
	}
	
	public void setClassIndex(int classIndex) {
		this.classIndex = classIndex;
	}
	
}
