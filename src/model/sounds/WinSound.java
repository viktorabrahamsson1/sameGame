package model.sounds;

import model.enums.SoundEvent;
import model.observers.SoundObserver;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;
import java.net.URL;

/**
 * Plays a sound effect when the player wins the game.
 */
public class WinSound implements SoundObserver {
  /**
   * Plays the win sound if the event is WON otherwise does nothing.
   *
   * @param event the sound event to handle
   */
  @Override
  public void playSound(SoundEvent event) {
    if(event != SoundEvent.WON)
      return;
    try {
      AudioInputStream audioStream = getAudioStream();
      Clip clip = AudioSystem.getClip();
      clip.open(audioStream);
      clip.start();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private AudioInputStream getAudioStream() throws Exception {
    URL soundUrl = getClass().getResource("/sounds/win.wav");

    if(soundUrl != null)
      return AudioSystem.getAudioInputStream(soundUrl);

    File soundFile = new File("src/sounds/win.wav");
    return AudioSystem.getAudioInputStream(soundFile);
  }
}
