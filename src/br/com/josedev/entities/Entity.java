package br.com.josedev.entities;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import br.com.josedev.main.Game;
import br.com.josedev.world.Camera;

public class Entity {
	
	protected double x;
	protected double y;
	protected int width;
	protected int height;
	protected BufferedImage sprite;
	public static final int TILE_SIZE = 16;
	
	public static BufferedImage LIFEPACK_EN = Game.spritesheet.getSprite(6*TILE_SIZE, 0, 16, TILE_SIZE);
	public static BufferedImage WEAPON_EN = Game.spritesheet.getSprite(7*TILE_SIZE, 0, TILE_SIZE, TILE_SIZE);
	public static BufferedImage AMMO_EN = Game.spritesheet.getSprite(6*TILE_SIZE, 16, TILE_SIZE, TILE_SIZE);
	public static BufferedImage ENEMY_EN = Game.spritesheet.getSprite(6*TILE_SIZE, 16, TILE_SIZE, TILE_SIZE);
	public static BufferedImage ENEMY_EN_DAMAGE = Game.spritesheet.getSprite(144, 16, TILE_SIZE, TILE_SIZE);
	public static BufferedImage WEAPON_RIGHT = Game.spritesheet.getSprite(128, 0, TILE_SIZE, TILE_SIZE);
	public static BufferedImage WEAPON_LEFT= Game.spritesheet.getSprite(128+TILE_SIZE, 0, TILE_SIZE, TILE_SIZE);
	
	private int maskx, masky, mwidth, mheight;
	
	public Entity(int x, int y, int width, int height, BufferedImage sprite) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.sprite = sprite;
		
		this.maskx = 0;
		this.masky = 0;
		this.mwidth = width;
		this.mheight = height;
	}
	
	
	public void setMask(int maskx, int masky, int mwidth, int mheight) {
		this.maskx = maskx;
		this.masky = masky;
		this.mwidth = mwidth;
		this.mheight = mheight;
	}
	
	public void setX(int newX) {
		this.x = newX;
	}
	
	public void setY(int newY) {
		this.y = newY;
	}
	
	
	public int getX() {
		return (int)this.x;
	}
	
	public int getY() {
		return (int)this.y;
	}
	
	public int getWidht() {
		return this.width;
	}
	
	public int getHeight() {
		return this.height;
	}
	
	public static boolean isColliding(Entity e , Entity e2) {
		Rectangle e1mask = new Rectangle(e.getX() + e.maskx, e.getY() + e.masky, e.mwidth, e.mheight);
		Rectangle e2mask = new Rectangle(e2.getX() + e2.maskx, e2.getY() + e2.masky, e2.mwidth, e2.mheight);
		
		return e1mask.intersects(e2mask);
	}
	
	public void render(Graphics g) {
		g.drawImage(sprite, this.getX() - Camera.x, this.getY() - Camera.y, null);
	}
	
	public void tick() {
	}


}
