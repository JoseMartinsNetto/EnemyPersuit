package br.com.josedev.entities;

import java.awt.image.BufferedImage;

public class Ammunition extends Entity {
	
	public static final int maxAmmunition = 50;
	public static final int ammunitionSize = 10;

	public Ammunition(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
	}
}
