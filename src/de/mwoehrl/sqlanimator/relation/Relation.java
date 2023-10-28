package de.mwoehrl.sqlanimator.relation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import de.mwoehrl.sqlanimator.query.ORDERBY;
import de.mwoehrl.sqlanimator.query.ProjectionColumn;
import de.mwoehrl.sqlanimator.query.SELECT;
import de.mwoehrl.sqlanimator.query.WHERE;
import de.mwoehrl.sqlanimator.query.expression.ColumnExpression;

public class Relation {

	private final class TypeSensitiveComparator implements Comparator<Row> {
		private final HashMap<String, Integer> colMap;
		private final ORDERBY orderby;
		private final Column[] cols;

		private TypeSensitiveComparator(HashMap<String, Integer> colMap, ORDERBY orderby, Column[] cols) {
			this.colMap = colMap;
			this.orderby = orderby;
			this.cols = cols;
		}

		@Override
		public int compare(Row left, Row right) {
			int compResult = 0;
			int colIndex = 0;
			while(compResult == 0 && colIndex < orderby.getOrderByColumns().length) {
				String orderCol = orderby.getOrderByColumns()[colIndex];
				int inverse = orderby.getAscending()[colIndex] ? 1 : -1;
				int ordinal = colMap.get(orderCol);

				if (cols[ordinal].isNumeric()) {
					Double leftVal = Double.parseDouble(left.getCell(ordinal).getValue());
					Double rightVal = Double.parseDouble(right.getCell(ordinal).getValue());
					compResult = leftVal.compareTo(rightVal) * inverse;
				} else {
					String leftVal = left.getCell(ordinal).getValue();
					String rightVal = right.getCell(ordinal).getValue();
					compResult = leftVal.compareTo(rightVal) * inverse;
				}
				colIndex++;
			}
			return compResult;
		}
	}
	
	private final String name;
	private final Column[] columns;
	private final Row header;
	private final Row[] rows;

	public Relation(List<String> relationLines) {
		name = relationLines.get(0);
		String[] cols = relationLines.get(1).split(";");
		columns = new Column[cols.length];
		header = new Row(cols.length);
		rows = new Row[relationLines.size() - 2];
		for (int i = 0; i < columns.length; i++) {
			columns[i] = new Column(cols[i], rows.length, name);
			header.setCell(columns[i].getNameCell(), i);
		}
		for (int i = 0; i < rows.length; i++) {
			rows[i] = new Row(cols.length);
			int ordinal = 0;
			for (String value: relationLines.get(i+2).split(";")) {
				Cell cell = new Cell(value);
				rows[i].setCell(cell, ordinal);
				columns[ordinal].setCell(cell, i);
				ordinal++;
			}
		}
	}
	
	private Relation (Relation fromRelation, String name) {
		this.name = name; 
		columns = fromRelation.columns;
		header = fromRelation.header;
		rows = fromRelation.rows;
	}

	
	private Relation (Relation fromRelation, ORDERBY orderby) {
		name = fromRelation.name; 
		columns = new Column[fromRelation.columns.length];
		header = fromRelation.header;
		rows = new Row[fromRelation.rows.length];
		
		for (int i = 0; i < rows.length; i++) {
			rows[i] = fromRelation.rows[i];
		}

		Arrays.sort(rows, new TypeSensitiveComparator(
				getNameToOrdinalMapping(fromRelation),
				orderby,
				fromRelation.columns));

		for (int i = 0; i < columns.length; i++) {
			for (int r = 0; r < rows.length; r++) {
				columns[i] = new Column(fromRelation.getColumns()[i].getName(), rows.length, fromRelation.getColumns()[i].getOriginalTable());
				columns[i].setCell(rows[r].getCell(i), r);
			}
		}
	}
	
