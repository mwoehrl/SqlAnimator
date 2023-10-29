package de.mwoehrl.sqlanimator.query.expression;

import java.util.HashMap;

import de.mwoehrl.sqlanimator.query.expression.ExpressionToken.TokenType;
import de.mwoehrl.sqlanimator.relation.Row;

public class LiteralExpression extends Expression {

	private final String literalValue;
	private final boolean isNumber;
	
	public LiteralExpression(String literal) {
		if (literal != null && literal.startsWith("\"")) {
			isNumber = false;;
			this.literalValue = literal.substring(1, literal.length()-1);
		} else {
			isNumber = true;
			this.literalValue = literal;
		}
	}

	@Override
	public String evaluate(Row targetRow, HashMap<String, Integer> nameToOrdinal) {
		return literalValue;
	}

	@Override
	public ExpressionToken[] getAllTokens() {
		String quotes="\"";
		if (isNumber) quotes = "";
		return new ExpressionToken[] {new ExpressionToken (TokenType.Literal, quotes + literalValue + quotes)};
	}

}
