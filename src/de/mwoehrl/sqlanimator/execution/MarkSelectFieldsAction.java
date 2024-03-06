package de.mwoehrl.sqlanimator.execution;

import java.util.ArrayList;

import de.mwoehrl.sqlanimator.query.Query;
import de.mwoehrl.sqlanimator.relation.Cell;
import de.mwoehrl.sqlanimator.renderer.AbsoluteCellPosition;
import de.mwoehrl.sqlanimator.renderer.AbstractCellCanvas;
import de.mwoehrl.sqlanimator.renderer.AllRelationCanvas;
import de.mwoehrl.sqlanimator.renderer.QueryCanvas;

public class MarkSelectFieldsAction extends AbstractAction {

	private final QueryCanvas queryCanvas;
	private final boolean isWhere;

	protected MarkSelectFieldsAction(Query query, QueryCanvas queryCanvas, boolean isWhere) {
		super(query, defaultSteps);
		this.queryCanvas = queryCanvas;
		this.isWhere = isWhere;
		showResultAfterAnimation = true;
	}

	@Override
	public AllRelationCanvas perform(AllRelationCanvas prevARC) throws PerformActionException {
		AbsoluteCellPosition[] queryCells = queryCanvas.getSelectionCellAbsolutePositions(isWhere);
		AbsoluteCellPosition[] tableCells = prevARC.getHeaderCellPositions();

		ArrayList<CellTransition> result = new ArrayList<>();
		ArrayList<AbstractCellCanvas> matchingHeaders = new ArrayList<AbstractCellCanvas>(); 
		for (int i = 0; i < queryCells.length; i++) {
			for (int j = 0; j < tableCells.length; j++) {
				Cell c = (Cell)tableCells[j].getCellCanvas().getCoreObject();
				if (queryCells[i].getCellCanvas().getCoreObject().equals(c.getValue())) {
					tableCells[j].matchAspectRatio(queryCells[i]);
					result.add(new MoveCellTransition(queryCells[i], tableCells[j], true));
					matchingHeaders.add((AbstractCellCanvas) tableCells[j].getCellCanvas());
				}
			}
		}
		transitions = new CellTransition[result.size()];
		result.toArray(transitions);
		resultingCanvas = new AllRelationCanvas(prevARC.getRelations(), prevARC.getPosition());
		getResultingCanvas().highlightMatchedColumns(matchingHeaders);
		showARCinAnimation = prevARC;
		return getResultingCanvas();		
	}

}
