package de.mwoehrl.sqlanimator.gui;

import java.awt.Color;
import java.awt.Graphics2D;

public class ControlContainer extends Control {

	private final Control[] controls;

	public ControlContainer(Control[] controls) {
		this.controls = controls;
	}

	@Override
	public void drawControl(Graphics2D g) {
		g.setColor(new Color(240,240,240));
		g.fill3DRect(xPos, yPos, width, height, true);
		for (Control c : controls) {
			c.drawControl(g);
		}
	}

	@Override
	protected void doClickEvent(int x, int y) {
		for (Control c : controls) {
			if (c.catchClick(x, y)) break;
		}
	}

	public void layoutHorizontally(int padding, int spacing) {
		int elementWidth = ((width - padding - padding) - (spacing * (controls.length - 1))) / controls.length;
		int x = padding;
		for (Control c : controls) {
			c.xPos = x;
			c.yPos = padding;
			c.width = elementWidth;
			c.height = elementWidth;
			x += elementWidth + spacing;
		}
	}

	public void layoutVertically() {
		int y = 0;
		for (Control c : controls) {
			c.xPos = 0;
			c.yPos = y;
			c.width = width;
			y += c.height;
		}
	}
}
