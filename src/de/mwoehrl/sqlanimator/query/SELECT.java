package de.mwoehrl.sqlanimator.query;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

import de.mwoehrl.sqlanimator.query.expression.ExpressionFactory;
import de.mwoehrl.sqlanimator.query.expression.antlr.ExpressionLexer;
import de.mwoehrl.sqlanimator.query.expression.antlr.ExpressionParser;

public class SELECT {

	private final ProjectionColumn[] projectionColumns;
	private final String[] rawSelects;
	
	public SELECT(String selectColumns) {
		rawSelects = selectColumns.split(",");
		this.projectionColumns = new ProjectionColumn[rawSelects.length];
		for (int i = 0; i < rawSelects.length; i++) {
			this.projectionColumns[i] = parseColExpression(rawSelects[i]);
		}
	}
	
	private ProjectionColumn parseColExpression(String colExpression) {
		CharStream inputStream = CharStreams.fromString(colExpression);
		ExpressionLexer lexer = new ExpressionLexer(inputStream);
	    CommonTokenStream commonTokenStream = new CommonTokenStream(lexer);
	    ExpressionParser parser = new ExpressionParser(commonTokenStream);
	    ProjectionColumn ex = ExpressionFactory.buildColExpression(parser.colexpression());
		return ex;
	}
	
	public String[] getRawSelects() {
		return rawSelects;
	}
	
	public ProjectionColumn[] getProjectionColumns() {
		return projectionColumns;
	}
	
	
}
