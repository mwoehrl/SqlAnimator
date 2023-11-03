package de.mwoehrl.sqlanimator.execution;

import de.mwoehrl.sqlanimator.query.Query;
import de.mwoehrl.sqlanimator.relation.Relation;
import de.mwoehrl.sqlanimator.renderer.AllRelationCanvas;

public class CartesianMergeAction extends AbstractAction {

	protected CartesianMergeAction(Query query) {
		super(query);
	}

	@Override
	public AllRelationCanvas perform(AllRelationCanvas prevARC) throws PerformActionException {
		Relation[] relations = prevARC.getRelations();
		Relation mergedRelation = relations[0].cartesianMerge(relations[1]);
		Relation[] result = new Relation[relations.length - 1];
		result[0] = mergedRelation;
		for (int i = 1; i < result.length; i++) {
			result[i] = relations[i+1];
		}
		resultingCanvas = new AllRelationCanvas(result, prevARC.getPosition());
		transitions = matchTransitions(prevARC.getAbsoluteCellPositions(), resultingCanvas.getAbsoluteCellPositions());
		return resultingCanvas;
	}

}
