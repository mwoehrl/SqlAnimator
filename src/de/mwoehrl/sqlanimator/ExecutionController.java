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

	public void goToPrevStep() {
		if (animationThread == null || !animationThread.isAlive()) {
			if (currentStep > 0) {
				if (currentStep == allExecutionSteps.length || allExecutionSteps[currentStep].isInStartPosition()) {
					if (currentStep > 1) {
						allExecutionSteps[(--currentStep) - 1].gotoResult(canvasPanel);
						canvasPanel.getQueryCanvas().setSpotlight(allExecutionSteps[currentStep].getSpotlight());
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
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
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
			canvasPanel.getQueryCanvas().setSpotlight(allExecutionSteps[currentStep].getSpotlight());
			allExecutionSteps[currentStep].doNextAnimationStep(canvasPanel);
			if (allExecutionSteps[currentStep].isFinishedAnimating()) {
				allExecutionSteps[currentStep].resetAnimationProgress();
				currentStep++;
				if (currentStep == allExecutionSteps.length) {
					canvasPanel.getQueryCanvas().setSpotlight(-1);
				}
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
		canvasPanel.getQueryCanvas().setSpotlight(-1);
	}

	public String[] getExecutionStepLabels() {
		String[] result = new String[allExecutionSteps.length + 1];
		for (int i = 0; i < allExecutionSteps.length; i++) {
			result[i] = allExecutionSteps[i].getName();
		}
		result[result.length-1] = "Fertig";
		return result;
	}
	
	public int[][] getExecutionStepFrameCounts() {
		int[][] result = new int[allExecutionSteps.length + 1][];
		for (int i = 0; i < allExecutionSteps.length; i++) {
			result[i] = allExecutionSteps[i].getFrameCounts();
		}
		result[result.length-1] = new int[] {0};
		return result;
	}

	public int getCurrentStep() {
		return currentStep;
	}
	
	public int getCurrentAction() {
		if (currentStep < allExecutionSteps.length) {
			return allExecutionSteps[currentStep].getCurrentAction();
		} else {
			return 0;
		}
	}
	
	public int getCurrentActionFrame() {
		if (currentStep < allExecutionSteps.length) { 
			return allExecutionSteps[currentStep].getCurrentActionFrame();
		} else {
			return 0;
		}
	}
}
