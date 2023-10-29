package de.mwoehrl.sqlanimator.query.expression;

import de.mwoehrl.sqlanimator.query.expression.ExpressionToken.TokenType;

public class AndOperator extends Operator {

	@Override
	public String evaluate(String left, String right) {
		if (left != null && right != null) {
			return booleanToString(stringToBoolean(left) && stringToBoolean(right));
		}
		return null;
	}
	
	public ExpressionToken getToken() {
		return new ExpressionToken(TokenType.Keyword, "AND");
	}

}
