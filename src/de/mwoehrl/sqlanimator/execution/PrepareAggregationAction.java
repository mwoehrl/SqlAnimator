package de.mwoehrl.sqlanimator.execution;

import java.util.ArrayList;

import de.mwoehrl.sqlanimator.query.Aggregate;
import de.mwoehrl.sqlanimator.query.Query;
import de.mwoehrl.sqlanimator.relation.Row;
import de.mwoehrl.sqlanimator.renderer.AbsoluteCellPosition;
import de.mwoehrl.sqlanimator.renderer.AllRelationCanvas;

public class PrepareAggregationAction extends GroupByAction {

	protected PrepareAggregationAction(Query query, ArrayList<ArrayList<Row>> bucketList) {
		super(query, bucketList);
	}

	@Override
	public AllRelationCanvas perform(AllRelationCanvas prevARC) throws PerformActionException {
		Aggregate[] aggregates = new Aggregate[query.select.getProjectionColumns().length];
		for (int i = 0; i < aggregates.length; i++) {
			aggregates[i] = query.select.getProjectionColumns()[i].aggregate;
		}
		resultingCanvas = new AllRelationCanvas(prevARC.getRelations()[0], aggregates, bucketList, prevARC.getPosition());
		AbsoluteCellPosition[] toCellsDest = resultingCanvas.getAbsoluteCellPositions();
		AbsoluteCellPosition[] fromPositions = prevARC.getAbsoluteCellPositions();
		transitions = matchTransitions(fromPositions, toCellsDest);
		return resultingCanvas;
	}

}
