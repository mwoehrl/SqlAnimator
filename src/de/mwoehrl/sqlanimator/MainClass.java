package de.mwoehrl.sqlanimator;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.geom.Rectangle2D;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		double screenwidth = screenSize.getWidth();

		int overallWidth = screenwidth < 1920 ? (int)screenwidth : 1920;
		int overallHeight = overallWidth * 9 / 16;
		int queryWidth = overallWidth / 4;
		
		List<Relation> allRelations = readRelations();
		Query query = readQuery();

		AllRelationCanvas.screenWidth = overallWidth - queryWidth;
		AllRelationCanvas.screenHeight = overallHeight;
		AllRelationCanvas arcEmpty = new AllRelationCanvas(new Relation[0], new Rectangle2D.Double(0d,0d,0d,0d));
		QueryCanvas queryCanvas = new QueryCanvas(query, queryWidth, overallHeight);
		ControlPanelCanvas controlPanel = new ControlPanelCanvas(queryWidth, (int)(queryWidth*1.64));
		CanvasPanel canvasPanel = new CanvasPanel(queryCanvas, controlPanel, arcEmpty);

		ExecutionStep[] allExecutionSteps = ExecutionStep.createAllExecutionSteps(query, allRelations, arcEmpty, queryCanvas);

		ExecutionController controller = new ExecutionController(allExecutionSteps, canvasPanel);
		controlPanel.setController(controller);

		JFrame frame = new JFrame("Animation");
		frame.setVisible(true);
		frame.setSize(overallWidth+16, overallHeight+40);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(canvasPanel);
	}
	
	public static PreparedAnimation prepareAnimation(int overallWidth, int overallHeight, int queryWidth) {
		List<Relation> allRelations = readRelations();
		Query query = readQuery();

		AllRelationCanvas.screenWidth = overallWidth - queryWidth;
		AllRelationCanvas.screenHeight = overallHeight;
		AllRelationCanvas arcEmpty = new AllRelationCanvas(new Relation[0], new Rectangle2D.Double(queryWidth,0d,0d,0d));
		QueryCanvas queryCanvas = new QueryCanvas(query, queryWidth, overallHeight);
		return new PreparedAnimation(ExecutionStep.createAllExecutionSteps(query, allRelations, arcEmpty, queryCanvas), queryCanvas);
	}

	private static Query readQuery() {
		List<String> lines = readLinesFromFileOrResource("query.txt");
		
		return new Query(
				lines.get(0),
				lines.get(1),
				lines.get(2),
				lines.get(3),
				lines.get(4),
				lines.get(5));
	}

	private static List<String> readLinesFromFileOrResource(String fileName){
		try {
			//Check file in execution dir
			List<String> allLines = Files.readAllLines(new File(fileName).toPath());
			return allLines;
		} catch (IOException e) {
			//Use included resource file as fallback
			try (InputStream in = MainClass.class.getResourceAsStream("/" + fileName);
				    BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
					ArrayList<String> allLines = new ArrayList<String>();
					String s = reader.readLine();
					while (s != null) {
						allLines.add(s);
						s = reader.readLine();
					}			
					return allLines;				
				} catch (IOException e1) {
					e1.printStackTrace();
				}
		}
		return null;
	}
	
	private static List<Relation> readRelations() {
		return linesToRelations(readLinesFromFileOrResource("data.txt"));
	}

	private static List<Relation> linesToRelations(List<String> allLines) {
		List<Relation> result = new ArrayList<Relation>();
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
		return result;
	}

}
