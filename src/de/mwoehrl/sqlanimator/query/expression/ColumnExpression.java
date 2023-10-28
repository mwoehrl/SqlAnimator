package de.mwoehrl.sqlanimator.query.expression;

import java.util.HashMap;

import de.mwoehrl.sqlanimator.relation.Row;

public class ColumnExpression extends Expression {

	private final String columnName;
	
	public ColumnExpression(String columnName) {
		this.columnName = columnName;
	}	

	@Override
	public String evaluate(Row targetRow, HashMap<String, Integer> nameToOrdinal) {
		return targetRow.getCell(nameToOrdinal.get(columnName).intValue()).getValue();
	}
	
	public String getColumnName() {
		return columnName;
	}

}
