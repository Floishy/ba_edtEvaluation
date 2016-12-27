package de.uni.due.paluno.graph;
import java.io.File;
import java.io.IOException;
import java.io.StringBufferInputStream;

import weka.core.Instances;
import weka.core.converters.CSVLoader;

import org.graphstream.graph.Graph;
import org.junit.Test;

import de.uni.due.paluno.se.edt.Utils;


public class GraphTest {

	//@Test
	public void testSimpleInstance() throws IOException{
			

		CSVLoader trainDataLoader = new CSVLoader();
		trainDataLoader.setFieldSeparator(",");
		trainDataLoader.setSource(new StringBufferInputStream("name,resource,affiliation \n dan,http://dan,unidue"));
		
		Instances trainData = trainDataLoader.getDataSet();
		
		Graph graph = Utils.graph(trainData.get(0));
		graph.display();
		
		System.out.println("test");
		//trainData.setClassIndex(tc.getClassIndex());
		
	}
	
	@Test
	public void testInstanceWithCycles() throws IOException{
		

		CSVLoader trainDataLoader = new CSVLoader();
		trainDataLoader.setFieldSeparator(",");
		trainDataLoader.setSource(new StringBufferInputStream("name,"
				+ "resource,affiliation,"
				+ "resource:::http://swrc.ontoware.org/ontology#worksAtProject,"
				+ "resource:::http://swrc.ontoware.org/ontology#worksAtProject:::executingOranization"
				+ "\n dan,http://dan,http://unidue.de,sim4bgm,http://unidue.de"));
		
		Instances trainData = trainDataLoader.getDataSet();
		
		Graph graph = Utils.graph(trainData.get(0));
		graph.display();
		
		System.out.println("test");
		//trainData.setClassIndex(tc.getClassIndex());
		
	}
	
}
