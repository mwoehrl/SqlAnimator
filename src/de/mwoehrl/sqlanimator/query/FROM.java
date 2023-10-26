package de.mwoehrl.sqlanimator.query;

public class FROM {

	private final String[] fromTables;
	
	public FROM(String fromTables) {
		this.fromTables = fromTables.split(",");
	}

	public String[] getFromTables() {
		return fromTables;
	}

}
