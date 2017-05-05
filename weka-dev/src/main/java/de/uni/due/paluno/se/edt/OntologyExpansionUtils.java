package de.uni.due.paluno.se.edt;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.math.NumberUtils;
import org.apache.jena.datatypes.RDFDatatype;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.query.ParameterizedSparqlString;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;

import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;

public class OntologyExpansionUtils {
	

	
	public static final String rdfs = "http://www.w3.org/2000/01/rdf-schema#"; 
	
	public static final String elementSeparator = Constants.keySeparator;
	
	public static ParameterizedSparqlString selectSameAs;

	
	public  Model model;

	private OntModel ontModel;

	static {
		selectSameAs = new ParameterizedSparqlString();
		selectSameAs.setNsPrefix("owl","http://www.w3.org/2002/07/owl#");
		selectSameAs.setNsPrefix("rdfs", "http://www.w3.org/2000/01/rdf-schema#");
		selectSameAs.setCommandText("Select ?s WHERE {?s owl:sameAs ?o}");
		
	}
	
	/**
	 * 
	 * 
	 * @param model
	 * @param ontModel
	 */
	public OntologyExpansionUtils(Model model, OntModel ontModel) {
		this.model =model;
		this.ontModel = ontModel;
	}
	
	
	public Instances addNewAttributes(Instances instOrr, AttributeSpecification newAttributeValues){
		
		Instances inst = new Instances(instOrr);
		
		HashMap<String, DataType> attributeType = newAttributeValues.getAttributeType();
		
		HashMap<String, String[]> newAttributes = newAttributeValues.getNewAttributes();
		
		//TODO Take the attribute name from the predicate 
		List<String> attributeNames = new LinkedList(newAttributes.keySet());
		
		//XXX Add the new Attributes to the dataset
		
		for (String attributeName : attributeNames) {
			
			
			if(!acceptAttribute(attributeName)){
				continue;
			}
			//TODO 
			if(instOrr.hasAttributeNamed(attributeName)){
				continue;
			}
			
			int numberOfAttributes = inst.numAttributes();
			
			Attribute att;
			
			if(attributeType.get(attributeName).equals(DataType.String)){
			
				List<String> attributeValuesWithoutDuplicates = getAttributeValuesRemoveDuplicates(newAttributes.get(attributeName));
				att = new Attribute(attributeName,attributeValuesWithoutDuplicates);//(List)null);
				
				
				inst.insertAttributeAt(att,numberOfAttributes);
				
				List<String> attributeValues = getAttributeValues(newAttributes.get(attributeName));
				for(int i=0;i<attributeValues.size();i++){
					
					inst.get(i).setValue(numberOfAttributes,attributeValuesWithoutDuplicates.indexOf(attributeValues.get(i)));
				}
			
			}
			else{
				att = new Attribute(attributeName);
				
				inst.insertAttributeAt(att,numberOfAttributes);
				
				List<String> attributeValues = getAttributeValues(newAttributes.get(attributeName));
				for(int i=0;i<attributeValues.size();i++){
					 if(NumberUtils.isNumber(attributeValues.get(i))){
						 inst.get(i).setValue(numberOfAttributes,Double.parseDouble(attributeValues.get(i)));
					 }
				}
				
			}
						
			
			
		}
		
		return inst;
	}
	
