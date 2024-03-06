package de.mwoehrl.sqlanimator.renderer;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public class HContainerCanvas extends RenderCanvas {
	private final RenderCanvas[] content;

	public HContainerCanvas(RenderCanvas[] content) {
		this.content = content;
		calculateRequiredSizes();
		setPositions();
	}	

	private void calculateRequiredSizes() {
		double w = 0d;
		double h = 0d;
		for (int i = 0; i < content.length; i++) {
			w += content[i].requiredSize.getWidth();
			if (content[i].requiredSize.getHeight() > h) h = content[i].requiredSize.getHeight();
		}		
		requiredSize = new Rectangle2D.Double(0, 0, w, h);
	}

	private void setPositions() {
		int xpos = 0;
		for (int i = 0; i < content.length; i++) {
			content[i].setPosition(xpos, 0);
			xpos += content[i].requiredSize.getWidth();
		}
	}

	@Override
	public Image drawImage() {
		Image img = new BufferedImage((int)requiredSize.getWidth(), (int)requiredSize.getHeight(), BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = (Graphics2D) img.getGraphics();

		for (int i = 0; i < content.length; i++) {
			g.drawImage(content[i].drawImage(), (int)content[i].position.getX(), (int)content[i].position.getY(), null);
		}
		g.dispose();
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
