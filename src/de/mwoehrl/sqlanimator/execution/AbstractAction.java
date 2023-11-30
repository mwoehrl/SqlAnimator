package de.mwoehrl.sqlanimator.execution;

import java.util.ArrayList;

import de.mwoehrl.sqlanimator.query.Query;
import de.mwoehrl.sqlanimator.relation.Relation;
import de.mwoehrl.sqlanimator.renderer.AbsoluteCellPosition;
import de.mwoehrl.sqlanimator.renderer.AllRelationCanvas;
import de.mwoehrl.sqlanimator.renderer.CanvasPanel;
import de.mwoehrl.sqlanimator.renderer.RenderCanvas;
import de.mwoehrl.sqlanimator.renderer.TransitionCanvas;

public abstract class AbstractAction {
	protected static final int defaultSteps = 20;
	public final int animationFrameCount;
	protected final Query query;
	protected CellTransition[] transitions;
	protected AllRelationCanvas resultingCanvas;
	protected AllRelationCanvas showARCinAnimation;
	private int animationStep;
	protected boolean showResultAfterAnimation = false;
	
	protected AbstractAction(Query query) {
		this(query, defaultSteps);
	}

	public AbstractAction(Query query, int stepCount) {
		this.query = query;
		this.animationFrameCount = stepCount;
	}

	public abstract AllRelationCanvas perform(AllRelationCanvas prevARC) throws PerformActionException;

	public void doAnimation(CanvasPanel canvasPanel) {
		RenderCanvas[] staticCanvases;
		if (showARCinAnimation != null) {
			staticCanvases = new RenderCanvas[] { canvasPanel.getLeftSideCanvas(), showARCinAnimation };
		} else {
			staticCanvases = new RenderCanvas[] { canvasPanel.getLeftSideCanvas() };
		}
		
		TransitionCanvas transCanvas = new TransitionCanvas(transitions, canvasPanel.getWidth(),
				canvasPanel.getHeight(), staticCanvases);
		canvasPanel.setTransitionCanvas(transCanvas);

		for (animationStep = 0; animationStep < animationFrameCount; animationStep++) {
			transCanvas.setProgress(((double)animationStep) / animationFrameCount);
			canvasPanel.repaint();
			try {
				Thread.sleep(40);
			} catch (InterruptedException e) {
			}
		}
		transCanvas.setProgress(1d);
		canvasPanel.repaint();

		if (showResultAfterAnimation ) canvasPanel.setRenderCanvas(resultingCanvas);
		animationStep = 0;
	}
	
	protected CellTransition[] matchTransitions(AbsoluteCellPosition[] src, AbsoluteCellPosition[] dest) {
		return matchTransitions(src, dest, true);
	}
	
	protected CellTransition[] matchTransitions(AbsoluteCellPosition[] src, AbsoluteCellPosition[] dest, boolean showVanishing) {
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
	
	public AllRelationCanvas cloneCells(AllRelationCanvas arc) {
		Relation[] relations = arc.getRelations();
		for (int i = 0; i < relations.length; i++) {
			relations[i].cloneAllCells();
		}
		AllRelationCanvas newARC = new AllRelationCanvas(relations, arc.getPosition());
		return newARC;
	}
	
	public void gotoResult(CanvasPanel canvasPanel) {
		canvasPanel.setRenderCanvas(resultingCanvas);
	}

	public void gotoAnimationProgress(CanvasPanel canvasPanel, double progress) {
		RenderCanvas[] staticCanvases;
		if (showARCinAnimation != null) {
			staticCanvases = new RenderCanvas[] { canvasPanel.getLeftSideCanvas(), showARCinAnimation };
		} else {
			staticCanvases = new RenderCanvas[] { canvasPanel.getLeftSideCanvas() };
		}
		
		TransitionCanvas transCanvas = new TransitionCanvas(transitions, canvasPanel.getWidth(),
				canvasPanel.getHeight(), staticCanvases);
		canvasPanel.setTransitionCanvas(transCanvas);

		transCanvas.setProgress(progress);
	}

	public int getFrameCount() {
		return animationFrameCount;
	}

	public int getCurrentActionFrame() {
		return animationStep;
	}
}
