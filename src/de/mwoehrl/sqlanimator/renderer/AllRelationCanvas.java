package de.mwoehrl.sqlanimator.renderer;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import de.mwoehrl.sqlanimator.execution.CellTransition;
import de.mwoehrl.sqlanimator.query.Aggregate;
import de.mwoehrl.sqlanimator.relation.Relation;
import de.mwoehrl.sqlanimator.relation.Row;

public class AllRelationCanvas extends RenderCanvas {
	public static int screenWidth;
	public static int screenHeight;
	
	private static final int padding = 20;
	private static final int spacing = 60;
	private int centerPadding = 0;

	private final Relation[] relations;
	private RelationCanvas[] relationCanvases;
	private final int targetWidth;
	private final int targetHeight;
	private double scale;

	public AllRelationCanvas(Relation[] allRelations, Rectangle2D position) {
		relations = allRelations;
		targetWidth = screenWidth;
		targetHeight = screenHeight;
		this.position = position;
		renderRelations(null);
	}

	public AllRelationCanvas(Relation relation, ArrayList<ArrayList<Row>> bucketList, Rectangle2D position) {
		relations = new Relation[] {relation};
		targetWidth = screenWidth;
		targetHeight = screenHeight;
		this.position = position;
		renderRelations(bucketList);
	}

	public AllRelationCanvas(Relation relation, Aggregate[] aggregates, ArrayList<ArrayList<Row>> bucketList, Rectangle2D position) {
		relations = new Relation[] {relation};
		targetWidth = screenWidth;
		targetHeight = screenHeight;
		this.position = position;
		renderRelations(bucketList, aggregates);
	}

	private void renderRelations(ArrayList<ArrayList<Row>> bucketList, Aggregate[] aggregates) {
		relationCanvases = new RelationCanvas[1];
		relationCanvases[0] = new AggregateRelationCanvas(relations[0], bucketList, aggregates);
		BufferedImage img = new BufferedImage(16,16, BufferedImage.TYPE_INT_ARGB);
		calculateRequiredSizes(img.getGraphics());
		scaleToFit();
		setPositions();
	}

	private void renderRelations(ArrayList<ArrayList<Row>> bucketList) {
		relationCanvases = new RelationCanvas[relations.length];
		for (int i = 0; i < relations.length; i++) {
			relationCanvases[i] = new RelationCanvas(relations[i], bucketList);
		}
		BufferedImage img = new BufferedImage(16,16, BufferedImage.TYPE_INT_ARGB);
		calculateRequiredSizes(img.getGraphics());
		scaleToFit();
		setPositions();
	}

	private void scaleToFit() {
		double wScale = (double) targetWidth / requiredSize.getWidth();
		double hScale = (double) targetHeight / requiredSize.getHeight();

		if (wScale < hScale) {
			scale = wScale;
		} else {
			scale = hScale;
			centerPadding = (int)((targetWidth - requiredSize.getWidth()*scale) / 2);
		}

		for (RelationCanvas rc : relationCanvases) {
			rc.scaleUp(scale);
		}
		requiredSize = new Rectangle2D.Double(0, 0, targetWidth, targetHeight);
	}

	private void calculateRequiredSizes(Graphics g) {
		double w = padding;
		double h = 0;

		for (RelationCanvas rc : relationCanvases) {
			if (rc.requiredSize.getHeight() > h)
				h = rc.requiredSize.getHeight();
			w += rc.requiredSize.getWidth() + spacing;
		}
		w += padding - spacing;
		requiredSize = new Rectangle2D.Double(0, 0, w, h + padding * 2);
	}

	private void setPositions() {
		int xRel = (int)(centerPadding + padding * scale);
		for (RelationCanvas rc : relationCanvases) {
			rc.setPosition(xRel, padding * scale);
			xRel += rc.requiredSize.getWidth() + spacing * scale;
		}
	}

	@Override
	public void setPosition(double x, double y) {
		position = new Rectangle2D.Double(x, y, 0, 0);
	}

	@Override
	public Image drawImage() {
		Image img = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
		Graphics g = img.getGraphics();

		g.setColor(Color.WHITE);
		g.fillRect(0, 0, targetWidth, targetHeight);
		g.setColor(Color.black);
		for (RelationCanvas rc : relationCanvases) {
			g.drawImage(rc.drawImage(), (int) rc.position.getX(), (int) rc.position.getY(), null);
		}
		return img;
	}

	public AbsoluteCellPosition[] getTableNameCellPosition() {
		AbsoluteCellPosition[] result = new AbsoluteCellPosition[relationCanvases.length];
		for (int i = 0; i < relationCanvases.length; i++) {
			result[i] = relationCanvases[i].getTableNameCellPosition();
			result[i].addParentPosition(position);
		}
		return result;
	}
	
	public AbsoluteCellPosition[] getHeaderCellPositions() {
		AbsoluteCellPosition[][] result = new AbsoluteCellPosition[relationCanvases.length][];
		int flatCount = 0;
		for (int i = 0; i < relationCanvases.length; i++) {
			result[i] = relationCanvases[i].getHeaderCellPositions();
			for (int r = 0; r < result[i].length; r++) {
				result[i][r].addParentPosition(position);
				flatCount++;
			}
		}
		AbsoluteCellPosition[] flat = new AbsoluteCellPosition[flatCount];
		flatCount = 0;
		for (int i = 0; i < relationCanvases.length; i++) {
			for (int r = 0; r < result[i].length; r++) {
				flat[flatCount++] = result[i][r];
			}
		}
		return flat;
	}
	
	public AbsoluteCellPosition[] getAbsoluteCellPositions() {
		AbsoluteCellPosition[][] result = new AbsoluteCellPosition[relationCanvases.length][];
		int count = 0;
		for (int i = 0; i < relationCanvases.length; i++) {
			result[i] = relationCanvases[i].getAbsoluteCellPositions();
			count += result[i].length;
		}
		AbsoluteCellPosition[] flatResult = new AbsoluteCellPosition[count];
		count = 0;
		for (int i = 0; i < relationCanvases.length; i++) {
			for (int j = 0; j < result[i].length; j++) {
				result[i][j].addParentPosition(position);
				flatResult[count++] = result[i][j];
			}
		}
		return flatResult;
	}

	@Override
	public void scaleUp(double factor) { // Keine impl. nötig, da feste Größe
	}

	public Relation[] getRelations() {
		return relations;
	}

	@Override
	public Object getCoreObject() {
		return null;
	}

	public Rectangle2D getPosition() {
		return position;
	}

	public AbsoluteCellPosition[] getFirstColumnCellPositions() {
		AbsoluteCellPosition[] result = relationCanvases[0].getFirstColumnCellPositions();
		for (AbsoluteCellPosition a : result) {
			a.addParentPosition(position);
		}
		return result;
	}
	
	public CellTransition[] getAggregateTransistions() {
		return ((AggregateRelationCanvas)(relationCanvases[0])).getTransitions(position);
	}
	
	public void setAggregateValuesFromRelation(Relation aggregatedRelation) {
		((AggregateRelationCanvas)(relationCanvases[0])).setAggregateValuesFromRelation(aggregatedRelation);
	}
	
}
