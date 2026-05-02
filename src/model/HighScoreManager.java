package model;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Saves and loads the high score list using a readable text file.
 */
public class HighScoreManager {
  private static final String HIGH_SCORE_FILE = "highscores.txt";
  private final ArrayList<Integer> highScores;

  public HighScoreManager() {
    this.highScores = new ArrayList<>();
    loadHighScores();
  }

  /**
   * Adds a score to the high score list and saves the updated list.
   *
   * @param score the score achieved in a finished game
   */
  public void addHighScore(int score) {
    if(score < 0)
      throw new IllegalArgumentException("score cant be negative");

    this.highScores.add(score);
    sortHighScores();
    saveHighScores();
  }

  /**
   * @return a copy of the saved high score list, sorted from highest to lowest
   */
  public List<Integer> getHighScores() {
    return new ArrayList<>(this.highScores);
  }

  /**
   * @return the highest saved score, or 0 if no scores exist
   */
  public int getBestScore() {
    if(this.highScores.isEmpty())
      return 0;

    return this.highScores.get(0);
  }

  /**
   * Saves the high score list to disk as plain text.
   */
  public void saveHighScores() {
    ArrayList<String> scoreLines = new ArrayList<>();

    for(Integer score : this.highScores) {
      scoreLines.add(String.valueOf(score));
    }

    try {
      Files.write(Path.of(HIGH_SCORE_FILE), scoreLines);
    } catch(IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Loads the high score list from a plain text file if it exists.
   */
  public void loadHighScores() {
    File file = new File(HIGH_SCORE_FILE);

    if(!file.exists())
      return;

    try {
      this.highScores.clear();

      for(String line : Files.readAllLines(file.toPath())) {
        String trimmedLine = line.trim();
        if(trimmedLine.isEmpty())
          continue;

        this.highScores.add(Integer.parseInt(trimmedLine));
      }

      sortHighScores();
    } catch(IOException | NumberFormatException e) {
      e.printStackTrace();
    }
  }

  private void sortHighScores() {
    this.highScores.sort(Comparator.reverseOrder());
  }
}
