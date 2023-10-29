package de.mwoehrl.sqlanimator.query.expression;

import de.mwoehrl.sqlanimator.query.expression.ExpressionToken.TokenType;

public class AddOperator extends Operator {

	@Override
	public String evaluate(String left, String right) {
		return String.valueOf(Integer.parseInt(left) + Integer.parseInt(right));
	}

	public ExpressionToken getToken() {
		return new ExpressionToken(TokenType.Other, "+");
	}

}
