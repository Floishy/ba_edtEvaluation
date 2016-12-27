package de.uni.due.paluno.dt.linkeddata;

import java.io.File;
import java.util.Random;

import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import weka.classifiers.Evaluation;
import weka.classifiers.trees.HoeffdingTree;
import weka.classifiers.trees.TestConfiguration;
import weka.core.Instances;
import weka.core.SelectedTag;
import weka.core.converters.CSVLoader;

public class AifbEvaluationTest {

	

	
	
	@Test
	public void test() throws Exception{
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("Beans.xml");
		
		
		TestConfiguration tc = context.getBean(TestConfiguration.class);
		
		
		CSVLoader trainDataLoader = new CSVLoader();
		trainDataLoader.setFieldSeparator(",");

		//ArffLoader trainDataLoader = new ArffLoader();


		trainDataLoader.setSource(new File(tc.getCsvSource()));

		Instances trainData = trainDataLoader.getDataSet();
		trainData.setClassIndex(tc.getClassIndex());
				
		
		System.out.println(trainData);
		
		
		Random rand1 = new Random();   // create seeded number generator
		trainData.randomize(rand1);
		
		
		HoeffdingTree ht = context.getBean(HoeffdingTree.class);
		SelectedTag st = new SelectedTag(0, HoeffdingTree.TAGS_SELECTION2);
		ht.setLeafPredictionStrategy(st);
					
		
		Evaluation evaluation = new Evaluation(trainData);
		Random rand = new Random(trainData.size()); 
		evaluation.crossValidateModel(ht, trainData, trainData.size(), rand);
		
		System.out.println(evaluation.toClassDetailsString());
		
		context.close();
	}
}
