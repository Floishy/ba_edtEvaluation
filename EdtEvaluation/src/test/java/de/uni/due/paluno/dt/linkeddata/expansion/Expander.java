package de.uni.due.paluno.dt.linkeddata.expansion;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.graphstream.graph.Graph;

import de.uni.due.paluno.se.edt.Attributes;
import de.uni.due.paluno.se.edt.Utils;
import weka.classifiers.trees.LinkedDataExpansionTool;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.CSVSaver;

public class Expander{
	
	Instances trainData;
	LinkedDataExpansionTool expansionTool;
	
	public Expander(Instances trainData, LinkedDataExpansionTool expansionTool){
		this.trainData = trainData;
		this.expansionTool = expansionTool;
	}
	
	public void expanding(int hops) throws IOException{
		int i=0;
		while(i<=hops){
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
		 saver.setFile(new File("newData/inst"+i+".csv"));
		 saver.writeBatch();
	}
	
	
	
}