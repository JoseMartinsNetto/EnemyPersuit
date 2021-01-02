package br.com.josedev.world;

import br.com.josedev.main.Game;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Tile {
	public static BufferedImage TILE_FLOOR = Game.spritesheet.getSprite(0, 0, 16, 16);
	public static BufferedImage TILE_WALL = Game.spritesheet.getSprite(16, 0, 16, 16);
	
	private final BufferedImage sprite;
	private final int x;
	private final int y;
	
	public Tile (int x, int y, BufferedImage sprite) {
		this.x = x;
		this.y = y;
		this.sprite = sprite;
	}
	
	public void render(Graphics g) {
		g.drawImage(sprite, x - Camera.x, y - Camera.y, null);
	}
}
