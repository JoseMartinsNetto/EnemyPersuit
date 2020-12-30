package br.com.josedev.entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import br.com.josedev.main.*;
import br.com.josedev.world.*;

public class Player extends Entity {
	public boolean up = false, down = false, left = false, rigth = false;

	public double speed = 0.8;
	private int animationFrames = 0, maxAnimationFrames = 5, indexAnimation = 0, maxIndexAnimation = 3;

	public int rightDir = 0, leftDir = 1;
	public int dir = rightDir;

	private BufferedImage[] rightAnimationPlayer;
	private BufferedImage[] leftAnimationPlayer;

	private BufferedImage damagedPlayer;
	private int damegedFrames = 0;

	public boolean isDamaged = false;
	private boolean moved = false;

	public double life = 100, maxLife = 100;
	public int ammo = 0;

	private boolean hasGun = false;
	public boolean isShooting = false;

	private int walkCounter = 0, walkCounterLimit = 20;

	public Player(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
		setupAnimations();
	}

	public void update() {
		generateAndCheckAnimations();
		checkColisionWithLifePack();
		checkColisionLifeWithAmmunition();
		checkColisionWithWeapon();

		if (life <= 0) {
			life = 0;
			Game.gameState = GameState.GameOver;
		}

		updateCamera();

	}

	public void updateCamera() {
		Camera.x = Camera.clamp(this.getX() - (Game.WIDTH / 2), 0, World.WIDTH * 16 - Game.WIDTH);
		Camera.y = Camera.clamp(this.getY() - (Game.HEIGHT / 2), 0, World.HEIGHT * 16 - Game.HEIGHT);
	}

	public void render(Graphics g) {
		if (isDamaged) {
			g.drawImage(damagedPlayer, this.getX() - Camera.x, this.getY() - Camera.y, null);
			if (hasGun) {
				if (dir == rightDir) {
					g.drawImage(Entity.WEAPON_DAMAGE_RIGHT, this.getX() + 6 - Camera.x, this.getY() - Camera.y, null);
				} else {
					g.drawImage(Entity.WEAPON_DAMAGE_LEFT, this.getX() - 6 - Camera.x, this.getY() - Camera.y, null);
				}
			}
		} else {
			if (dir == rightDir) {
				g.drawImage(rightAnimationPlayer[indexAnimation], this.getX() - Camera.x, this.getY() - Camera.y, null);
				if (hasGun) {
					g.drawImage(Entity.WEAPON_RIGHT, this.getX() + 6 - Camera.x, this.getY() - Camera.y, null);
				}
			} else if (dir == leftDir) {
				g.drawImage(leftAnimationPlayer[indexAnimation], this.getX() - Camera.x, this.getY() - Camera.y, null);
				if (hasGun) {
					g.drawImage(Entity.WEAPON_LEFT, this.getX() - 6 - Camera.x, this.getY() - Camera.y, null);
				}
			}
		}
	}

	private void setupAnimations() {
		rightAnimationPlayer = new BufferedImage[4];
		leftAnimationPlayer = new BufferedImage[4];

		damagedPlayer = Game.spritesheet.getSprite(0, 16, 16, 16);

		for (int i = 0; i < 4; i++) {
			rightAnimationPlayer[i] = Game.spritesheet.getSprite(32 + (i * 16), 0, 16, 16);
			leftAnimationPlayer[i] = Game.spritesheet.getSprite(32 + (i * 16), 16, 16, 16);
		}
	}

	private void generateAndCheckAnimations() {
		moved = false;
		if (up && World.isFree(this.getX(), (int) (y - speed))) {
			moved = true;
			y -= speed;
		} else if (down && World.isFree(this.getX(), (int) (y + speed))) {
			moved = true;
			y += speed;
		}

		if (rigth && World.isFree((int) (x + speed), this.getY())) {
			moved = true;
			dir = rightDir;
			x += speed;
		} else if (left && World.isFree((int) (x - speed), this.getY())) {
			moved = true;
			dir = leftDir;
			x -= speed;
		}

		if (moved) {

			walkCounter++;
			if (walkCounter == (int) walkCounterLimit / 2) {
				Sound.playerWalk.play();
			}
			if (walkCounter == walkCounterLimit) {
				walkCounter = 0;
			}

			animationFrames++;
			if (animationFrames == maxAnimationFrames) {
				animationFrames = 0;
				indexAnimation++;
				if (indexAnimation > maxIndexAnimation) {
					indexAnimation = 0;
				}
			}
		}

		if (isShooting) {
			isShooting = false;

			if (hasGun && ammo > 0) {
				ammo--;

				int dx = 0;
				int px = 0;
				int py = 6;

				if (dir == rightDir) {
					px = 18;
					dx = 1;
				} else {
					px = -8;
					dx = -1;
				}

				Bullet bullet = new Bullet(this.getX() + px, this.getY() + py, 3, 3, null, dx, 0);

				Game.bullets.add(bullet);
				Sound.playerShoot.play();

			}
		}

		if (isDamaged) {
			damegedFrames++;
			if (damegedFrames == 3) {
				damegedFrames = 0;
				isDamaged = false;
			}
		}
	}

	private void checkColisionLifeWithAmmunition() {
		for (int i = 0; i < Game.ammunition.size(); i++) {
			Ammunition currentAmmo = Game.ammunition.get(i);

			if (Entity.isColliding(this, currentAmmo)) {
				ammo += Ammunition.ammunitionSize;

				/*
				 * if(ammo > Ammunition.maxAmmunition) { ammo = Ammunition.maxAmmunition; }
				 */
				Sound.gunLoad.play();

				Game.ammunition.remove(currentAmmo);
				Game.entities.remove(currentAmmo);

			}
		}
	}

	private void checkColisionWithLifePack() {
		for (int i = 0; i < Game.lifepacks.size(); i++) {
			Lifepack lifepack = Game.lifepacks.get(i);

			if (Entity.isColliding(this, lifepack)) {
				life += Lifepack.lifepackSize;

				if (Game.player.life > Game.player.maxLife) {
					Game.player.life = Game.player.maxLife;
				}

				Sound.lifePackRecovered.play();

				Game.lifepacks.remove(lifepack);
				Game.entities.remove(lifepack);

			}
		}

	}

	private void checkColisionWithWeapon() {
		for (int i = 0; i < Game.waepons.size(); i++) {
			Weapon waepon = Game.waepons.get(i);

			if (Entity.isColliding(this, waepon)) {
				hasGun = true;
				Sound.weaponRecovered.play();

				Game.waepons.remove(waepon);
				Game.entities.remove(waepon);

			}
		}

	}

}
