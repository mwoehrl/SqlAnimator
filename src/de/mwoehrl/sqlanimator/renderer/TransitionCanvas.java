package de.mwoehrl.sqlanimator.renderer;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import de.mwoehrl.sqlanimator.execution.CellTransition;

public class TransitionCanvas extends RenderCanvas {

	private final CellTransition[] transitions;
	private double progress = 0d;    //0...1
	private final int width;
	private final int height;
	private RenderCanvas[] staticCanvases;
	
	public TransitionCanvas(CellTransition[] transitions, int w, int h) {
		this(transitions, w, h, new RenderCanvas[0]);
	}
	
	public TransitionCanvas(CellTransition[] transitions, int w, int h, RenderCanvas[] staticCanvases) {
		this.transitions = transitions;
		this.width = w;
		this.height = h;
		this.staticCanvases = staticCanvases;
	}
	
	@Override
	public void setPosition(double x, double y) {
	}

	@Override
	public Image drawImage() {
		Image img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = (Graphics2D)img.getGraphics();

		g.setColor(Color.white);
		g.fillRect(0, 0, width, height);

		RenderingHints rh = new RenderingHints(RenderingHints.KEY_INTERPOLATION,
	            RenderingHints.VALUE_INTERPOLATION_BICUBIC);
		g.setRenderingHints(rh);

		for (RenderCanvas rc : staticCanvases) {
			g.drawImage(rc.drawImage(), (int)rc.position.getX(), (int)rc.position.getY(), null);
		}
		
		for (CellTransition trans : transitions) {
			trans.drawCellInTransition(g, progress);
		}
		return img;
	}

	@Override
	public void scaleUp(double factor) {
	}

	@Override
	public Object getCoreObject() {
		return null;
	}

	public void setProgress(double p) {
		this.progress = p;
	}
}
