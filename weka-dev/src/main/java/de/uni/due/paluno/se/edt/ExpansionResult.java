package de.uni.due.paluno.se.edt;

import java.util.HashMap;
import java.util.List;

import weka.core.Instances;

public class ExpansionResult {

	Instances expandedInstances;
	
	HashMap<String,List<String>> expansionSchema;
	
	
	public ExpansionResult() {
		
	}
	
	public ExpansionResult(Instances expandedInstances, HashMap<String,List<String>>expansionSchema) {
		this.expandedInstances = expandedInstances;
		this.expansionSchema = expansionSchema;
		
	}

	
	public void setExpandedInstances(Instances expandedInstances) {
		this.expandedInstances = expandedInstances;
	}
	
	public void setExpansionSchema(HashMap<String,List<String>> expansionSchema) {
		this.expansionSchema = expansionSchema;
	}
	
	public Instances getExpandedInstances() {
		return expandedInstances;
	}
	
	public HashMap<String,List<String>> getExpansionSchema() {
		return expansionSchema;
	}
	
}
