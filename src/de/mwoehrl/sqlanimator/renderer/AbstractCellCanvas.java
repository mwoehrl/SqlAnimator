package de.mwoehrl.sqlanimator.renderer;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public abstract class AbstractCellCanvas extends RenderCanvas {
	protected static final int initialFontSize = 24;
	protected String fontname = "Verdana";
	protected static final int hPadding = 10;
	protected static final int vPadding = 5;
	protected Font font;
	protected final boolean isHeader;
	protected double scale = 1d;
	protected boolean isEvenLine = false;

	protected AbstractCellCanvas(boolean isHeader, String text) {
		this.isHeader = isHeader;
		this.font = new Font(fontname, isHeader ? Font.BOLD : 0 , initialFontSize);
		calculateRequiredSizes(text);
	}
	
	public void calculateRequiredSizes(String text) {
		Graphics g = new BufferedImage(16,16, BufferedImage.TYPE_INT_ARGB).getGraphics();
		g.setFont(font);
		requiredSize = g.getFontMetrics().getStringBounds(text,g);
		requiredSize = new Rectangle2D.Double(0, 0, requiredSize.getWidth()+ 2*hPadding, requiredSize.getHeight()+ 2*vPadding);
	}

	@Override
	public void setPosition(double x, double y) {
		position = new Rectangle2D.Double(x, y, 0d, 0d);
	}

	public void adjustWidth(double maxWidth) {
		requiredSize = new Rectangle2D.Double(0, 0, maxWidth, requiredSize.getHeight());
	}

	@Override
	public void scaleUp(double factor) {
		requiredSize = new Rectangle2D.Double(0, 0, (int)(requiredSize.getWidth() * factor), (int)(requiredSize.getHeight() * factor));
		this.font = new Font(fontname, isHeader ? Font.BOLD : 0 , (int)(initialFontSize * this.scale * factor));
		this.scale *= factor;
	}
	
	protected Color getOddColor(Color color) {
		if (isEvenLine) {
			return new Color(color.getRed() * 0.9f / 256f, color.getGreen() * 0.9f / 256f, color.getBlue() * 0.9f / 256f);
		} else {
			return color;
		}
	}

}
