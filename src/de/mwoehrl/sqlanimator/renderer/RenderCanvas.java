package de.mwoehrl.sqlanimator.renderer;

import java.awt.Image;
import java.awt.geom.Rectangle2D;

public abstract class RenderCanvas {
	protected Rectangle2D requiredSize;
	protected Rectangle2D position;
	
	public abstract Image drawImage();
	public abstract void scaleUp(double factor);
	public abstract Object getCoreObject();
	
	public void setPosition(double x, double y) {
		position = new Rectangle2D.Double(x, y, 0d, 0d);
	}

}
