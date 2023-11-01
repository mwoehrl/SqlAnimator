package de.mwoehrl.sqlanimator.renderer;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public class HContainerCanvas extends RenderCanvas {
	private final RenderCanvas[] content;

	public HContainerCanvas(RenderCanvas[] content) {
		this.content = content;
		BufferedImage img = new BufferedImage(16,16, BufferedImage.TYPE_INT_ARGB);
		calculateRequiredSizes(img.getGraphics());
		setPositions();
	}	

	public void calculateRequiredSizes(Graphics g) {
		double w = 0d;
		double h = 0d;
		for (int i = 0; i < content.length; i++) {
			w += content[i].requiredSize.getWidth();
			if (content[i].requiredSize.getHeight() > h) h = content[i].requiredSize.getHeight();
		}		
		requiredSize = new Rectangle2D.Double(0, 0, w, h);
	}

	@Override
	public void setPosition(double x, double y) {
		position = new Rectangle2D.Double(x, y, 0, 0);
	}

	public void setPositions() {
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

		RenderingHints rh = new RenderingHints(RenderingHints.KEY_TEXT_ANTIALIASING,
	            RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g.setRenderingHints(rh);
		
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
