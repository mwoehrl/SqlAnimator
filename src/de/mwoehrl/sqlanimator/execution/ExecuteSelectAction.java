package de.mwoehrl.sqlanimator.execution;

import de.mwoehrl.sqlanimator.query.Query;
import de.mwoehrl.sqlanimator.relation.Relation;
import de.mwoehrl.sqlanimator.renderer.AbsoluteCellPosition;
import de.mwoehrl.sqlanimator.renderer.AllRelationCanvas;

public class ExecuteSelectAction extends AbstractAction {
	private final boolean isWhere;

	protected ExecuteSelectAction(Query query, boolean isWhere) {
		super(query);
		this.isWhere = isWhere; 
	}

	@Override
	public AllRelationCanvas perform(AllRelationCanvas prevARC) throws PerformActionException {
		Relation selectedRelations = prevARC.getRelations()[0].selection(isWhere ? query.where : query.having);
		resultingCanvas = new AllRelationCanvas(new Relation[] {selectedRelations}, prevARC.getPosition());
		AbsoluteCellPosition[] toCellsDest = resultingCanvas.getAbsoluteCellPositions();
		AbsoluteCellPosition[] fromPositions = prevARC.getAbsoluteCellPositions();
		transitions = matchTransitions(fromPositions, toCellsDest);
		resultingCanvas = cloneCells(resultingCanvas);
		return resultingCanvas;
	}

}
