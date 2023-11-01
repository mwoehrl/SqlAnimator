package de.mwoehrl.sqlanimator.renderer;

import java.awt.geom.Rectangle2D;

public class AbsoluteCellPosition {
	private final RenderCanvas cellCanvas;
	private double x;
	private double y;
	private double w;
	private double h;
	
	public AbsoluteCellPosition(double x,double y,double w,double h, RenderCanvas cellCanvas) {
		this.x=x;
		this.y=y;
		this.w=w;
		this.h=h;
		this.cellCanvas = cellCanvas;
	}
	
	public double getX() {
		return x;
	}
	
	public double getY() {
		return y;
	}
	
	public double getW() {
		return w;
	}
	
	public double getH() {
		return h;
	}
	
	public RenderCanvas getCellCanvas() {
		return cellCanvas;
	}

	public void addParentPosition(Rectangle2D position) {
		x += position.getX();
		y += position.getY();
	}

	public void matchAspectRatio(AbsoluteCellPosition other) {
		this.w = (other.w * this.h) / other.h;
	}
	
}