	private ArrayList<Object> newAttributes(HashMap<String, DataType> attributeType,HashMap<String, String[]> newAttributes,String attributeValue,int instancePosition,List<String> attributesToConsider,
			Set<String> addedAttributes,int numInstances,String orrAttributeName){
		ArrayList<Object> result = new ArrayList<Object>();
		
		Resource resource=getResource(attributeValue);
		
		if(attributeValue.contains("Arthur")){
			System.out.println("Fehler");
		}
		
		//TODO here all instances have to be checked before this exception is thrown
		if(resource!=null){
			
			Statement property = resource.getProperty(this.model.getProperty(rdfs, "instanceof"));
			
			List<Statement> resourceProperties = new LinkedList<Statement>();
			
			resourceProperties.addAll(getResourceProperties(resource));
									
			
			//XXX SAME AS RESOURCE
//				List<Resource> sameResources = findSameAs(resource);
//				
//				for (Resource sameResource : sameResources) {
//					resourceProperties.addAll(getResourceProperties(sameResource));
//				}
			
			//---------------------
			
			//TODO YTE Set to missing instead of deleting RDF resources which are replaced through their attributes
			//TODO YTE Delete doesn't work
		//	instance.setMissing(attributePosition);
			
			//Collections.sort(resourceProperties, new StatementComparator());
			
			
			//HashMap<K, V>
			
			for(Statement stmt:resourceProperties){
				 Resource  subject   = stmt.getSubject();    
				 Property  predicate = stmt.getPredicate();  
				 RDFNode   object    = stmt.getObject(); 
				 
				//Build the name of the namespace and the localname
				 //Also store the previous attibute name
				 //XXX New Attributes Name Contstuction
				 String attributeName = orrAttributeName+elementSeparator+getPropertyName(predicate);
				
				 if(addedAttributes.contains(attributeName)){
					 System.out.println("contained");
				 }
				 
				 
				 if(attributesToConsider!=null){
					 if(!attributesToConsider.contains(attributeName)){
						 continue;
					 }
					 else{
						 addedAttributes.add(attributeName);
					 }
				 }
				 
				 if(!newAttributes.containsKey(attributeName)){
					 newAttributes.put(attributeName, new String[numInstances]);
				 }
				 
				 String value = object.toString();
				 
				 if (object instanceof Resource) {
					 //Object is another resource
					 String storedValue = newAttributes.get(attributeName)[instancePosition];
					 if(storedValue==null){
						 newAttributes.get(attributeName)[instancePosition]=value;
					 }
					 else{
						 //Multi-value
						 storedValue = storedValue+Constants.multiValueSeparator+value;
						 newAttributes.get(attributeName)[instancePosition]=storedValue;
					 }
					 attributeType.put(attributeName, DataType.String);
				 } else {
					 //Object is literal
					 Literal literal = object.asLiteral();
					 RDFDatatype datatype = literal.getDatatype();
					 
					 //TODO XML schema types should be also be converted to numeric
					 
					 if(datatype.getURI().equals("http://www.w3.org/2001/XMLSchema#double")){
						 
						 value = Double.toString(literal.getDouble());
						 
						 String storedValue = newAttributes.get(attributeName)[instancePosition];
						 if(storedValue==null){
							 newAttributes.get(attributeName)[instancePosition]=value;
						 }
						 else{
							 //Multi-value
							 storedValue = storedValue+Constants.multiValueSeparator+value;
							 newAttributes.get(attributeName)[instancePosition]=storedValue;
						 }
						
						 attributeType.put(attributeName, DataType.Numeric);
					 }
					 else if(datatype.getURI().equals("http://www.w3.org/2001/XMLSchema#gYear")){
						 value = literal.toString();
						 
						 String storedValue = newAttributes.get(attributeName)[instancePosition];
						 if(storedValue==null){
							 newAttributes.get(attributeName)[instancePosition]=value;
						 }
						 else{
							 //Multi-value
							 storedValue = storedValue+Constants.multiValueSeparator+value;
							 newAttributes.get(attributeName)[instancePosition]=storedValue;
						 }
						 attributeType.put(attributeName, DataType.String);
					 }
					 else{
						 String storedValue = newAttributes.get(attributeName)[instancePosition];
						 if(storedValue==null){
							 newAttributes.get(attributeName)[instancePosition]=value;
						 }
						 else{
							 //Multi-value
							 storedValue = storedValue+Constants.multiValueSeparator+value;
							 newAttributes.get(attributeName)[instancePosition]=storedValue;
						 }
						 if(NumberUtils.isNumber(value)){
							attributeType.put(attributeName, DataType.Numeric);
						 }
						 else{
							attributeType.put(attributeName, DataType.String);
						 }
					 }
					 
				 }		 
			}
		}
		//getResourceAttributes(resourceProperties, orrAttributeName, attributeType, newAttributes, numInstances, instancePosition,attributesToConsider);
		
		result.add(attributeType);
		result.add(newAttributes);
		return result;
	}

	
	
