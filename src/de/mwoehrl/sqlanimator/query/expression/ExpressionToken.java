package de.mwoehrl.sqlanimator.query.expression;

public class ExpressionToken {
	public enum TokenType{
		Column,
		Keyword,
		Literal,
		Other
	}
	
	public final String text;
	public final TokenType type;
	
	public ExpressionToken(TokenType type, String text){
		this.text = text;
		this.type = type;		
	}
}
