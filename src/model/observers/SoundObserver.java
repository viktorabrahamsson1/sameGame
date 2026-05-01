package model.observers;

import model.enums.SoundEvent;

public interface SoundObserver {
  void playSound(SoundEvent event);
}
