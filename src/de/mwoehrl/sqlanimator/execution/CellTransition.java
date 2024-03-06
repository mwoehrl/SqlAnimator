package de.mwoehrl.sqlanimator.execution;

import java.awt.Graphics;

import de.mwoehrl.sqlanimator.renderer.AbsoluteCellPosition;

public abstract class CellTransition {

	public abstract void drawCellInTransition(Graphics g, double progress) ;

	public abstract AbsoluteCellPosition[] getCellPositions();
	
}
