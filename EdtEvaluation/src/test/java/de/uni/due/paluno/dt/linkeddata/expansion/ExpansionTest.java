package de.uni.due.paluno.dt.linkeddata.expansion;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import org.graphstream.graph.Graph;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import de.uni.due.paluno.se.edt.Attributes;
import de.uni.due.paluno.se.edt.Utils;
import weka.classifiers.trees.HoeffdingTree;
import weka.classifiers.trees.LinkedDataExpansionTool;
import weka.classifiers.trees.TestConfiguration;
import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.SelectedTag;
import weka.core.converters.ArffSaver;
import weka.core.converters.CSVLoader;
import weka.core.converters.CSVSaver;

public class ExpansionTest {

	
	@Test
	public void expansionTest() throws IOException{
		
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("Beans2.xml");
		TestConfiguration tc = context.getBean(TestConfiguration.class);
		
		CSVLoader trainDataLoader = new CSVLoader();
		trainDataLoader.setFieldSeparator(",");
		trainDataLoader.setSource(new File(tc.getCsvSource()));
		
		Instances trainData = trainDataLoader.getDataSet();
		trainData.setClassIndex(tc.getClassIndex());
		
		
		
		
		LinkedDataExpansionTool expansionTool = context.getBean(LinkedDataExpansionTool.class);

		
		
		int i=0;
		while(i<9){
//			Graph graph = Utils.graph(instance);
//			graph.display();
			writeTrainData(trainData, i);
			
			Instance instance = trainData.get(0);
			
			Graph graph = Utils.graph(instance);
			graph.display();
			
			trainData = expansionTool.expand(trainData,new Attributes(trainData.getM_Attributes()), null);
			 
			
			i= i+1;
			
			try {
				TimeUnit.SECONDS.sleep(5);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
	
	}
	
	public static void writeTrainData(Instances inst,int i) throws IOException{
		CSVSaver saver = new CSVSaver();
		 //ArffSaver saver = new ArffSaver();
		 saver.setInstances(inst);
		 saver.setFile(new File("newData/tryInst"+i+".csv"));
		 saver.writeBatch();
	}
}