	private Relation(Relation fromRelation, SELECT selectFields) {		//Contructor for relation from projection 
		//TODO: Select * irgendwie abdecken

		ProjectionColumn[] projectionColumns = selectFields.getProjectionColumns();
		this.columns = new Column[projectionColumns.length];
		for (int i = 0; i < projectionColumns.length; i++) {
			this.columns[i] = getColumnFromExpression(fromRelation, projectionColumns[i]);
		}
		
		this.name = fromRelation.name;
		this.header = new Row(this.columns.length);
		this.rows = new Row[fromRelation.rows.length];
		for (int i = 0; i < columns.length; i++) {
			header.setCell(columns[i].getNameCell(), i);
		}
		for (int r = 0; r < rows.length; r++) {
			rows[r] = new Row(columns.length);
			for (int i = 0; i < columns.length; i++) {
				rows[r].setCell(columns[i].getCell(r), i);
			}
		}
	}
	
	private Column getColumnFromExpression (Relation fromRelation, ProjectionColumn projCol) {
		if (projCol.columnExpression instanceof ColumnExpression) {
			ColumnExpression col = (ColumnExpression)projCol.columnExpression;
			String[] colName = col.getColumnName().split("\\.");
			for (int i = 0; i < fromRelation.getColumns().length; i++) {
				if (colName.length == 2) {
					Column column = fromRelation.getColumns()[i];
					if (colName[0].equals(column.getOriginalTable()) && colName[1].equals(column.getName())){
						return column.clone(projCol.columnName);
					}
				} else {
					//TODO wenn feld wegen AS anderen Namen hat: neue Column mit alias-Namen!
					if (col.getColumnName().equals(fromRelation.getColumns()[i].getName())){
						return fromRelation.getColumns()[i].clone(projCol.columnName);
					}
				}
			}
			return null;
		} else {		//Expression: new column, evaluate against all rows and fill cells
			Column resultCol = new Column(projCol.columnName, fromRelation.getRows().length, null);
			for (int i = 0; i < fromRelation.getRows().length; i++) {
				resultCol.setCell(new Cell(projCol.columnExpression.evaluate(fromRelation.getRows()[i], getNameToOrdinalMapping(fromRelation))), i);
			}
			return resultCol;
		}
	}
	
	private static HashMap<String, Integer> getNameToOrdinalMapping(Relation fromRelation) {
		HashMap<String, Integer> colMap = new HashMap<String, Integer>();
		Column col;
		for (int i = 0; i < fromRelation.columns.length; i++) {
			col = fromRelation.columns[i];
			colMap.put(col.getName(), Integer.valueOf(i));
			colMap.put(col.getOriginalTable() + "." + col.getName(), Integer.valueOf(i));
		}
		return colMap;
	}
	

	private Relation(Relation fromRelation, WHERE condition) {		//Contructor for relation from selection 
		this.name = fromRelation.name;
		columns = fromRelation.columns;
		header = fromRelation.header;

		ArrayList<Row> selectedRows =  new ArrayList<Row>(); 
		for (Row row : fromRelation.rows) {
			String evalResult = condition.getWhereCondition().evaluate(row, getNameToOrdinalMapping(fromRelation));
			if (evalResult != null && !evalResult.equals("0")) {
				selectedRows.add(row);
			}
		}
		
		this.rows = new Row[selectedRows.size()]; 
		selectedRows.toArray(this.rows);
	}

	private Relation(Relation fromRelation, int factor) {		//Blow up Rows for cartesian product
		name = fromRelation.name;
		columns = new Column[fromRelation.columns.length];
		header = fromRelation.header;
		rows = new Row[fromRelation.rows.length * factor];
		
		for (int i = 0; i < fromRelation.rows.length; i++) {
			rows[i] = fromRelation.rows[i];
		}
		
		for (int c = 0; c < columns.length; c++) {
			columns[c] = new Column(fromRelation.columns[c].getName(), rows.length, fromRelation.columns[c].getOriginalTable());
			for (int i = 0; i < fromRelation.rows.length; i++) {
				columns[c].setCell(fromRelation.rows[i].getCell(c), i);
			}
		}
	}
	
