package de.mwoehrl.sqlanimator.renderer;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import de.mwoehrl.sqlanimator.relation.Cell;

public class CellCanvas extends AbstractCellCanvas{

	private final Cell cell;
	private final Color backColor;

	public CellCanvas(Cell cell, boolean isHeader) {
		this.cell = cell;
		this.font = new Font(fontname, isHeader ? Font.BOLD : 0 , 12);
		this.backColor = isHeader ? Color.LIGHT_GRAY : Color.white;
		this.isHeader = isHeader;
	}
	
	public CellCanvas(Cell cell) {
		this(cell, false);
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
		g.setColor(backColor);
		g.fillRect(0, 0, width, height);
		g.setColor(Color.black);
		g.setStroke(new BasicStroke((int)(scale * 1.5d), BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER));
		g.drawRect(0, 0, width-1, height-1);
		g.drawString(cell.getValue(), (int)(hPadding * scale), height - (int)((vPadding+1) * scale));
		return img;
	}

	public Cell getCell() {
		return cell;
	}

	@Override
	protected  String getCellText(){
			return cell == null ? "?" : cell.getValue();
	}

	@Override
	public Object getCoreObject() {
		return getCell();
	}

}
