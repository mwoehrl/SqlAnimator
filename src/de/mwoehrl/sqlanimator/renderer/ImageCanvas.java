package de.mwoehrl.sqlanimator.renderer;

import java.awt.Graphics;
import java.awt.Image;

public class ImageCanvas extends RenderCanvas {

	private Image img;
	
	public void setImage(Image image) {
		img = image;
	}
	
	public Image getImage() {
		return img;
	}

	@Override
	public void setPosition(double x, double y) {
		// TODO Auto-generated method stub

	}

	@Override
	public Image drawImage() {
		return img;
	}

	@Override
	public void scaleUp(double factor) {
		// TODO Auto-generated method stub

	}

	@Override
	public Object getCoreObject() {
		// TODO Auto-generated method stub
		return null;
	}

}
