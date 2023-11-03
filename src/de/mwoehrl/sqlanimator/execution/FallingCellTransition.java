package de.mwoehrl.sqlanimator.execution;

import java.awt.Graphics;
import java.awt.Image;

import de.mwoehrl.sqlanimator.renderer.AbsoluteCellPosition;

public class FallingCellTransition extends CellTransition {

	private final AbsoluteCellPosition cell;
	private final AbsoluteCellPosition bottom;
	private final double depth;
	private final Image img;
	
	public FallingCellTransition(AbsoluteCellPosition cell, AbsoluteCellPosition bottom, double depth) {
		this.cell = cell;
		this.depth = depth;
		this.bottom = bottom;
		this.img = cell.getCellCanvas().drawImage();
	}
	
	@Override
	public void drawCellInTransition(Graphics g, double progress) {
		double fall = depth * progress * progress;
		double y = (int)(cell.getY() + fall);
		if (y < bottom.getY()) {
			g.drawImage(img, (int)cell.getX(), (int)y, null);
		}
	}

}
