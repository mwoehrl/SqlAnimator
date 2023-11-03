package de.mwoehrl.sqlanimator.execution;

import de.mwoehrl.sqlanimator.query.Query;
import de.mwoehrl.sqlanimator.relation.Relation;
import de.mwoehrl.sqlanimator.renderer.AbsoluteCellPosition;
import de.mwoehrl.sqlanimator.renderer.AllRelationCanvas;
import de.mwoehrl.sqlanimator.renderer.QueryCanvas;

public class OrderByAction extends AbstractAction {

	protected OrderByAction(Query query, QueryCanvas queryCanvas) {
		super(query);
	}

	@Override
	public AllRelationCanvas perform(AllRelationCanvas prevARC) throws PerformActionException {
		Relation orderedRelation = prevARC.getRelations()[0].orderBy(query.orderby);
		resultingCanvas = new AllRelationCanvas(new Relation[] {orderedRelation}, prevARC.getPosition());
		AbsoluteCellPosition[] toCellsDest = resultingCanvas.getAbsoluteCellPositions();
		AbsoluteCellPosition[] fromPositions = prevARC.getAbsoluteCellPositions();
		transitions = matchTransitions(fromPositions, toCellsDest);
		return resultingCanvas;
	}

}
