package br.com.josedev.entities;

import java.awt.image.BufferedImage;

public class Lifepack extends Entity{
	
	public static int lifepackSize = 10;

	public Lifepack(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
	}

}
