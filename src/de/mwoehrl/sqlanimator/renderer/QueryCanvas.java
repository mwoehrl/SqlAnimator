package de.mwoehrl.sqlanimator.renderer;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import de.mwoehrl.sqlanimator.query.Query;
import de.mwoehrl.sqlanimator.relation.Relation;

public class QueryCanvas extends RenderCanvas {
	private static final int padding = 5;
	private static final Color keywordColor = new Color(0, 0, 192);
	
	private final int targetWidth;
	private final int targetHeight;
	private double scale;

	private final TextCanvas[][] textCells;
	private BufferedImage tick;
	private BufferedImage cross;
	private final Query query;
	
	public QueryCanvas(Query query,int targetWidth, int targetHeight) {
		this.targetWidth = targetWidth;
		this.targetHeight = targetHeight;
		this.textCells = new TextCanvas[6][];
		this.query = query;
		
		try {
			tick = javax.imageio.ImageIO.read(new File("tick.png"));
			cross = javax.imageio.ImageIO.read(new File("cross.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		String[] colnames = query.select.getRawSelects();
		textCells[0] = new TextCanvas[colnames.length * 2];
		textCells[0][0] = new TextCanvas("SELECT ", null, "Courier New", Font.BOLD, keywordColor);
		for (int i = 0; i < colnames.length; i++) {
			textCells[0][i * 2 + 1] = new TextCanvas(colnames[i], null, "Verdana", 0, Color.BLACK);
			if (i < colnames.length - 1)
				textCells[0][i * 2 + 2] = new TextCanvas(",", null);
		}

		String[] tableNames = query.from.getFromTables();
		textCells[1] = new TextCanvas[tableNames.length * 2];
		textCells[1][0] = new TextCanvas("FROM ", null, "Courier New", Font.BOLD, keywordColor);
		for (int i = 0; i < tableNames.length; i++) {
			textCells[1][i * 2 + 1] = new TextCanvas(tableNames[i], null);
			if (i < tableNames.length - 1)
				textCells[1][i * 2 + 2] = new TextCanvas(",", null);
		}

		//Expression whereExpr = query.where.getWhereCondition();
		textCells[2] = new TextCanvas[2];
		textCells[2][0] = new TextCanvas("WHERE ", null, "Courier New", Font.BOLD, keywordColor);
		for (int i = 0; i < 1; i++) {
			textCells[2][i + 1] = new TextCanvas(query.where.getConditionString(), null);
		}
		textCells[3] = new TextCanvas[2];
		textCells[3][0] = new TextCanvas("GROUP BY ", null, "Courier New", Font.BOLD, keywordColor);
		for (int i = 0; i < 1; i++) {
			textCells[3][i + 1] = new TextCanvas("<ToDo>", null);
		}
		textCells[4] = new TextCanvas[2];
		textCells[4][0] = new TextCanvas("HAVING ", null, "Courier New", Font.BOLD, keywordColor);
		for (int i = 0; i < 1; i++) {
			textCells[4][i + 1] = new TextCanvas("<ToDo>", null);
		}
		textCells[5] = new TextCanvas[2];
		textCells[5][0] = new TextCanvas("ORDER BY ", null, "Courier New", Font.BOLD, keywordColor);
		for (int i = 0; i < 1; i++) {
			textCells[5][i + 1] = new TextCanvas(String.join(",", query.orderby.getOrderByColumns()), null);
		}
		renderRelations();
	}

	private void renderRelations() {
		BufferedImage img = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_ARGB);
		calculateRequiredSizes(img.getGraphics());
		scaleToFit();
		setPositions(0, 0);
	}
	
	private void scaleToFit() {
		double wScale = (double) targetWidth / requiredSize.getWidth();
		double hScale = (double) targetHeight / requiredSize.getHeight();

		if (wScale < hScale) {
			scale = wScale;
		} else {
			scale = hScale;
		}

		for (int i = 0; i < textCells.length; i++) {
			for (int j = 0; j < textCells[i].length; j++) {
				textCells[i][j].scaleUp(scale);
			}
		}
		requiredSize = new Rectangle2D.Double(0, 0, requiredSize.getWidth() * scale, requiredSize.getHeight() * scale);
	}
	
	@Override
	public void calculateRequiredSizes(Graphics g) {
		int h = 0;
		int maxW = 0;
		for (int i = 0; i < textCells.length; i++) {
			int w = padding;
			for (int j = 0; j < textCells[i].length; j++) {
				textCells[i][j].calculateRequiredSizes(g);
				w += textCells[i][j].requiredSize.getWidth();
			}
			if (w > maxW)
				maxW = w;
			h += textCells[i][0].requiredSize.getHeight();
		}
		requiredSize = new Rectangle2D.Double(0, 0, maxW + padding, h + padding * 2);
	}

	@Override
	public void setPositions(double x, double y) {
		position = new Rectangle2D.Double(x, y, 0, 0);
		int ypos = (int)(padding * scale);
		for (int i = 0; i < textCells.length; i++) {
			int xpos = (int)(padding * scale);
			for (int j = 0; j < textCells[i].length; j++) {
				textCells[i][j].setPositions(xpos, ypos);
				xpos += textCells[i][j].requiredSize.getWidth();
			}
			ypos += textCells[i][0].requiredSize.getHeight();
		}
	}

	@Override
	public Image drawImage() {
		int height = (int)requiredSize.getHeight();
		int width = (int)requiredSize.getWidth();
		Image img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = (Graphics2D) img.getGraphics();

		RenderingHints rh = new RenderingHints(RenderingHints.KEY_INTERPOLATION,
	            RenderingHints.VALUE_INTERPOLATION_BICUBIC);
		g.setRenderingHints(rh);

		g.setColor(Color.LIGHT_GRAY);
		g.fillRect(0, 0, width, height);

		for (int i = 0; i < textCells.length; i++) {
			for (int j = 0; j < textCells[i].length; j++) {
			 TextCanvas t = textCells[i][j];
				g.drawImage(t.drawImage(), (int)t.position.getX(), (int)t.position.getY(), null) ;
			}
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

	public AbsoluteCellPosition[] getTableCellAbsolutePositions() {
		return getAbsolutePositions(1);
	}

	public AbsoluteCellPosition[] getColumnCellAbsolutePositions() {
		return getAbsolutePositions(0);
	}

	private AbsoluteCellPosition[] getAbsolutePositions(int n) {
		AbsoluteCellPosition[] result =  new AbsoluteCellPosition[textCells[n].length - 1];
		for (int i = 1; i < textCells[n].length; i++) {
			TextCanvas c = textCells[n][i];
			result[i-1] = new AbsoluteCellPosition(
					(int)(c.position.getX() + position.getX()),
					(int)(c.position.getY() + position.getY()),
					(int)c.requiredSize.getWidth(),
					(int)c.requiredSize.getHeight(),
					c); 
		}
		return result;
	}
	
	public AbsoluteCellPosition[] getWhereTicks(int length, Relation relation) {
		AbsoluteCellPosition[] result = new AbsoluteCellPosition[length];
		TextCanvas c = textCells[2][1];
		for (int r = 0; r < length; r++) {
			ImageCanvas imageCanvas = new ImageCanvas();
			imageCanvas.setImage(relation.rowMatchesCondition(query.where, r) ? tick : cross);
			result[r] = new AbsoluteCellPosition(
					(int)(c.position.getX() + position.getX()),
					(int)(c.position.getY() + position.getY()),
					(int)c.requiredSize.getHeight(),
					(int)c.requiredSize.getHeight(),
					imageCanvas); 
		}
		return result;
	}
}
