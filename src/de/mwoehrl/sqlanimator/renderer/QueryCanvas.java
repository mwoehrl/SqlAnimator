package de.mwoehrl.sqlanimator.renderer;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import de.mwoehrl.sqlanimator.query.ProjectionColumn;
import de.mwoehrl.sqlanimator.query.Query;
import de.mwoehrl.sqlanimator.query.expression.ExpressionToken;
import de.mwoehrl.sqlanimator.query.expression.ExpressionToken.TokenType;
import de.mwoehrl.sqlanimator.relation.Relation;

public class QueryCanvas extends RenderCanvas {
	private static final Color COLOR_Background = new Color (245,240,220);
	private static final int padding = 5;
	private static final Color keywordColor = new Color(0, 0, 192);

	private final int targetWidth;
	private final int targetHeight;
	private double scale = 1d;

	private final TextCanvas[][] textCells;
	private BufferedImage tick;
	private BufferedImage cross;
	private final Query query;
	private int havingLine;

	public QueryCanvas(Query query, int targetWidth, int targetHeight) {
		this.targetWidth = targetWidth;
		this.targetHeight = targetHeight;
		this.textCells = new TextCanvas[2 + (query.where==null ? 0 : 1) + (query.orderby==null ? 0 : 1) + (query.groupby==null ? 0 : 1) + (query.having==null ? 0 : 1)][];
		this.query = query;

		try {
			tick = javax.imageio.ImageIO.read(new File("tick.png"));
			cross = javax.imageio.ImageIO.read(new File("cross.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		ArrayList<ExpressionToken> allTokens = new ArrayList<ExpressionToken>();
		for (ProjectionColumn pc : query.select.getProjectionColumns()) {
			allTokens.addAll(Arrays.asList(pc.getAllTokens()));
			allTokens.add(new ExpressionToken(TokenType.Other, ", "));
		}

		textCells[0] = new TextCanvas[allTokens.size()];
		textCells[0][0] = new TextCanvas("SELECT ", null, "Courier New", Font.BOLD, keywordColor);

		for (int i = 1; i < allTokens.size(); i++) {
			ExpressionToken tok = allTokens.get(i - 1);
			textCells[0][i] = new TextCanvas(tok.text, null, tok.type == TokenType.Keyword ? "Courier New" : "Verdana",
					tok.type == TokenType.Keyword ? Font.BOLD : 0,
					tok.type == TokenType.Keyword ? keywordColor : Color.BLACK, tok.type == TokenType.Keyword ? 4 : 0);
		}

		String[] tableNames = query.from.getFromTables();
		textCells[1] = new TextCanvas[tableNames.length * 2];
		textCells[1][0] = new TextCanvas("FROM ", null, "Courier New", Font.BOLD, keywordColor);
		for (int i = 0; i < tableNames.length; i++) {
			textCells[1][i * 2 + 1] = new TextCanvas(tableNames[i], null);
			if (i < tableNames.length - 1)
				textCells[1][i * 2 + 2] = new TextCanvas(",", null);
		}

		int queryRow = 2;
		if (query.where != null) {
			textCells[queryRow] = new TextCanvas[2];
			textCells[queryRow][0] = new TextCanvas("WHERE ", null, "Courier New", Font.BOLD, keywordColor);
			for (int i = 0; i < 1; i++) {
				textCells[queryRow][i + 1] = new TextCanvas(query.where.getConditionString(), null);
			}
			queryRow++;
		}

		if (query.groupby != null) {
			textCells[queryRow] = new TextCanvas[2];
			textCells[queryRow][0] = new TextCanvas("GROUP BY ", null, "Courier New", Font.BOLD, keywordColor);
			for (int i = 0; i < 1; i++) {
				textCells[queryRow][i + 1] = new TextCanvas(query.groupby, null, "Verdana", 0, Color.black);
			}
			queryRow++;
		}
		
		if (query.having != null) {
			textCells[queryRow] = new TextCanvas[2];
			textCells[queryRow][0] = new TextCanvas("HAVING ", null, "Courier New", Font.BOLD, keywordColor);
			for (int i = 0; i < 1; i++) {
				textCells[queryRow][i + 1] = new TextCanvas(query.having.getConditionString(), null);
			}
			havingLine = queryRow;
			queryRow++;
		}
		
		if (query.orderby != null) {
			textCells[queryRow] = new TextCanvas[2];
			textCells[queryRow][0] = new TextCanvas("ORDER BY ", null, "Courier New", Font.BOLD, keywordColor);
			for (int i = 0; i < 1; i++) {
				textCells[queryRow][i + 1] = new TextCanvas(String.join(",", query.orderby.getOrderByColumns()), null);
			}
		}
		renderRelations();
	}

	private void renderRelations() {
		setPositions();
		calculateRequiredSizes();
		scaleToFit();
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
				textCells[i][j].setPosition(textCells[i][j].position.getX() * scale,textCells[i][j].position.getY() * scale);
			}
		}
		requiredSize = new Rectangle2D.Double(0, 0, requiredSize.getWidth() * scale, requiredSize.getHeight() * scale);
	}

	private void calculateRequiredSizes() {
		int h;
		int maxW = 0;
		double  maxH = 0;
		int w;
		for (int i = 0; i < textCells.length; i++) {
			for (int j = 0; j < textCells[i].length; j++) {
				w = (int)(textCells[i][j].position.getX() + textCells[i][j].requiredSize.getWidth());
				h = (int)(textCells[i][j].position.getY() + textCells[i][j].requiredSize.getHeight());
				if (w > maxW)
					maxW = w;
				if (h > maxH)
					maxH = h;
			}
		}
		requiredSize = new Rectangle2D.Double(0, 0, maxW + padding, maxH + padding * 2);
	}

	@Override
	public void setPosition(double x, double y) {
		position = new Rectangle2D.Double(x, y, 0, 0);
	}
	
	private void setPositions() {
		int ypos = (int) (padding * scale);
		for (int i = 0; i < textCells.length; i++) {
			int xpos = (int) (padding * scale);
			double  maxH = 0;
			for (int j = 0; j < textCells[i].length; j++) {
				textCells[i][j].setPosition(xpos, ypos);
				xpos += textCells[i][j].requiredSize.getWidth();
				if (textCells[i][j].requiredSize.getHeight() > maxH) maxH = textCells[i][j].requiredSize.getHeight(); 
				if (xpos > 400 && textCells[i][j].getCoreObject().equals(", ")) {
					xpos = 25;
					ypos += maxH - TextCanvas.vPadding * scale;
				}
			}
			for (int j = 0; j < textCells[i].length; j++) {
				textCells[i][j].adjustHeight(maxH); 
			}			
			ypos += maxH;
		}
	}

	@Override
	public Image drawImage() {
		int height = (int) requiredSize.getHeight();
		int width = (int) requiredSize.getWidth();
		Image img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = (Graphics2D) img.getGraphics();

		RenderingHints rh = new RenderingHints(RenderingHints.KEY_INTERPOLATION,
				RenderingHints.VALUE_INTERPOLATION_BICUBIC);
		g.setRenderingHints(rh);

		g.setColor(COLOR_Background);
		g.fillRect(0, 0, width, height);

		for (int i = 0; i < textCells.length; i++) {
			for (int j = 0; j < textCells[i].length; j++) {
				TextCanvas t = textCells[i][j];
				g.drawImage(t.drawImage(), (int) t.position.getX(), (int) t.position.getY(), null);
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
		AbsoluteCellPosition[] result = new AbsoluteCellPosition[textCells[n].length - 1];
		for (int i = 1; i < textCells[n].length; i++) {
			TextCanvas c = textCells[n][i];
			result[i - 1] = new AbsoluteCellPosition(
					c.position.getX() + position.getX(),
					c.position.getY() + position.getY(),
					c.requiredSize.getWidth(),
					c.requiredSize.getHeight(),
					c);
		}
		return result;
	}

	public AbsoluteCellPosition[] getWhereTicks(int length, Relation relation, boolean where) {
		AbsoluteCellPosition[] result = new AbsoluteCellPosition[length];
		TextCanvas c = textCells[where ? 2 : havingLine][1];
		for (int r = 0; r < length; r++) {
			ImageCanvas imageCanvas = new ImageCanvas();
			imageCanvas.setImage(relation.rowMatchesCondition(where ? query.where : query.having, r) ? tick : cross);
			result[r] = new AbsoluteCellPosition(
					c.position.getX() + position.getX(),
					c.position.getY() + position.getY(),
					c.requiredSize.getHeight(),
					c.requiredSize.getHeight(),
					imageCanvas);
		}
		return result;
	}
}
