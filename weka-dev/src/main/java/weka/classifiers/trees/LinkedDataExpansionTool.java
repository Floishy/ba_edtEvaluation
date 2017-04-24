package weka.classifiers.trees;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.tdb.TDBFactory;
import org.apache.jena.util.FileManager;

import org.apache.jena.query.Query;
import org.apache.jena.query.Dataset;

import weka.classifiers.trees.ht.ActiveHNode;
import weka.classifiers.trees.ht.NodeInstances;
import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;
import de.uni.due.paluno.se.edt.Attributes;
import de.uni.due.paluno.se.edt.AttributeSpecification;
import de.uni.due.paluno.se.edt.NotAResourceException;
import de.uni.due.paluno.se.edt.OntologyExpansionUtils;


public class LinkedDataExpansionTool implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2098556338154409367L;
	
	
	ExpansionSchemaHistory expansionSchemaHistory;

	private TestConfiguration testConfiguration;
	
	private Model model;
	
	private OntModel ontModel;

	private OntologyExpansionUtils wekaUtils;
	
	private int numberExpansions = 0;
	
	private HashMap<String, List<String>> resourceExpansionSchema;
	
	
	public LinkedDataExpansionTool() {
		resourceExpansionSchema = new HashMap<String, List<String>>();
		expansionSchemaHistory = new ExpansionSchemaHistory();
	}
	
	
	
	/**
	 * 	Adjust nijk (l) to newly introduced feature
	 *  NH=NH+1
	 * 
	 * @param node
	 * @return
	 */
	public ActiveHNode expandOneBFSHop(ActiveHNode node){
		
		
		NodeInstances node_instances = node.getNode_instances();
		
	
		if(this.expansionSchemaHistory.isEmpty()){
			expansionSchemaHistory.add(node.getAttributes());
		}
		
		Instances instances = expand(node_instances, this.expansionSchemaHistory.get(this.numberExpansions),null);
    	
    	
    	ActiveHNode newNode = new ActiveHNode(instances);
    	
    	for(int i=0;i<instances.numInstances();i++){
    		Instance instance = instances.get(i);
    		try {
				newNode.updateNode(instance);
			} catch (Exception e) {
				e.printStackTrace();
			}
    	}
    	
    	this.expansionSchemaHistory.add(new Attributes(instances.getM_Attributes()));
    	this.numberExpansions = this.numberExpansions+1;
    	
    	
    	
    	//System.out.println("New Node Instances");

    	//System.out.println(newNode.getNode_instances());
    	
    	//System.out.println("-------------------");
    	
    	
    	return newNode;
		
		
		
	}
	
	/**
	 * Expanding single instance required for the case when new instance is passed to the tree
	 * 
	 * @param inst
	 * @return
	 */
	public Instance expand(Instance inst) {
		
		for(int i=0;i<this.numberExpansions;i++){
			Set<Instance> instancesSet = new HashSet<Instance>();
			instancesSet.add(inst);
		  	
			Attributes currentSchema = this.expansionSchemaHistory.get(i);
			Attributes nextSchema = this.expansionSchemaHistory.get(i+1);

		  	Instances expand = this.expand(instancesSet,currentSchema,nextSchema);
		  	
		  	inst = expand.firstInstance();
		}
		
	  	
	  	return inst; 
	}
	
	/**
	 * Expand a set of instances - required for the situation when multiple instances e.g. collected in the leaf node must be expanded 
	 * 
	 * @param instancesSet
	 * @param attributes
	 * @param attributesSchema
	 * @return
	 */
	public Instances expand(Collection<Instance> instancesSet, Attributes attributes, Attributes attributesSchema){
		Instances instances = new Instances("dummyName",attributes, 0);
      	instances.setClassIndex(testConfiguration.getClassIndex());
      	
		
    	for (Instance instance : instancesSet) {
    		instances.add(instance);
		}
    
    	List<AttributeSpecification> newAttributesSet = new LinkedList<AttributeSpecification>();
    	
    	for(int i=0;i<instances.numAttributes();i++){
    		try {
    			//get new attributes for the specific attribute
    			AttributeSpecification newAttributes = getOntologyExpansionUtils().getNewAttributes(instances,i, getAttributeNames(attributesSchema));
    			newAttributesSet.add(newAttributes);
    		} catch (NotAResourceException e) {
    			continue;
    		}	
    	}
    	
    	System.out.println(newAttributesSet.size());
    	
    	for (AttributeSpecification newAttributes : newAttributesSet) {
    		
    		System.out.println(newAttributes.toString());
		}
    	
    	
    	for (AttributeSpecification newAttributes : newAttributesSet) {
    		
    		instances = getOntologyExpansionUtils().addNewAttributes(instances, newAttributes);
		}
    	
    	//expansionSchemaHistory.add(new Attributes(instances.getM_Attributes()));
    	
//    	int i=0;
//    	for (Attributes attr : expansionSchemaHistory) {
//    		System.out.println(i+"\n");
//			System.out.println(attr);
//			i=i+1;
//		}
    
    	return instances;
	}
	
	public List<String> getAttributeNames( ArrayList<Attribute> currentAttributesSchema){
		//TODO YTE Refactor thats not a nice thing
		if(currentAttributesSchema==null){
			return null;
		}
		
		LinkedList<String> result = new LinkedList<String>();
		for (Attribute attribute : currentAttributesSchema) {
			result.add(attribute.name());
		}
		return result;
	}
	
	public TestConfiguration getTestConfiguration() {
		return testConfiguration;
	}
	
	public void setTestConfiguration(TestConfiguration testConfiguration) {
		this.testConfiguration = testConfiguration;
	}
	
	public Model getModel(){
		if(model==null){
//			if(testConfiguration.getLinkedData().contains("DBPedia")){
//				System.out.println("WE start the code!!");
//				Dataset dataset = TDBFactory.createDataset("F:\\DBPedia_Data\\Output");
//
//				// assume we want the default model, or we could get a named model here
//				Model tdb = dataset.getDefaultModel();
//
//				// read the input file - only needs to be done once
//
//				File folder = new File(testConfiguration.getLinkedData());
//				if (folder.listFiles().length > 0)
//					System.out.println("We are at the correct file");
//				for (final File fileEntry : folder.listFiles()) {
//
//					System.out.println(fileEntry.getName());
//					if (fileEntry.getName().contains(".bz2"))
//						continue;
//					System.out.println(fileEntry.getAbsolutePath());
//					System.out.println(fileEntry.getPath());
//					try {
//						FileManager.get().readModel(tdb, fileEntry.getPath(),
//								"N-TRIPLES");
//					} catch (Exception e) {
//						System.out.println("File didn't finish");
//						e.printStackTrace();
//					}
//				}
//				dataset.close();
//			}
//			else{
				model = TDBFactory.createDataset().getDefaultModel();
				FileManager.get().readModel(model,testConfiguration.getLinkedData() ,"RDF/XML");
//			}
		}
		return model;
	}
	
	public OntModel getOntModel(){
		if(ontModel==null){
			ontModel= ModelFactory.createOntologyModel( OntModelSpec.OWL_MEM, null );
			FileManager.get().readModel( ontModel, testConfiguration.getOntModel() );
		}
		return ontModel;
	}
	
	public OntologyExpansionUtils getOntologyExpansionUtils(){
		if(this.wekaUtils==null){
			this.wekaUtils = new OntologyExpansionUtils(getModel(),getOntModel());
		}
		return wekaUtils;
	}
	
	public int getNumberExpansions() {
		return numberExpansions;
	}

	


	public Attributes getCurrentSchema() {
		return this.expansionSchemaHistory.get(expansionSchemaHistory.size()-1);
	}

	
	public void printSchemeHistory(){
		for (int i=0;i<expansionSchemaHistory.size();i++) {
			Attributes attributes = expansionSchemaHistory.get(i);
			System.out.println(attributes);
		}
	}



}


