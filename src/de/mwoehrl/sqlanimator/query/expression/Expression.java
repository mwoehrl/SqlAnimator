package de.mwoehrl.sqlanimator.query.expression;

import java.util.HashMap;

import de.mwoehrl.sqlanimator.relation.Row;

public abstract class Expression {

	public abstract String evaluate(Row targetRow, HashMap<String, Integer> nameToOrdinal);
}
