package de.mwoehrl.sqlanimator.query;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

import de.mwoehrl.sqlanimator.query.expression.Expression;
import de.mwoehrl.sqlanimator.query.expression.ExpressionFactory;
import de.mwoehrl.sqlanimator.query.expression.antlr.ExpressionLexer;
import de.mwoehrl.sqlanimator.query.expression.antlr.ExpressionParser;

public class Query {
	public final SELECT select;
	public final FROM from;
	public final WHERE where;
	public final ORDERBY orderby;
	public final GROUPBY groupby;
	public final WHERE having;
	
	public Query(String selectFields, String fromTables, String whereCondition, String groupBy, String havingCondition, String orderBy) {
		select = new SELECT(selectFields);
		from = new FROM(fromTables);
		if (whereCondition != null && whereCondition.length() > 0) {
			where = new WHERE(parseCondition(whereCondition), whereCondition);
		} else {
			where = null;
		}
		if (groupBy != null && groupBy.length() > 0) {
			groupby = new GROUPBY(groupBy);
		} else {
			groupby = null;
		}
		if (havingCondition != null && havingCondition.length() > 0) {
			having = new WHERE(parseCondition(havingCondition), havingCondition);
		} else {
			having = null;
		}
		if (orderBy != null && orderBy.length() > 0) {
			orderby = new ORDERBY(orderBy);
		} else {
			orderby = null;
		}
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
		return "SELECT " + "<todo>" + " FROM " + from.getFromTables() + " WHERE " + where.getWhereCondition();
	}
}
