package de.mwoehrl.sqlanimator;

import java.awt.geom.Rectangle2D;

import de.mwoehrl.sqlanimator.execution.ExecutionStep;
import de.mwoehrl.sqlanimator.relation.Relation;
import de.mwoehrl.sqlanimator.renderer.AllRelationCanvas;
import de.mwoehrl.sqlanimator.renderer.CanvasPanel;

public class ExecutionController {
	private final ExecutionStep[] allExecutionSteps;
	private final CanvasPanel canvasPanel;
	private int currentStep = 0;
	private Thread animationThread;

	public ExecutionController(ExecutionStep[] allExecutionSteps, CanvasPanel canvasPanel) {
		this.allExecutionSteps = allExecutionSteps;
		this.canvasPanel = canvasPanel;
	}

	public void playAllSteps() {
		if (animationThread == null || !animationThread.isAlive()) {
			animationThread = new Thread(new Runnable() {
				@Override
				public void run() {
					while(currentStep < allExecutionSteps.length) {
						playToNextStep();
					}
				}
			});
			animationThread.start();
		}
	}

	public void goToStep(int stepIndex) {
		// TODO: reset substeps!
		if (animationThread == null || !animationThread.isAlive()) {
			allExecutionSteps[stepIndex].gotoResult(canvasPanel);
			currentStep = stepIndex;
		}
	}

	public void goToNextStep() {
		// TODO: reset substeps!
		if (animationThread == null || !animationThread.isAlive()) {
			if (currentStep < allExecutionSteps.length - 1)
				allExecutionSteps[currentStep++].gotoResult(canvasPanel);
		}
	}

	public void goToPrevStep() {
		if (animationThread == null || !animationThread.isAlive()) {
			if (currentStep > 0) {
				if (currentStep == allExecutionSteps.length || allExecutionSteps[currentStep].isInStartPosition()) {
					if (currentStep > 1) {
						allExecutionSteps[(--currentStep) - 1].gotoResult(canvasPanel);
					} else {
						gotoStart();
					}
				} else {
					allExecutionSteps[currentStep].resetAnimationProgress();
					allExecutionSteps[currentStep - 1].gotoResult(canvasPanel);
				}
			} else {
				gotoStart();
			}
		}
	}

	private void playToNextStep() {
		int start = currentStep;
		while (currentStep < start + 1 && start < allExecutionSteps.length) {
			playToNextSubAction();
		}
	}

	public void plusButton() {
		if (animationThread == null || !animationThread.isAlive()) {
			animationThread = new Thread(new Runnable() {
				@Override
				public void run() {
					playToNextSubAction();
				}
			});
			animationThread.start();
		}
	}

	private void playToNextSubAction() {
		if (currentStep < allExecutionSteps.length) {
			allExecutionSteps[currentStep].doNextAnimationStep(canvasPanel);
			if (allExecutionSteps[currentStep].isFinishedAnimating()) {
				allExecutionSteps[currentStep].resetAnimationProgress();
				currentStep++;
			}
		}
	}

	public void gotoStart() {
		if (animationThread == null || !animationThread.isAlive()) {
			internalGotoStart();
		}
	}

	private void internalGotoStart() {
		currentStep = 0;
		canvasPanel.setRenderCanvas(new AllRelationCanvas(new Relation[0], new Rectangle2D.Double(0, 0, 0, 0)));
	}
}
