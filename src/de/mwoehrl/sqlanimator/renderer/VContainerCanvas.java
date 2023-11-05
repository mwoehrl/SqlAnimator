package de.mwoehrl.sqlanimator.renderer;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public class VContainerCanvas extends RenderCanvas {

	protected final RenderCanvas[] content;

	public VContainerCanvas(RenderCanvas[] content) {
		this.content = content;
		calculateRequiredSizes();
		setPositions();
	}	

	private void calculateRequiredSizes() {
		double w = 0d;
		double h = 0d;
		for (int i = 0; i < content.length; i++) {
			h += content[i].requiredSize.getHeight();
			if (content[i].requiredSize.getWidth() > w) w = content[i].requiredSize.getWidth();
		}		
		requiredSize = new Rectangle2D.Double(0, 0, w, h);
	}

	private void setPositions() {
		int ypos = 0;
		for (int i = 0; i < content.length; i++) {
			content[i].setPosition(0, ypos);
			ypos += content[i].requiredSize.getHeight();
		}
	}

	@Override
	public Image drawImage() {
		Image img = new BufferedImage((int)requiredSize.getWidth(), (int)requiredSize.getHeight(), BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = (Graphics2D) img.getGraphics();

		for (int i = 0; i < content.length; i++) {
			g.drawImage(content[i].drawImage(), (int)content[i].position.getX(), (int)content[i].position.getY(), null);
		}
		return img;
	}

	@Override
	public void scaleUp(double factor) {
	}

	@Override
	public Object getCoreObject() {
		return null;
	}
}
