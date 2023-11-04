package de.mwoehrl.sqlanimator.gui;

import java.awt.Graphics2D;

public abstract class Control {
	protected int xPos;
	protected int yPos;
	protected int width;
	protected int height;

	public abstract void drawControl(Graphics2D g);

	public boolean catchClick(int x, int y) {
		boolean result = ((x >= xPos && x < xPos + width) && (y >= yPos && y < yPos + height));
		if (result)
			doClickEvent(x, y);
		return result;
	}

	protected abstract void doClickEvent(int x, int y);

	public void setPosition(int x, int y) {
		xPos = x;
		yPos = y;
	}

	public void setSize(int w, int h) {
		width = w;
		height = h;
	}

}
