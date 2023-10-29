package de.mwoehrl.sqlanimator.query.expression;

public abstract class Operator {

	public abstract String evaluate(String evaluate, String evaluate2);
	
	protected boolean stringToBoolean(String s) {
		return !s.equals("0");
	}
	
	protected String booleanToString(boolean b) {
		return b ? "1" : "0";
	}

	public abstract ExpressionToken getToken();	
	
}
