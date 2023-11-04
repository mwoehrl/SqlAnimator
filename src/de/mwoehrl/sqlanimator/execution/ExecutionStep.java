package de.mwoehrl.sqlanimator.execution;

import java.util.ArrayList;
import java.util.List;

import de.mwoehrl.sqlanimator.query.Query;
import de.mwoehrl.sqlanimator.relation.Relation;
import de.mwoehrl.sqlanimator.relation.Row;
import de.mwoehrl.sqlanimator.renderer.AllRelationCanvas;
import de.mwoehrl.sqlanimator.renderer.CanvasPanel;
import de.mwoehrl.sqlanimator.renderer.QueryCanvas;

public class ExecutionStep {
	
	private final String name;
	private final AbstractAction[] actions;
	private PerformActionException preparationException = null;
	private int animationStep = 0;
	
	private ExecutionStep(String name, AbstractAction[] actions) {
		this.name = name;
		this.actions = actions;		
	}
	
	public static ExecutionStep[] createAllExecutionSteps(Query query, List<Relation> originalRelations, AllRelationCanvas firstCanvas, QueryCanvas queryCanvas) {
		ArrayList<ExecutionStep> allSteps = new ArrayList<ExecutionStep>(); 
		allSteps.add(createPickTablesStep(query, queryCanvas, originalRelations));			//Pick Tables
		for (int i = 0; i < query.from.getFromTables().length - 1; i++) {					//Cartesian Products if necessary
			allSteps.add(createCartesianProductStep(query, i));
		}
		if (query.where != null) allSteps.add(createSelectionStep(query, queryCanvas));		//Selection if necessary
		allSteps.add(createProjectionStep(query, queryCanvas));								//Projection
		if (query.groupby != null) allSteps.add(createGroupByStep(query, queryCanvas));			
		if (query.having != null) allSteps.add(createHavingStep(query, queryCanvas));		//Selection 2 if necessary
		if (query.orderby != null) allSteps.add(createOrderByStep(query, queryCanvas));		//Sorting if necessary
		
		ExecutionStep[] result = new ExecutionStep[allSteps.size()];
		allSteps.toArray(result);
		
		AllRelationCanvas arcPrev = firstCanvas;
		for (ExecutionStep step : result) {
			arcPrev = step.prepare(arcPrev);
		}		
		return result ;
	}

	private static ExecutionStep createPickTablesStep(Query query, QueryCanvas queryCanvas, List<Relation> originalRelations) {
		AbstractAction[] action = new AbstractAction[] {new PickTablesAction(query, originalRelations, queryCanvas)};
		return new ExecutionStep("Tabellen auswählen", action);
	}

	private static ExecutionStep createCartesianProductStep(Query query, int i) {
		String leftTable = query.from.getFromTables()[i];		//TODO alle vorigen Tables
		String rightTable = query.from.getFromTables()[i+1];
		AbstractAction[] actions = new AbstractAction[] {
				new CartesianPrepareAction(query),
				new CartesianFillAction(query),
				new CartesianMergeAction(query)
				};
		return new ExecutionStep("Kartesisches Produkt " + leftTable + "x" + rightTable, actions);
	}

	private static ExecutionStep createSelectionStep(Query query, QueryCanvas queryCanvas) {
		AbstractAction[] actions = new AbstractAction[] {
				new MarkSelectAction(query, queryCanvas, true),
				new ExecuteSelectAction(query, true)
				};
		return new ExecutionStep("Selektion", actions);
	}

	private static ExecutionStep createProjectionStep(Query query, QueryCanvas queryCanvas) {
		AbstractAction[] actions = new AbstractAction[] {
				new MarkProjectAction(query, queryCanvas),
				new ExecuteProjectAction(query)
				};
		return new ExecutionStep("Projektion", actions);
	}

	private static ExecutionStep createGroupByStep(Query query, QueryCanvas queryCanvas) {
		ArrayList<ArrayList<Row>> bucketList = new ArrayList<ArrayList<Row>>();
		AbstractAction[] actions = new AbstractAction[] {
				new PrepareGroupsAction(query, bucketList),
				new PrepareAggregationAction(query, bucketList),
				new ExecuteAggregationAction(query, bucketList),
				new FinishGroupByAction(query, bucketList)
				};
		return new ExecutionStep("Gruppierung", actions);
	}
	

	private static ExecutionStep createHavingStep(Query query, QueryCanvas queryCanvas) {
		AbstractAction[] actions = new AbstractAction[] {
				new MarkSelectAction(query, queryCanvas, false),
				new ExecuteSelectAction(query, false)
				};
		return new ExecutionStep("Selektion nach Gruppierung", actions);
	}

	private static ExecutionStep createOrderByStep(Query query, QueryCanvas queryCanvas) {
		AbstractAction[] action = new AbstractAction[] {new OrderByAction(query, queryCanvas)};
		return new ExecutionStep("Sortierung", action);
	}

	
	private AllRelationCanvas prepare(AllRelationCanvas arcPrev) {
		try {
			for (AbstractAction a : actions) {
				arcPrev = a.perform(arcPrev);
			}
		} catch (PerformActionException e) {
			preparationException = e;
		}
		return arcPrev;
	}
	
	public void doAnimation(CanvasPanel canvasPanel) {
		for (animationStep = 0; animationStep < actions.length; animationStep++) {
			actions[animationStep].doAnimation(canvasPanel);
		}		
	}

	
	public void doNextAnimationStep(CanvasPanel canvasPanel) {
		if (animationStep < actions.length) {
			actions[animationStep].doAnimation(canvasPanel);
			animationStep++;
		}		
	}

	
	public void gotoResult(CanvasPanel canvasPanel) {
		actions[actions.length-1].gotoResult(canvasPanel);
	}

	public void gotoAnimationProgress(CanvasPanel canvasPanel, double progress) {
		int actionIndex = (int)(progress / (1d / actions.length));
		actions[actionIndex].gotoAnimationProgress(canvasPanel, (progress - (actionIndex * (1d / actions.length)))* actions.length);
	}
	
	public boolean isFinishedAnimating() {
		return animationStep == actions.length;
	}

	public boolean isInStartPosition() {
		return animationStep == 0;
	}

	public void resetAnimationProgress() {
		animationStep = 0;
	}
}
