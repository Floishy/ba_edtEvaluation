package de.uni.due.paluno.se.edt;

import java.util.ArrayList;

import weka.core.Attribute;

public class Attributes extends ArrayList<Attribute>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5114610723070126583L;
	
	public Attributes(ArrayList<Attribute> m_Attributes) {
		this.addAll(m_Attributes);
	}

	public Attributes() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("\n----------ATTRIBUTES---------");
		
		for (Attribute attribute : this) {
			sb.append("\n"+attribute.name()+":");
			for(int i=0;i<attribute.numValues();i++){
				sb.append(attribute.value(i)+";");
			}
		}
		sb.append("\n");
		sb.append("----------------------------");
		
		return sb.toString();
	}

}
