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
	private final boolean showARCinAnimation;
	
	protected AbstractAction(Query query) {
		this(query, defaultSteps, false);
	}

	public AbstractAction(Query query, int stepCount) {
		this(query, stepCount, false);
	}

	public AbstractAction(Query query, int stepCount, boolean showARCinAnimation) {
		this.query = query;
		this.animationFrameCount = stepCount;
		this.showARCinAnimation = showARCinAnimation;
	}

	public abstract AllRelationCanvas perform(AllRelationCanvas prevARC) throws PerformActionException;

	public void doAnimation(CanvasPanel canvasPanel) {
		RenderCanvas[] staticCanvases;
		if (showARCinAnimation) {
			staticCanvases = new RenderCanvas[] { canvasPanel.getQueryCanvas(), resultingCanvas };
		} else {
			staticCanvases = new RenderCanvas[] { canvasPanel.getQueryCanvas() };
		}
		
		TransitionCanvas transCanvas = new TransitionCanvas(transitions, canvasPanel.getWidth(),
				canvasPanel.getHeight(), staticCanvases);
		canvasPanel.setTransitionCanvas(transCanvas);

		for (int i = 0; i < animationFrameCount; i++) {
			try {
				Thread.sleep(40);
			} catch (InterruptedException e) {
			}
			transCanvas.setProgress(((double)i) / animationFrameCount);
			canvasPanel.repaint();
		}
		canvasPanel.setRenderCanvas(resultingCanvas);
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
		if (showARCinAnimation) {
			staticCanvases = new RenderCanvas[] { canvasPanel.getQueryCanvas(), resultingCanvas };
		} else {
			staticCanvases = new RenderCanvas[] { canvasPanel.getQueryCanvas() };
		}
		
		TransitionCanvas transCanvas = new TransitionCanvas(transitions, canvasPanel.getWidth(),
				canvasPanel.getHeight(), staticCanvases);
		canvasPanel.setTransitionCanvas(transCanvas);

		transCanvas.setProgress(progress);
	}
}
