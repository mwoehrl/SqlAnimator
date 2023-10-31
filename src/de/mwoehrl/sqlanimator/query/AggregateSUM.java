package de.mwoehrl.sqlanimator.query;

public class AggregateSUM extends Aggregate {

	@Override
	public String doAggregation(double[] cellValues) {
		double sum = 0d;
		for (double d : cellValues) {
			sum += d;
		}
		if (sum == (double)((int)sum)) {
			return String.valueOf((int)sum);
		} else {
			return String.valueOf(sum);
		}
	}
	
	@Override
	public String toString() {
		return "SUM";
	}
}
