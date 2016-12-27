package de.uni.due.paluno.se.edt;

import java.util.HashMap;
import java.util.Set;

public class AttributeSpecification{
	HashMap<String, DataType> attributeType;
	
	HashMap<String, String[]> newAttributes;
	
	
	public AttributeSpecification() {
	}
	
	public AttributeSpecification(HashMap<String, DataType> attributeType,HashMap<String, String[]> newAttributes){
		this.attributeType = attributeType;
		this.newAttributes = newAttributes;
	}
	
	public HashMap<String, DataType> getAttributeType() {
		return attributeType;
	}
	
	public HashMap<String, String[]> getNewAttributes() {
		return newAttributes;
	}
	
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		Set<String> keySet = attributeType.keySet();
		
		for (String string : keySet) {
			sb.append(string+"\t");
		}
		return sb.toString();
	}
	
}