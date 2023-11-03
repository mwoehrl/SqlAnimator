package de.mwoehrl.sqlanimator.execution;

import java.util.ArrayList;

import de.mwoehrl.sqlanimator.query.Query;
import de.mwoehrl.sqlanimator.renderer.AbsoluteCellPosition;
import de.mwoehrl.sqlanimator.renderer.AllRelationCanvas;
import de.mwoehrl.sqlanimator.renderer.QueryCanvas;

public class MarkSelectAction extends AbstractAction {

	private final QueryCanvas queryCanvas;
	private final boolean isWhere;
	
	protected MarkSelectAction(Query query, QueryCanvas queryCanvas, boolean isWhere) {
		super(query, defaultSteps, true);
		this.queryCanvas = queryCanvas;
		this.isWhere = isWhere;
	}

	@Override
	public AllRelationCanvas perform(AllRelationCanvas prevARC) throws PerformActionException {
		AbsoluteCellPosition[] tableCells = prevARC.getFirstColumnCellPositions();
		AbsoluteCellPosition[] queryCells = queryCanvas.getWhereTicks(tableCells.length, prevARC.getRelations()[0], isWhere);

		ArrayList<CellTransition> result = new ArrayList<>();
			for (int j = 0; j < tableCells.length; j++) {
				result.add(new MoveCellTransition(queryCells[j], tableCells[j], true));
			}
		transitions = new CellTransition[result.size()];
		result.toArray(transitions);
		resultingCanvas = prevARC;
		return prevARC;
	}

}