	public AttributeSpecification getNewAttributes(Instances inst, int attributePosition,List<String> attributesToConsider) throws NotAResourceException{
	
		//TODO YTE Expand using ontological specification not the attributes
		
		HashMap<String, DataType> attributeType = new HashMap<String, DataType>();
		HashMap<String, String[]> newAttributes = new HashMap<String, String[]>();
		
		String orrAttributeName = inst.attribute(attributePosition).name();
		
		int numInstances = inst.numInstances();
		for(int instancePosition=0;instancePosition<numInstances;instancePosition++) {
			Set<String> addedAttributes = new HashSet<String>();
			
//			System.out.println(instancePosition);
			
			Instance instance = inst.get(instancePosition);
			Attribute attribute = instance.attribute(attributePosition);
			
			if(attribute.isNominal()){
				
				//TODO Check if resource
			}
			else if(attribute.isString()){
				
				//TODO Check if resource
			}
			else if(attribute.isNumeric()){
				
			}
			else if(attribute.isDate()){
				
			}
			else{
				throw new RuntimeException("Unrecognized attribute Type");
			}
			
			String attributeValue = null;
			try{				
				attributeValue = instance.stringValue(attributePosition);
			}
			catch(Throwable t){
				continue;
			}
			
			String[] multiValue;
			if (attributeValue.contains(Constants.multiValueSeparator)){
				multiValue = attributeValue.split(Constants.multiValueSeparator);
				int length = multiValue.length;
				for(int n = 0;n<length;n++){
					ArrayList<Object> temp = this.newAttributes(attributeType, newAttributes, multiValue[n], instancePosition, attributesToConsider, addedAttributes, numInstances, orrAttributeName);
					attributeType.putAll((HashMap<String, DataType>) temp.get(0));
					newAttributes.putAll((HashMap<String, String[]>)temp.get(1));
				}
			}
			
			else{
				
				//getResourceAttributes(resourceProperties, orrAttributeName, attributeType, newAttributes, numInstances, instancePosition,attributesToConsider);
				ArrayList<Object> temp = this.newAttributes(attributeType, newAttributes, attributeValue, instancePosition, attributesToConsider, addedAttributes, numInstances, orrAttributeName);
				attributeType.putAll((HashMap<String, DataType>) temp.get(0));
				newAttributes.putAll((HashMap<String, String[]>)temp.get(1));
				
			}
			
			
			if(newAttributes.isEmpty()){
				throw new NotAResourceException();
			}
			
			if(attributesToConsider!=null){
				for(String attributeName:attributesToConsider){
					if(addedAttributes.contains(attributeName)){
						continue;
					}
					if(!newAttributes.containsKey(attributeName)){
						if(!inst.hasAttributeNamed(attributeName)){
							newAttributes.put(attributeName, new String[numInstances]);
							newAttributes.get(attributeName)[instancePosition]=null;
							//TODO set the correct data type from the extension schema 
							attributeType.put(attributeName, DataType.String);
						}
					}
					
					
				}
			}
			
		}
		
		
		
		return new AttributeSpecification(attributeType,newAttributes);
		
	}
	
	/**
	 **** NOT USED*** Sorts the instances by the class attribute and then divides them in sets to the class they belong
	 * 
	 * @param trainInstances
	 * @return
	 */
	public  List<Instances> getClassSeparatedInstances(Instances trainInstances){
		//Sort the instances by the class index
		trainInstances.sort(trainInstances.classIndex());
		
		Enumeration<Instance> enumerateInstances = trainInstances.enumerateInstances();
		
		String currentClass=null;
		
		List<Instances> classSeparatedInstances = new LinkedList<Instances>();
		int first =0;
		int num =0;
		while(enumerateInstances.hasMoreElements()){
			Instance nextElement = enumerateInstances.nextElement();
			String _classValue =nextElement.stringValue(trainInstances.classIndex()); 
			if(currentClass==null){
				currentClass= _classValue;
			}
			else{
				if(!currentClass.equals(_classValue)){
					//Copy the current set to new Instances
					Instances instances = new Instances(trainInstances, first, num);
					classSeparatedInstances.add(instances);
					
					//Set the new current class
					currentClass = _classValue;
					
					//Set new starting point and reset the num count
					first = first+num;
					num = 0;
				}
			}
			num = num+1;
		}
		//Add the last set of same classed instances
		Instances instances = new Instances(trainInstances, first, num);
		classSeparatedInstances.add(instances);
		
		return classSeparatedInstances;
	}
	
