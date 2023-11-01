package de.mwoehrl.sqlanimator.renderer;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import de.mwoehrl.sqlanimator.AggregateCellTransition;
import de.mwoehrl.sqlanimator.CellTransition;
import de.mwoehrl.sqlanimator.FallingCellTransition;
import de.mwoehrl.sqlanimator.MoveCellTransition;
import de.mwoehrl.sqlanimator.query.Aggregate;
import de.mwoehrl.sqlanimator.relation.Cell;
import de.mwoehrl.sqlanimator.relation.Relation;
import de.mwoehrl.sqlanimator.relation.Row;

public class AggregateRelationCanvas extends RelationCanvas {

	private class TransitionPair{
		RenderCanvas start;
		RenderCanvas end;
	}
	
	private static final double scaleFactor = 0.75;
	
	private final AbstractCellCanvas[][] aggregateCells;
	private final int[] aggregateOrdinals;
	private final TransitionPair[] transitionPairs;
	private double fallingDepth = 0d;
	
	public AggregateRelationCanvas(Relation relation, ArrayList<ArrayList<Row>> bucketList, Aggregate[] aggregates) {
		super(relation, bucketList);
		ArrayList<TransitionPair> transitions = new ArrayList<TransitionPair>();
		int aggregationsCount = 0;
		TransitionPair tp = new TransitionPair();		//Cell transition onto istself
		tp.start = tableNameCanvas;
		tp.end = tp.start ;
		transitions.add(tp);
		aggregateOrdinals = new int[aggregates.length];
		int maxArgCount = 0;
		for (int c = 0; c < aggregates.length; c++) {
			tp = new TransitionPair();	//Header transition onto itself
			tp.start = cells[c][0];
			tp.end = tp.start ;
			transitions.add(tp);
			
			if (aggregates[c] != null) {
				aggregateOrdinals[aggregationsCount] = c;
				aggregationsCount++;
				for (int r = 1; r < cells[c].length; r++) {
					AbstractCellCanvas ce = cells[c][r];
					double dx = ce.requiredSize.getWidth() * (1d - scaleFactor) / 2d;
					double dy = ce.requiredSize.getHeight() * (1d - scaleFactor) / 2d;
					ce.position = new Rectangle2D.Double(ce.position.getX() + dx, ce.position.getY() + dy, 0, 0);
					ce.scaleUp(scaleFactor);
				}
			} else {
				int r = 1;
				for (int b = 0; b < bucketList.size(); b++) {
					AbstractCellCanvas endCell = cells[c][r + bucketList.get(b).size()-1];
					double h = endCell.position.getY() + endCell.requiredSize.getHeight() - cells[c][r].position.getY();
					double y = cells[c][r].position.getY();
					double x = cells[c][r].position.getX();
					double w = cells[c][r].requiredSize.getWidth();
					for (int i = 0; i < bucketList.get(b).size(); i++) {
						cells[c][r + i].setPosition(x, y);
						cells[c][r + i].requiredSize = new Rectangle2D.Double(0,0,w,h);
						tp = new TransitionPair();		//Cell transition onto istself
						tp.start = cells[c][r + i];
						tp.end = tp.start ;
						transitions.add(tp);
					}	
					r += bucketList.get(b).size();
					if (h > fallingDepth) fallingDepth = h;
					if (bucketList.get(b).size() >  maxArgCount) maxArgCount = bucketList.get(b).size(); 
				}
			}
		}
		aggregateCells = new AbstractCellCanvas[aggregationsCount][];
		aggregationsCount = 0;
		for (int c = 0; c < aggregates.length; c++) {
			if (aggregates[c] != null) {
				int r = 1;
				double x = cells[c][0].position.getX();
				double w = cells[c][0].requiredSize.getWidth();
				aggregateCells[aggregationsCount] = new AbstractCellCanvas[bucketList.size()]; 
				for (int b = 0; b < bucketList.size(); b++) {
					AbstractCellCanvas endCell = cells[c][r + bucketList.get(b).size()-1];
					double y = endCell.position.getY() + endCell.requiredSize.getHeight();
					double[] agrCellValues = new double[bucketList.get(b).size()];
					for (int i = 0; i < bucketList.get(b).size(); i++) {
						agrCellValues[i] = bucketList.get(b).get(i).getCell(c).getNumericValue();
					}
					aggregateCells[aggregationsCount][b] = new AggregateCellCanvas(aggregates[c].toString(), aggregates[c], agrCellValues, maxArgCount);
					aggregateCells[aggregationsCount][b].setPosition(x, y + aggregateCells[aggregationsCount][b].requiredSize.getHeight() * (1d - scaleFactor) / 4d);
					aggregateCells[aggregationsCount][b].adjustWidth(w);
					for (int i = 0; i < bucketList.get(b).size(); i++) {
						tp = new TransitionPair();		//Transition from Cell to its Aggregate cell
						tp.start = cells[c][r + i];
						tp.end = aggregateCells[aggregationsCount][b];
						transitions.add(tp);
					}
					tp = new TransitionPair();		//Cell transition onto istself
					tp.start = aggregateCells[aggregationsCount][b];
					tp.end = tp.start ;
					transitions.add(tp);
					r += bucketList.get(b).size();
				}
				aggregationsCount++;
			}
		}
		transitionPairs = new TransitionPair[transitions.size()];
		transitions.toArray(transitionPairs);
	}
	
