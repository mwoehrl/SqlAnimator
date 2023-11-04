package de.mwoehrl.sqlanimator;

import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

import de.mwoehrl.sqlanimator.execution.ExecutionStep;
import de.mwoehrl.sqlanimator.query.Query;
import de.mwoehrl.sqlanimator.relation.Relation;
import de.mwoehrl.sqlanimator.renderer.AllRelationCanvas;
import de.mwoehrl.sqlanimator.renderer.CanvasPanel;
import de.mwoehrl.sqlanimator.renderer.ControlPanelCanvas;
import de.mwoehrl.sqlanimator.renderer.QueryCanvas;

public class MainClass {
	
	public static void main(String[] args) throws Exception {
		int overallWidth = 1920;
		int overallHeight = overallWidth * 9 / 16;
		int queryWidth = overallWidth / 4;
		AllRelationCanvas.screenWidth = overallWidth - queryWidth;
		AllRelationCanvas.screenHeight = overallHeight;
		
		List<Relation> allRelations = readRelations();
		Query query = new Query("vorname,name AS nachname,AVG(note),COUNT(note) AS anzahl", "Schueler,Noten", "schueler_nr=nr", "nachname,vorname", "anzahl<3", "vorname");

		AllRelationCanvas arcEmpty = new AllRelationCanvas(new Relation[0], new Rectangle2D.Double(0d,0d,0d,0d));
		QueryCanvas queryCanvas = new QueryCanvas(query, queryWidth, overallHeight);
		ControlPanelCanvas controlPanel = new ControlPanelCanvas(queryWidth, queryWidth/2);
		CanvasPanel canvasPanel = new CanvasPanel(queryCanvas,controlPanel, arcEmpty);
		ExecutionController controller = new ExecutionController(ExecutionStep.createAllExecutionSteps(query, allRelations, arcEmpty, queryCanvas), canvasPanel);
		controlPanel.setController(controller);

		JFrame frame = new JFrame("Animation");
		frame.setVisible(true);
		frame.setSize(overallWidth+16, overallHeight+40);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(canvasPanel);

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
