package br.com.josedev.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import br.com.josedev.main.Game;
import br.com.josedev.world.Camera;

public class Bullet extends Entity{
	private int dx,dy;
	private double speed = 4;
	private int life = 35, currentLife = 0;
	
	public static final int DAMAGE = 10;

	public Bullet(int x, int y, int width, int height, BufferedImage sprite, int dx, int dy) {
		super(x, y, width, height, sprite);
		this.dx = dx;
		this.dy = dy;
	}
	
	public void tick() {
		x+=dx*speed;
		y+=dy*speed;
		currentLife++;
		
		if(currentLife == life) {
			destroySelf();
		}
	}
	
	private void destroySelf() {
		Game.bullets.remove(this);
		return;
	}
	
	public void render(Graphics g) {
		g.setColor(Color.yellow);
		g.fillOval(this.getX() - Camera.x, this.getY() - Camera.y, 3,3);
	}

}
