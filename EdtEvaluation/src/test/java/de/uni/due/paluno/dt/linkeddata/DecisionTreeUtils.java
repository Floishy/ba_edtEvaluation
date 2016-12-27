package de.uni.due.paluno.dt.linkeddata;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;


public class DecisionTreeUtils {

	
	
	
	
	/*
	 * 
	 * First column is attribute, second column is class
	 * 
	 */
	public static Double calculateInformationGain(String [][] columns){
		
		java.util.List<String> classList = Arrays.asList(columns[1]);
		
		Double entropyBefore = calculateShannonEntropy(classList);
		
		HashMap<String, LinkedList<String>> sublists = new HashMap<String, LinkedList<String>>();
		
		
		
		for(int i=0;i<columns[0].length;i++){
			String string = columns[0][i];
			String _class = columns[1][i];
			if(!sublists.containsKey(string)){
				sublists.put(string, new LinkedList<String>());
			}
			sublists.get(string).add(_class);
		}
		
		double entropyAfter = 0;
		
		for(String key:sublists.keySet()){
			double proportion = (double)sublists.get(key).size()/(double)classList.size();
			double entropyProportion = calculateShannonEntropy(sublists.get(key));
			entropyAfter = entropyAfter+proportion*entropyProportion;
			
		}
		
		
		return entropyBefore- entropyAfter;
	}
	
	public static Double calculateShannonEntropy(String [] values) {
		return calculateShannonEntropy( Arrays.asList(values));
		
	}
	
	public static String [] split(String string,String spliter){
		 String[] split = StringUtils.split(string, spliter);
		 
		for (int i = 0; i < split.length; i++) {
			split[i] = split[i].trim();
			
		}
		
		return split;
	}
	
	public static Double calculateShannonEntropy(java.util.List<String> values) {
		  Map<String, Integer> map = new HashMap<String, Integer>();
		  // count the occurrences of each value
		  for (String sequence : values) {
		    if (!map.containsKey(sequence)) {
		      map.put(sequence, 0);
		    }
		    map.put(sequence, map.get(sequence) + 1);
		  }
		 
		  // calculate the entropy
		  Double result = 0.0;
		  for (String sequence : map.keySet()) {
		    Double frequency = (double) map.get(sequence) / values.size();
		    result -= frequency * (log2(frequency));
		  }
		 
		  //System.out.println(StringUtils.join(values,",")+":"+result);
		  
		  return result;
		
	}
	
	public static Double calculateSplitInfo(java.util.List<String> values){
		
		double wholeSetSize = values.size();
		
		HashMap<String, Integer> counter= new HashMap<String, Integer>();
		
		for (String attributeValue : values) {
			if(counter.containsKey(attributeValue)){
				counter.put(attributeValue, counter.get(attributeValue)+1);
			}
			else{
				counter.put(attributeValue, 1);
			}
		}
		
		Set<String> keySet = counter.keySet();
		
		Double result = 0.0;
		
		for (String key : keySet) {
			double groupCount = counter.get(key);
			double splitInfoGroup = (groupCount/wholeSetSize)*log2(groupCount/wholeSetSize);
			result = result+splitInfoGroup;
		}
		
		return result*-1.0d;
	}
	
	
	public static double log2(double n)
	{
	    return (Math.log(n) / Math.log(2));
	}
}
