package de.mwoehrl.sqlanimator.execution;

import java.util.ArrayList;
import java.util.List;

import de.mwoehrl.sqlanimator.query.Query;
import de.mwoehrl.sqlanimator.relation.Relation;
import de.mwoehrl.sqlanimator.renderer.AbsoluteCellPosition;
import de.mwoehrl.sqlanimator.renderer.AllRelationCanvas;
import de.mwoehrl.sqlanimator.renderer.QueryCanvas;

public class PickTablesAction extends AbstractAction {
	private final List<Relation> originalRelations;
	private final QueryCanvas queryCanvas;
	
	protected PickTablesAction(Query query, List<Relation> originalRelations, QueryCanvas queryCanvas) {
		super(query);
		this.originalRelations = originalRelations;
		this.queryCanvas = queryCanvas;
		showResultAfterAnimation = true;
	}

	@Override
	public AllRelationCanvas perform(AllRelationCanvas prevARC) throws PerformActionException {
		Relation[] step1Relations = selectTables();
		resultingCanvas = new AllRelationCanvas(step1Relations, prevARC.getPosition());
		animateTableNames(resultingCanvas);
		return resultingCanvas;
	}
	
	private Relation[] selectTables() throws PerformActionException {
		String[] tableNames = query.from.getFromTables();
		Relation[] relationsResult = new Relation[tableNames.length];
		int index = 0;
		for (int i = 0; i < tableNames.length; i++) {
			for (Relation r : originalRelations) {
				if (r.getName().equals(tableNames[i])) {
					relationsResult[index++] = r.changeName(tableNames[i]);
				}
			}
		}
		if (index != tableNames.length)
			throw new PerformActionException("Table name not found");
		return relationsResult;
	}
	
	public void animateTableNames(AllRelationCanvas arc) {
		AbsoluteCellPosition[] queryCells = queryCanvas.getTableCellAbsolutePositions();
		AbsoluteCellPosition[] tableCells = arc.getTableNameCellPosition();

		ArrayList<CellTransition> result = new ArrayList<>();
		for (int i = 0; i < queryCells.length; i++) {
			for (int j = 0; j < tableCells.length; j++) {
				if (queryCells[i].getCellCanvas().getCoreObject()==tableCells[j].getCellCanvas().getCoreObject()) {
					result.add(new MoveCellTransition(queryCells[i], tableCells[j]));
				}
			}
		}
		transitions = new CellTransition[result.size()];
		result.toArray(transitions);
	}
}
