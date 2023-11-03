package de.mwoehrl.sqlanimator;

import de.mwoehrl.sqlanimator.execution.ExecutionStep;
import de.mwoehrl.sqlanimator.renderer.CanvasPanel;

public class ExecutionController {
	private final ExecutionStep[] allExecutionSteps;
	private final CanvasPanel canvasPanel;

	public ExecutionController(ExecutionStep[] allExecutionSteps, CanvasPanel canvasPanel) {
		this.allExecutionSteps = allExecutionSteps;
		this.canvasPanel = canvasPanel;
	}
	
	public void playAllSteps() {
		for (ExecutionStep step : allExecutionSteps) {
			step.doAnimation(canvasPanel);
		}
	}
	
	public void goToStep (int stepIndex) {
		allExecutionSteps[stepIndex].gotoResult(canvasPanel);
	}
	
	public void playSingleStep (int stepIndex) {
		allExecutionSteps[stepIndex].doAnimation(canvasPanel);
	}

	public void goToStepInProgress (int stepIndex, double progress) {
		allExecutionSteps[stepIndex].gotoAnimationProgress(canvasPanel, progress);
	}
	
	
}