	@Override
	public Image drawImage() {
		Image img = super.drawImage();
		Graphics2D g = (Graphics2D) img.getGraphics();

		for (int i = 0; i < aggregateCells.length; i++) {
			for (int j = 0; j < aggregateCells[i].length; j++) {
				AbstractCellCanvas cell = aggregateCells[i][j];
				g.drawImage(cell.drawImage(), (int)cell.position.getX(), (int)cell.position.getY(), null);				
			}
		}
		return img;		
	}
	
	@Override
	public void scaleUp(double factor) {
		super.scaleUp(factor);
		for (int i = 0; i < aggregateCells.length; i++) {
			for (int j = 0; j < aggregateCells[i].length; j++) {
				AbstractCellCanvas cell = aggregateCells[i][j];
				cell.scaleUp(factor);
				cell.setPosition(cell.position.getX() * factor, cell.position.getY() * factor);
			}
		}
		fallingDepth *= factor;
	}
	
	private AbsoluteCellPosition getAbsoluteCellPosition(RenderCanvas cell, Rectangle2D parentPosition) {
		return new AbsoluteCellPosition((int)(cell.position.getX() + position.getX() + parentPosition.getX()), (int)(cell.position.getY() + position.getY() + parentPosition.getY()), (int)(cell.requiredSize.getWidth()), (int)(cell.requiredSize.getHeight()), cell);
	}
	
	public CellTransition[] getTransitions(Rectangle2D parentPosition) {
		CellTransition[] result = new CellTransition[transitionPairs.length];
		for (int i = 0; i < transitionPairs.length; i++) {
			if (transitionPairs[i].start == transitionPairs[i].end) {	//stay in place
				if (transitionPairs[i].start instanceof AggregateCellCanvas) {
					result[i] = new AggregateCellTransition(getAbsoluteCellPosition(transitionPairs[i].start, parentPosition));
				} else {
					result[i] = new MoveCellTransition(getAbsoluteCellPosition(transitionPairs[i].start, parentPosition), getAbsoluteCellPosition(transitionPairs[i].end, parentPosition), true);
				}
			} else {
				result[i] = new FallingCellTransition(getAbsoluteCellPosition(transitionPairs[i].start, parentPosition), getAbsoluteCellPosition(transitionPairs[i].end, parentPosition), fallingDepth);
			}
			
		}
		return result;
	}
	
	public void setAggregateValuesFromRelation(Relation aggregatedRelation) {
		for (int i = 0; i < aggregateCells.length; i++) {
			for (int r=0;r < aggregatedRelation.getRows().length; r++) {
				AbstractCellCanvas oldCell = aggregateCells[i][r];
				CellCanvas newCell = new CellCanvas(aggregatedRelation.getRows()[r].getCell(aggregateOrdinals[i]));
				newCell.scaleUp(oldCell.scale);
				newCell.position = oldCell.position;
				newCell.adjustWidth(cells[aggregateOrdinals[i]][0].requiredSize.getWidth());
				aggregateCells[i][r] = newCell;
			}
		}
	}
	
	@Override
	public AbsoluteCellPosition[] getAbsoluteCellPositions() {
		if (aggregateCells.length > 0) {
			AbsoluteCellPosition[] superCellPositions = super.getAbsoluteCellPositions();
			AbsoluteCellPosition[] result = new AbsoluteCellPosition[superCellPositions.length + aggregateCells.length * aggregateCells[0].length];
			for (int i = 0; i < superCellPositions.length; i++) {
				result[i] = superCellPositions[i];
			}
			for (int c = 0; c < aggregateCells.length; c++) {
				for (int r = 0; r < aggregateCells[0].length; r++) {
					result[superCellPositions.length + aggregateCells[0].length * c + r]  = getAbsoluteCellPosition(aggregateCells[c][r], new Rectangle2D.Double(0,0,0,0)); 
				}
			}
			return result;
		} else {
			return super.getAbsoluteCellPositions();
		}
	}
}
