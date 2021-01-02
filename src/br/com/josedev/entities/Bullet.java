package br.com.josedev.entities;

import br.com.josedev.main.Game;
import br.com.josedev.world.Camera;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Bullet extends Entity {
	private final int dx;
	private final int dy;
	private final double speed = 4;
	private final int life = 35;
	private int currentLife = 0;

	public static final int DAMAGE = 10;

	public Bullet(int x, int y, int width, int height, BufferedImage sprite, int dx, int dy) {
		super(x, y, width, height, sprite);
		this.dx = dx;
		this.dy = dy;
	}

	public void update() {
		x += dx * speed;
		y += dy * speed;
		currentLife++;

		if (currentLife == life) {
			destroySelf();
		}
	}

	private void destroySelf() {
		Game.bullets.remove(this);
	}

	public void render(Graphics g) {
		g.setColor(Color.yellow);
		g.fillOval(this.getX() - Camera.x, this.getY() - Camera.y, 3, 3);
	}

}
