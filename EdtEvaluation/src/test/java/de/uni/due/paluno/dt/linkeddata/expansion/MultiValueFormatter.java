package de.uni.due.paluno.dt.linkeddata.expansion;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;

import javafx.util.Pair;
import weka.classifiers.trees.J48;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.InstanceComparator;
import weka.core.Instances;
import weka.core.converters.CSVLoader;
import weka.core.converters.CSVSaver;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.AddValues;
import weka.filters.unsupervised.attribute.Remove;
import weka.filters.unsupervised.attribute.StringToNominal;

public class MultiValueFormatter {
	static long startTime = System.currentTimeMillis();
	public static void main(String[] args)
	{

		MultiValueFormatter formatter = new MultiValueFormatter();
		
		try
		{
			//formatWithNewInstances test
			
//			1. load arff or csv file 
//			Instances res = formatter.loadCSV("C:\\Users\\philipp\\Desktop\\testCSV datei BA\\inst1.csv", ",", 2);
//			
//			System.out.println(res);			
//			
//			//2. format the loaded data by splitting the instances
//			long timer = System.currentTimeMillis();
//			res = formatter.formatWithNewInstances(res, ";", "$");
//			
////			System.out.println(res);
//			System.out.println("Dauer:" + (System.currentTimeMillis() - timer));
//			System.out.println("Dauer formatWithNewInstances: " + formatter.timerformatWithNewInstances);
//			System.out.println("Dauer splitAndAddNewInstances: " + formatter.timersplitAndAddNewInstances);
//			System.out.println("Dauer splitInstance: " + formatter.timersplitInstance);
//			System.out.println("Dauer addInstances: " + formatter.timeraddInstances);
//			System.out.println("Dauer addAttributeValue: " + formatter.timeraddAttributeValue);
//			System.out.println("Dauer addAttributeValue2: " + formatter.timeraddAttributeValue2);
//			System.out.println("Dauer addAttributeValue3: " + formatter.timeraddAttributeValue3);
//			System.out.println("Dauer removeInstances: " + formatter.timerremoveInstances);
//			System.out.println("Dauer removeInstances2: " + formatter.timerremoveInstances2);
			
			//3. output the result to a file (you can not write res.toString() into a csv file!)
//			formatter.writeFile(res.toString(), "C:\\Users\\philipp\\Desktop\\inst2NI.arff");
			
//			4. use the result to build a tree (e.g. C4.5 or HT)
//			J48 j48 = new J48();
//			j48.buildClassifier(res);
//			System.out.println();
//			System.out.println("Tree: " + j48.toString());
			
			
			
//			//formatWithBinarySets test
			
			//1. load arff or csv file
			Calendar cal = Calendar.getInstance();
			SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
			
	        for(int i =4;i<=4;i++){
			Instances res = formatter.loadCSV("F:\\Workspace_Bachelor_Arbeit\\maven.1482858439171\\EdtEvaluation\\src\\test\\resources\\MUTAG\\mutagtraining04"+".csv", ",", 2);
			System.out.println(startTime/1000);
			//System.out.println(res);
			
//			System.out.println(res.numAttributes());
//			System.out.println(res.getM_Attributes().size());
//			System.out.println(res.instance(0).attribute(132).value(0));
//			System.out.println(res.instance(0).attribute(132).name());
//			System.out.println(res.instance(0).numValues());
//			for(Attribute att : res.getM_Attributes()){
//				System.out.println(att.type());
//				if (att.type()==0){
//					System.out.println(att.name());
//					System.out.println(att.value(0));
//				}
//			}
			
			//2. format the loaded data with binary sets
			res = formatter.formatWithBinarySets(res, ";");
			
			//System.out.println(res);
			
			formatter.saveInstanceToCSV(res,"src/test/resources/MUTAG/",i+"HopsMutagTraining");
			System.out.println((System.currentTimeMillis()-startTime)/1000 );
	        }
	        for(int i =4;i<=4;i++){
				Instances res = formatter.loadCSV("F:\\Workspace_Bachelor_Arbeit\\maven.1482858439171\\EdtEvaluation\\src\\test\\resources\\MUTAG\\mutagtest04"+".csv", ",", 2);
				System.out.println(startTime/1000);
				//System.out.println(res);
				
//				System.out.println(res.numAttributes());
//				System.out.println(res.getM_Attributes().size());
//				System.out.println(res.instance(0).attribute(132).value(0));
//				System.out.println(res.instance(0).attribute(132).name());
//				System.out.println(res.instance(0).numValues());
//				for(Attribute att : res.getM_Attributes()){
//					System.out.println(att.type());
//					if (att.type()==0){
//						System.out.println(att.name());
//						System.out.println(att.value(0));
//					}
//				}
				
				//2. format the loaded data with binary sets
				res = formatter.formatWithBinarySets(res, ";");
				
				//System.out.println(res);
				
				formatter.saveInstanceToCSV(res,"src/test/resources/MUTAG/",i+"HopsMutagTest");
				System.out.println((System.currentTimeMillis()-startTime)/1000 );
		    }
			//3. output the result to a file (you can not write res.toString() into a csv file!)
//			formatter.writeFile(res.toString(), "C:\\Users\\philipp\\Desktop\\inst3BS.arff");
//			
//			//4. use the result to build a tree (C4.5 or HT)
//			J48 j48 = new J48();
//			j48.buildClassifier(res);
//			
//			System.out.println();
//			System.out.println("Tree: " + j48.toString());
			
//			HoeffdingTree ht = new HoeffdingTree();
//			ht.buildClassifier(res);
//			System.out.println("\n" + ht.toString());
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}
	
	
	public void saveInstanceToCSV(Instances inst,String pathPrefix,String filename) throws IOException{
		CSVSaver saver = new CSVSaver();
		 //ArffSaver saver = new ArffSaver();
		 saver.setInstances(inst);
		 saver.setFile(new File(pathPrefix + filename +".csv"));
		 saver.writeBatch();
	}
	
	long timerformatWithNewInstances = 0;	
	public Instances formatWithNewInstances(Instances data, String separator, String classseparator) throws Exception
	{	
		long tempTime = System.currentTimeMillis();
		
		for(int i = 0; i < data.numInstances(); i++){
			for(int j = 0; j < data.numAttributes(); j++){
				if(instanceHasMultiValues(data.instance(i), data.attribute(j), separator)){
					data = splitAndAddNewInstances(data, data.instance(i), data.attribute(j), separator, classseparator);
//					i = -1;
					i--;
					break;
				}
			}
		}
		
		timerformatWithNewInstances += System.currentTimeMillis() - tempTime + timerformatWithNewInstances;
		return data;
	}
	
	long timersplitAndAddNewInstances = 0;	
	private Instances splitAndAddNewInstances(Instances data, weka.core.Instance instance, weka.core.Attribute multiValuedAttribute, String separator, String classseparator) throws Exception{
		long tempTime = System.currentTimeMillis();
		
		
		LinkedList<weka.core.Instance> newInstances = splitInstance(instance, multiValuedAttribute, separator);
		data = instance.dataset();
		data = addInstances(data, newInstances, data.numInstances() - 1, classseparator); //data.indexOf(instance)
		
		timersplitAndAddNewInstances += System.currentTimeMillis() - tempTime;
		return removeInstances(data, instance);
	}
	
	private Boolean instanceHasMultiValues(weka.core.Instance instance, weka.core.Attribute currentAttribute, String separator){
		
		int attributeType = currentAttribute.type();
		switch(attributeType){
		case 0:
			return false; //if an attribute is numerical it can not contain a separator, so numerical attributes can not have multi-values.
		case 1:
		default:
			return instance.stringValue(currentAttribute).split(separator).length > 1;
		}
	}
	
	long timersplitInstance = 0;
	private LinkedList<weka.core.Instance> splitInstance(weka.core.Instance instance, weka.core.Attribute currentAttribute, String separator) throws Exception{
		
		long tempTime = System.currentTimeMillis();
		
		LinkedList<weka.core.Instance> newInstances = new LinkedList<weka.core.Instance>();
		
		String[] values = instance.stringValue(currentAttribute).split(separator);
		for(int i = 0; i < values.length; i++){
			DenseInstance newInstance = new DenseInstance(instance.numAttributes());
			newInstance.setDataset(instance.dataset());
			newInstance.setWeight(instance.weight());
			for(int j = 0; j < instance.numAttributes(); j++){
				if(!instance.attribute(j).name().equals(currentAttribute.name())){
					int attributeType = instance.attribute(j).type();					
					switch(attributeType){
						case 0:
							newInstance.setValue(instance.attribute(j), instance.value(instance.attribute(j)));
							break;
						case 1:
						default:
							String val = instance.stringValue(instance.attribute(j));
							if (!val.equals("?"))
								newInstance.setValue(instance.attribute(j), instance.stringValue(instance.attribute(j)));
							else{
								newInstance.setDataset(addAttributeValue(newInstance.dataset(), j, "?"));
								instance.setDataset(newInstance.dataset());
								newInstance.setValue(instance.attribute(j), "?");
							}
							break;
					}
				}
				else{
					instance.setDataset(addAttributeValue(instance.dataset(), j, values[i]));
					newInstance.setDataset(instance.dataset());
					newInstance.setValue(instance.attribute(j), values[i]);
				}
			}	
			newInstances.add(newInstance);
		}		
		
		timersplitInstance += System.currentTimeMillis() - tempTime;
		return newInstances;
	}
	
	long timeraddInstances = 0;
	private Instances addInstances(Instances data, LinkedList<weka.core.Instance> instances, int index, String classseparator) throws Exception{
		
		long tempTime = System.currentTimeMillis();
		
		String[] oldWeight = instances.getFirst().stringValue(instances.getFirst().attribute(instances.getFirst().classIndex())).toString().split("\\" + classseparator);
		double oldWeightNumber = 0;
		if(oldWeight.length > 1){
			oldWeightNumber = Double.parseDouble(oldWeight[1]);
		}
		if(oldWeightNumber != 0){
			oldWeightNumber = oldWeightNumber * (1.0/instances.size());
		}
		else{
			oldWeightNumber = (1.0/instances.size());
		}
		String weight = classseparator + oldWeightNumber + classseparator;
		
		String newClassValue = "";
		if(oldWeight.length > 1){
			newClassValue = weight + instances.getFirst().stringValue(data.classAttribute()).split("\\" + classseparator)[2];
		}
		else{
			newClassValue = weight + instances.getFirst().stringValue(data.classAttribute());
		}
		
		
		for (Instance instance : instances) {	
			
			data = addAttributeValue(data, data.classIndex(), newClassValue);
			
			instance.setDataset(data);
			instance.setClassValue(newClassValue);	
			
			data.add(index + 1, instance);
					
		}
		
		timeraddInstances += System.currentTimeMillis() - tempTime;
		return data;
	}	
	
	long timeraddAttributeValue = 0;
	long timeraddAttributeValue2 = 0;
	long timeraddAttributeValue3 = 0;
	private Instances addAttributeValue(Instances data, int attributeIndex, String value) throws Exception{
		long tempTime = System.currentTimeMillis();
		
		if(data.attribute(attributeIndex).indexOfValue(value) == -1){
			long tempTime2 = System.currentTimeMillis();
			weka.filters.unsupervised.attribute.AddValues addFilter = new AddValues();
			addFilter.setAttributeIndex((attributeIndex + 1) + "");
			addFilter.setLabels(value);
			addFilter.setInputFormat(data);
			long tempTime3 = System.currentTimeMillis();
			data = Filter.useFilter(data, addFilter);
			timeraddAttributeValue3 += System.currentTimeMillis() - tempTime3;
			timeraddAttributeValue2 += System.currentTimeMillis() - tempTime2;
		}
		
		timeraddAttributeValue += System.currentTimeMillis() - tempTime;
		return data;
	}
	
	long timerremoveInstances = 0;
	long timerremoveInstances2 = 0;
	private Instances removeInstances(Instances data, weka.core.Instance instanceToBeRemoved){
		long tempTime = System.currentTimeMillis();
		
//		for (Instance instance : data) {
//			if(instance.toString().equals(instanceToBeRemoved.toString())){		
//
//				long tempTime2 = System.currentTimeMillis();
//				data.remove(instance.dataset().indexOf(instance));
//				timerremoveInstances2 += System.currentTimeMillis() - tempTime2;
//				break;
//			}
//		}
		
		InstanceComparator comparator = new InstanceComparator();
		for (Instance instance : data) {
			if(comparator.compare(instance, instanceToBeRemoved) == 0){		

				long tempTime2 = System.currentTimeMillis();
				data.remove(instance.dataset().indexOf(instance));
				timerremoveInstances2 += System.currentTimeMillis() - tempTime2;
				break;
			}
		}
		
		timerremoveInstances += System.currentTimeMillis() - tempTime;
		return data;
	}
	
	public Instances formatWithBinarySets(Instances data, String separator) throws Exception
	{
		//find the multi-valued attributes
		LinkedList<weka.core.Attribute> multiValuedAttributes = getMultiValuedAttributes(data, separator);
		System.out.println("Multi found");
		System.out.println((System.currentTimeMillis()-startTime)/1000 );
		//list all possible values
		HashMap<String, LinkedList<String>> values = listPossibleValues(data, multiValuedAttributes, separator);
		System.out.println("Listing");
		System.out.println((System.currentTimeMillis()-startTime)/1000 );
		System.out.println(values.size());
		//replace original attributes with attributes derived from the values and save old Instances and old Attributes before they were changed
		ArrayList<Object> container = replaceAttributesWithBinarySets(data, multiValuedAttributes, values);		
		data = (Instances) container.get(0);
		HashMap<Instance,Pair<Instance,HashMap<Attribute,LinkedList<Attribute>>>> attributeMapping = 
				(HashMap<Instance,Pair<Instance,HashMap<Attribute,LinkedList<Attribute>>>>) container.get(1);
		LinkedList<weka.core.Attribute> newAttributes = (LinkedList<weka.core.Attribute>) container.get(2);
		System.out.println("Replaced");
		System.out.println((System.currentTimeMillis()-startTime)/1000 );
		//fill new Attributes with appropriate values
		data = fillNewAttributes(data, multiValuedAttributes, newAttributes, attributeMapping, separator);
		System.out.println("adding finished");
		System.out.println((System.currentTimeMillis()-startTime)/1000 );
		return data;
	}
	
	public Instances formatWithBinarySets(Instances data, LinkedList<weka.core.Attribute> multiValuedAttributes, String separator) throws Exception
	{		
		//list all possible values
		HashMap<String, LinkedList<String>> values = listPossibleValues(data, multiValuedAttributes, separator);
		
		//replace original attributes with attributes derived from the values and save old Instances and old Attributes before they were changed
		ArrayList<Object> container = replaceAttributesWithBinarySets(data, multiValuedAttributes, values);		
		data = (Instances) container.get(0);
		HashMap<Instance,Pair<Instance,HashMap<Attribute,LinkedList<Attribute>>>> attributeMapping = 
				(HashMap<Instance,Pair<Instance,HashMap<Attribute,LinkedList<Attribute>>>>) container.get(1);
		LinkedList<weka.core.Attribute> newAttributes = (LinkedList<weka.core.Attribute>) container.get(2);
		
		//fill new Attributes with appropriate values
		data = fillNewAttributes(data, multiValuedAttributes, newAttributes, attributeMapping, separator);
		
		return data;
	}
	
	private LinkedList<weka.core.Attribute> getMultiValuedAttributes(Instances data, String separator){
		LinkedList<weka.core.Attribute> attributes = new LinkedList<weka.core.Attribute>();
		for (weka.core.Instance instance : data) {	
			for(int i = 0; i < instance.numAttributes(); i++){	
				int attributeType = instance.attribute(i).type();
				
				String temp = "";
				switch(attributeType){
					case 0:
						temp = "" + instance.value(i);
						if(temp.contains(separator) && !attributes.contains(instance.attribute(i))){
							attributes.add(instance.attribute(i));
						}	
						break;
					case 1:
					default:
						temp = instance.stringValue(i);
						if(temp.contains(separator) && !attributes.contains(instance.attribute(i))){
							attributes.add(instance.attribute(i));
						}	
						break;
				}
			}
		}	
		return attributes;
	}
	
	private HashMap<String, LinkedList<String>> listPossibleValues(Instances data, LinkedList<weka.core.Attribute> multiValuedAttributes, String separator){
		
		HashMap<String, LinkedList<String>> values = new HashMap<String, LinkedList<String>>();
		for (weka.core.Instance instance : data) {
			LinkedList<String> attributeValues = new LinkedList<String>();
			for (int j = 0; j < multiValuedAttributes.size(); j++) {
				for (int i = 0; i < instance.numAttributes(); i++) {
					if (instance.attribute(i).equals(multiValuedAttributes.get(j))) {
							String[] currentValues = instance.stringValue(instance.attribute(i)).split(separator);
							for (int m = 0; m < currentValues.length; m++) {
								if (!attributeValues.contains(currentValues[m].trim())) {
									attributeValues.add(currentValues[m].trim());									
								}
							}
						if(values.get((instance.attribute(i).name())) != null){
							for(int k = 0; k < attributeValues.size(); k++){
								if(!values.get((instance.attribute(i).name())).contains(attributeValues.get(k))){
									values.get((instance.attribute(i).name())).add(attributeValues.get(k));
								}
							}
							values.put(instance.attribute(i).name(), values.get((instance.attribute(i).name())));
						}
						else{
							values.put(instance.attribute(i).name(), attributeValues);
						}
						break;
					}
				}
				attributeValues = new LinkedList<String>();
			}
		}	
		return values;
	}	
	
	private ArrayList<Object> replaceAttributesWithBinarySets(Instances data, LinkedList<weka.core.Attribute> attributes, HashMap<String, LinkedList<String>> values) throws Exception{		
		
		LinkedList<weka.core.Attribute> oldMultiValuedAttributes = (LinkedList<weka.core.Attribute>) attributes.clone();
		
		HashMap<weka.core.Instance, Pair<weka.core.Instance, HashMap<weka.core.Attribute, LinkedList<weka.core.Attribute>>>> Map1 = 
				new HashMap<weka.core.Instance, Pair<weka.core.Instance, HashMap<weka.core.Attribute, LinkedList<weka.core.Attribute>>>>();
		LinkedList<Pair<weka.core.Instance, HashMap<weka.core.Attribute, LinkedList<weka.core.Attribute>>>> test = 
				new LinkedList<Pair<weka.core.Instance, HashMap<weka.core.Attribute, LinkedList<weka.core.Attribute>>>>();
		
		int i = 0;
		for(weka.core.Instance instance : data){
			HashMap<weka.core.Attribute, LinkedList<weka.core.Attribute>> Map2 = new HashMap<weka.core.Attribute, LinkedList<weka.core.Attribute>>();
			Pair<weka.core.Instance, HashMap<weka.core.Attribute, LinkedList<weka.core.Attribute>>> Pair = 
					new Pair<weka.core.Instance, HashMap<weka.core.Attribute, LinkedList<weka.core.Attribute>>>((weka.core.Instance)instance, Map2);
			test.add(i, Pair);
			i++;
		}
		
		LinkedList<weka.core.Attribute> newAttributesList = new LinkedList<weka.core.Attribute>();
		int newAttributes = 0;
		for (int j = 0; j < oldMultiValuedAttributes.size(); j++) {
			LinkedList<weka.core.Attribute> Liste = new LinkedList<weka.core.Attribute>();
			
			for(weka.core.Instance instance : data){
				((Pair<weka.core.Instance, HashMap<weka.core.Attribute, LinkedList<weka.core.Attribute>>>)
						test.get(data.indexOf(instance))).getValue().put(oldMultiValuedAttributes.get(j), Liste);
			}
			
			Remove remove = new Remove();
			if(newAttributes == 0)
				remove.setAttributeIndices((oldMultiValuedAttributes.get(j).index() + 1) + "");
			else
				remove.setAttributeIndices((oldMultiValuedAttributes.get(j).index() + newAttributes) + "");
			
			remove.setInvertSelection(false);
			remove.setInputFormat(data);
			data = Filter.useFilter(data, remove);

			newAttributes = 0;			
			
			LinkedList<String> currentValues = values.get(oldMultiValuedAttributes.get(j).name());
			newAttributes = currentValues.size();
			for (int k = 0; k < currentValues.size(); k++) {	
//				data = addBooleanAttribute(data, oldMultiValuedAttributes.get(j).index(), currentValues.get(k));				
//				newAttributesList.add(data.attribute(currentValues.get(k)));
//				Liste.add(data.attribute(currentValues.get(k)));
				data = addAttribute(data, currentValues.get(k), oldMultiValuedAttributes.get(j).index(), oldMultiValuedAttributes.get(j).name() + ":::" + currentValues.get(k));				
				newAttributesList.add(data.attribute(oldMultiValuedAttributes.get(j).name() + ":::" + currentValues.get(k)));
				Liste.add(data.attribute(oldMultiValuedAttributes.get(j).name() + ":::" + currentValues.get(k)));
			}
		}
		
		i = 0;
		for(weka.core.Instance instance : data){	
			Map1.put(instance, (Pair<Instance, HashMap<Attribute, LinkedList<Attribute>>>) test.get(i));
			i++;
		}

		ArrayList<Object> result = new ArrayList<Object>();
		result.add(data);
		result.add(Map1);
		result.add(newAttributesList);
		return result;
	}
	
	private Instances addAttribute(Instances data, String value, int index, String name){
		

		weka.core.Attribute newAttribute = createAttribute(name, value);
		
		Boolean isNewAttribute = false;
		while(!isNewAttribute){
			isNewAttribute = true;
			for(int i = 0; i < data.numAttributes(); i++){
				if(data.attribute(i).name().equals(newAttribute.name()))
					isNewAttribute = false;				
			}
			
			if(!isNewAttribute){
				newAttribute = createAttribute(newAttribute.name() + "2", value);
			}
		}
		
		data.insertAttributeAt(newAttribute, index);
			
		return data;
	}
	
	private weka.core.Attribute createAttribute(String name, String value){
		
		LinkedList<String> booleanValues = new LinkedList<String>();
		booleanValues.add("0");
		booleanValues.add(value);	
		
		return new Attribute(name, booleanValues);
	}
	
	private Instances fillNewAttributes(Instances data, LinkedList<weka.core.Attribute> multiValuedAttributes, LinkedList<weka.core.Attribute> newAttributes,
			HashMap<weka.core.Instance, Pair<weka.core.Instance, HashMap<weka.core.Attribute, LinkedList<weka.core.Attribute>>>> attributeMapping, String separator)
	{
		
		Map<String, weka.core.Attribute> newAttributeMap = new HashMap<String, weka.core.Attribute>();
		for (Attribute attribute : newAttributes) {
			newAttributeMap.put(attribute.name().trim(), attribute);
		}
			
		for (weka.core.Instance instance : data) {
			
			for(int i = 0; i < instance.numAttributes(); i++) {
				Attribute attribute = newAttributeMap.get(instance.attribute(i).name().trim());
				
				if (attribute != null) {
					weka.core.Instance oldInstance = attributeMapping.get(instance).getKey();
					
					weka.core.Attribute savedAttribute = null;
					Iterator<Entry<Attribute, LinkedList<Attribute>>> savedAttributes = attributeMapping.get(instance).getValue().entrySet().iterator();
				    while (savedAttributes.hasNext()) {
				        Map.Entry<Attribute, LinkedList<Attribute>> pair = (Map.Entry<Attribute, LinkedList<Attribute>>)savedAttributes.next();
				        
				        LinkedList<weka.core.Attribute> newMappedAttributes = pair.getValue();
				        if(newMappedAttributes.contains(instance.attribute(i))){
							savedAttribute = pair.getKey();
							break;
				        }
				    }
				    
				    String[] savedValues;
					if (savedAttribute != null)
						savedValues = oldInstance.stringValue(savedAttribute).split(separator);
					else{
						instance.setValue(instance.attribute(i), 0);
						break;
					}
					
					Boolean instanceHasValue = false;
					for(int g = 0; g < savedValues.length; g++){
						String newAttributeName = oldInstance.attribute(savedAttribute.index()).name().trim() + ":::" + savedValues[g].trim();
						if(newAttributeName.equals( instance.attribute(i).name().trim() )){
							instance.setValue(instance.attribute(i), savedValues[g].trim());
							instanceHasValue = true;
							break;
						}
					}
					
					if(!instanceHasValue)
						instance.setValue(instance.attribute(i), 0);
				}
			}
		}
		
		return data;
	}

	public Instances loadCSV(String csvPath, String separator, int classIndex) throws Exception	
	{
		CSVLoader trainDataLoader = new CSVLoader();
  		trainDataLoader.setFieldSeparator(separator);
  		trainDataLoader.setSource(new File(csvPath));

  		Instances trainData = trainDataLoader.getDataSet();
  		trainData.setClassIndex(classIndex);
  		
  		trainData = filterStringAttributes(trainData);
  		System.out.println("Loaded");
  		return trainData;
	}
	
	private Instances loadArff(String arffPath, int classIndex) throws Exception{
		BufferedReader reader = new BufferedReader(new FileReader(arffPath));
		Instances data = new Instances(reader);
		reader.close();
		data.setClassIndex(classIndex);
		
		data = filterStringAttributes(data);
		
		return data;
	}
	
	private Instances filterStringAttributes(Instances data) throws Exception{

  		
  		weka.filters.unsupervised.attribute.StringToNominal filter = new StringToNominal();
  		filter.setInputFormat(data);
  		
  		String attributeRange = "";
  		for(int i = 0; i < data.numAttributes(); i++){
  			if(i == 0 && data.attribute(i).isString()){
  				attributeRange += i;
  				continue;
  			}
  				
  			else if(data.attribute(i).isString())
  				attributeRange += (i + 1) + ",";
  		}
  		
  		
  		
  		if(attributeRange.length() > 1){
  			attributeRange = attributeRange.substring(0, attributeRange.length() - 1);
  			
  	        String[] options = new String[2];
  	        options[0] = "-R";
  	        options[1] = attributeRange;
  	        filter.setOptions(options);
  	  		data = Filter.useFilter(data, filter);
  		}
  		
  		
  		return data;
	}
	
	private void writeFile(String input, String path) throws IOException{
		
		PrintWriter out = new PrintWriter(path);
		out.write(input);
		out.flush();
		out.close();
	}

}
















