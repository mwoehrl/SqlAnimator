package de.mwoehrl.sqlanimator.query.expression;

import org.antlr.v4.runtime.tree.ParseTree;

import de.mwoehrl.sqlanimator.query.Aggregate;
import de.mwoehrl.sqlanimator.query.AggregateSUM;
import de.mwoehrl.sqlanimator.query.ProjectionColumn;
import de.mwoehrl.sqlanimator.query.expression.antlr.ExpressionParser.ColumnContext;
import de.mwoehrl.sqlanimator.query.expression.antlr.ExpressionParser.ExpressionContext;
import de.mwoehrl.sqlanimator.query.expression.antlr.ExpressionParser.LiteralContext;

public class ExpressionFactory {

	public static ProjectionColumn buildColExpression(ParseTree colExpression) {
		switch (colExpression.getChildCount()) {
		case 1:
			//no aggregate or alias
			Expression expression = buildExpression(colExpression.getChild(0));
			return new ProjectionColumn(colExpression.getChild(0).getText(), expression, null, false);
		case 3:
			expression = buildExpression(colExpression.getChild(0));
			return new ProjectionColumn(colExpression.getChild(2).getText(), expression, null, true);
		case 4:   //Aggregate without alias: SUM(expression)
			expression = buildExpression(colExpression.getChild(2));
			return new ProjectionColumn(colExpression.getChild(2).getText(), expression, getAggregate(colExpression), false);
		case 6:   //Aggregate with alias: SUM(expression) AS name
			expression = buildExpression(colExpression.getChild(2));
			return new ProjectionColumn(colExpression.getChild(5).getText(), expression, getAggregate(colExpression), true);
		}
		return null;
	}


	private static Aggregate getAggregate(ParseTree colExpression) {
		Aggregate aggregate = null;
		switch (colExpression.getChild(0).getText()) {
		case "SUM":
			aggregate = new AggregateSUM();
		}
		return aggregate;
	}

	
	public static Expression buildExpression(ParseTree expression) {
		switch (expression.getChildCount()) {
		case 3:
			ParseTree child0 = expression.getChild(0);
			if (child0 instanceof ExpressionContext) {
				return buildBinaryExpression(child0, expression.getChild(1), expression.getChild(2));
			} else { 
				return buildExpression(expression.getChild(1));  //Klammern
			}
		case 2:
			return null;
		case 1:
			ParseTree child = expression.getChild(0);
			if (child instanceof ColumnContext) {
				//column name
				return new ColumnExpression(child.getText());
			} else if (child instanceof LiteralContext){
				return new LiteralExpression(child.getText());
			}
			return null;
		}
		return null;
	}

	private static Expression buildBinaryExpression(ParseTree left, ParseTree operator, ParseTree right) {
		return new BinaryExpression(buildExpression(left), buildExpression(right), buildOperator(operator));
	}

	private static Operator buildOperator(ParseTree operator) {
		switch (operator.getText()) {
		case "=":
			return new EqualsOperator();
		case "+":
			return new AddOperator();
		case "AND":
			return new AndOperator();
		}
		return null;
	}

}
