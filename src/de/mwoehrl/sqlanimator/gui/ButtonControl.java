package de.mwoehrl.sqlanimator.gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;

public class ButtonControl extends Control {
	private ButtonActionListener listener;
	private final Image img;
	private static final Color backColor = new Color(230,230,230);
	
	public ButtonControl(Image img) {
		this.img = img;
	}
	
	@Override
	public void drawControl(Graphics2D g) {
		g.setColor(backColor);
		g.setStroke(new BasicStroke(5, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER));

		g.fill3DRect(xPos, yPos, width - 1, height - 1, true);
		int padding = (int)(width * 0.90);
		g.drawImage(img, xPos + padding, yPos+padding, width-padding-padding, height-padding-padding, null);
	}

	public void setActionListener(ButtonActionListener listener) {
		this.listener = listener;
	}
	
	@Override
	protected void doClickEvent(int x, int y) {
		listener.onAction();
	}

}
