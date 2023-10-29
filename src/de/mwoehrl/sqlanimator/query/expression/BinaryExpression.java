package de.mwoehrl.sqlanimator.query.expression;

import java.util.HashMap;

import de.mwoehrl.sqlanimator.query.expression.ExpressionToken.TokenType;
import de.mwoehrl.sqlanimator.relation.Row;

public class BinaryExpression extends Expression {

	private final Expression left;
	private final Expression right;
	
	private final Operator operator;
	
	public BinaryExpression(Expression left, Expression right, Operator operator) {
		this.left = left;
		this.right = right;
		this.operator = operator;		
	}
	
	@Override
	public String evaluate(Row targetRow, HashMap<String, Integer> nameToOrdinal) {
		return operator.evaluate(left.evaluate(targetRow, nameToOrdinal), right.evaluate(targetRow, nameToOrdinal));
	}

	@Override
	public ExpressionToken[] getAllTokens() {
		ExpressionToken[] leftTokens = left.getAllTokens();
		ExpressionToken[] rightTokens = right.getAllTokens();
		ExpressionToken[] result = new ExpressionToken[leftTokens.length + 1 + rightTokens.length];
		for (int i = 0; i < leftTokens.length; i++) {
			result[i] = leftTokens[i];
		}
		result[leftTokens.length] = operator.getToken();;
		for (int i = 0; i < rightTokens.length; i++) {
			result[i + 1 + leftTokens.length] = rightTokens[i];
		}
		return result;		
	}

}
