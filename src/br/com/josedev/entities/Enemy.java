package br.com.josedev.entities;

import java.awt.image.BufferedImage;

import br.com.josedev.main.Game;
import br.com.josedev.world.World;

public class Enemy extends Entity {
	private double speed = 0.9;

	public Enemy(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
	}
	
	public void tick() {
		if((int)x < Game.player.getX() && World.isFree((int)(x+speed), this.getY())) {
			x+=speed;
		} else if ((int)x > Game.player.getX() && World.isFree((int)(x-speed), this.getY())) {
			x-=speed;
		} 
		
		if((int)y < Game.player.getY() && World.isFree(this.getX(), (int)(y+speed))) {
			y+=speed;
		} else if ((int)y > Game.player.getY() && World.isFree(this.getX(), (int)(y-speed))) {
			y-=speed;
		}
	}
	

}
