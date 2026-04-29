package model;

public class Tile {
  private Color color;

  public Tile(Color color){
    this.color = color;
  }

  public Color getColor(){return this.color;}

  public void setColor(Color color){
    if(color == null)
      throw new IllegalArgumentException("color cant be null");

    this.color = color;
  }
}
