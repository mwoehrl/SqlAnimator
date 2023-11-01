package de.mwoehrl.sqlanimator;

import java.util.ArrayList;
import java.util.List;

import de.mwoehrl.sqlanimator.query.Aggregate;
import de.mwoehrl.sqlanimator.query.Query;
import de.mwoehrl.sqlanimator.query.SELECT;
import de.mwoehrl.sqlanimator.relation.Cell;
import de.mwoehrl.sqlanimator.relation.Relation;
import de.mwoehrl.sqlanimator.relation.Row;
import de.mwoehrl.sqlanimator.renderer.AbsoluteCellPosition;
import de.mwoehrl.sqlanimator.renderer.AllRelationCanvas;
import de.mwoehrl.sqlanimator.renderer.QueryCanvas;

public class Director {
	private final Query query;
	private final List<Relation> originalRelations;

	private final int screenWidth;
	private final int screenHeight;
	
	
	public CellTransition[] transitions;
	private ArrayList<ArrayList<Row>> bucketList;
	
	public Director(Query query, List<Relation> originalRelations,int screenWidth,int screenHeight) {
		this.screenHeight = screenHeight;
		this.screenWidth = screenWidth;
		this.query = query;
		this.originalRelations = originalRelations;
	}
	
	public AllRelationCanvas executeStep1(AllRelationCanvas prevARC) throws Exception {
		//Schritt 1: Leere Canvas -> Tabellen
		Relation[] step1Relations = selectTables();
		AllRelationCanvas step1TablesCanvas = new AllRelationCanvas(step1Relations, screenWidth, screenHeight);
		step1TablesCanvas.setPosition(prevARC.getPosition().getX(), prevARC.getPosition().getY());
		return step1TablesCanvas;
	}
	
	public AllRelationCanvas executeCartesianPrepare(AllRelationCanvas prevARC) {
		Relation[] relations = new Relation[prevARC.getRelations().length];
		relations[0] = prevARC.getRelations()[0].cartesianProductPrepare(prevARC.getRelations()[1].getRows().length);
		relations[1] = prevARC.getRelations()[1].cartesianProductPrepare(prevARC.getRelations()[0].getRows().length);
		for (int i = 2; i < relations.length; i++) {
			relations[i] = prevARC.getRelations()[i];
		}
		AllRelationCanvas newARC = new AllRelationCanvas(relations, screenWidth, screenHeight);
		newARC.setPosition(prevARC.getPosition().getX(), prevARC.getPosition().getY());
		transitions = matchTransitions(prevARC.getAbsoluteCellPositions(), newARC.getAbsoluteCellPositions());
		return newARC;
	}

	public AllRelationCanvas executeCartesianFill(AllRelationCanvas prevARC) {
		Relation[] relations = prevARC.getRelations();
		relations[0].multiplyRowsBlockwise();
		relations[1].separateRows();
		relations[1].multiplyRowsRowwise();
		AllRelationCanvas newARC = new AllRelationCanvas(relations, screenWidth, screenHeight);
		newARC.setPosition(prevARC.getPosition().getX(), prevARC.getPosition().getY());
		transitions = matchTransitions(prevARC.getAbsoluteCellPositions(), newARC.getAbsoluteCellPositions());
		return newARC;
	}

	public AllRelationCanvas executeCartesianMerge(AllRelationCanvas prevARC) {
		Relation[] relations = prevARC.getRelations();
		Relation mergedRelation = relations[0].cartesianMerge(relations[1]);
		Relation[] result = new Relation[relations.length - 1];
		result[0] = mergedRelation;
		for (int i = 1; i < result.length; i++) {
			result[i] = relations[i+1];
		}
		AllRelationCanvas newARC = new AllRelationCanvas(result, screenWidth, screenHeight);
		newARC.setPosition(prevARC.getPosition().getX(), prevARC.getPosition().getY());
		transitions = matchTransitions(prevARC.getAbsoluteCellPositions(), newARC.getAbsoluteCellPositions());
		return newARC;
	}

	
	public AllRelationCanvas executeProjectionStep(AllRelationCanvas prevARC) throws Exception {
		Relation projectedRelations = prevARC.getRelations()[0].projection(query.select);
		AllRelationCanvas newARC = new AllRelationCanvas(new Relation[] {projectedRelations}, screenWidth, screenHeight);
		newARC.setPosition(prevARC.getPosition().getX(), prevARC.getPosition().getY());
		AbsoluteCellPosition[] toCellsDest = newARC.getAbsoluteCellPositions();
		AbsoluteCellPosition[] fromPositions = prevARC.getAbsoluteCellPositions();
		transitions = matchTransitions(fromPositions, toCellsDest);
		return newARC;

	}

