package de.mwoehrl.sqlanimator.relation;

public class Column {

	private final Cell nameCell;
	private final Cell[] cells;
	private final String originalTable;
	private Boolean isNumeric = null;
	
	public Column(String name, int length, String originalTable) {
		this.nameCell = new Cell(name);
		cells = new Cell[length];
		this.originalTable = originalTable;
	}

	public Column(Cell nameCell, int length, String originalTable) {
		this.nameCell = nameCell;
		cells = new Cell[length];
		this.originalTable = originalTable;
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

	public String getOriginalTable() {
		return originalTable;
	}

	public Column clone(String newName) {
		if (newName.equals(getName())) {
			return this;
		}
		Column result = new Column(newName, cells.length, originalTable);
		for (int i = 0; i < cells.length; i++) {
			result.cells[i] = cells[i];
		}
		return result;
	}
	
	public boolean isNumeric() {
		if (isNumeric == null) {
			boolean result = true;
			try {
				for (int i = 0; i < cells.length; i++) {
					Double.parseDouble(cells[i].getValue());
				}
			} catch (NumberFormatException ex) {
				result = false;
			}
			isNumeric = result;
		}
		return isNumeric;
	}
}
