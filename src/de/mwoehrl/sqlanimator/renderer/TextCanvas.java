package de.mwoehrl.sqlanimator.renderer;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public class TextCanvas extends RenderCanvas {
	protected final String fontname;
	protected static final int hPadding = 2;
	protected final int padding;
	protected static final int vPadding = 3;
	protected Font font;
	protected int  fontModifiers;
	protected double scale = 1d;
	private final Color backColor;
	private final Color fontColor;
	private final Object text;
	
	public TextCanvas(Object text, Color backColor) {
		this(text, backColor, "Verdana", Font.ITALIC, Color.black);
	}

	public TextCanvas(Object text, Color backColor, String fontName, int fontModifiers, Color fontColor) {
		this(text, backColor, fontName, fontModifiers, fontColor, hPadding);
	}
	
	public TextCanvas(Object text, Color backColor, String fontName, int fontModifiers, Color fontColor, int hPadding) {
		this.text = text;
		this.backColor = backColor;
		this.fontname = fontName;
		this.fontColor = fontColor;
		this.fontModifiers = fontModifiers;
		this.font = new Font(fontName, fontModifiers, 12);
		this.padding = hPadding;
	}
	
	@Override
	public void calculateRequiredSizes(Graphics g) {
		g.setFont(font);
		requiredSize = g.getFontMetrics().getStringBounds(text.toString(),g);
		requiredSize = new Rectangle2D.Double(0, 0, requiredSize.getWidth()+ padding * 2, requiredSize.getHeight()+ 2*vPadding);
	}

	@Override
	public void setPositions(double x, double y) {
		position = new Rectangle2D.Double(x, y, 0d, 0d);
	}

	@Override
	public Image drawImage() {
		int height = (int)requiredSize.getHeight();
		int width = (int)requiredSize.getWidth();
		Image img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = (Graphics2D) img.getGraphics();

		RenderingHints rh = new RenderingHints(RenderingHints.KEY_TEXT_ANTIALIASING,
	            RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g.setRenderingHints(rh);
		
		g.setFont(font);
		if (backColor != null) {
			g.setColor(backColor);
			g.fillRect(0, 0, width, height);
		}
		g.setColor(fontColor);
		g.drawString(text.toString(), padding, height - (int)(vPadding * scale));
		
		return img;
	}

	@Override
	public void scaleUp(double factor) {
		requiredSize = new Rectangle2D.Double(0, 0, (int)(requiredSize.getWidth() * factor), (int)(requiredSize.getHeight() * factor));
		this.font = new Font(fontname, fontModifiers, (int)(12 * factor));
		this.scale = factor;
	}

	public void adjustHeight(double h) {
		requiredSize = new Rectangle2D.Double(0, 0, requiredSize.getWidth(), h);
	}

	@Override
	public Object getCoreObject() {
		return text;
	}
}
