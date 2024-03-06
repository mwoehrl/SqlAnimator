package de.mwoehrl.sqlanimator;

import de.mwoehrl.sqlanimator.execution.ExecutionStep;
import de.mwoehrl.sqlanimator.renderer.QueryCanvas;

public class PreparedAnimation {
	public final ExecutionStep[] steps;
	public final QueryCanvas queryCanvas;
	
	public PreparedAnimation(ExecutionStep[] steps, QueryCanvas queryCanvas) {
		this.steps = steps;
		this.queryCanvas = queryCanvas;		
	}
	
}
