package de.uni.due.paluno.dt.linkeddata;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;



public class DecisionTreeUtilsOnDataSet {

	
	public static void main(String[] args) {
		
		
		//Medications
		String attribute = "None,None,None,None,XYZ,ZYX";
		
		//Patient
		//String attribute ="http://…/A,http://…/B,http://…/C,http://…/D,http://…/E,http://…/F";
		
		//Disease
		//String attribute ="None,Cold,Cold,Cold,None,Migraine";
		
		//Disease_hasSymptoms
		//String attribute ="N/A,Pain,Pain,Pain,N/A,Pain";
		
		//Disease_isA
		//String attribute ="N/A,Resp. Infection,Resp. Infection,Resp. Infection,N/A,Disease";
		
		String classes ="Ibuprofen,Paracetamol,Paracetamol,Paracetamol,Change Med.,Paracetamol"; 		
		
		//String classes = "1,2,3,1,3,2,3,3";
		
		String[] classSplit = StringUtils.split(classes, ",");
				
		java.util.List<String> classesList = Arrays.asList(classSplit);
		
		
		Double calculateShannonEntropy = DecisionTreeUtils.calculateShannonEntropy(classesList);
		System.out.println("Entropy original: "+calculateShannonEntropy);
		
		String columns [][] = new String[2][classSplit.length];
		
		columns[0] = StringUtils.split(attribute,",");
		
		columns[1] = classSplit;
		
		
		List<String> attributesList = Arrays.asList(columns[0]);
		
		Double splitInfo = DecisionTreeUtils.calculateSplitInfo(attributesList);
		
		Double informationGain = DecisionTreeUtils.calculateInformationGain(columns);
		
		System.out.println("Information Gain: "+informationGain);
		
		
		System.out.println("Gain Ratio:" + informationGain/splitInfo);
		
	}
	
}
