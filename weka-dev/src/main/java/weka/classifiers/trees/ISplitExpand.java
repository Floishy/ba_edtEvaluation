package weka.classifiers.trees;

import java.io.Serializable;

import weka.classifiers.trees.ht.ActiveHNode;
import weka.classifiers.trees.ht.SplitNode;

public interface ISplitExpand extends Serializable{

	 public void trySplit(ActiveHNode node, SplitNode parent, String parentBranch) throws Exception;
	 
	 
	 public HoeffdingTree getHoeffdingTree();
	 
	 public void setHoeffdingTree(HoeffdingTree hoeffdingTree);
	
}
