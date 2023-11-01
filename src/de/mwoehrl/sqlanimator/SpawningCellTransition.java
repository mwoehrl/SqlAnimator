package de.mwoehrl.sqlanimator;

import java.awt.Graphics;
import java.awt.Image;

import de.mwoehrl.sqlanimator.renderer.AbsoluteCellPosition;

public class SpawningCellTransition extends CellTransition {

	private final AbsoluteCellPosition absoluteCellPosition;
	private final Image img;	

	public SpawningCellTransition(AbsoluteCellPosition absoluteCellPosition) {
		this.absoluteCellPosition = absoluteCellPosition;
		img = absoluteCellPosition.getCellCanvas().drawImage();
	}

	@Override
	public void drawCellInTransition(Graphics g, double progress) {
		progress *= progress;
		g.drawImage(img,
				(int)(absoluteCellPosition.getX() + absoluteCellPosition.getW() * (1d-progress) / 2),
				(int)(absoluteCellPosition.getY() + absoluteCellPosition.getH() * (1d-progress) / 2),
				(int)(absoluteCellPosition.getW() * progress),
				(int)(absoluteCellPosition.getH() * progress),
				null);

	}


}
