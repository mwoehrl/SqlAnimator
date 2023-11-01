package de.mwoehrl.sqlanimator.renderer;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import de.mwoehrl.sqlanimator.relation.Relation;
import de.mwoehrl.sqlanimator.relation.Row;

public class RelationCanvas extends RenderCanvas {

	protected final Relation relation;
	protected final AbstractCellCanvas[][] cells;
	protected final TextCanvas tableNameCanvas;
	protected final ArrayList<ArrayList<Row>> bucketList;
	
	public RelationCanvas(Relation relation, ArrayList<ArrayList<Row>> bucketList) {
		this.relation = relation;
		this.bucketList = bucketList;
		this.tableNameCanvas = new TextCanvas(relation.getName(), null);
		cells = new AbstractCellCanvas[relation.getColumns().length][relation.getRows().length + 1];
		for (int i = 0; i < relation.getColumns().length; i++) {
			cells[i][0] = new CellCanvas(relation.getHeader().getCell(i), true);
			for (int r = 0; r < relation.getRows().length; r++) {
				if (relation.getRows()[r] != null) {
					cells[i][r + 1] = new CellCanvas(relation.getRows()[r].getCell(i));
				} else {
					cells[i][r + 1] = new EmptyCellCanvas();
				}
			}
		}
		BufferedImage img = new BufferedImage(16,16, BufferedImage.TYPE_INT_ARGB);
		calculateRequiredSizes(img.getGraphics());
		setPositions();
	}


	private void calculateRequiredSizes(Graphics g) {
		double requiredWidth = 0;
		for (int i = 0; i < relation.getColumns().length; i++) {
			double maxWidth = 0;
			for (int r = 0; r < relation.getRows().length + 1; r++) {
				//cells[i][r].calculateRequiredSizes(g);
				if (cells[i][r].requiredSize.getWidth() > maxWidth)
					maxWidth = cells[i][r].requiredSize.getWidth();
			}
			requiredWidth += maxWidth;
			for (int r = 0; r < relation.getRows().length + 1; r++) {
				cells[i][r].adjustWidth(maxWidth);
			}
		}
		
		double requiredHeight = 0;
		if (cells.length > 0) {
			for (int r = 0; r < relation.getRows().length + 1; r++) {
				requiredHeight += cells[0][r].requiredSize.getHeight();
			}
			if (bucketList != null) {
				int gapHeight = (int)cells[0][0].requiredSize.getHeight();  //Header Höhe
				requiredHeight += gapHeight * bucketList.size() + gapHeight / 2 ;
			}
		}

		tableNameCanvas.calculateRequiredSizes(g);
		if (requiredWidth == 0) requiredWidth = 60;		//Falls 0 spalten
		requiredSize = new Rectangle2D.Double(0, 0, requiredWidth, requiredHeight + tableNameCanvas.requiredSize.getHeight());		
	}

	@Override
	public void setPosition(double x, double y) {
		position = new Rectangle2D.Double(x, y, 0, 0);
	}
	
	private void setPositions() {
		double colX = 0;
		tableNameCanvas.setPosition((requiredSize.getWidth() - tableNameCanvas.requiredSize.getWidth())/2 , 0);
		for (int i = 0; i < relation.getColumns().length; i++) {
			double rowY = tableNameCanvas.requiredSize.getHeight();
			if (bucketList == null) {
				for (int r = 0; r < relation.getRows().length + 1; r++) {
					cells[i][r].setPosition(colX, rowY);
					rowY += cells[i][r].requiredSize.getHeight();
				}
			} else {
				cells[i][0].setPosition(colX, rowY);
				int gapHeight = (int)cells[0][0].requiredSize.getHeight();  //Header Höhe
				rowY += cells[i][0].requiredSize.getHeight() + gapHeight / 2;
				int rowIndex = 1;
				for (int b = 0; b < bucketList.size(); b++) {
					for (int r = 0; r < bucketList.get(b).size(); r++) {
						cells[i][r + rowIndex].setPosition(colX, rowY);
						rowY += cells[i][r + rowIndex].requiredSize.getHeight();
					}
					rowIndex += bucketList.get(b).size();
					rowY += gapHeight;
				}
			}
			colX += cells[i][0].requiredSize.getWidth();
		}
	}

