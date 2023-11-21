package de.mwoehrl.sqlanimator.renderer;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import de.mwoehrl.sqlanimator.ExecutionController;
import de.mwoehrl.sqlanimator.gui.ButtonActionListener;
import de.mwoehrl.sqlanimator.gui.ButtonControl;
import de.mwoehrl.sqlanimator.gui.Control;
import de.mwoehrl.sqlanimator.gui.ControlContainer;
import de.mwoehrl.sqlanimator.gui.ProgressIndicator;

public class ControlPanelCanvas extends RenderCanvas {

	private final ControlContainer control;
	private final int targetWidth;
	private final int targetHeight;
	private ExecutionController controller;
	private ProgressIndicator progress;
	
	public ControlPanelCanvas(int targetWidth, int targetHeight) {
		ButtonControl[] buttons = new ButtonControl[0];
		
		try {
			BufferedImage play = javax.imageio.ImageIO.read(new File("Button_Play.png"));
			BufferedImage plus = javax.imageio.ImageIO.read(new File("Button_Plus.png"));
			BufferedImage minus = javax.imageio.ImageIO.read(new File("Button_Minus.png"));
			BufferedImage rewind = javax.imageio.ImageIO.read(new File("Button_Rewind.png"));
			
			buttons = new ButtonControl[4];
			
			buttons[0] = new ButtonControl(rewind);
			buttons[0].setActionListener(new ButtonActionListener() {
				@Override
				public void onAction() {
					rewindButtonPressed();
				}
			});
			buttons[1] = new ButtonControl(minus);
			buttons[1].setActionListener(new ButtonActionListener() {
				@Override
				public void onAction() {
					minusButtonPressed();
				}
			});			buttons[2] = new ButtonControl(play);
			buttons[2].setActionListener(new ButtonActionListener() {
				@Override
				public void onAction() {
					playButtonPressed();
				}
			});
			buttons[3] = new ButtonControl(plus);
			buttons[3].setActionListener(new ButtonActionListener() {
				@Override
				public void onAction() {
					plusButtonPressed();
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		int padding = 10;
		int spacing = 2*padding;
		this.targetWidth = targetWidth;
		this.targetHeight = targetHeight; //;
		ControlContainer buttonContainer = new ControlContainer(buttons);
		int buttonContainerHeight = padding + padding + ((targetWidth - padding - padding) - (spacing * (buttons.length - 1))) / buttons.length;
		buttonContainer.setSize(targetWidth, buttonContainerHeight);
		buttonContainer.layoutHorizontally(padding, spacing);
		progress = new ProgressIndicator();
		progress.setSize(targetWidth, targetHeight-buttonContainerHeight);
		Control[] topLevelControls = new Control[] {buttonContainer, progress};
		control = new ControlContainer(topLevelControls);
		control.setSize(targetWidth, targetHeight);
		control.layoutVertically();
		requiredSize = new Rectangle2D.Double(0,0,targetWidth,this.targetHeight);
	}
	
	public void setController(ExecutionController controller) {
		this.controller = controller;
		this.progress.setController(controller);
	}
	
	private void playButtonPressed() {
		controller.playAllSteps();
	}

	private void plusButtonPressed() {
		controller.plusButton();;
	}

	private void minusButtonPressed() {
		controller.goToPrevStep();;
	}

	private void rewindButtonPressed() {
		controller.gotoStart();
	}

	@Override
	public Image drawImage() {
		Image img = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = (Graphics2D) img.getGraphics();

		RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		rh.add(new RenderingHints(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC));
		g.setRenderingHints(rh);

		control.drawControl(g);
		
		return img;
	}

	@Override
	public void scaleUp(double factor) {
	}

	@Override
	public Object getCoreObject() {
		return null;
	}

	public void processClick (int x, int y) {
		control.catchClick((int)(x - position.getX()), (int)(y - position.getY()));
	}

}
