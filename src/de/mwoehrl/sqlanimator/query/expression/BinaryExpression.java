package de.mwoehrl.sqlanimator.query.expression;

import java.util.HashMap;

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

}
