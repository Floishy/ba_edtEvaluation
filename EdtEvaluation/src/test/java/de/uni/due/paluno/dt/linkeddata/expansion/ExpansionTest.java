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
		
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("Beans4.xml");
		TestConfiguration tc = context.getBean(TestConfiguration.class);
		
		CSVLoader trainDataLoader = new CSVLoader();
		trainDataLoader.setFieldSeparator(",");
		trainDataLoader.setSource(new File(tc.getCsvSource()));
		
		Instances trainData = trainDataLoader.getDataSet();
		trainData.setClassIndex(tc.getClassIndex());
		
		
		
		
		LinkedDataExpansionTool expansionTool = context.getBean(LinkedDataExpansionTool.class);

		
		
		int i=0;
		while(i<1){
//			Graph graph = Utils.graph(instance);
//			graph.display();

			
			Instance instance = trainData.get(0);
			
			Graph graph = Utils.graph(instance);
			graph.display();
			
			int count = graph.getAttributeCount();
			ArrayList<Attribute> test = trainData.getM_Attributes();
			
			for(Attribute att : test){
				System.out.println(att.name());
			}
			
			System.out.println(trainData.numAttributes());
			System.out.println(trainData.getM_Attributes().size());
			System.out.println(trainData.instance(0).attribute(132).value(0));
			System.out.println(trainData.instance(0).attribute(132).name());
			System.out.println(trainData.instance(0).numValues());
			for(Attribute att : trainData.getM_Attributes()){
				System.out.println(att.type());
			}
			
//			for(int n = 0; n<trainData.numInstances();n++){
//				Instance inst = trainData.get(n);
//				for(int m = 0;m<inst.numAttributes();m++){
//					Attribute att = inst.attribute(m);
//				}
//			}

//			trainData = expansionTool.expand(trainData,new Attributes(trainData.getM_Attributes()), null);
			 
			
			i= i+1;
//			writeTrainData(trainData, i);
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
		 saver.setFile(new File("newData/NeuerTest4HopsAllInst"+i+".csv"));
		 saver.writeBatch();
	}
}
