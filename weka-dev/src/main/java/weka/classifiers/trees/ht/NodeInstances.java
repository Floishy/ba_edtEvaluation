package weka.classifiers.trees.ht;

import java.util.HashSet;

import weka.core.Instance;

public class NodeInstances extends HashSet<Instance>{

	/**
	 * 
	 */
	private static final long serialVersionUID = -9064634067336277747L;

	
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		
		for (Instance instance : this) {
			sb.append(instance.toString()+"\n");
		}
		return sb.toString();
	}
}
