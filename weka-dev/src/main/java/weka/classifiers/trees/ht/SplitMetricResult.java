package weka.classifiers.trees.ht;

public class SplitMetricResult {

	private double result;
	
	private double entropyBefore;
	
	private double entropyAfter;

	public SplitMetricResult(double result, double entropyBefore,
			double entropyAfter) {
		super();
		this.result = result;
		this.entropyBefore = entropyBefore;
		this.entropyAfter = entropyAfter;
	}

	public double getResult() {
		return result;
	}
	
	public void setResult(double result) {
		this.result = result;
	}
	
	public double getEntropyBefore() {
		return entropyBefore;
	}

	public void setEntropyBefore(double entropyBefore) {
		this.entropyBefore = entropyBefore;
	}

	public double getEntropyAfter() {
		return entropyAfter;
	}

	public void setEntropyAfter(double entropyAfter) {
		this.entropyAfter = entropyAfter;
	}
	
	
	
	
}
