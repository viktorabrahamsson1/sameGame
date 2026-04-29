package model;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;

public class GameModel {
  private final Board board;
  private GameState gameState;
  private int numberOfColors;
  private List<GameObserver> observers;
  private int points;
  private int maxPoints;

  public GameModel(Board board,int numberOfColors) {
    this.board = board;
    this.gameState = GameState.PLAYING;
    this.numberOfColors = numberOfColors;
    this.observers = new ArrayList<>();

    this.board.setNumberOfColors(numberOfColors);

    this.points = 0;
    this.maxPoints = 0;
  }

  public int getPoints() {
    return points;
  }
  public int getMaxPoints() {
    return maxPoints;
  }
  public Board getBoard() {
    return this.board;
  }
  public GameState getGameState() {
    return this.gameState;
  }
  private void setMaxPoints(int maxPoints) {
    this.maxPoints = maxPoints;
  }



  public void addObserver(GameObserver observer){
    if(observer == null)
      throw new IllegalArgumentException("observer cant be null");

    this.observers.add(observer);
  }

  public void removeObserver(GameObserver observer){
    this.observers.remove(observer);
  }

  public void notifyObservers(){
    for(GameObserver obs : observers){
      obs.updateBoard(this.board);
    }
  }

  public void removeConnectedTiles(ArrayList<Tile> connectedTiles){
    if(connectedTiles == null)
      throw new IllegalArgumentException("connectedTiles cant be null");

    for(Tile tile : connectedTiles){
      int[] pos = this.findTilePosition(tile);
      if(pos == null)
        continue;
      this.board.setTile(pos[0], pos[1], null);
    }

    colapseBoardVertical();
    colapseBoardHorizontal();
    addPoints(connectedTiles.size());
    updateGameState();
    notifyObservers();
  }

  public void colapseBoardVertical(){
    int rows = this.board.getRowSize();
    int cols = this.board.getColumnSize();

    for(int col = 0; col < cols; col++){
      int writeRow = rows - 1;

      for(int row = rows - 1; row >= 0; row--){
        Tile tile = this.board.getTile(row, col);

        if(tile == null)
          continue;

        this.board.setTile(writeRow, col, tile);

        if(writeRow != row)
          this.board.setTile(row, col, null);

        writeRow--;
      }
    }
  }

  public void colapseBoardHorizontal(){
    int cols = this.board.getColumnSize();
    int writeCol = 0;

    for(int col = 0; col < cols; col++){
      if(isColumnEmpty(col))
        continue;

      if(writeCol != col)
        moveColumn(col, writeCol);

      writeCol++;
    }
  }

  private boolean isColumnEmpty(int col){
    for(int row = 0; row < this.board.getRowSize(); row++){
      if(this.board.getTile(row, col) != null)
        return false;
    }

    return true;
  }

  private void moveColumn(int fromCol, int toCol){
    for(int row = 0; row < this.board.getRowSize(); row++){
      this.board.setTile(row, toCol, this.board.getTile(row, fromCol));
      this.board.setTile(row, fromCol, null);
    }
  }

  public ArrayList<Tile> findConnectedTiles(Tile tile){
    ArrayList<int[]> connectedPositions = findConnectedTilePositions(tile);
    ArrayList<Tile> tiles = new ArrayList<>();

    for(int[] position : connectedPositions){
      tiles.add(this.board.getTile(position[0], position[1]));
    }

    return tiles;
  }


  private ArrayList<int[]> findConnectedTilePositions(Tile tile){
    if(tile == null)
      throw new NullPointerException("tile cant be null");

    int[] startPosition = findTilePosition(tile);
    if(startPosition == null)
      throw new IllegalArgumentException("tile does not exist on this board");

    Color targetColor = tile.getColor();
    ArrayList<int[]> connectedPositions = new ArrayList<>();
    boolean[][] visited = new boolean[this.board.getRowSize()][this.board.getColumnSize()];
    ArrayDeque<int[]> positionsToCheck = new ArrayDeque<>();

    positionsToCheck.add(startPosition);
    visited[startPosition[0]][startPosition[1]] = true;

    while(!positionsToCheck.isEmpty()){
      int[] position = positionsToCheck.removeFirst();
      int row = position[0];
      int col = position[1];
      Tile currentTile = this.board.getTile(row, col);

      if(currentTile == null || currentTile.getColor() != targetColor)
        continue;

      connectedPositions.add(position);

      addPositionIfValid(row - 1, col, visited, positionsToCheck);
      addPositionIfValid(row + 1, col, visited, positionsToCheck);
      addPositionIfValid(row, col - 1, visited, positionsToCheck);
      addPositionIfValid(row, col + 1, visited, positionsToCheck);
    }

    return connectedPositions;
  }

  private int[] findTilePosition(Tile tile){
    for(int row = 0; row < this.board.getRowSize(); row++){
      for(int col = 0; col < this.board.getColumnSize(); col++){
        if(this.board.getTile(row, col) == tile)
          return new int[]{row, col};
      }
    }

    return null;
  }

  private void addPositionIfValid(int row, int col, boolean[][] visited, ArrayDeque<int[]> positionsToCheck){
    if(!this.board.isValidPosition(row, col) || visited[row][col])
      return;

    visited[row][col] = true;
    positionsToCheck.add(new int[]{row, col});
  }

  //TODO
  public boolean checkIfUserLost(){
    for(int i = 0; i < this.board.getRowSize(); i++){
      for(int j = 0; j < this.board.getColumnSize(); j++){
        Tile tile = this.board.getTile(i,j);
        if(tile == null)
          continue;
        if(this.findConnectedTilePositions(tile).size() > 1){
          return false;
        }
      }
    }
    return true;
  }

  public boolean checkIfNoTilesLeft(){
    for(int i = 0; i < this.board.getRowSize(); i++){
      for(int j = 0; j < this.board.getColumnSize(); j++){
        if(this.board.getTile(i,j) != null)
          return false;
      }
    }
    return true;
  }


  public void updateGameState(){
    if(this.checkIfNoTilesLeft()){
      this.gameState = GameState.WON;
    } else if(this.checkIfUserLost()){
      this.gameState = GameState.LOST;
    } else {
      return;
    }

    if(this.points >= this.maxPoints)
      this.setMaxPoints(this.points);
    this.points = 0;
  }

  private void addPoints(int tilesRemoved){
  if(tilesRemoved < 2)
    return;

  this.points += (tilesRemoved - 2) * (tilesRemoved - 2);
}
}
