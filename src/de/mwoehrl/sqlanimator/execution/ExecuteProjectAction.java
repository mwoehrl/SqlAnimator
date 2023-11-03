package de.mwoehrl.sqlanimator.execution;

import de.mwoehrl.sqlanimator.query.Query;
import de.mwoehrl.sqlanimator.relation.Relation;
import de.mwoehrl.sqlanimator.renderer.AbsoluteCellPosition;
import de.mwoehrl.sqlanimator.renderer.AllRelationCanvas;

public class ExecuteProjectAction extends AbstractAction {

	protected ExecuteProjectAction(Query query) {
		super(query);
	}

	@Override
	public AllRelationCanvas perform(AllRelationCanvas prevARC) throws PerformActionException {
		Relation projectedRelations = prevARC.getRelations()[0].projection(query.select);
		resultingCanvas = new AllRelationCanvas(new Relation[] {projectedRelations}, prevARC.getPosition());
		AbsoluteCellPosition[] toCellsDest = resultingCanvas.getAbsoluteCellPositions();
		AbsoluteCellPosition[] fromPositions = prevARC.getAbsoluteCellPositions();
		transitions = matchTransitions(fromPositions, toCellsDest);
		resultingCanvas = cloneCells(resultingCanvas);
		return resultingCanvas;
	}
}
