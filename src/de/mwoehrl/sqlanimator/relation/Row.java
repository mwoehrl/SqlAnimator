package de.mwoehrl.sqlanimator.relation;

public class Row {
	private final Cell[] cells;

	public Row(int length) {
		cells = new Cell[length];
	}

	public void setCell(Cell cell, int ordinal) {
		cells[ordinal] = cell;
	}

	@Override
	public String toString() {
		String r = "";
		for (Cell c : cells) {
			r += c.getValue() + " ";
		}
		return "Row: \"" + r + "\"";
	}

	public Cell getCell(int i) {
		return cells[i];
	}
	

}
