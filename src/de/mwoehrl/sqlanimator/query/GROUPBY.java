package de.mwoehrl.sqlanimator.query;

public class GROUPBY {
	private final String[] groupByColumns;
	
	public GROUPBY(String groupByColumns) {
		this.groupByColumns = groupByColumns.split(",");
	}
	
	public String[] getGroupByColumns() {
		return groupByColumns;
	}

	public ORDERBY convertToORDERBY() {
		return new ORDERBY(String.join(",", groupByColumns));
	}
	
	@Override
	public String toString() {
		return String.join(",", groupByColumns);
	}

}
