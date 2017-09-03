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
 * 
 *
 */
public class Expanding {

	public static void main(String[] args) throws IOException {
		
		// Number of hops for the bfs
		int hops = 4;
		
		//Setting the Configuration
		String beans = "BeansAAUPWiki.xml";
		String separator = "\t";
		
		Instances trainData = loadTrainData(beans,separator);	
		LinkedDataExpansionTool expansionTool = getExpansionTool(beans);
		
		//Setting the output of the expansion
		
		String path = "F:\\";
		String fileName = "source";
		
		
		int i=0;
		while(i<=hops){
			
			trainData = expansionTool.expand(trainData,new Attributes(trainData.getM_Attributes()), null);
			i= i+1;
			writeTrainData(trainData, path, fileName, i);
		}
		
	
	}
		
	public static void writeTrainData(Instances inst, String path , String file, int i) throws IOException{
		CSVSaver saver = new CSVSaver();
		 //ArffSaver saver = new ArffSaver();
		 saver.setInstances(inst);
		 saver.setFile(new File(path+file+i +".csv"));
		 saver.setFieldSeparator("\t");
		 saver.writeBatch();
	}
	
	public static Instances loadTrainData(String beans, String separator) throws IOException{
		// Loading the Configuration file, one for each data set
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(beans);
		TestConfiguration tc = context.getBean(TestConfiguration.class);
		System.out.println(tc.getCsvSource());
		CSVLoader trainDataLoader = new CSVLoader();
		
		// Seperator has to be adjusted eihter , or Tab
		trainDataLoader.setFieldSeparator(separator);
		trainDataLoader.setSource(new File(tc.getCsvSource()));
		
		Instances trainData = trainDataLoader.getDataSet();
		trainData.setClassIndex(tc.getClassIndex());
		return trainData;
	}
	
	public static LinkedDataExpansionTool getExpansionTool(String beans){
		
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(beans);
		
		return context.getBean(LinkedDataExpansionTool.class);
	}
}


