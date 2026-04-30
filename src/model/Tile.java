package model;

/**
 * Represents a tile in the board and contains a color.
 */
public class Tile {
  private Color color;

  /**
   * @param color initializes a new Tiles with a color
   */
  public Tile(Color color){
    this.color = color;
  }
  public Color getColor(){return this.color;}

}
