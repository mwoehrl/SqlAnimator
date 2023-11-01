package de.mwoehrl.sqlanimator.renderer;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public class AggregateCellCanvas extends AbstractCellCanvas {

	private String text;
	
	protected AggregateCellCanvas(String text) {
		super(true, text);
		this.fontname = "Courier New";
		this.text = text;
	}

	@Override
	public Image drawImage() {
		int height = 1+(int)requiredSize.getHeight();
		int width = 1+(int)requiredSize.getWidth();
		Image img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = (Graphics2D) img.getGraphics();

		RenderingHints rh = new RenderingHints(RenderingHints.KEY_TEXT_ANTIALIASING,
	            RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g.setRenderingHints(rh);
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
		
		g.setFont(font);
		int[] x = new int[] {(int)(2*scale), (int)(hPadding*scale), width-(int)(hPadding*scale) -1, width-(int)(2*scale)};
		int[] y = new int[] {(int)(2*scale), height-(int)(2*scale)-1, height-(int)(2*scale)-1, (int)(2*scale)};
		g.setColor(new Color(255, 255, 192));
		g.fillPolygon(x, y, x.length);

		g.setColor(Color.black);
		g.setStroke(new BasicStroke((int)(scale * 1.0d), BasicStroke.CAP_BUTT, BasicStroke.CAP_ROUND));
		g.drawPolyline(x, y, x.length);

		Rectangle2D requiredHeight = g.getFontMetrics().getStringBounds(text, g);

		g.drawString(text, (int)(((width - requiredHeight.getWidth())/2d)), (int) ((height + requiredHeight.getHeight() -  5d * scale) / 2));
		return img;
	}

	@Override
	public Object getCoreObject() {
		// TODO Auto-generated method stub
		return null;
	}

}
