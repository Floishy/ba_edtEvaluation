package de.uni.due.paluno.dt.linkeddata.expansion;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import org.graphstream.graph.Graph;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import de.uni.due.paluno.se.edt.Attributes;
import de.uni.due.paluno.se.edt.Utils;
import weka.classifiers.trees.LinkedDataExpansionTool;
import weka.classifiers.trees.TestConfiguration;
import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.CSVLoader;
import weka.core.converters.CSVSaver;

/**
 * The class for expanding a given data set.
 * It takes a given xml configuration file and generates four hops of the defined data set
 * @author Florian
 *
 */
public class Expanding {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		// Loading the Configuration file, one for each data set
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("BeansAAUPWiki.xml");
		TestConfiguration tc = context.getBean(TestConfiguration.class);
		System.out.println(tc.getCsvSource());
		CSVLoader trainDataLoader = new CSVLoader();
		// Seperator has to be adjusted eihter , or Tab
		trainDataLoader.setFieldSeparator("\t");
		trainDataLoader.setSource(new File(tc.getCsvSource()));
		
		Instances trainData = trainDataLoader.getDataSet();
		trainData.setClassIndex(tc.getClassIndex());
		
		
		
		
		LinkedDataExpansionTool expansionTool = context.getBean(LinkedDataExpansionTool.class);

		// Expanding the data set 4 times
		
		int i=0;
		while(i<5){
//			Graph graph = Utils.graph(instance);
//			graph.display();

			
//			Instance instance = trainData.get(0);
			
//			Graph graph = Utils.graph(instance);
//			graph.display();
			
//			int count = graph.getAttributeCount();
			ArrayList<Attribute> test = trainData.getM_Attributes();
			
			for(Attribute att : test){
				System.out.println(att.name());
			}

			trainData = expansionTool.expand(trainData,new Attributes(trainData.getM_Attributes()), null);
			 
			
			i= i+1;
			writeTrainData(trainData, i);
			try {
				TimeUnit.SECONDS.sleep(5);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
	
	}
	
	// Output of newly generated data sets, Path has to be adjusted
	
	public static void writeTrainData(Instances inst,int i) throws IOException{
		CSVSaver saver = new CSVSaver();
		 //ArffSaver saver = new ArffSaver();
		 saver.setInstances(inst);
		 saver.setFile(new File("F:\\Wikidata\\AAUP\\AAUPWiki0"+i+".csv"));
		 saver.setFieldSeparator("\t");
		 saver.writeBatch();
	}
}


