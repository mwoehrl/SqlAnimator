package de.mwoehrl.sqlanimator;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

import de.mwoehrl.sqlanimator.query.Query;
import de.mwoehrl.sqlanimator.relation.Relation;
import de.mwoehrl.sqlanimator.renderer.AllRelationCanvas;
import de.mwoehrl.sqlanimator.renderer.CanvasPanel;
import de.mwoehrl.sqlanimator.renderer.QueryCanvas;
import de.mwoehrl.sqlanimator.renderer.RenderCanvas;
import de.mwoehrl.sqlanimator.renderer.TransitionCanvas;

public class MainClass {
	
	public static void main(String[] args) throws Exception {
		TransitionCanvas transCanvas;
		int steps = 16;

		int overallWidth = 1904;
		int overallHeight = overallWidth * 9 / 16;
		int queryWidth = overallWidth / 4;
		
		List<Relation> allRelations = readRelations();
		
		Query query = new Query("vorname,name AS nachname,AVG(note),SUM(1) AS anzahl", "Schueler,Noten", "nr=schueler_nr", "nachname,vorname", null, "nachname");
		
		Director director = new Director(query, allRelations, overallWidth- queryWidth, overallHeight);

		AllRelationCanvas arcEmpty = new AllRelationCanvas(new Relation[0], overallWidth - queryWidth, overallHeight);
		
		QueryCanvas queryCanvas = new QueryCanvas(query, queryWidth, overallHeight);
		CanvasPanel canvasPanel = new CanvasPanel(queryCanvas, arcEmpty );
		JFrame frame = new JFrame("Animation");
		frame.setVisible(true);
		frame.setSize(overallWidth+16, overallHeight+40);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(canvasPanel);
		
		Thread.sleep(1000);		
		AllRelationCanvas arcOriginal = director.executeStep1(arcEmpty);		//Pick all Tables mentioned in FROM
		
		director.animateTableNames(queryCanvas, arcOriginal);

		transCanvas = new TransitionCanvas(director.transitions, overallWidth, overallHeight, new RenderCanvas[] {queryCanvas});
		canvasPanel.setTransitionCanvas(transCanvas);
	
		for (int i = 0; i < steps; i++) {
			Thread.sleep(40);
			transCanvas.advanceProgress(1d/steps);
			canvasPanel.repaint();
		}
		
		canvasPanel.setRenderCanvas(arcOriginal);
		Thread.sleep(1000);		
		
		AllRelationCanvas arcMultiRelationCandidate = arcOriginal;
		while(arcMultiRelationCandidate.getRelations().length > 1) {
			AllRelationCanvas arcCartesianBlowUp = director.executeCartesianPrepare(arcMultiRelationCandidate);
			transCanvas = new TransitionCanvas(director.transitions, overallWidth, overallHeight, new RenderCanvas[] {queryCanvas});
			canvasPanel.setTransitionCanvas(transCanvas);

			int quickSteps=steps/2;
			for (int i = 0; i < quickSteps; i++) {
				Thread.sleep(40);
				transCanvas.advanceProgress(1d/quickSteps);
				canvasPanel.repaint();
			}
			canvasPanel.setRenderCanvas(arcCartesianBlowUp);
			
			Thread.sleep(steps*40);		
			
			AllRelationCanvas arcfilled = director.executeCartesianFill(arcCartesianBlowUp);
			transCanvas = new TransitionCanvas(director.transitions, overallWidth, overallHeight, new RenderCanvas[] {queryCanvas});
			canvasPanel.setTransitionCanvas(transCanvas);
		
			for (int i = 0; i < steps; i++) {
				Thread.sleep(40);
				transCanvas.advanceProgress(1d/steps);
				canvasPanel.repaint();
			}
			canvasPanel.setRenderCanvas(arcfilled);
			Thread.sleep(steps*40);
			
			arcfilled = director.cloneCells(arcfilled);
			
			AllRelationCanvas arcMerged = director.executeCartesianMerge(arcfilled);
			transCanvas = new TransitionCanvas(director.transitions, overallWidth, overallHeight, new RenderCanvas[] {queryCanvas});
			canvasPanel.setTransitionCanvas(transCanvas);
		
			for (int i = 0; i < quickSteps; i++) {
				Thread.sleep(40);
				transCanvas.advanceProgress(1d/quickSteps);
				canvasPanel.repaint();
			}	
	
			canvasPanel.setRenderCanvas(arcMerged);
			Thread.sleep(steps*40);
			arcMultiRelationCandidate = arcMerged;
		}
		
		AllRelationCanvas afterPotentialSelection;
		
		if (query.where != null) {
			director.animateWhereCondition(queryCanvas, arcMultiRelationCandidate);
			transCanvas = new TransitionCanvas(director.transitions, overallWidth, overallHeight, new RenderCanvas[] {queryCanvas, arcMultiRelationCandidate});
			canvasPanel.setTransitionCanvas(transCanvas);
	
			for (int i = 0; i < steps; i++) {
				Thread.sleep(40);
				transCanvas.advanceProgress(1d/steps);
				canvasPanel.repaint();
			}
			Thread.sleep(steps*100);		
			canvasPanel.setRenderCanvas(arcMultiRelationCandidate);
			
			AllRelationCanvas arcSelected = director.executeSelectionStep(arcMultiRelationCandidate);
			transCanvas = new TransitionCanvas(director.transitions, overallWidth, overallHeight, new RenderCanvas[] {queryCanvas});
			canvasPanel.setTransitionCanvas(transCanvas);
	
			for (int i = 0; i < steps; i++) {
				Thread.sleep(40);
				transCanvas.advanceProgress(1d/steps);
				canvasPanel.repaint();
			}
			canvasPanel.setRenderCanvas(arcSelected);
			Thread.sleep(steps*40);		
			afterPotentialSelection = arcSelected;
		} else {
			afterPotentialSelection = arcMultiRelationCandidate;
		}

		director.animateColumnNames(queryCanvas, afterPotentialSelection);
		transCanvas = new TransitionCanvas(director.transitions, overallWidth, overallHeight, new RenderCanvas[] {queryCanvas, afterPotentialSelection});
		canvasPanel.setTransitionCanvas(transCanvas);
	
		for (int i = 0; i < steps; i++) {
			Thread.sleep(40);
			transCanvas.advanceProgress(1d/steps);
			canvasPanel.repaint();
		}
		
		afterPotentialSelection = director.cloneCells(afterPotentialSelection);
		canvasPanel.setRenderCanvas(afterPotentialSelection);
		Thread.sleep(steps*40);		
		
		AllRelationCanvas arcProjected = director.executeProjectionStep(afterPotentialSelection);
		transCanvas = new TransitionCanvas(director.transitions, overallWidth, overallHeight, new RenderCanvas[] {queryCanvas});
		canvasPanel.setTransitionCanvas(transCanvas);
		for (int i = 0; i < steps; i++) {
			Thread.sleep(40);
			transCanvas.advanceProgress(1d/steps);
			canvasPanel.repaint();
		}
		canvasPanel.setRenderCanvas(arcProjected);
		
		Thread.sleep(steps*40);		
		arcProjected = director.cloneCells(arcProjected);

		AllRelationCanvas arcPotentiallyGrouped;
		if (query.groupby != null) {

			AllRelationCanvas arcPreGrouped = director.prepareGroupByStep(arcProjected);
			transCanvas = new TransitionCanvas(director.transitions, overallWidth, overallHeight, new RenderCanvas[] {queryCanvas});
			canvasPanel.setTransitionCanvas(transCanvas);
			for (int i = 0; i < steps; i++) {
				Thread.sleep(40);
				transCanvas.advanceProgress(1d/steps);
				canvasPanel.repaint();
			}
			canvasPanel.setRenderCanvas(arcPreGrouped);
			Thread.sleep(steps*40);		

			AllRelationCanvas arcPreAggregated = director.prepareAggregationStep(arcPreGrouped);
			transCanvas = new TransitionCanvas(director.transitions, overallWidth, overallHeight, new RenderCanvas[] {queryCanvas});
			canvasPanel.setTransitionCanvas(transCanvas);
			int quickStep = steps/2;
			for (int i = 0; i < quickStep; i++) {
				Thread.sleep(40);
				transCanvas.advanceProgress(1d/quickStep);
				canvasPanel.repaint();
			}
			canvasPanel.setRenderCanvas(arcPreAggregated);
			Thread.sleep(steps*20);		

			director.executeAggregationStep(arcPreAggregated);
			transCanvas = new TransitionCanvas(director.transitions, overallWidth, overallHeight, new RenderCanvas[] {queryCanvas});
			canvasPanel.setTransitionCanvas(transCanvas);
			for (int i = 0; i < steps; i++) {
				Thread.sleep(40);
				transCanvas.advanceProgress(1d/steps);
				canvasPanel.repaint();
			}
			
			AllRelationCanvas arcGrouped = director.finishGroupByStep(arcPreAggregated);
			transCanvas = new TransitionCanvas(director.transitions, overallWidth, overallHeight, new RenderCanvas[] {queryCanvas});
			canvasPanel.setTransitionCanvas(transCanvas);
			
			for (int i = 0; i < steps; i++) {
				Thread.sleep(40);
				transCanvas.advanceProgress(1d/steps);
				canvasPanel.repaint();
			}
			canvasPanel.setRenderCanvas(arcGrouped);
			Thread.sleep(steps*40);		

			
			arcPotentiallyGrouped = arcGrouped;
		} else {
			arcPotentiallyGrouped = arcProjected;
		}
		
		if (query.orderby != null) {
			AllRelationCanvas arcSorted = director.executeOrderByStep(arcPotentiallyGrouped);
			transCanvas = new TransitionCanvas(director.transitions, overallWidth, overallHeight, new RenderCanvas[] {queryCanvas});
			canvasPanel.setTransitionCanvas(transCanvas);
			for (int i = 0; i < steps; i++) {
				Thread.sleep(40);
				transCanvas.advanceProgress(1d/steps);
				canvasPanel.repaint();
			}
			canvasPanel.setRenderCanvas(arcSorted);
		}
	}

	private static List<Relation> readRelations() {
		List<Relation> result = null;
		try {
			result = new ArrayList<Relation>();
			List<String> allLines = Files.readAllLines(new File("data.txt").toPath());
			
			List<String> currentRelationLines = new ArrayList<String>();
			for (String line:allLines){
				if (line.equals("")) {
					result.add(new Relation(currentRelationLines));
					currentRelationLines = new ArrayList<String>();
				} else {
					currentRelationLines.add(line);
				}
			}
			if (currentRelationLines.size() > 2) {
				result.add(new Relation(currentRelationLines));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

}
