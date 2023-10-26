package de.mwoehrl.sqlanimator.relation;

public class Cell {

	private final String value;

	public Cell(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "Cell: \"" + value + "\"";
	}

	public String getValue() {
		return value;
	}

	public Cell getClone() {
		return new Cell(value);
	}
	

}
