package de.mwoehrl.sqlanimator.execution;

import java.awt.Graphics;
import java.awt.Image;

import de.mwoehrl.sqlanimator.renderer.AbsoluteCellPosition;

public class MoveCellTransition extends CellTransition{
	private final AbsoluteCellPosition startPos;
	private final AbsoluteCellPosition endPos;
	private Image img;	
	private boolean imgIsStart;
	private final boolean alwaysSource;
	
	public MoveCellTransition(AbsoluteCellPosition startPos, AbsoluteCellPosition endPos, boolean alwaysSource) {
		this.endPos=endPos;
		this.startPos=startPos;
		img = startPos.getCellCanvas().drawImage();
		imgIsStart = true;
		this.alwaysSource = alwaysSource;
	}

	
	public MoveCellTransition(AbsoluteCellPosition startPos, AbsoluteCellPosition endPos) {
		this(startPos, endPos, false);
	}

	@Override
	public void drawCellInTransition(Graphics g, double progress) {
		progress = (1d - Math.cos(progress * Math.PI))/2;	//Runde Bewegung!

		if (imgIsStart && !alwaysSource && progress > 0.5d ) {
			img = endPos.getCellCanvas().drawImage();
			imgIsStart = false;
		}		
		if (!imgIsStart && !alwaysSource && progress < 0.5d ) {
			img = startPos.getCellCanvas().drawImage();
			imgIsStart = true;
		}		
		
		int x = (int)(startPos.getX() + (endPos.getX() - startPos.getX()) * progress);
		int y = (int)(startPos.getY() + (endPos.getY() - startPos.getY()) * progress);
		int w = (int)(startPos.getW() + (endPos.getW() - startPos.getW()) * progress);
		int h = (int)(startPos.getH() + (endPos.getH() - startPos.getH()) * progress);
		g.drawImage(img, x, y, w, h, null);
	}
}
