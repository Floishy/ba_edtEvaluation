package de.uni.due.paluno.se.edt;

import java.io.OutputStream;
import java.io.PrintStream;

import org.apache.jena.ontology.OntTools;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;

public class SparqlUtils {

	
	public static void iterateModel(String uri,PrintStream out){
		Model model = ModelFactory.createDefaultModel().read(uri);
        //model.write( System.out );
		
        System.out.println("---------------------------");
        
        iterateModel(model,out);
      
	}
	
	

	
	
	public static void iterateModel(Model model,PrintStream out){
		//list the statements in the Model
        StmtIterator iter = model.listStatements();

        
        
        // print out the predicate, subject and object of each statement
        while (iter.hasNext()) {
            Statement stmt      = iter.nextStatement();  // get next statement
            Resource  subject   = stmt.getSubject();     // get the subject
            Property  predicate = stmt.getPredicate();   // get the predicate
            RDFNode   object    = stmt.getObject();      // get the object

            out.print(subject.toString());
            out.print(" " + predicate.toString() + " ");
           
            
            if (object instanceof Resource) {
               out.print(object.toString());
            } else {
                // object is a literal
                out.print(" \"" + object.toString() + "\"");
            }

            out.println(" .");
        } 
	}
	
	
	public void printSubclassHierarchy(Model model, OutputStream out){
		
	}
}
