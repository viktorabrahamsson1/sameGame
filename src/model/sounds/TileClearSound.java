package model.sounds;

import model.enums.SoundEvent;
import model.observers.SoundObserver;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;
import java.net.URL;

/**
 * Plays a sound effect when the player clears a group of tiles.
 */
public class TileClearSound implements SoundObserver {
  /**
   * Plays the clear tile sound if the event is TILE_CLEAR otherwise does nothing.
   *
   * @param event the sound event to handle
   */
  @Override
  public void playSound(SoundEvent event) {
    if(event != SoundEvent.TILE_CLEAR)
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
    URL soundUrl = getClass().getResource("/sounds/pop.wav");

    if(soundUrl != null)
      return AudioSystem.getAudioInputStream(soundUrl);

    File soundFile = new File("src/sounds/pop.wav");
    return AudioSystem.getAudioInputStream(soundFile);
  }
}
