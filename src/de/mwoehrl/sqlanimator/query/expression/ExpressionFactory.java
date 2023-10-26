package de.mwoehrl.sqlanimator.query.expression;

import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;

import de.mwoehrl.sqlanimator.query.expression.antlr.ExpressionParser.ExpressionContext;
import de.mwoehrl.sqlanimator.query.expression.antlr.ExpressionParser.LiteralContext;

public class ExpressionFactory {

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
			if (child instanceof TerminalNode) {
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
		case "AND":
			return new AndOperator();
		}
		return null;
	}

}
