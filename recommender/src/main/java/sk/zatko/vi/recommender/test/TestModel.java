package sk.zatko.vi.recommender.test;

public class TestModel {

	private double tp;
	private double fp;
	private double fn;
	
	public double getPrecision() {
		
		if (tp + fn == 0.0) {
			return 0.0;
		}
		
		return tp / (tp + fn);

	}
	
	public double getRecall() {
		
		if (tp + fp == 0.0) {
			return 0.0;
		}
		
		return tp / (tp + fp);
	}

	
	public double getTp() {
		return tp;
	}
	public void setTp(double tp) {
		this.tp = tp;
	}

	public double getFp() {
		return fp;
	}
	public void setFp(double fp) {
		this.fp = fp;
	}

	public double getFn() {
		return fn;
	}
	public void setFn(double fn) {
		this.fn = fn;
	}
	
}
