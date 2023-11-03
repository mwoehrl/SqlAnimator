package de.mwoehrl.sqlanimator.execution;

import de.mwoehrl.sqlanimator.query.Query;
import de.mwoehrl.sqlanimator.relation.Relation;
import de.mwoehrl.sqlanimator.renderer.AllRelationCanvas;

public class CartesianFillAction extends AbstractAction {

	protected CartesianFillAction(Query query) {
		super(query);
	}

	@Override
	public AllRelationCanvas perform(AllRelationCanvas prevARC) throws PerformActionException {
		Relation[] relations = prevARC.getRelations();
		relations[0].multiplyRowsBlockwise();
		relations[1].separateRows();
		relations[1].multiplyRowsRowwise();
		resultingCanvas = new AllRelationCanvas(relations, prevARC.getPosition());
		transitions = matchTransitions(prevARC.getAbsoluteCellPositions(), resultingCanvas.getAbsoluteCellPositions());
		resultingCanvas = cloneCells(resultingCanvas);
		return resultingCanvas;
	}

}
