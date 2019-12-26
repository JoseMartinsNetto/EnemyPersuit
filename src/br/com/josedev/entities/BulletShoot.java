package br.com.josedev.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import br.com.josedev.main.Game;
import br.com.josedev.world.Camera;

public class BulletShoot extends Entity {
	
	private int dx,dy;
	private double speed = 4;
	private int life = 35, curLife = 0;

	public BulletShoot(int x, int y, int width, int height, BufferedImage sprite, int dx, int dy) {
		super(x, y, width, height, sprite);
		this.dx = dx;
		this.dy = dy;
	}
	
	public void tick() {
		x+=dx*speed;
		y+=dy*speed;
		curLife++;
		
		if(curLife == life) {
			Game.bulletShoots.remove(this);
			return;
		}
	}
	
	public void render(Graphics g) {
		g.setColor(Color.yellow);
		g.fillOval(this.getX() - Camera.x, this.getY() - Camera.y, 3,3);
	}

}
