package de.mwoehrl.sqlanimator.query;

public class ORDERBY {

	private final String[] orderByColumns;
	private final boolean[] ascending;
	
	public ORDERBY(String orderByColumns) {
		this.orderByColumns = orderByColumns.split(",");
		this.ascending = new boolean[this.orderByColumns.length];
		for (int i = 0; i < ascending.length; i++) {
			ascending[i]=true;
		}
	}

	public String[] getOrderByColumns() {
		return orderByColumns;
	}
	
	public boolean[] getAscending() {
		return ascending;
	}
}
