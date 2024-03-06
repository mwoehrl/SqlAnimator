package de.mwoehrl.sqlanimator.renderer;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.LinearGradientPaint;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import de.mwoehrl.sqlanimator.query.Aggregate;

public class AggregateCellCanvas extends AbstractCellCanvas {

	private String text;
	private final Aggregate aggregate;
	private double progress = 0;
	private final double[] agrCellValues;
	private final int maxAggrCount;
	
	protected AggregateCellCanvas(String text, Aggregate aggregate, double[] agrCellValues, int maxAggrCount) {
		super(true, text);
		this.fontname = "Courier New";
		this.text = text;
		this.aggregate = aggregate;
		this.agrCellValues = agrCellValues;
		this.maxAggrCount = maxAggrCount;
	}

	public void setProgress(double p) {
		progress = p;
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
		if (isHeader) g.setPaint(new LinearGradientPaint(0f,0f,0f,height,new float[] {0.0f, 1.0f}, new Color[] {new Color(255, 255, 245), new Color(255, 255, 180)}));
		g.fillPolygon(x, y, x.length);

		g.setColor(Color.black);
		g.setStroke(new BasicStroke((int)(scale * 2.0d), BasicStroke.CAP_BUTT, BasicStroke.CAP_ROUND));
		g.drawPolyline(x, y, x.length);

		Rectangle2D requiredHeight = g.getFontMetrics().getStringBounds(text, g);

		double progressStep = 1d / (maxAggrCount + 2);
		if (progress < progressStep) {
			g.drawString(text, (int)(((width - requiredHeight.getWidth())/2d)), (int) ((height + requiredHeight.getHeight() -  5d * scale) / 2));
		} else {
			int stepCount = (int)(progress/progressStep);
			if (stepCount > agrCellValues.length) stepCount = agrCellValues.length; 
			double[] lastN = new double[stepCount];
			for (int i = 0; i < stepCount; i++) {
				lastN[i] = agrCellValues[agrCellValues.length-i-1];
			}
			String aggr = aggregate.doAggregation(lastN);
			g.drawString(aggr, (int)(((width - requiredHeight.getWidth())/2d)), (int) ((height + requiredHeight.getHeight() -  5d * scale) / 2));
		}
		g.dispose();
		return img;
	}

	@Override
	public Object getCoreObject() {
		// TODO Auto-generated method stub
		return null;
	}

}
