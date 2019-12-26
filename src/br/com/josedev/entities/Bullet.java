package br.com.josedev.entities;

import java.awt.image.BufferedImage;

public class Bullet extends Entity{
	
	public static final int maxAmmo = 50;
	public static final int ammoSize = 10;

	public Bullet(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
	}

}
