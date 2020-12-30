package br.com.josedev.main;

import java.io.*;
import javax.sound.sampled.*;

public class Sound {

	public static final AudioStream musicBackground = load("/hadouken-theme.wav", 1);
	public static final AudioStream hurtEffect = load("/hurt.wav", 1);
	public static final AudioStream playerShoot = load("/shot.wav", 1);
	public static final AudioStream lifePackRecovered = load("/lifepack.wav", 1);
	public static final AudioStream gunLoad = load("/gun_load.wav", 1);
	public static final AudioStream weaponRecovered = load("/weapon-recovered.wav", 1);
	public static final AudioStream enemyHit = load("/enemy-hit.wav", 1);
	public static final AudioStream playerWalk = load("/player-walk.wav", 1);

	private static AudioStream load(String name, int count) {
		try {
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			DataInputStream inputStream = new DataInputStream(Sound.class.getResourceAsStream(name));

			byte[] buffer = new byte[1024];
			int read = 0;

			while ((read = inputStream.read(buffer)) >= 0) {
				outputStream.write(buffer, 0, read);
			}

			inputStream.close();

			byte[] data = outputStream.toByteArray();

			return new AudioStream(data, count);

		} catch (Exception e) {
			try {
				return new AudioStream(null, 0);
			} catch (Exception ee) {
				return null;
			}
		}
	}

	public static class AudioStream {
		private Clip[] clips;
		private int audioCounter;
		private int count;

		public AudioStream(byte[] buffer, int count)
				throws LineUnavailableException, IOException, UnsupportedAudioFileException {
			if (buffer == null)
				return;

			clips = new Clip[count];
			this.count = count;

			for (int i = 0; i < count; i++) {
				clips[i] = AudioSystem.getClip();
				clips[i].open(AudioSystem.getAudioInputStream(new ByteArrayInputStream(buffer)));
			}
		}

		public void play() {
			if (clips == null)
				return;

			clips[audioCounter].stop();
			clips[audioCounter].setFramePosition(0);
			clips[audioCounter].start();
			audioCounter++;

			if (audioCounter >= count) {
				audioCounter = 0;
			}
		}

		public void pause() {
			clips[audioCounter].stop();
		}

		public void resume() {
			if (clips == null)
				return;

			clips[audioCounter].start();
		}

		public void stop() {
			clips[audioCounter].stop();
			clips[audioCounter].setFramePosition(0);
		}

		public void loop() {
			if (clips == null)
				return;

			clips[audioCounter].loop(Clip.LOOP_CONTINUOUSLY);
		}

		public void loop(float volume) {
			if (clips == null)
				return;

			FloatControl gainControl = (FloatControl) clips[audioCounter].getControl(FloatControl.Type.MASTER_GAIN);

			float range = gainControl.getMaximum() - gainControl.getMinimum();
			float gain = (range * volume) + gainControl.getMinimum();
			gainControl.setValue(gain);

			clips[audioCounter].loop(300);
		}

	}

}
