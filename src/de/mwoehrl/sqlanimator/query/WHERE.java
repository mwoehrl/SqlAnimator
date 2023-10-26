package de.mwoehrl.sqlanimator.query;

import de.mwoehrl.sqlanimator.query.expression.Expression;

public class WHERE {
	private final Expression whereCondition;
	private final String conditionString;
	
	public WHERE(Expression whereCondition, String conditionString) {
		this.whereCondition = whereCondition;
		this.conditionString = conditionString;
	}
	
	public Expression getWhereCondition() {
		return whereCondition;
	}

	public Object getConditionString() {
		return conditionString;
	}

}
