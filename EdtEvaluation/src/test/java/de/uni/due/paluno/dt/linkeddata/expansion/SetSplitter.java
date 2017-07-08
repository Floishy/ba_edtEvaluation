package de.uni.due.paluno.dt.linkeddata.expansion;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.CSVLoader;
import weka.core.converters.CSVSaver;

/**
 * @author Florian
 *
 *Class for testing the split of a given data set into test and training sets
 */
public class SetSplitter {


	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
	
		Instances all1 = loadCSV("F:\\DBPedia_Data\\AAUP\\3HopAAUP"+".csv", ",", 18);
		Instances all2 = loadCSV("F:\\DBPedia_Data\\AAUP\\3HopAAUP"+".csv", ",", 18);
		Instances training = loadCSV("F:\\DBPedia_Data\\AAUP\\TrainingSet(salary)"+".csv", "\t", 18);
		Instances test = loadCSV("F:\\DBPedia_Data\\AAUP\\TestSet(salry)"+".csv", "\t", 18);
		
		ArrayList<Integer> list = new ArrayList<Integer>();
		int counter = 0;
		
		
		for(Instance train : training){
			train.stringValue(1).replaceAll("§", "'");
		}
		
		
		for(Instance current : all1){
			for(Instance inner : training){
				if ((current.value(1)) == (inner.value(1))){
					list.add(counter);
					break;
				}
			}
			counter++;
		}
		
		
		for (int i = counter;i>0;i--){
			if (list.contains(i)){
				all1.remove(i);
			}
		}

		
		counter = 0;
		list.clear();
		
		for(Instance current : all2){
			for(Instance inner : test){
				if ((current.value(1)) == (inner.value(1))){
					list.add(counter);
					break;
				}
			}
			counter++;
		}
		
		for (int i = counter;i>0;i--){
			if (list.contains(i)){
				all2.remove(i);
			}
		}

		
		saveInstanceToCSV(all1, "F:\\DBPedia_Data\\AAUP\\",3+"HopAAUPTest");
		saveInstanceToCSV(all2, "F:\\DBPedia_Data\\AAUP\\",3+"HopAAUPTraining");
	}

	public static Instances loadCSV(String csvPath, String separator, int classIndex) throws Exception	
	{
		CSVLoader trainDataLoader = new CSVLoader();
  		trainDataLoader.setFieldSeparator(separator);
  		trainDataLoader.setSource(new File(csvPath));

  		Instances trainData = trainDataLoader.getDataSet();
  		trainData.setClassIndex(classIndex);
  	
  		System.out.println("Loaded");
  		return trainData;
	}
	
	public static void saveInstanceToCSV(Instances inst,String pathPrefix,String filename) throws IOException{
		CSVSaver saver = new CSVSaver();
		 //ArffSaver saver = new ArffSaver();
		 saver.setInstances(inst);
		 saver.setFile(new File(pathPrefix + filename +".csv"));
		 saver.writeBatch();
	}
}