	public AllRelationCanvas executeSelectionStep(AllRelationCanvas prevARC, boolean where) throws Exception {
		Relation selectedRelations = prevARC.getRelations()[0].selection(where ? query.where : query.having);
		AllRelationCanvas newARC = new AllRelationCanvas(new Relation[] {selectedRelations}, screenWidth, screenHeight);
		newARC.setPosition(prevARC.getPosition().getX(), prevARC.getPosition().getY());
		AbsoluteCellPosition[] toCellsDest = newARC.getAbsoluteCellPositions();
		AbsoluteCellPosition[] fromPositions = prevARC.getAbsoluteCellPositions();
		transitions = matchTransitions(fromPositions, toCellsDest);
		return newARC;
	}


	public AllRelationCanvas executeOrderByStep(AllRelationCanvas prevARC) throws Exception {
		Relation orderedRelation = prevARC.getRelations()[0].orderBy(query.orderby);
		AllRelationCanvas newARC = new AllRelationCanvas(new Relation[] {orderedRelation}, screenWidth, screenHeight);
		newARC.setPosition(prevARC.getPosition().getX(), prevARC.getPosition().getY());
		AbsoluteCellPosition[] toCellsDest = newARC.getAbsoluteCellPositions();
		AbsoluteCellPosition[] fromPositions = prevARC.getAbsoluteCellPositions();
		transitions = matchTransitions(fromPositions, toCellsDest);
		return newARC;
	}

	public AllRelationCanvas prepareGroupByStep(AllRelationCanvas prevARC) throws Exception {
		bucketList = new ArrayList<ArrayList<Row>>();
		Relation preGroupedRelation = prevARC.getRelations()[0].prepareGroupBy(query.groupby, bucketList);
		
		AllRelationCanvas newARC = new AllRelationCanvas(preGroupedRelation, screenWidth, screenHeight, bucketList);
		newARC.setPosition(prevARC.getPosition().getX(), prevARC.getPosition().getY());
		AbsoluteCellPosition[] toCellsDest = newARC.getAbsoluteCellPositions();
		AbsoluteCellPosition[] fromPositions = prevARC.getAbsoluteCellPositions();
		transitions = matchTransitions(fromPositions, toCellsDest);
		return newARC;
	}

	public AllRelationCanvas prepareAggregationStep(AllRelationCanvas prevARC) throws Exception {
		Aggregate[] aggregates = new Aggregate[query.select.getProjectionColumns().length];
		for (int i = 0; i < aggregates.length; i++) {
			aggregates[i] = query.select.getProjectionColumns()[i].aggregate;
		}
		AllRelationCanvas newARC = new AllRelationCanvas(prevARC.getRelations()[0], aggregates, screenWidth, screenHeight, bucketList);
		newARC.setPosition(prevARC.getPosition().getX(), prevARC.getPosition().getY());
		AbsoluteCellPosition[] toCellsDest = newARC.getAbsoluteCellPositions();
		AbsoluteCellPosition[] fromPositions = prevARC.getAbsoluteCellPositions();
		transitions = matchTransitions(fromPositions, toCellsDest);
		return newARC;
	}
	
	public AllRelationCanvas executeAggregationStep(AllRelationCanvas prevARC) throws Exception {
		transitions = prevARC.getAggregateTransistions();
		return prevARC;
	}	
	
	public AllRelationCanvas finishGroupByStep(AllRelationCanvas prevARC) throws Exception {
		Relation groupedRelation = prevARC.getRelations()[0].finishGroupBy(query.select, bucketList);
		prevARC.setAggregateValuesFromRelation(groupedRelation);
		
		AllRelationCanvas newARC = new AllRelationCanvas(new Relation[] {groupedRelation}, screenWidth, screenHeight);
		newARC.setPosition(prevARC.getPosition().getX(), prevARC.getPosition().getY());
		AbsoluteCellPosition[] toCellsDest = newARC.getAbsoluteCellPositions();
		AbsoluteCellPosition[] fromPositions = prevARC.getAbsoluteCellPositions();
		transitions = matchTransitions(fromPositions, toCellsDest, false);
		return newARC;
	}
		
	private CellTransition[] matchTransitions(AbsoluteCellPosition[] src, AbsoluteCellPosition[] dest) {
		return matchTransitions(src, dest, true);
	}
	
