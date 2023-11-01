package de.mwoehrl.sqlanimator.renderer;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.geom.Rectangle2D;

public abstract class RenderCanvas {
	protected Rectangle2D requiredSize;
	protected Rectangle2D position;
	
	public abstract void setPosition(double x, double y);
	public abstract Image drawImage();
	public abstract void scaleUp(double factor);
	public abstract Object getCoreObject();
}
