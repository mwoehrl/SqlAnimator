package de.mwoehrl.sqlanimator.query;

import java.util.Arrays;

import de.mwoehrl.sqlanimator.query.expression.Expression;
import de.mwoehrl.sqlanimator.query.expression.ExpressionToken;
import de.mwoehrl.sqlanimator.query.expression.ExpressionToken.TokenType;

public class ProjectionColumn {
	public final String columnName;
	public final Expression columnExpression;
	public final Aggregate aggregate;
	private final boolean hasAlias;
	
	public ProjectionColumn(String columnName, Expression columnExpression, Aggregate aggregate, boolean hasAlias) {
		this.columnName = columnName;
		this.columnExpression = columnExpression;
		this.aggregate = aggregate;
		this.hasAlias = hasAlias;
	}
	
	public ExpressionToken[] getAllTokens() {
		ExpressionToken[] inner = columnExpression.getAllTokens();
		
		if (aggregate != null) {
			ExpressionToken[] aggregateTokens = new ExpressionToken[inner.length + 3]; 
			for (int i = 0; i < inner.length; i++) {
				aggregateTokens[i+2] = inner[i];
			}
			aggregateTokens[0] = new ExpressionToken(TokenType.Keyword, aggregate.toString());
			aggregateTokens[1] = new ExpressionToken(TokenType.Other, "(");
			aggregateTokens[aggregateTokens.length-1] = new ExpressionToken(TokenType.Other, ")");
			inner= aggregateTokens;
		}
		if (hasAlias) {
			inner = Arrays.copyOf(inner, inner.length + 2);
			inner[inner.length - 2] = new ExpressionToken(TokenType.Keyword, "AS");
			inner[inner.length - 1] = new ExpressionToken(TokenType.Column, columnName);
		}
		
		return inner;		
			
	}
	
}
