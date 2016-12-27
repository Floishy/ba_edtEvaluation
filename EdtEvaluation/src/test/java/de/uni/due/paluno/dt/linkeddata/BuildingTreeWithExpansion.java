package de.uni.due.paluno.dt.linkeddata;

import java.io.File;

import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import weka.classifiers.trees.HoeffdingTree;
import weka.classifiers.trees.TestConfiguration;
import weka.core.Instances;
import weka.core.SelectedTag;
import weka.core.converters.CSVLoader;

public class BuildingTreeWithExpansion {

	
	
	@Test
	public void buildTree() throws Exception{
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("Beans1.xml");
		
		TestConfiguration tc = context.getBean(TestConfiguration.class);
		
		CSVLoader trainDataLoader = new CSVLoader();
		trainDataLoader.setFieldSeparator(",");

		trainDataLoader.setSource(new File(tc.getCsvSource()));

		Instances trainData = trainDataLoader.getDataSet();
		trainData.setClassIndex(tc.getClassIndex());
				
		
		HoeffdingTree ht = context.getBean(HoeffdingTree.class);
		SelectedTag st = new SelectedTag(0, HoeffdingTree.TAGS_SELECTION2);
		ht.setLeafPredictionStrategy(st);
		
		
		//ht.getExpansionTool().get
		
		ht.buildClassifier(trainData);
		
		System.out.println(ht.toString());
		
		context.close();
	}
}
