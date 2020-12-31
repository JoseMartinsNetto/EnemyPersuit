package br.com.josedev.main;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.swing.JFrame;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

import br.com.josedev.entities.*;
import br.com.josedev.graphics.*;
import br.com.josedev.world.World;

public class Game extends Canvas implements Runnable, KeyListener {	
	public static JFrame frame;
	
	public static final int WIDTH = 240;
	public static final int HEIGHT = 160;
	public static final int SCALE = 4;
	
	public static String curLevelName;
	public static GameState gameState = GameState.Loading;
	
	public static List<Entity> entities;
	public static List<Enemy> enemies;
	public static List<Lifepack> lifepacks;
	public static List<Bullet> bullets;
	public static List<Weapon> waepons;
	public static List<Ammunition> ammunition;
	
	public static Spritesheet spritesheet;
	public static Player player;
	public static World world;
	public static Random rand;

	public Menu menu;
	public UI ui;

	private static final long serialVersionUID = 1L;
	private static double waitTime = 0, waitTimeLimit = 300.0;
	
	private int CUR_LEVEL = 1, MAX_LEVEL = 2;
	private int framesToggleGameOver = 0;
	private boolean toggleGameOver = true;
	private boolean isRunning = false;
	private boolean restartGame = false;
	
	private Thread thread;
	private BufferedImage image;

	public Game() {
		rand = new Random();
		addKeyListener(this);
		setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
		initFrame();

		ui = new UI();
		menu = new Menu();
		image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		
		initGameLevel("level1.png");
	}

	public static void main(String[] args) {
		new Game().start();
	}

	public static void initGameLevel(String level) {
		curLevelName = level;
		
		entities = new ArrayList<Entity>();
		enemies = new ArrayList<Enemy>();
		lifepacks = new ArrayList<Lifepack>();
		bullets = new ArrayList<Bullet>();
		waepons = new ArrayList<Weapon>();
		ammunition = new ArrayList<Ammunition>();

		spritesheet = new Spritesheet("/spritesheet.png");
		
		player = new Player(0, 0, 16, 16, spritesheet.getSprite(32, 0, 16, 16));
		entities.add(player);
		
		world = new World("/" + level);
		player.updateCamera();
		waitTime = 0.0;
	}

	public synchronized void start() {
		thread = new Thread(this);
		isRunning = true;
		thread.start();
	}

	public synchronized void stop() {
		isRunning = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static void saveGame(String[] val1, int[] val2, int encode) {
		// BufferedWriter writer = null;
	}

	public void initFrame() {
		frame = new JFrame("Enemy persuit");
		frame.add(this);
		frame.setResizable(false);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}

	public void update() {
		if (gameState == GameState.Normal) {
			restartGame = false;
			
			for (int i = 0; i < entities.size(); i++) {
				entities.get(i).update();
			}

			for (int i = 0; i < bullets.size(); i++) {
				bullets.get(i).update();
			}

			if (enemies.size() == 0) {
				CUR_LEVEL++;
				
				if (CUR_LEVEL > MAX_LEVEL) {
					CUR_LEVEL = 1;
					
					gameState = GameState.Menu;
				}

				String newWorld = "level" + CUR_LEVEL + ".png";
				
				curLevelName = newWorld;

				initGameLevel(newWorld);
			}
		} else if (gameState == GameState.GameOver) {
			framesToggleGameOver++;

			if (framesToggleGameOver == 30) {
				framesToggleGameOver = 0;
				if (toggleGameOver) {
					toggleGameOver = false;
				} else {
					toggleGameOver = true;
				}
			}

			if (restartGame) {
				gameState = GameState.Normal;
				
				initGameLevel(curLevelName);
			}
			
		} else if (gameState == GameState.Menu) {
			menu.update();
		}

	}

	public void render() {
		BufferStrategy bs = this.getBufferStrategy();

		if (bs == null) {
			this.createBufferStrategy(3);
			return;
		}

		Graphics g = image.getGraphics();
		g.setColor(new Color(0, 0, 0));
		g.fillRect(0, 0, WIDTH, HEIGHT);

		// Rendering the items of the game
		world.render(g);

		for (int i = 0; i < entities.size(); i++) {
			entities.get(i).render(g);
		}

		for (int i = 0; i < bullets.size(); i++) {
			bullets.get(i).render(g);
		}

		g.dispose();
		g = bs.getDrawGraphics();
		g.drawImage(image, 0, 0, WIDTH * SCALE, HEIGHT * SCALE, null);

		ui.render(g);

		if (gameState == GameState.Loading) {
			ui.renderLoading(g);
		} else if (gameState == GameState.GameOver) {
			ui.renderGameOver(g, toggleGameOver);
		} else if (gameState == GameState.Menu) {
			menu.render(g);
		}

		bs.show();
	}

	@Override
	public void run() {
		long lastTime = System.nanoTime();
		int frames = 0;
		
		double amountOfupdates = 60.0;
		double minFrameUpdates = 55.0;
		double ns = 1000000000 / amountOfupdates;
		double timer = System.currentTimeMillis();
		double delta = 0;

		boolean firstRun = true;
		
		requestFocus();

		while (isRunning) {
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;

			if (delta >= 1) {
				update();
				render();
				frames++;
				delta--;
				waitTime ++;
				
				if(waitTime >= waitTimeLimit) {
					waitTime = waitTimeLimit;
				}
				
			}
			

			if ((frames >= minFrameUpdates && frames <= amountOfupdates) && firstRun && waitTime >= waitTimeLimit) {
				gameState = GameState.Menu;
				
				firstRun = false;
				
				Sound.musicBackground.loop(0.8f);
			}

			if (System.currentTimeMillis() - timer >= 1000 || frames >= amountOfupdates) {
				Debug.log("FPS: " + frames);
				frames = 0;
				timer += 1000;
			}
		}

		stop();
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D) {
			player.rigth = true;

		} else if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A) {
			player.left = true;
		}

		if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W) {
			player.up = true;
			if (gameState == GameState.Menu) {
				menu.up = true;
			}

		} else if (e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S) {
			player.down = true;
			if (gameState == GameState.Menu) {
				menu.down = true;
			}
		}

		if (e.getKeyCode() == KeyEvent.VK_SPACE) {
			player.isShooting = true;
		}

		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			restartGame = true;

			if (gameState == GameState.Menu) {
				menu.enter = true;
			}
		}

		if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			if (gameState == GameState.Normal) {
				gameState = GameState.Menu;
				menu.pause = true;
				Sound.musicBackground.pause();
			}
		}

	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D) {
			player.rigth = false;

		} else if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A) {
			player.left = false;
		}

		if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W) {
			player.up = false;

		} else if (e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S) {
			player.down = false;
		}

	}

	@Override
	public void keyTyped(KeyEvent e) {

	}

}
