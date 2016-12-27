package de.uni.due.paluno.se.edt.ontology;

import java.util.function.Predicate;

import org.apache.jena.ontology.OntTools;
import org.apache.jena.ontology.OntTools.Path;
import org.apache.jena.query.Dataset;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.tdb.TDBFactory;
import org.apache.jena.util.FileManager;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.VCARD;
import org.junit.Test;

import de.uni.due.paluno.se.edt.SparqlUtils;

public class LocalDBPedia {

	
	
	/** The Constant tdbDirectory. */
    public static final String tdbDirectory = "C:\\Evaluation\\DBPedia_Dump\\JenaModel\\"; 

    /** The Constant dbdump0. */
    public static final String dbdump0 = "C:\\Evaluation\\DBPedia_Dump\\Datasets\\dbpedia_2015-04.owl";

    /** The Constant dbdump1. */
    public static final String dbdump1 = "C:\\Evaluation\\DBPedia_Dump\\Datasets\\instance-types_en.nt";
    
    public static final String dbdump2 = "C:\\Evaluation\\DBPedia_Dump\\Datasets\\mappingbased-properties_en.nt";
    
    
    public static final String gesisMovies = "C:\\Evaluation\\DBPedia_Dump\\Datasets\\GesisMovies.rdf";
	
	//@Test
	public void indexDump(){
		 Model tdbModel = TDBFactory.createDataset(tdbDirectory).getDefaultModel();
         /*Incrementally read data to the Model, once per run , RAM > 6 GB*/
         FileManager.get().readModel( tdbModel, dbdump0);
         FileManager.get().readModel(tdbModel, gesisMovies,"RDF/XML");
         FileManager.get().readModel( tdbModel, dbdump1, "N-TRIPLES");
         FileManager.get().readModel( tdbModel, dbdump2, "N-TRIPLES");
       
         tdbModel.close();
	}
	
	
	@Test
	public void readModel(){
		String queryString ="PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"+ 
				"  CONSTRUCT {?concept} WHERE {?concept rdf:id <http://dbpedia.org/resource/Killer_of_Sheep>}";

		Dataset dataset = TDBFactory.createDataset(tdbDirectory);
		          Model tdb = dataset.getDefaultModel();
		          Query query = QueryFactory.create(queryString);
		          
		          
		          QueryExecution qexec = QueryExecutionFactory.create(query, tdb);
		          
		          Model execConstruct = qexec.execConstruct();
		          
		          System.out.println(execConstruct);
		          
		          
		          //XXX list certain propertie
//		          StmtIterator listProperties2 = ratatouille.listProperties(RDF.type);
//		        
//		          while(listProperties2.hasNext()){
//		        	  Statement stmt = listProperties2.next();
//		        	  
//		        	  System.out.print(stmt.getSubject());
//		        	  System.out.print("  "+stmt.getPredicate());
//		        	  System.out.print("  "+stmt.getObject());
//		        	  
//		        	  
//		        	  System.out.println();
//		          }
//		          
//		          System.out.println("*************************");
//		          
//		          StmtIterator listProperties = ratatouille.listProperties();
//		          
//		          //XXX Idea look for shared properties in a certain diameter
//		          
//		          while(listProperties.hasNext()){
//		        	  Statement stmt = listProperties.next();
//		        	  
//		        	  System.out.print(stmt.getSubject());
//		        	  System.out.print("  "+stmt.getPredicate());
//		        	  System.out.print("  "+stmt.getObject());
//		        	  
//		        	  
//		        	  System.out.println();
//		          }
//		        
		          
		          //OntTools.getLCA(tdb, ratatouille, killerofsheep);
		          
		          //QueryExecution qexec = QueryExecutionFactory.create(query, tdb);
		          
		          
////		          Execute the Query
//		          Path shortestPath= null;
//		            ResultSet results = qexec.execSelect();
//		            while (results.hasNext()) {
//		            	
//		            	QuerySolution querySolution = results.next();
//		            	RDFNode rdfNode = querySolution.get("concept");
//		            	
//		            	 shortestPath= OntTools.findShortestPath(tdb, resource, rdfNode,new Predicate<Statement>() {
//							
//							public boolean test(Statement arg0) {
//								return true;
//							}
//						} );
//		            	
//		            	System.out.println(rdfNode);
//		            }
//		            
//		            System.out.println(shortestPath);
//		            
		            //qexec.close();
		            tdb.close() ;
		
	}
}
