package de.uni.due.paluno.dt.linkeddata.expansion;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.graphstream.graph.Graph;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import de.uni.due.paluno.se.edt.Attributes;
import de.uni.due.paluno.se.edt.Utils;
import weka.classifiers.trees.LinkedDataExpansionTool;
import weka.classifiers.trees.TestConfiguration;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.CSVLoader;
import weka.core.converters.CSVSaver;

public class TraningDataExpansion {
		

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("Beans.xml");
		TestConfiguration tc = context.getBean(TestConfiguration.class);
		
		CSVLoader trainDataLoader = new CSVLoader();
		trainDataLoader.setFieldSeparator(",");
		trainDataLoader.setSource(new File(tc.getCsvSource()));
		
		Instances trainData = trainDataLoader.getDataSet();
		trainData.setClassIndex(tc.getClassIndex());
		
		
		
		
		LinkedDataExpansionTool expansionTool = context.getBean(LinkedDataExpansionTool.class);

		
		Expander expander = new Expander(trainData,expansionTool);
		
		expander.expanding(8);

		
		MultiValueFormatter formatter = new MultiValueFormatter();
		
		Instances res = formatter.loadCSV("F:\\Workspace_Bachelor_Arbeit\\maven.1482858439171\\EdtEvaluation\\data\\Inst2Both.csv", ",", 2);
		
		
		//2. format the loaded data with binary sets
		res = formatter.formatWithBinarySets(res, ";");
		
		System.out.println(res);
		
		formatter.saveInstanceToCSV(res);
		
	
	}
	
	
	
}
		

