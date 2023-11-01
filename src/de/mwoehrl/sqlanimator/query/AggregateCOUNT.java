package de.mwoehrl.sqlanimator.query;

public class AggregateCOUNT extends Aggregate {

	@Override
	public String doAggregation(double[] cellValues) {
		int sum = 0;
		for (double d : cellValues) {
			if (!Double.isNaN(d)) sum++;
		}
		return String.valueOf((int)sum);
	}
	
	@Override
	public String toString() {
		return "COUNT";
	}
}