	private CellTransition[] matchTransitions(AbsoluteCellPosition[] src, AbsoluteCellPosition[] dest, boolean showVanishing) {
		ArrayList<CellTransition> result = new ArrayList<CellTransition>(); 
		for (int i = 0; i < dest.length; i++) {
			for (int j = 0; j < src.length; j++) {
				if (dest[i].getCellCanvas().getCoreObject() != null && dest[i].getCellCanvas().getCoreObject()==src[j].getCellCanvas().getCoreObject()) {
					result.add(new MoveCellTransition(src[j], dest[i]));
				}
			}
		}
		ArrayList<AbsoluteCellPosition> lost = new ArrayList<AbsoluteCellPosition>(); 
		for (int j = 0; j < src.length && showVanishing; j++) {
			boolean found = false;
			for (int i = 0; i < dest.length && !found; i++) {
				if (dest[i].getCellCanvas().getCoreObject() != null && dest[i].getCellCanvas().getCoreObject()==src[j].getCellCanvas().getCoreObject()) {
					found = true;
				}
			}
			if (!found) {
				lost.add(src[j]);
			}
		}
		ArrayList<AbsoluteCellPosition> spawning = new ArrayList<AbsoluteCellPosition>(); 
		for (int j = 0; j < dest.length; j++) {
			boolean found = false;
			for (int i = 0; i < src.length && !found; i++) {
				if (src[i].getCellCanvas().getCoreObject() != null && src[i].getCellCanvas().getCoreObject()==dest[j].getCellCanvas().getCoreObject()) {
					found = true;
				}
			}
			if (!found) {
				spawning.add(dest[j]);
			}
		}
		
		CellTransition[] merged = new CellTransition[result.size() + lost.size() + spawning.size()];
		for (int i = 0; i < lost.size(); i++) {
			merged[i] = new VanishingCellTransition(lost.get(i));
		}
		for (int i = 0; i < spawning.size(); i++) {
			merged[i + lost.size()] = new SpawningCellTransition(spawning.get(i));
		}
		for (int i = 0; i < result.size(); i++) {
			merged[i + lost.size() + spawning.size()] = result.get(i);
		}
		return merged;
	}

	public Relation[] selectTables() throws Exception {
		String[] tableNames = query.from.getFromTables();
		Relation[] relationsResult = new Relation[tableNames.length];
		int index = 0;
		for (int i = 0; i < tableNames.length; i++) {
			for (Relation r : originalRelations) {
				if (r.getName().equals(tableNames[i])) {
					relationsResult[index++] = r.changeName(tableNames[i]);
				}
			}
		}
		if (index != tableNames.length)
			throw new Exception("Table name not found");
		return relationsResult;
	}

	public AllRelationCanvas cloneCells(AllRelationCanvas arc) {
		Relation[] relations = arc.getRelations();
		for (int i = 0; i < relations.length; i++) {
			relations[i].cloneAllCells();
		}
		AllRelationCanvas newARC = new AllRelationCanvas(relations, screenWidth, screenHeight);
		newARC.setPosition(arc.getPosition().getX(), arc.getPosition().getY());
		return newARC;
	}

	public void animateTableNames(QueryCanvas queryCanvas, AllRelationCanvas arc) {
		AbsoluteCellPosition[] queryCells = queryCanvas.getTableCellAbsolutePositions();
		AbsoluteCellPosition[] tableCells = arc.getTableNameCellPosition();

		ArrayList<CellTransition> result = new ArrayList<>();
		for (int i = 0; i < queryCells.length; i++) {
			for (int j = 0; j < tableCells.length; j++) {
				if (queryCells[i].getCellCanvas().getCoreObject()==tableCells[j].getCellCanvas().getCoreObject()) {
					result.add(new MoveCellTransition(queryCells[i], tableCells[j]));
				}
			}
		}
		transitions = new CellTransition[result.size()];
		result.toArray(transitions);
	}
	
	public void animateColumnNames(QueryCanvas queryCanvas, AllRelationCanvas arc) {
		AbsoluteCellPosition[] queryCells = queryCanvas.getColumnCellAbsolutePositions();
		AbsoluteCellPosition[] tableCells = arc.getHeaderCellPositions();

		ArrayList<CellTransition> result = new ArrayList<>();
		for (int i = 0; i < queryCells.length; i++) {
			for (int j = 0; j < tableCells.length; j++) {
				Cell c = (Cell)tableCells[j].getCellCanvas().getCoreObject();
				if (queryCells[i].getCellCanvas().getCoreObject().equals(c.getValue())) {
					tableCells[j].matchAspectRatio(queryCells[i]);
					result.add(new MoveCellTransition(queryCells[i], tableCells[j], true));
				}
			}
		}
		transitions = new CellTransition[result.size()];
		result.toArray(transitions);
	}
	
	public void animateWhereCondition(QueryCanvas queryCanvas, AllRelationCanvas arc, boolean where) {
		AbsoluteCellPosition[] tableCells = arc.getFirstColumnCellPositions();
		AbsoluteCellPosition[] queryCells = queryCanvas.getWhereTicks(tableCells.length, arc.getRelations()[0], where);

		ArrayList<CellTransition> result = new ArrayList<>();
			for (int j = 0; j < tableCells.length; j++) {
				result.add(new MoveCellTransition(queryCells[j], tableCells[j], true));
			}
		transitions = new CellTransition[result.size()];
		result.toArray(transitions);
	}	
	
}
