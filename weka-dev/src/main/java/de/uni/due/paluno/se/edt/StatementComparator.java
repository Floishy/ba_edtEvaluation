package de.uni.due.paluno.se.edt;

import java.util.Comparator;

import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Statement;

public class StatementComparator implements Comparator<Statement> {

	@Override
	public int compare(Statement s1, Statement s2) {
		Property predicate1 = s1.getPredicate();
		Property predicate2 = s2.getPredicate();
		
		throw new RuntimeException(); 
	
		//return WekaUtils.getPropertyName(predicate1).compareTo(WekaUtils.getPropertyName(predicate2));
		
	}

	
	
}
