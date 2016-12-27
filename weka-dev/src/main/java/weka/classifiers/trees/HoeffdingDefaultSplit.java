package weka.classifiers.trees;

import java.util.Collections;
import java.util.List;

import weka.classifiers.trees.ht.ActiveHNode;
import weka.classifiers.trees.ht.SplitCandidate;
import weka.classifiers.trees.ht.SplitNode;
import weka.core.Attribute;

public class HoeffdingDefaultSplit implements ISplitExpand{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2452516696060705449L;
	private HoeffdingTree hoeffdingTree;

	public HoeffdingDefaultSplit() {
		
	}
	
	@Override
	public void trySplit(ActiveHNode node, SplitNode parent, String parentBranch)
			throws Exception {

	    // non-pure?
	    if (node.numEntriesInClassDistribution() > 1) {
	      List<SplitCandidate> bestSplits = node.getPossibleSplits(getHoeffdingTree().m_splitMetric);
	      Collections.sort(bestSplits);

	      boolean doSplit = false;
	      if (bestSplits.size() < 2) {
	        doSplit = bestSplits.size() > 0;
	      } else {
	        // compute the Hoeffding bound
	        double metricMax = getHoeffdingTree().m_splitMetric.getMetricRange(node.m_classDistribution);
	        double hoeffdingBound = getHoeffdingTree().computeHoeffdingBound(metricMax,
	            getHoeffdingTree().m_splitConfidence, node.totalWeight());

	        SplitCandidate best = bestSplits.get(bestSplits.size() - 1);
	        SplitCandidate secondBest = bestSplits.get(bestSplits.size() - 2);

	        if (best.m_splitMerit - secondBest.m_splitMerit > hoeffdingBound
	            || hoeffdingBound < getHoeffdingTree().m_hoeffdingTieThreshold) {
	          doSplit = true;
	        }
	        
	        // TODO - remove poor attributes stuff?
	      }

	      if (doSplit) {
	        SplitCandidate best = bestSplits.get(bestSplits.size() - 1);

	        if (best.m_splitTest == null) {
	          // preprune
	          getHoeffdingTree().deactivateNode(node, parent, parentBranch);
	        } else {
	          SplitNode newSplit = new SplitNode(node.m_classDistribution,
	              best.m_splitTest);

	          for (int i = 0; i < best.numSplits(); i++) {
	            ActiveHNode newChild = getHoeffdingTree().newLearningNode();
	            newChild.m_classDistribution = best.m_postSplitClassDistributions
	                .get(i);
	            newChild.m_weightSeenAtLastSplitEval = newChild.totalWeight();
	            String branchName = "";
	            if (getHoeffdingTree().m_header.attribute(best.m_splitTest.splitAttributes().get(0))
	                .isNumeric()) {
	              branchName = i == 0 ? "left" : "right";
	            } else {
	              Attribute splitAtt = getHoeffdingTree().m_header.attribute(best.m_splitTest
	                  .splitAttributes().get(0));
	              branchName = splitAtt.value(i);
	            }
	            newSplit.setChild(branchName, newChild);
	          }

	          getHoeffdingTree().m_activeLeafCount--;
	          getHoeffdingTree().m_decisionNodeCount++;
	          getHoeffdingTree().m_activeLeafCount += best.numSplits();

	          if (parent == null) {
	        	  getHoeffdingTree().m_root = newSplit;
	          } else {
	            parent.setChild(parentBranch, newSplit);
	          }
	        }
	      }
	    }
		
	}

	@Override
	public HoeffdingTree getHoeffdingTree() {
		return hoeffdingTree;
	}

	public void setHoeffdingTree(HoeffdingTree hoeffdingTree) {
		this.hoeffdingTree = hoeffdingTree;
	}
}
