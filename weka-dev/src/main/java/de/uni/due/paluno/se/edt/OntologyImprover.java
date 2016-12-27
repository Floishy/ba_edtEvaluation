package de.uni.due.paluno.se.edt;

import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;

import org.apache.jena.query.Dataset;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;
import org.apache.jena.tdb.TDBFactory;

import de.uni.due.paluno.se.edt.ontology.LocalDBPedia;
import weka.classifiers.trees.j48.Distribution;
import weka.classifiers.trees.j48.GainRatioSplitCrit;
import weka.classifiers.trees.j48.InfoGainSplitCrit;
import weka.core.Instance;
import weka.core.Instances;

public class OntologyImprover {

	private int m_minNoObj;
	private double m_sumOfWeights;
	private boolean useMDLcorrection;
	private int m_attIndex;

	/** InfoGain of split. */
	protected double m_infoGain;

	/** GainRatio of split. */
	protected double m_gainRatio;
	private int m_complexityIndex;
	private int m_index;
	private Distribution m_distribution;
	private int m_numSubsets;

	private static int RDF_Resource_Index[] = { 2 };

	/** Static reference to splitting criterion. */
	protected static InfoGainSplitCrit infoGainCrit = new InfoGainSplitCrit();

	/** Static reference to splitting criterion. */
	protected static GainRatioSplitCrit gainRatioCrit = new GainRatioSplitCrit();

	public static Model dbPediaModel;

	static {
		Dataset dataset = TDBFactory.createDataset(LocalDBPedia.tdbDirectory);
		dbPediaModel = dataset.getDefaultModel();
	}

	public OntologyImprover(int attr_index, int minNoObj, double sumOfWeights,
			boolean useMDLcorrection) {
		this.m_attIndex = attr_index;
		this.m_minNoObj = minNoObj;
		this.m_sumOfWeights = sumOfWeights;
		this.useMDLcorrection = useMDLcorrection;
	}

	public Instances findBetterAttributes(Instances trainInstances)
			throws Exception {

		if (!isRDF_Resource()) {
			return trainInstances;
		}

		// Initialize the remaining instance variables.

		m_infoGain = 0;
		m_gainRatio = 0;

		// Different treatment for enumerated and numeric
		// attributes.
		if (trainInstances.attribute(m_attIndex).isNominal()) {
			m_complexityIndex = trainInstances.attribute(m_attIndex)
					.numValues();
			m_index = m_complexityIndex;
			return handleEnumeratedAttribute(trainInstances);
		} else {
			return trainInstances;
		}
	}

	private boolean isRDF_Resource() {
		for (int i = 0; i < RDF_Resource_Index.length; i++) {
			if (RDF_Resource_Index[i] == m_attIndex) {
				return true;
			}
		}
		return false;
	}

	private Instances handleEnumeratedAttribute(Instances trainInstances)
			throws Exception {
		//Handle instances for a specific attribute

		Instance instance;

		m_distribution = new Distribution(m_complexityIndex,
				trainInstances.numClasses());

		// TODO get instances by class
//
//		List<Instances> classSeparatedInstances = WekaUtils
//				.getClassSeparatedInstances(trainInstances);
//
//		// TODO look for common properties connected by the same connection
//		// types in the current attribute
//		
//		//TODO look for properties that are different but don't play role in the classification as 
//		//instances belong to the same class
//
//		WekaUtils.getCommonAttributes(classSeparatedInstances, m_attIndex);

		// TODO Use the new properties to check if the information gain improves

		// TODO what about numeric attributes/properties

		// Look for better attributes
		Enumeration<Instance> enumer = trainInstances.enumerateInstances();
		while (enumer.hasMoreElements()) {
			instance = enumer.nextElement();
			if (!instance.isMissing(m_attIndex)) {

				// Model model = ModelFactory.createDefaultModel().read(uri);
			}
		}

		// Check if minimum number of Instances in at least two
		// subsets.
		if (m_distribution.check(m_minNoObj)) {
			m_numSubsets = m_complexityIndex;
			m_infoGain = infoGainCrit.splitCritValue(m_distribution,
					m_sumOfWeights);
			m_gainRatio = gainRatioCrit.splitCritValue(m_distribution,
					m_sumOfWeights, m_infoGain);
		}

		return trainInstances;

	}

}
