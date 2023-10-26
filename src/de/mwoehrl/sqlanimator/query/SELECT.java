package de.mwoehrl.sqlanimator.query;

public class SELECT {

	private final String[] selectColumns;
	
	public SELECT(String selectColumns) {
		this.selectColumns = selectColumns.split(",");
	}

	public String[] getSelectColumns() {
		return selectColumns;
	}
}