	private Relation(Relation leftRelation, Relation rightRelation) {		//Combine left and right Relations into a new one
		name = leftRelation.name + " x " + rightRelation.name;
		columns = new Column[leftRelation.columns.length + rightRelation.columns.length];
		header = new Row(leftRelation.columns.length + rightRelation.columns.length);
		rows = new Row[leftRelation.rows.length];
		
		for (int i = 0; i < leftRelation.columns.length; i++) {
			columns[i] = leftRelation.columns[i];
		}
		for (int i = leftRelation.columns.length; i < rightRelation.columns.length + leftRelation.columns.length; i++) {
			columns[i] = rightRelation.columns[i-leftRelation.columns.length];
		}
		for (int i = 0; i < leftRelation.columns.length; i++) {
			header.setCell(leftRelation.header.getCell(i), i);
		}
		for (int i = leftRelation.columns.length; i < rightRelation.columns.length + leftRelation.columns.length; i++) {
			header.setCell(rightRelation.header.getCell(i-leftRelation.columns.length), i);
		}

		for (int r = 0; r < leftRelation.rows.length; r++) {
			rows[r] = new Row(rightRelation.columns.length + leftRelation.columns.length);
			for (int i = 0; i < leftRelation.columns.length; i++) {
				rows[r].setCell(leftRelation.rows[r].getCell(i), i);
			}
			for (int i = leftRelation.columns.length; i < rightRelation.columns.length + leftRelation.columns.length; i++) {
				rows[r].setCell(rightRelation.rows[r].getCell(i-leftRelation.columns.length), i);
			}
		}
	}
	
	@Override
	public String toString() {
		return "Relation: \"" + name + "\"";
	}

	public Column[] getColumns() {
		return columns;
	}

	public Row[] getRows() {
		return rows;
	}

	public Row getHeader() {
		return header;
	}

	public String getName() {
		return name;
	}

	public Relation projection(SELECT selectFields) {
		return new Relation(this, selectFields);
	}
	
	public Relation cartesianProductPrepare(int factor) {
		return new Relation(this, factor);
	}
	
	public void separateRows() {
		int rowCount = countNonNullRows();
		int factor = rows.length / rowCount;
		if (factor > 1) {
			for (int i = rowCount - 1; i >0; i--) {
				rows[i * factor] = rows[i];
				rows[i] = null;
			}
		}
	}
	
	public void multiplyRowsBlockwise() {
		int rowCount = countNonNullRows();
		int factor = rows.length / rowCount;
		for (int b = 1; b < factor; b++) {
			for (int i = 0; i < rowCount; i++) {
				rows[i + b * rowCount] = rows[i];
			}
		}
	}

	public void multiplyRowsRowwise() {
		int rowCount = countNonNullRows();
		int factor = rows.length / rowCount;
		for (int b = 0; b < rowCount; b++) {
			for (int i = 1; i < factor; i++) {
				rows[i + b * factor] = rows[b*factor];
			}
		}
	}
	
	private int countNonNullRows() {
		int count=0;
		if (columns.length==0) return 1;
		for (int i = 0; i < rows.length; i++) {
			if (rows[i] != null && rows[i].getCell(0)!= null) {
				count++;
			}
		}
		return count;
	}
	
	public Relation cartesianMerge(Relation other) {
		return new Relation(this, other);
	}
	
	public void cloneAllCells() {
		for (int r = 0; r < rows.length; r++) {
			Row newRow = new Row(columns.length);
			for (int i = 0; i < columns.length; i++) {
				newRow.setCell(rows[r].getCell(i).getClone(), i);
			}
			rows[r]=newRow;
		}
		for (int i = 0; i < columns.length; i++) {
			header.setCell(columns[i].getNameCell(), i);
			for (int r = 0; r < rows.length; r++) {
				columns[i].setCell(rows[r].getCell(i), r);
			}
		}
	}
	
	public Relation selection(WHERE where) {
		return new Relation(this, where);
	}

	public Relation orderBy(ORDERBY orderby) {
		return new Relation(this, orderby);
	}
	
	public Relation changeName(String name) {
		return new Relation(this, name);
	}

	public boolean rowMatchesCondition(WHERE condition, int rowIndex) {
		String evalResult = condition.getWhereCondition().evaluate(rows[rowIndex], getNameToOrdinalMapping(this));
		return (evalResult != null && !evalResult.equals("0")); 
	}
}
