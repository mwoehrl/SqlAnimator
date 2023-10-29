package de.mwoehrl.sqlanimator.query.expression;

import de.mwoehrl.sqlanimator.query.expression.ExpressionToken.TokenType;

public class EqualsOperator extends Operator {

	@Override
	public String evaluate(String left, String right) {
		if (left != null && right != null) {
			return booleanToString(left.equals(right));
		}
		return null;
	}

	public ExpressionToken getToken() {
		return new ExpressionToken(TokenType.Other, "=");
	}

}
