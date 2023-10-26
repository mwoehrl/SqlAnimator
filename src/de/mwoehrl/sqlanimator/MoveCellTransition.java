package de.mwoehrl.sqlanimator;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;

import de.mwoehrl.sqlanimator.renderer.AbsoluteCellPosition;

public class MoveCellTransition extends CellTransition{
	private final AbsoluteCellPosition startPos;
	private final AbsoluteCellPosition endPos;
	private final Image img;	
	
	public MoveCellTransition(AbsoluteCellPosition startPos, AbsoluteCellPosition endPos, boolean alwaysSource) {
		this.endPos=endPos;
		this.startPos=startPos;
		if (!alwaysSource && (endPos.getH() > startPos.getH())) {
			img = endPos.getCellCanvas().drawImage();
		} else {
			img = startPos.getCellCanvas().drawImage();
		}
	}

	
	public MoveCellTransition(AbsoluteCellPosition startPos, AbsoluteCellPosition endPos) {
		this(startPos, endPos, false);
	}

	@Override
	public void drawCellInTransition(Graphics g, double progress) {
		progress = (1d - Math.cos(progress * Math.PI))/2;	//Runde Bewegung!
		//progress = (Math.sin((progress-0.3)*6)+(progress-0.3)*3.5)*0.28+0.56;//Bounce
		int x = (int)(startPos.getX() + ((endPos.getX() - startPos.getX()) * progress));
		int y = (int)(startPos.getY() + ((endPos.getY() - startPos.getY()) * progress));
		int w = (int)(startPos.getW() + ((endPos.getW() - startPos.getW()) * progress));
		int h = (int)(startPos.getH() + ((endPos.getH() - startPos.getH()) * progress));
		
		RenderingHints rh = new RenderingHints(RenderingHints.KEY_TEXT_ANTIALIASING,
	            RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		((Graphics2D)g).setRenderingHints(rh);

		g.drawImage(img, x, y, w, h, null);
	}
}
