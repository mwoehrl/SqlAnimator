package de.mwoehrl.sqlanimator.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import de.mwoehrl.sqlanimator.ExecutionController;

public class ProgressIndicator extends Control {
	private ExecutionController controller;
	private Font font;
	private int allFrameCount;
	private int[][] frameCounts;
	private BufferedImage pointer;
	private static final double verticalPointerFraction = 0.38;
	
	public ProgressIndicator() {
		try {
			pointer = javax.imageio.ImageIO.read(getClass().getResourceAsStream("/pointing.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void setController(ExecutionController controller) {
		this.controller = controller;
		this.font = new Font("Verdana", 1, 15);
		allFrameCount = 0;
		frameCounts = controller.getExecutionStepFrameCounts();
		for (int[] e : frameCounts) {
			for (int a : e) {
				allFrameCount += a;
			}
		}
	}

	@Override
	public void drawControl(Graphics2D g) {
		g.setColor(new Color(240, 240, 240));
		g.fill3DRect(xPos, yPos, width, height, true);
		int x1 = xPos + width / 4;
		int barHeight = height * 8 / 10;
		double pixPerFrame = ((double) barHeight) / allFrameCount;
		String[] labels = controller.getExecutionStepLabels();
		int r = 7;
		int textHeight = (int) (g.getFontMetrics().getStringBounds("?", g).getHeight() / 2);
		g.setFont(font);
		int yTop = yPos + height / 10;
		int y = yTop;
		for (int i = 0; i < labels.length; i++) {
			g.setColor(Color.black);
			g.drawString(labels[i], x1 + 10, y + textHeight-1);
			g.setColor(Color.red);
			g.fillOval(x1 - r, y - r, r * 2 + 1, r * 2 + 1);
			if (i < labels.length - 1) {
				int r2 = 5;
				y += pixPerFrame * frameCounts[i][0];
				for (int j = 1; j < frameCounts[i].length; j++) {
					g.fillOval(x1 - r2, y - r2, r2 * 2 + 1, r2 * 2 + 1);
					y += pixPerFrame * frameCounts[i][j];
				}
			}
		}
		g.setColor(Color.red);
		g.drawLine(x1, yTop, x1, y);
		int execStep = controller.getCurrentStep();
		int execAct = controller.getCurrentAction();
		y = yTop;
		for (int i = 0; i < execStep; i++) {
			for (int j = 0; j < frameCounts[i].length; j++) {
				y += pixPerFrame * frameCounts[i][j];
			}
		}
		for (int j = 0; j < execAct; j++) {
			y += pixPerFrame * frameCounts[execStep][j];
		}
		y+= pixPerFrame * controller.getCurrentActionFrame();
		int pointerSize = x1-20;
		g.drawImage(pointer, 10, y - (int)(verticalPointerFraction * pointerSize) , pointerSize, pointerSize, null);
	}

	@Override
	protected void doClickEvent(int x, int y) {

	}
}
