/*
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

/*
 *    ActiveHNode.java
 *    Copyright (C) 2013 University of Waikato, Hamilton, New Zealand
 *
 */

package weka.classifiers.trees.ht;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.uni.due.paluno.se.edt.Attributes;
import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;

/**
 * Node that is "active" (i.e. growth can occur) in a Hoeffding tree
 * 
 * @author Richard Kirkby (rkirkby@cs.waikato.ac.nz)
 * @author Mark Hall (mhall{[at]}pentaho{[dot]}com)
 * @version $Revision$
 */
public class ActiveHNode extends LeafNode implements LearningNode, Serializable {

	
   	
  /**
   * For serialization
   */
  private static final long serialVersionUID = 3284585939739561683L;
  
  private NodeInstances node_instances = new NodeInstances();

  /** The weight of instances seen at the last split evaluation */
  public double m_weightSeenAtLastSplitEval = 0;

  /** Statistics for nominal or numeric attributes conditioned on the class */
  protected Map<String, ConditionalSufficientStats> m_nodeStats = new HashMap<String, ConditionalSufficientStats>();

private Instances instances;
  
  public ActiveHNode() {
	// TODO Auto-generated constructor stub
  }
  


  public ActiveHNode(Instances instances) {
	this.instances = instances;
	// TODO Auto-generated constructor stub
  }
  
  public Instances getInstances() {
	return instances;
}

@Override
  public void updateNode(Instance inst) throws Exception {
	
	node_instances.add(inst);
	  
    super.updateDistribution(inst);

    for (int i = 0; i < inst.numAttributes(); i++) {
      Attribute a = inst.attribute(i);
      
      if (i != inst.classIndex()) {
        ConditionalSufficientStats stats = m_nodeStats.get(a.name());
        if (stats == null) {
          if (a.isNumeric()) {
            stats = new GaussianConditionalSufficientStats();
          } else {
            stats = new NominalConditionalSufficientStats();
          }
          m_nodeStats.put(a.name(), stats);
        }
       
        stats
            .update(inst.value(a),
                inst.classAttribute().value((int) inst.classValue()),
                inst.weight());
      }
    }
  }
  
  public Attributes getAttributes(){
	  Attributes result = new Attributes();
	  Instance inst = this.node_instances.iterator().next();
	  for (int i = 0; i < inst.numAttributes(); i++) {
	      Attribute a = inst.attribute(i);
	      result.add(a);
	  }
	  return result;
  }
  
  public NodeInstances getNode_instances() {
	return node_instances;
}

  /**
   * Returns a list of split candidates
   * 
   * @param splitMetric the splitting metric to use
   * @return a list of split candidates
   */
  public List<SplitCandidate> getPossibleSplits(SplitMetric splitMetric) {

    List<SplitCandidate> splits = new ArrayList<SplitCandidate>();

    // null split
    List<Map<String, WeightMass>> nullDist = new ArrayList<Map<String, WeightMass>>();
    nullDist.add(m_classDistribution);
    
    SplitMetricResult result = splitMetric.evaluateSplit(m_classDistribution, nullDist);
    double merit = result.getResult();
    
    SplitCandidate nullSplit = new SplitCandidate(null, nullDist,
        merit,result);
    splits.add(nullSplit);

    for (Map.Entry<String, ConditionalSufficientStats> e : m_nodeStats
        .entrySet()) {
      ConditionalSufficientStats stat = e.getValue();

      SplitCandidate splitCandidate = stat.bestSplit(splitMetric,
          m_classDistribution, e.getKey());

      if (splitCandidate != null) {
        splits.add(splitCandidate);
      }
    }

    return splits;
  }
  
  @Override
  public String toString() {
	StringBuilder sb = new StringBuilder();
	Set<String> keySet = m_nodeStats.keySet();
	for (String attrName : keySet) {
		sb.append(attrName+";");
	}
	return sb.toString();
	  
  }
}