	@Override
	public Image drawImage() {
		int height = (int)requiredSize.getHeight();
		int width = (int)requiredSize.getWidth();
		Image img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = (Graphics2D) img.getGraphics();

		RenderingHints rh = new RenderingHints(RenderingHints.KEY_TEXT_ANTIALIASING,
	            RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g.setRenderingHints(rh);

		g.setColor(Color.white);
		g.fillRect(0, 0, width, height);

		g.drawImage(tableNameCanvas.drawImage(), (int)tableNameCanvas.position.getX(), (int)tableNameCanvas.position.getY(), null);				

		for (int i = 0; i < relation.getColumns().length; i++) {
			for (int r = 0; r < relation.getRows().length + 1; r++) {
				AbstractCellCanvas cell = cells[i][r];
				g.drawImage(cell.drawImage(), (int)cell.position.getX(), (int)cell.position.getY(), null);				
			}
		}

		return img;
	}

	@Override
	public void scaleUp(double factor) {
		tableNameCanvas.scaleUp(factor);
		for (int i = 0; i < relation.getColumns().length; i++) {
			for (int r = 0; r < relation.getRows().length + 1; r++) {
				cells[i][r].scaleUp(factor);
			}
		}
		requiredSize = new Rectangle2D.Double(0, 0, (int)(requiredSize.getWidth() * factor), (int)(requiredSize.getHeight() * factor));
		scaleUpPositions(factor);
	}
	
	private void scaleUpPositions(double factor) {
		tableNameCanvas.setPosition(tableNameCanvas.position.getX() * factor,tableNameCanvas.position.getY() * factor);
		for (int i = 0; i < relation.getColumns().length; i++) {
			for (int r = 0; r < relation.getRows().length + 1; r++) {
				cells[i][r].setPosition(cells[i][r].position.getX() * factor,cells[i][r].position.getY() * factor);
			}
		}
	}


	public AbsoluteCellPosition getTableNameCellPosition() {
		TextCanvas c = tableNameCanvas;
		return new AbsoluteCellPosition(
				(int)(c.position.getX() + position.getX()),
				(int)(c.position.getY() + position.getY()),
				(int)c.requiredSize.getWidth(),
				(int)c.requiredSize.getHeight(),
				c);
	}
	
	public AbsoluteCellPosition[] getHeaderCellPositions() {
		AbsoluteCellPosition[] result = new  AbsoluteCellPosition[cells.length]; 
		for (int i = 0; i < cells.length; i++) {
			AbstractCellCanvas c = cells[i][0];
			result[i] = new AbsoluteCellPosition(
					(int)(c.position.getX() + position.getX() + c.scale * (AbstractCellCanvas.hPadding - TextCanvas.hPadding)),
					(int)(c.position.getY() + position.getY()),
					(int)c.requiredSize.getWidth(),
					(int)c.requiredSize.getHeight(),
					c); 
		}

		return result;
	}
	
	public AbsoluteCellPosition[] getAbsoluteCellPositions() {
		if (cells.length > 0) {
			int count = cells.length * cells[0].length;
			AbsoluteCellPosition[] result = new AbsoluteCellPosition[count + 1];
			result[0] = new AbsoluteCellPosition((int)(tableNameCanvas.position.getX() + position.getX()), (int)(tableNameCanvas.position.getY() + position.getY()), (int)(tableNameCanvas.requiredSize.getWidth()), (int)(tableNameCanvas.requiredSize.getHeight()), tableNameCanvas);
			int index = 1;
			for (int i = 0; i < cells.length; i++) {
				for (int r = 0; r < cells[0].length; r++) {
					result[index++] = new AbsoluteCellPosition((int)(cells[i][r].position.getX() + position.getX()), (int)(cells[i][r].position.getY() + position.getY()), (int)(cells[i][r].requiredSize.getWidth()), (int)(cells[i][r].requiredSize.getHeight()), cells[i][r]);
				}
			}
			return result;
		} else {
			return new AbsoluteCellPosition[0];
		}
	}

	@Override
	public Object getCoreObject() {
		return relation;
	}

	public AbsoluteCellPosition[] getFirstColumnCellPositions() {
		AbsoluteCellPosition[] result = new  AbsoluteCellPosition[cells[0].length - 1]; 
		for (int r = 1; r < cells[0].length; r++) {
			AbstractCellCanvas c = cells[0][r];
			result[r - 1] = new AbsoluteCellPosition(
					(int)(c.position.getX() + position.getX() + c.scale * (AbstractCellCanvas.hPadding - TextCanvas.hPadding))-(int)c.requiredSize.getHeight(),
					(int)(c.position.getY() + position.getY()),
					(int)c.requiredSize.getWidth(),
					(int)c.requiredSize.getHeight(),
					c); 
		}
		return result;
	}
}
