package de.mwoehrl.sqlanimator.query.expression;

public class AddOperator extends Operator {

	@Override
	public String evaluate(String left, String right) {
		return String.valueOf(Integer.parseInt(left) + Integer.parseInt(right));
	}

}
