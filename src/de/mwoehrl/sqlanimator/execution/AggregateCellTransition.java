package de.mwoehrl.sqlanimator.execution;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import de.mwoehrl.sqlanimator.renderer.AbsoluteCellPosition;
import de.mwoehrl.sqlanimator.renderer.AggregateCellCanvas;

public class AggregateCellTransition extends CellTransition {
	private final AbsoluteCellPosition startPos;
	
	public AggregateCellTransition(AbsoluteCellPosition startPos) {
		this.startPos=startPos;
	}

	@Override
	public void drawCellInTransition(Graphics g, double progress) {
		progress = progress * progress;	//quadratischer Fortschritt, entspricht der Fallbeschleunigung 

		((AggregateCellCanvas)startPos.getCellCanvas()).setProgress(progress);
		
		int x = (int)startPos.getX();
		int y = (int)startPos.getY();
		int w = (int)startPos.getW();
		int h = (int)startPos.getH();
		
		RenderingHints rh = new RenderingHints(RenderingHints.KEY_TEXT_ANTIALIASING,
	            RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		((Graphics2D)g).setRenderingHints(rh);

		g.drawImage(startPos.getCellCanvas().drawImage(), x, y, w, h, null);
	}
}
