package de.uni.due.paluno.dt.linkeddata.expansion;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.apache.jena.datatypes.RDFDatatype;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import weka.classifiers.trees.LinkedDataExpansionTool;
import weka.classifiers.trees.TestConfiguration;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.CSVLoader;
import weka.core.converters.CSVSaver;
import de.uni.due.paluno.se.edt.Constants;

public class SpecialFileExpansion {

	public final static int attributeIndex = 5;
	
	@Test
	public void test() throws IOException{
		
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("Beans.xml");
		TestConfiguration tc = context.getBean(TestConfiguration.class);
		
		String fileName ="C:\\Projects\\Diss\\workspace\\EdtEvaluation\\data\\inst1.csv";
		
		CSVLoader trainDataLoader = new CSVLoader();
		trainDataLoader.setFieldSeparator(",");
		
		
		
		File file = new File(fileName);
		System.out.println(file.exists());
		trainDataLoader.setSource(file);
		
		
		
		Instances trainData = trainDataLoader.getDataSet();
		trainData.setClassIndex(1);
		
		String attrName = trainData.getM_Attributes().get(attributeIndex).name();
		
		LinkedDataExpansionTool expansionTool = context.getBean(LinkedDataExpansionTool.class);
		
		LinkedList<LinkedList<String>> result = new LinkedList<LinkedList<String>>(); 
		
		List<Property> properties = new LinkedList<Property>();
	
		
		
		for(int i=0;i<trainData.size();i++){
			Instance instance = trainData.get(i);
			String stringValue = instance.stringValue(attributeIndex);
			
			
			
			if(!stringValue.equals("NaN")){
				
				List<Resource> resources = expansionTool.getOntologyExpansionUtils().getResources(stringValue);
				for (Resource resource : resources) {
					List<Statement> resourceProperties = expansionTool.getOntologyExpansionUtils().getResourceProperties(resource);
					
					for (Statement stmt : resourceProperties) {
						 Resource  subject   = stmt.getSubject();    
						 Property  predicate = stmt.getPredicate();  
						 RDFNode   object    = stmt.getObject();
						 
						 
						 if(!properties.contains(predicate)){
							 properties.add(predicate);
						 }
						 
						 
					}
					
					
				}
				
				
			}
		
		}
		
		LinkedList<String> attibutes = new LinkedList<String>();
		
		for (Property property : properties) {
			attibutes.add(attrName+Constants.keySeparator+property.toString());
		}
		
		result.add(attibutes);
		
		
		
		for(int i=0;i<trainData.size();i++){
			LinkedList<String> instanceValues = new LinkedList<String>();
			Instance instance = trainData.get(i);
			String stringValue = instance.stringValue(attributeIndex);
			for(int j=0;j<properties.size();j++){
				Property propety = properties.get(j);
				
				if(!stringValue.equals("NaN")){

					String resultString = new String();
					
					List<Resource> resources = expansionTool.getOntologyExpansionUtils().getResources(stringValue);
					boolean added = false;
					for (Resource resource : resources) {
						
						
						StmtIterator listProperties = resource.listProperties();
						
						while(listProperties.hasNext()){
										
							Statement nextStmt = listProperties.next();
							
							if(nextStmt.getPredicate().equals(propety)){
							
								if(nextStmt.getObject() instanceof Resource){
								
									added = true;
									Resource propertyResourceValue = (Resource)nextStmt.getObject();
									
									if(propertyResourceValue!=null){
										resultString = resultString.concat(propertyResourceValue.toString());
										if(!resources.get(resources.size()-1).equals(resource)){
											resultString=resultString.concat(Constants.multiValueSeparator);
										}
									}
								}
								else{
									added = true;
									
									if(!resultString.isEmpty()){
										resultString = resultString.concat(Constants.multiValueSeparator);
									}
									resultString = resultString.concat(getLiteralStringValue(nextStmt.getObject()));
									
								}
							}
						}
						
						
						
					}
					if(!added&&resultString.isEmpty()){
						resultString="NaN";
					}
					
					instanceValues.add(resultString);

				}
				else{
					instanceValues.add("NaN");
				}


			}
			
			result.add(instanceValues);
		
		}
		
		
		for(int i=0;i<result.size();i++){
			LinkedList<String> linkedList = result.get(i);
			for (int j=0;j<linkedList.size();j++) {
				System.out.print(linkedList.get(j));
				if(j==linkedList.size()-1){
					System.out.println();
				}
				else{
					System.out.print(",");
				}
			}
		}
		
		
		

//		
//		trainData = expansionTool.expand(trainData,new Attributes(trainData.getM_Attributes()), null);
		
		//writeTrainData(trainData, fileName+"1");
	}
	
	
	public static String getLiteralStringValue(RDFNode object){
		 Literal literal = object.asLiteral();
		 RDFDatatype datatype = literal.getDatatype();
		 
		 //TODO XML schema types should be also be converted to numeric
		 String value = null;
		 
		 if(datatype.getURI().equals("http://www.w3.org/2001/XMLSchema#double")){
			 
			 value = Double.toString(literal.getDouble());
			 
			 
		 }
		 else if(datatype.getURI().equals("http://www.w3.org/2001/XMLSchema#gYear")){
			 value = literal.toString();
			 
			
		 }
		 else{
			 
			value = literal.toString();
			if(value.contains(",")){
				value = value.replace(",", ".");
			}
			
			if(value.contains(";")){
				value = value.replace(";", ".");
			}
			
			if(value.contains("\n")){
				value = value.replace("\n", " ");
			}
			if(value.contains("\t")){
				value = value.replace("\t", "-");
			}
			
			value = value.replace("\"", "-");
			value = value.replace("\'","-");
		 }
		 
		 return value;
	}
	
	public static void writeTrainData(Instances inst,String fileName) throws IOException{
		CSVSaver saver = new CSVSaver();
		 //ArffSaver saver = new ArffSaver();
		 saver.setInstances(inst);
		 saver.setFile(new File(fileName));
		 saver.writeBatch();
	}
}
