package de.mwoehrl.sqlanimator.execution;

import java.util.ArrayList;

import de.mwoehrl.sqlanimator.query.Query;
import de.mwoehrl.sqlanimator.relation.Relation;
import de.mwoehrl.sqlanimator.relation.Row;
import de.mwoehrl.sqlanimator.renderer.AbsoluteCellPosition;
import de.mwoehrl.sqlanimator.renderer.AllRelationCanvas;

public class FinishGroupByAction extends GroupByAction {

	protected FinishGroupByAction(Query query, ArrayList<ArrayList<Row>> bucketList) {
		super(query, bucketList);
	}

	@Override
	public AllRelationCanvas perform(AllRelationCanvas prevARC) throws PerformActionException {
		Relation groupedRelation = prevARC.getRelations()[0].finishGroupBy(query.select, bucketList);
		prevARC.setAggregateValuesFromRelation(groupedRelation);
		resultingCanvas = new AllRelationCanvas(new Relation[] {groupedRelation}, prevARC.getPosition());
		AbsoluteCellPosition[] toCellsDest = getResultingCanvas().getAbsoluteCellPositions();
		AbsoluteCellPosition[] fromPositions = prevARC.getAbsoluteCellPositions();
		transitions = matchTransitions(fromPositions, toCellsDest, false);
		return getResultingCanvas();
	}

}
