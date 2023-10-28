package de.mwoehrl.sqlanimator.query;

import de.mwoehrl.sqlanimator.query.expression.Expression;

public class ProjectionColumn {
	public final String columnName;
	public final Expression columnExpression;
	public final Aggregate aggregate;

	public ProjectionColumn(String columnName, Expression columnExpression, Aggregate aggregate) {
		this.columnName = columnName;
		this.columnExpression = columnExpression;
		this.aggregate = aggregate;
		
	}
	
}
