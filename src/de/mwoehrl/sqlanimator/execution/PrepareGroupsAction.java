package de.mwoehrl.sqlanimator.execution;

import java.util.ArrayList;

import de.mwoehrl.sqlanimator.query.Query;
import de.mwoehrl.sqlanimator.relation.Relation;
import de.mwoehrl.sqlanimator.relation.Row;
import de.mwoehrl.sqlanimator.renderer.AbsoluteCellPosition;
import de.mwoehrl.sqlanimator.renderer.AllRelationCanvas;

public class PrepareGroupsAction extends GroupByAction {



	protected PrepareGroupsAction(Query query, ArrayList<ArrayList<Row>> bucketList) {
		super(query, bucketList);
	}

	@Override
	public AllRelationCanvas perform(AllRelationCanvas prevARC) throws PerformActionException {
		Relation preGroupedRelation = prevARC.getRelations()[0].prepareGroupBy(query.groupby, bucketList);
		
		resultingCanvas = new AllRelationCanvas(preGroupedRelation, bucketList, prevARC.getPosition());
		AbsoluteCellPosition[] toCellsDest = getResultingCanvas().getAbsoluteCellPositions();
		AbsoluteCellPosition[] fromPositions = prevARC.getAbsoluteCellPositions();
		transitions = matchTransitions(fromPositions, toCellsDest);
		return getResultingCanvas();
	}

}
