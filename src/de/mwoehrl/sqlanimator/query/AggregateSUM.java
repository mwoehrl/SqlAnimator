package de.mwoehrl.sqlanimator.query;

public class AggregateSUM extends Aggregate {

	@Override
	public String doAggregation(String[] cellValues) {
		int sum = 0;
		for (String s : cellValues) {
			sum += Integer.parseInt(s);
		}
		return String.valueOf(sum);
	}
}
