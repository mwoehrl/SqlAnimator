package de.mwoehrl.sqlanimator.query;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

import de.mwoehrl.sqlanimator.query.expression.BinaryExpression;
import de.mwoehrl.sqlanimator.query.expression.ColumnExpression;
import de.mwoehrl.sqlanimator.query.expression.EqualsOperator;
import de.mwoehrl.sqlanimator.query.expression.Expression;
import de.mwoehrl.sqlanimator.query.expression.ExpressionFactory;
import de.mwoehrl.sqlanimator.query.expression.antlr.ExpressionLexer;
import de.mwoehrl.sqlanimator.query.expression.antlr.ExpressionParser;
import de.mwoehrl.sqlanimator.query.expression.antlr.ExpressionParser.ExpressionContext;

public class Query {
	public final SELECT select;
	public final FROM from;
	public final WHERE where;
	public final ORDERBY orderby;
	
	public Query(String selectFields, String fromTables, String whereCondition, String groupBy, String having, String orderBy) {
		select = new SELECT(selectFields);
		from = new FROM(fromTables);
		if (whereCondition == null) {
			where = null;
		} else {
			where = new WHERE(parseCondition(whereCondition), whereCondition);
		}
		orderby = new ORDERBY(orderBy);
	}
	
	private Expression parseCondition(String condition) {
		CharStream inputStream = CharStreams.fromString(condition);
		ExpressionLexer lexer = new ExpressionLexer(inputStream);
	    CommonTokenStream commonTokenStream = new CommonTokenStream(lexer);
	    ExpressionParser parser = new ExpressionParser(commonTokenStream);
	    Expression ex = ExpressionFactory.buildExpression (parser.expression());
		return ex;
	}
	
	@Override
	public String toString() {
		return "SELECT " + select.getSelectColumns() + " FROM " + from.getFromTables() + " WHERE " + where.getWhereCondition();
	}
}
