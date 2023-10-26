package de.mwoehrl.sqlanimator.renderer;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;

import de.mwoehrl.sqlanimator.relation.Cell;

public abstract class AbstractCellCanvas extends RenderCanvas {
	
	protected static final String fontname = "Verdana";
	protected static final int hPadding = 5;
	protected static final int vPadding = 4;
	protected Font font;
	protected boolean isHeader;
	protected double scale = 1d;
	
	
	@Override
	public void calculateRequiredSizes(Graphics g) {
		String text = getCellText();
		g.setFont(font);
		requiredSize = g.getFontMetrics().getStringBounds(text,g);
		requiredSize = new Rectangle2D.Double(0, 0, requiredSize.getWidth()+ 2*hPadding, requiredSize.getHeight()+ 2*vPadding);
	}
	
	protected abstract String getCellText();

	@Override
	public void setPositions(double x, double y) {
		position = new Rectangle2D.Double(x, y, 0d, 0d);
	}

	public void adjustWidth(double maxWidth) {
		requiredSize = new Rectangle2D.Double(0, 0, maxWidth, requiredSize.getHeight());
	}

	@Override
	public void scaleUp(double factor) {
		requiredSize = new Rectangle2D.Double(0, 0, (int)(requiredSize.getWidth() * factor), (int)(requiredSize.getHeight() * factor));
		this.font = new Font(fontname, isHeader ? Font.BOLD : 0 , (int)(12 * factor));
		this.scale = factor;
	}

}
