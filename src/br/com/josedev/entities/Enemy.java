package br.com.josedev.entities;

import br.com.josedev.main.Game;
import br.com.josedev.main.Sound;
import br.com.josedev.world.Camera;
import br.com.josedev.world.World;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Enemy extends Entity {
	private final double speed = 1;

	private final int maxIndex = 1;
	private final int damageFrames = 10;
	private final int maxFrames = 10;
	private int frames = 0;
	private int index = 0;
	private int damageCurrent = 0;
	private int life = 30;
	
	private BufferedImage[] animationSprites;

	private boolean isDamage = false;

	public Enemy(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
		setAnimationSprites();
		setMask(3,4, 10,13);
	}

	public void update() {
		animate();
		verifyBulletCollision();

		if (life <= 0) {
			destroySelf();
			return;
		}

		if (isCollidingWithPlayer()) {
			makeDamageInPlayer();
		} else {
			pursuitPlayer();
		}
	}

	public void render(Graphics g) {
		if (isDamage) {
			g.drawImage(Entity.ENEMY_EN_DAMAGE, this.getX() - Camera.x, this.getY() - Camera.y, null);
		} else {
			g.drawImage(animationSprites[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
		}
	}

	private void setAnimationSprites() {
		animationSprites = new BufferedImage[2];
		
		int initialXPosition = 112;
		int incrementsXPosition = 0;

		for (int i = 0; i < animationSprites.length; i++) {
			animationSprites[i] = Game.spritesheet.getSprite(initialXPosition + incrementsXPosition, 16, 16, 16);
			incrementsXPosition = 16;
		}
	}

	private void animate() {
		frames++;
		if (frames == maxFrames) {
			frames = 0;
			index++;
			if (index > maxIndex) {
				index = 0;
			}
		}

		if (isDamage) {
			damageCurrent++;
			if (damageCurrent == damageFrames) {
				damageCurrent = 0;
				isDamage = false;
			}
		}
	}

	private void makeDamageInPlayer() {
		int probabilityOfDamage = Game.rand.nextInt(100);
		boolean shouldMakeDamage = probabilityOfDamage < 10;

		if (shouldMakeDamage) {
			Game.player.receiveDamage(Game.rand.nextInt(5));
			Game.player.isDamaged = true;

			if (Sound.hurtEffect != null) {
				Sound.hurtEffect.play();
			}
		}
	}

	private void pursuitPlayer() {
		int probabilityOfPursuit = Game.rand.nextInt(100);
		boolean shouldPursuit = probabilityOfPursuit < 50;

		if (shouldPursuit) {
			if ((int) x < Game.player.getX() && World.isFree((int) (x + speed), this.getY())
					&& !isCollidingWithFriends((int) (x + speed), this.getY())) {
				x += speed;
			} else if ((int) x > Game.player.getX() && World.isFree((int) (x - speed), this.getY())
					&& !isCollidingWithFriends((int) (x - speed), this.getY())) {
				x -= speed;
			}

			if ((int) y < Game.player.getY() && World.isFree(this.getX(), (int) (y + speed))
					&& !isCollidingWithFriends(this.getX(), (int) (y + speed))) {
				y += speed;
			} else if ((int) y > Game.player.getY() && World.isFree(this.getX(), (int) (y - speed))
					&& !isCollidingWithFriends(this.getX(), (int) (y - speed))) {
				y -= speed;
			}
		}
	}

	private void destroySelf() {
		Game.enemies.remove(this);
		Game.entities.remove(this);
	}

	private void verifyBulletCollision() {
		for (int i = 0; i < Game.bullets.size(); i++) {
			Bullet bs = Game.bullets.get(i);
			if (Entity.isColliding(this, bs)) {
				isDamage = true;
				life -= Bullet.DAMAGE;
				Game.bullets.remove(i);
				if (Sound.enemyHit != null) {
					Sound.enemyHit.play();
				}
				return;
			}
		}
	}

	private boolean isCollidingWithFriends(int xnext, int ynext) {
		Rectangle currentEnemy = new Rectangle(xnext, ynext, World.TILE_SIZE, World.TILE_SIZE);

		for (int i = 0; i < Game.enemies.size(); i++) {
			Enemy e = Game.enemies.get(i);
			if (e == this)
				continue;

			Rectangle targetEnemy = new Rectangle(e.getX(), e.getY(), World.TILE_SIZE, World.TILE_SIZE);

			if (currentEnemy.intersects(targetEnemy)) {
				return true;
			}
		}

		return false;
	}

	public boolean isCollidingWithPlayer() {
		return Entity.isColliding(this, Game.player);
	}

}
