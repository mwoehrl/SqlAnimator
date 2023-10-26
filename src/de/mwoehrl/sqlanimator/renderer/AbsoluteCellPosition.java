package de.mwoehrl.sqlanimator.renderer;

import java.awt.geom.Rectangle2D;

public class AbsoluteCellPosition {
	private final RenderCanvas cellCanvas;
	private int x;
	private int y;
	private int w;
	private int h;
	
	public AbsoluteCellPosition(int x,int y,int w,int h, RenderCanvas cellCanvas) {
		this.x=x;
		this.y=y;
		this.w=w;
		this.h=h;
		this.cellCanvas = cellCanvas;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public int getW() {
		return w;
	}
	
	public int getH() {
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
