package weka.classifiers.trees;

import java.util.Collections;
import java.util.List;

import weka.classifiers.trees.ht.ActiveHNode;
import weka.classifiers.trees.ht.SplitCandidate;
import weka.classifiers.trees.ht.SplitNode;
import weka.core.Attribute;
import weka.core.Instances;

public class HoeffdingExpansionSplit implements ISplitExpand {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3217185105236888859L;

	private HoeffdingTree hoeffdingTree;

	private double expectedEntropyReduction;

	private double hopEntropyDecrease;

	private double hoeffdingTieThreshold;

	public HoeffdingExpansionSplit() {

	}

	@Override
	public void trySplit(ActiveHNode node, SplitNode parent, String parentBranch)
			throws Exception {

		if (node.numEntriesInClassDistribution() > 1) {
			
			//Possible splits
			List<SplitCandidate> bestSplits = node.getPossibleSplits(getHoeffdingTree().m_splitMetric);
			Collections.sort(bestSplits);
			boolean doSplit = false;
			
			// XXX In our case this check is not needed as there may be only one attribute available that has to extended
//			if (bestSplits.size() < 2) {
//				doSplit = bestSplits.size() > 0;
//			} 
//			else {
			// compute the Hoeffding bound
			double metricMax = getHoeffdingTree().m_splitMetric.getMetricRange(node.m_classDistribution);
			double hoeffdingBound = getHoeffdingTree().computeHoeffdingBound(metricMax,getHoeffdingTree().m_splitConfidence,node.totalWeight());

			//Let 𝑋𝑎 be the attribute with the highest 𝐺𝑙 actual information gain is the 
			//attribute with the highest IG
			SplitCandidate best = bestSplits.get(bestSplits.size() - 1);
			double actualInformationGain = best.getSplitMetricResult().getResult();


			//𝐷𝐹(𝐸𝑅,𝑁𝐻)
			double expectedInformationGain = best.getSplitMetricResult().getEntropyBefore()
					- (best.getSplitMetricResult().getEntropyBefore() - 
							(best.getSplitMetricResult().getEntropyBefore() * getExpectedEntropyReductionForCurrentHop()));

			
			if (actualInformationGain - expectedInformationGain > hoeffdingBound) {
				//Split node found in the current hop 
				
				// XXX update header information
				getHoeffdingTree().updateHeaderAttributes();
				// XXX Split on the best node

				if (best.m_splitTest == null) {
					// preprune
					getHoeffdingTree().deactivateNode(node, parent,	parentBranch);
				} else {
					SplitNode newSplit = new SplitNode(node.m_classDistribution, best.m_splitTest);

					for (int i = 0; i < best.numSplits(); i++) {

						ActiveHNode newChild = getHoeffdingTree().newLearningNode();

						newChild.m_classDistribution = best.m_postSplitClassDistributions.get(i);
						newChild.m_weightSeenAtLastSplitEval = newChild.totalWeight();

						String branchName = "";

						HoeffdingTree ht = getHoeffdingTree();
						
						Instances instances = ht.m_header;
						
						List<String> splitAttributes = best.m_splitTest.splitAttributes();
						
						String splitAttributeName = splitAttributes.get(0);
						
						
						Attribute attribute = instances.attribute(splitAttributeName);
												
						if (attribute.isNumeric()) {
							branchName = i == 0 ? "left" : "right";
						} else {
							Attribute splitAtt = getHoeffdingTree().m_header
									.attribute(best.m_splitTest
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
			//Else If ∈<𝜎, then
			else if (hoeffdingBound < hoeffdingTieThreshold) {
				// Enough examples have been collected so it is time to look for expansion
				// Expand each 𝑋𝑗∈𝑿𝒍 with sem. related concepts
				
			
				ActiveHNode nodeWithExpandedAttributes = getHoeffdingTree().getExpansionTool().expandOneBFSHop(node);
				
				getHoeffdingTree().m_header=nodeWithExpandedAttributes.getInstances();
						
				System.out.println(getHoeffdingTree().m_header.getAttributesSchema());
				//TODO Add generated attributes to expansion scheme
								
				// TODO YTE update the collected node stats with that of the additional attributes resulting from the expansion of a resource
				getHoeffdingTree().replaceNode(node, nodeWithExpandedAttributes,parent, parentBranch);

			} else {
				// XXX Do nothing
			}


		}

	}

	public double getExpectedEntropyReduction() {
		return expectedEntropyReduction;
	}

	protected double getExpectedEntropyReductionForCurrentHop() {
		return expectedEntropyReduction
				- getHoeffdingTree().getExpansionTool().getNumberExpansions()
				* hopEntropyDecrease;
	}

	/**
	 * Sets the expected entropy reduction for this type of task
	 * 
	 * @param expectedEntropyReduction
	 */
	public void setExpectedEntropyReduction(double expectedEntropyReduction) {
		this.expectedEntropyReduction = expectedEntropyReduction;

	}

	@Override
	public HoeffdingTree getHoeffdingTree() {
		return hoeffdingTree;
	}

	public void setHoeffdingTree(HoeffdingTree hoeffdingTree) {
		this.hoeffdingTree = hoeffdingTree;
	}
	
	public void setHopEntropyDecrease(double hopEntropyDecrease) {
		this.hopEntropyDecrease = hopEntropyDecrease;
	}
	
	public double getHopEntropyDecrease() {
		return hopEntropyDecrease;
	}
	
	public double getHoeffdingTieThreshold() {
		return hoeffdingTieThreshold;
	}
	
	public void setHoeffdingTieThreshold(double hoeffdingTieThreshold) {
		this.hoeffdingTieThreshold = hoeffdingTieThreshold;
	}

}


//[1]
//if(best.m_splitMerit-expectedInformationGain>hoeffdingBound){
// doSplit = true;
// }

// SplitCandidate secondBest = bestSplits.get(bestSplits.size()
// - 2);
//
// if (best.m_splitMerit - secondBest.m_splitMerit >
// hoeffdingBound
// || hoeffdingBound < m_hoeffdingTieThreshold) {
// doSplit = true;
// }
// else{
// //TODO YTE Select a resource to expand - directly not
// possible without knowledge of the resource - evtl. possible
// on sequence of expansions in particular direction
// //XXX First version expand one hop in BFS manner
//
//
//
// ActiveHNode expandOneBFSHop =
// getExpansionTool().expandOneBFSHop(node);
//
// //TODO YTE update the collected node stats with that of the
// additional attributes resulting from the expansion of a
// resource
// replaceNode(node, expandOneBFSHop, parent, parentBranch);
//
//
//
// //TODO YTE detect cycles
//
// //TODO YTE different expansions for different objects ( use
// ontology schema to unify)
// /**
// * resource get ontology class and expand from there (restrict
// to that for the first version)
// *
// * s. JeanTests.getResourceType();
// * one resource might be from many different classes (e.g.
// person, PHdStudent) this must be handled somehow
// * -How to handle a list of referenced resources (e.g.
// publications)
// * -vectorizing to all properties of the deepest classes -
// consider only deepest classes and build the union of all
// features
// *
// * The cardinalities do not exist in the swcr ontology
// *
// */
//
//
//
//
// //TODO YTE recalculate whether there is a new attribute with
// an information gain (hoeffding bound) better than the
// previous best
//
// }
// TODO - remove poor attributes stuff?
//	}
