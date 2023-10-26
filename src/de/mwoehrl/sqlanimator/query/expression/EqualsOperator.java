package de.mwoehrl.sqlanimator.query.expression;

public class EqualsOperator extends Operator {

	@Override
	public String evaluate(String left, String right) {
		if (left != null && right != null) {
			return booleanToString(left.equals(right));
		}
		return null;
	}

}
