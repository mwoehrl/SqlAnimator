package de.mwoehrl.sqlanimator;

import java.awt.Graphics;
import java.awt.Image;

import de.mwoehrl.sqlanimator.renderer.AbsoluteCellPosition;

public class VanishingCellTransition extends CellTransition {

	private final AbsoluteCellPosition absoluteCellPosition;
	private final Image img;	

	public VanishingCellTransition(AbsoluteCellPosition absoluteCellPosition) {
		this.absoluteCellPosition = absoluteCellPosition;
		img = absoluteCellPosition.getCellCanvas().drawImage();
	}

	@Override
	public void drawCellInTransition(Graphics g, double progress) {
		progress = Math.sqrt(progress);
		g.drawImage(img,
				absoluteCellPosition.getX() + (int)(absoluteCellPosition.getW() * progress / 2),
				absoluteCellPosition.getY() + (int)(absoluteCellPosition.getH() * progress / 2),
				(int)(absoluteCellPosition.getW() * (1d-progress)),
				(int)(absoluteCellPosition.getH() * (1d-progress)),
				null);

	}

}
