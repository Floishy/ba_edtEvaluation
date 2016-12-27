package de.uni.due.paluno.dt.linkeddata;

import java.io.File;
import java.util.Random;

import org.junit.Test;

import weka.classifiers.Evaluation;
import weka.classifiers.trees.J48;
import weka.core.Instances;
import weka.core.converters.CSVLoader;

public class J48Evaluation {

	
	@Test
	public void test() throws Exception{
		
		CSVLoader trainDataLoader = new CSVLoader();
		trainDataLoader.setFieldSeparator(",");

		//ArffLoader trainDataLoader = new ArffLoader();


		trainDataLoader.setSource(new File("C:\\Users\\Yordan Terziev\\Desktop\\dataBeforeExpansion.csv"));

		Instances trainData = trainDataLoader.getDataSet();
		trainData.setClassIndex(3);
		
		
		
		
		
//		Random rand1 = new Random();   // create seeded number generator
//		trainData.randomize(rand1);
		
		
		
		J48 j48 = new J48();
		j48.setUnpruned(true);
		j48.setCollapseTree(false);
		j48.setMinNumObj(2);
		j48.buildClassifier(trainData);
		
		
		System.out.println(j48);
		
//		Evaluation evaluation = new Evaluation(trainData);
//		Random rand = new Random(trainData.size()); 
//		int folds = trainData.size();
//		
//		evaluation.crossValidateModel(j48, trainData, folds, rand);
//		
//		System.out.println("------");
//		System.out.println(evaluation.toSummaryString());
//		
//		System.out.println("------");
//		System.out.println(evaluation.toClassDetailsString());
//		System.out.println("------");
//		System.out.println(evaluation.errorRate());
		
	}
}
