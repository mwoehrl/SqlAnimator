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
	public Image drawImage() {
		return img;
	}

	@Override
	public void scaleUp(double factor) {
	}

	@Override
	public Object getCoreObject() {
		return null;
	}

}
