package de.mwoehrl.sqlanimator.renderer;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;

public class EmptyCellCanvas extends AbstractCellCanvas{


	protected EmptyCellCanvas() {
		super(false, "?");
	}

	@Override
	public Image drawImage() {
		int height = (int)requiredSize.getHeight();
		int width = (int)requiredSize.getWidth();
		Image img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = (Graphics2D) img.getGraphics();
		g.setColor(Color.YELLOW);
		g.fillRect(0, 0, width, height);
		return img;
	}

	@Override
	public Object getCoreObject() {
		return null;
	}

	
}
