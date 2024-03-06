package de.mwoehrl.sqlanimator.execution;

import de.mwoehrl.sqlanimator.query.Query;
import de.mwoehrl.sqlanimator.relation.Relation;
import de.mwoehrl.sqlanimator.renderer.AllRelationCanvas;

public class CartesianPrepareAction extends AbstractAction {

	protected CartesianPrepareAction(Query query) {
		super(query, defaultSteps);
	}

	@Override
	public AllRelationCanvas perform(AllRelationCanvas prevARC) throws PerformActionException {
		Relation[] relations = new Relation[prevARC.getRelations().length];
		relations[0] = prevARC.getRelations()[0].cartesianProductPrepare(prevARC.getRelations()[1].getRows().length);
		relations[1] = prevARC.getRelations()[1].cartesianProductPrepare(prevARC.getRelations()[0].getRows().length);
		for (int i = 2; i < relations.length; i++) {
			relations[i] = prevARC.getRelations()[i];
		}
		resultingCanvas = new AllRelationCanvas(relations, prevARC.getPosition());
		transitions = matchTransitions(prevARC.getAbsoluteCellPositions(), getResultingCanvas().getAbsoluteCellPositions());
		return getResultingCanvas();
	}

}
