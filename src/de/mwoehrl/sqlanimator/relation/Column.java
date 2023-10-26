package de.mwoehrl.sqlanimator.relation;

public class Column {

	private final Cell nameCell;
	private final Cell[] cells;
	
	public Column(String name, int length) {
		this.nameCell = new Cell(name);
		cells = new Cell[length];
	}

	public void setCell(Cell cell, int rowNr) {
		cells[rowNr] = cell;
	}
	
	@Override
	public String toString() {
		return "Column: \"" + getName() + "\"";
	}

	public String getName() {
		return nameCell.getValue();
	}
	
	public Cell getNameCell() {
		return nameCell;
	}

	public Cell getCell(int r) {
		return cells[r];
	}
	

}
