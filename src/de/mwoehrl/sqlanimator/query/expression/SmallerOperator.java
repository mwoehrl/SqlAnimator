package de.mwoehrl.sqlanimator.query.expression;

import de.mwoehrl.sqlanimator.query.expression.ExpressionToken.TokenType;

public class SmallerOperator extends Operator {

	@Override
	public String evaluate(String left, String right) {
		if (left != null && right != null) {
			return booleanToString(Double.parseDouble(left) < Double.parseDouble(right));
		}
		return null;
	}

	@Override
	public ExpressionToken getToken() {
		return new ExpressionToken(TokenType.Other, "<");
	}

}
