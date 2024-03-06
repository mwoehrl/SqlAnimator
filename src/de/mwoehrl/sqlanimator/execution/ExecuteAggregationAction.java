package de.mwoehrl.sqlanimator.execution;

import java.util.ArrayList;

import de.mwoehrl.sqlanimator.query.Query;
import de.mwoehrl.sqlanimator.relation.Row;
import de.mwoehrl.sqlanimator.renderer.AllRelationCanvas;

public class ExecuteAggregationAction extends GroupByAction {

	protected ExecuteAggregationAction(Query query, ArrayList<ArrayList<Row>> bucketList) {
		super(query, bucketList);
	}

	@Override
	public AllRelationCanvas perform(AllRelationCanvas prevARC) throws PerformActionException {
		transitions = prevARC.getAggregateTransistions();
		resultingCanvas = prevARC;
		return getResultingCanvas();
	}

}