	/**
	 * NOT USED
	 * 
	 * @param classSeparatedInstances
	 * @param m_attIndex
	 */
	public  void getCommonAttributes(List<Instances> classSeparatedInstances, int m_attIndex){
		
		
		
		for (Instances instances : classSeparatedInstances) {
			// Only Instances with known values are relevant.
			Enumeration<Instance> enu = instances.enumerateInstances();
			while (enu.hasMoreElements()) {
				Instance instance = enu.nextElement();
				if (!instance.isMissing(m_attIndex)) {
					String uri = instance.stringValue(m_attIndex);
					
					//Check if the uri is contained as subject within the model's statements
					Resource resource = ResourceFactory.createResource(uri);
					if(this.model.contains(resource,null,(RDFNode)null)){
						
						Model resourceModel = ModelFactory.createDefaultModel();
						
						List<Statement> resourceProperties = new LinkedList<Statement>();
												
						Resource attributeResource = this.model.getResource(resource.getURI());
						
						resourceProperties.addAll(getResourceProperties(attributeResource));
												
						//TODO select only the resources that come from a particular other namespaces that are predefined
						//Find same as instances across the model
						List<Resource> sameResources = findSameAs(attributeResource);
						
						for (Resource sameResource : sameResources) {
							resourceProperties.addAll(getResourceProperties(sameResource));
						}
				
						resourceModel.add(resourceProperties);
						
											
						
						//model.write(System.out, "TTL");
						
						//Select same as relationships to find other 
						
						
						
					}
				}
			}
			
		}
	}
	

	public  List<Resource> findSameAs(Resource resource){
		
		List<Resource> resources = new LinkedList<Resource>();
		selectSameAs.setIri("o", resource.getURI());
		
		String queryString = selectSameAs.toString();
		
		Query query = QueryFactory.create(queryString);
		
		QueryExecution qe = QueryExecutionFactory.create(query, this.model);
		
		ResultSet resSet = qe.execSelect();
		
		while(resSet.hasNext()){
			QuerySolution qs = resSet.next();
			Resource sameAsResource = qs.getResource("s");
			resources.add(sameAsResource);
			
		}
		return resources;
	}
		
	private  List<String> getAttributeValues(String[] strings) {
		
		for(int i=0;i<strings.length;i++){
			if(strings[i]==null){
				strings[i] = "NaN";
			}
		}
		
		List<String> values = Arrays.asList(strings);
		
		return values;
		
	}
	
	private static List<String> getAttributeValuesRemoveDuplicates(String[] strings) {
		for(int i=0;i<strings.length;i++){
			if(strings[i]==null){
				strings[i] = "NaN";
			}
		}
			
		List<String> values = Arrays.asList(strings);
		Set<String> hs = new HashSet<String>();
		hs.addAll(values);
		//values.clear();
		//values.addAll(hs);
		return new LinkedList<String>(hs);
		
	}

	
	public static List<Statement> getResourceProperties(Resource resource){
		List<Statement> properties= new LinkedList<Statement>();
		StmtIterator listProperties = resource.listProperties();
		while(listProperties.hasNext()){
			Statement next = listProperties.next();
			properties.add(next);
		}
		return properties;
	}
	
public  List<Resource> getResources(String value){
		

		List<Resource> result = new LinkedList<Resource>();
		//Check if multiple resources
		
		if(value.contains(Constants.multiValueSeparator)){
			String[] split = value.split(";");
			for (String string : split) {
				Resource resource = getResource(string);
				if(resource!=null){
					result.add(resource);
				}
			}
			return result;
		}
		else{
			
			Resource resource = getResource(value);
			if(resource!=null){
				result.add(resource);
			}
			return result;
		}
		
		
		
	}
	
	public  Resource getResource(String value){
		Resource resource = ResourceFactory.createResource(value);
		
		if(this.model.contains(resource,null,(RDFNode)null)){
			return this.model.getResource(resource.getURI());
		}
		return null;
		
	}
	
	
	/**
	 * Checks whether a particular attribute should be added to the list
	 * 
	 * @param name
	 * @return
	 */
	public  boolean acceptAttribute(String name){
		//TODO YTE a better handling of attributes is required
		if(name.endsWith("identifier")){
			return false;
		}else if(name.contains("hasLithogenesis")||name.contains("isMutagenic")){
			return false;
		}
		return true;
	}

	
	public  String getPropertyName(Property property){
		return property.getNameSpace()+property.getLocalName();
	}
	
//	public static ExpansionResult expandRDFResources(Instances inst,
//			HashMap<String, List<String>> expansionSchema) {
//		
//		
//		
//		Set<String> keySet = expansionSchema.keySet();
//		
//		int numInstances = inst.numInstances();
//		
//		for (String attributeToExtend : keySet) {
//			Attribute attribute = inst.attribute(attributeToExtend);
//			for (int i = 0; i < numInstances; i++) {
//				String attributeValue = inst.get(i).stringValue(attribute);
//				
//				Resource resource=getResource(attributeValue);
//				
//				if(resource!=null){
//					
//				}
//				
//			}
//		}
//		
//		
//		return new ExpansionResult(inst, expansionSchema);
//	}
	
	

}


