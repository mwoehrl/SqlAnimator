package de.mwoehrl.sqlanimator.renderer;

import java.awt.Graphics;

import javax.swing.JPanel;

public class CanvasPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private final RenderCanvas[] queryAndRelations;
	private RenderCanvas hContainer;
	
	public CanvasPanel(RenderCanvas query, ControlPanelCanvas controlPanel, RenderCanvas arc) {
		queryAndRelations = new RenderCanvas[2]; 
		queryAndRelations[0] = new VContainerCanvas(new RenderCanvas[] { query, controlPanel});
		queryAndRelations[1] = arc;
		hContainer = new HContainerCanvas(queryAndRelations);
		addMouseListener(new java.awt.event.MouseAdapter() {
			public void mousePressed(java.awt.event.MouseEvent evt) {
				controlPanel.processClick(evt.getX(),evt.getY());
				};
		});
	}
	
	public void setRenderCanvas(RenderCanvas arc) {
		queryAndRelations[1] = arc;
		hContainer = new HContainerCanvas(queryAndRelations);
		repaint();
	}
	
	public void setTransitionCanvas(RenderCanvas trans) {
		hContainer = trans;
		repaint();
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		g.drawImage(hContainer.drawImage(), 0, 0, null);
	}

	public RenderCanvas getQueryCanvas() {
		return queryAndRelations[0];
	}	
}
