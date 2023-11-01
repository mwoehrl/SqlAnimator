package de.mwoehrl.sqlanimator.query;

public class AggregateAVG extends Aggregate {

	@Override
	public String doAggregation(double[] cellValues) {
		double sum = 0d;
		for (double d : cellValues) {
			sum += d;
		}
		sum /= cellValues.length;
		return String.valueOf(sum);
	}
	
	@Override
	public String toString() {
		return "AVG";
	}
}
