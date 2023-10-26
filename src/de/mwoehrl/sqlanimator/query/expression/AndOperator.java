package de.mwoehrl.sqlanimator.query.expression;

public class AndOperator extends Operator {

	@Override
	public String evaluate(String left, String right) {
		if (left != null && right != null) {
			return booleanToString(stringToBoolean(left) && stringToBoolean(right));
		}
		return null;
	}
}
