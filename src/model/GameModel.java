package model;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;

/**
 * Central model for the SameGame.
 * The model owns the board, score, game state, and observer list. Controllers
 * ask this class to change the game state, and registered views are notified
 * after changes that should be displayed.
 */

public class GameModel {
  private final Board board;
  private GameState gameState;
  private int numberOfColors;
  private List<GameObserver> observers;
  private List<SoundObserver> soundObservers;
  private int points;
  private int maxPoints;

  /**
   * Creates a game model for the given board and difficulty level.
   *
   * @param board the board that this model will update
   * @param numberOfColors the number of colors to use when a new board is randomized
   */

  public GameModel(Board board,int numberOfColors) {
    this.board = board;
    this.gameState = GameState.PLAYING;
    this.numberOfColors = numberOfColors;
    this.observers = new ArrayList<>();
    this.soundObservers = new ArrayList<>();

    this.board.setNumberOfColors(numberOfColors);

    this.points = 0;
    this.maxPoints = 0;
  }


  public int getPoints() {return points;}
  public int getMaxPoints() {return maxPoints;}
  public Board getBoard() {return this.board;}
  public GameState getGameState() {return this.gameState;}
  public int getDifficultyLevel(){return this.numberOfColors;}

  private void setMaxPoints(int maxPoints) {this.maxPoints = maxPoints;}

  /**
   * Starts a new game by randomizing the board, resetting score and state, and
   * notifying all registered observers.
   */
  public void startNewGame(){
    this.board.randomizeBoard();
    this.gameState = GameState.PLAYING;
    this.points = 0;
    this.notifyObservers();
  }

  /**
   * Registers a view or other observer to receive board updates.
   *
   * @param observer the observer to register
   * @throws IllegalArgumentException if observer is null
   */
  public void addObserver(GameObserver observer){
    if(observer == null)
      throw new IllegalArgumentException("observer cant be null");

    this.observers.add(observer);
  }

  public void addSoundObserver(SoundObserver observer){
    if(observer == null)
      throw new IllegalArgumentException("observer cant be null");
    this.soundObservers.add(observer);
  }

  /**
   * Deregisters an observer so it no longer receives board updates.
   *
   * @param observer the observer to remove
   */
  public void removeObserver(GameObserver observer){
    this.observers.remove(observer);
  }

  /**
   * Notifies all registered observers that the board should be redrawn or
   * otherwise processed.
   */
  private void notifyObservers(){
    for(GameObserver obs : observers){
      obs.updateBoard(this.board);
    }
  }

  private void notifySoundObservers(SoundEvent event){
    for(SoundObserver obs : soundObservers){
      obs.playSound(event);
    }
  }
  /**
   * Removes a connected group from the board, collapses the remaining tiles,
   * updates score and game state, and notifies observers.
   *
   * @param connectedTiles a list of connected tiles that should be removed
   * @throws IllegalArgumentException if the list is null
   */
  public void removeConnectedTiles(ArrayList<Tile> connectedTiles){
    if(connectedTiles == null)
      throw new IllegalArgumentException("connectedTiles cant be null");

    for(Tile tile : connectedTiles){
      int[] pos = this.findTilePosition(tile);
      if(pos == null)
        continue;
      this.board.setTile(pos[0], pos[1], null);
    }

    collapseBoardVertical();
    collapseBoardHorizontal();
    addPoints(connectedTiles.size());
    updateGameState();
    notifyObservers();
    notifySoundObservers(SoundEvent.TILE_CLEAR);
  }

  /**
   * Collapses each column vertically after tiles have been removed.
   * Non null tiles are packed toward the bottom of their column, leaving all
   * null positions at the top.
   */
  private void collapseBoardVertical(){
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

  /**
   * Collapses empty columns horizontally after vertical collapse.

   * Non empty columns keep their relative order but are shifted left so fully
   * empty columns end up on the right side of the board.
   */
  private void collapseBoardHorizontal(){
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

  /**
   * Checks whether a column contains no tiles.
   *
   * @param col the column index to check
   * @return true if every row in the column is null, otherwise false
   */
  private boolean isColumnEmpty(int col){
    for(int row = 0; row < this.board.getRowSize(); row++){
      if(this.board.getTile(row, col) != null)
        return false;
    }

    return true;
  }

  /**
   * Moves all tiles from one column to another and clears the original column.
   *
   * @param fromCol the column to move from
   * @param toCol the column to move to
   */
  private void moveColumn(int fromCol, int toCol){
    for(int row = 0; row < this.board.getRowSize(); row++){
      this.board.setTile(row, toCol, this.board.getTile(row, fromCol));
      this.board.setTile(row, fromCol, null);
    }
  }

  /**
   * Finds all tiles connected to the given tile.
   * Only vertical and horizontal neighbors with the same color are included.
   *
   * @param tile the tile to start searching from
   * @return all connected tiles of the same color, including the starting tile
   */
  public ArrayList<Tile> findConnectedTiles(Tile tile){
    ArrayList<int[]> connectedPositions = findConnectedTilePositions(tile);
    ArrayList<Tile> tiles = new ArrayList<>();

    for(int[] position : connectedPositions){
      tiles.add(this.board.getTile(position[0], position[1]));
    }

    return tiles;
  }


  /**
   * uses the flood fill algorithm from the given tile and returns board coordinates for
   * all connected tiles of the same color.
   *
   * @param tile the tile to start searching from
   * @return row/column positions for all connected matching tiles
   * @throws NullPointerException if tile is null
   * @throws IllegalArgumentException if tile does not exist on this board
   */
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

  /**
   * Finds the current board position of a tile.
   *
   * @param tile the tile to be located
   * @return an int array containing row and column, or null if the tile is not on the board
   */
  private int[] findTilePosition(Tile tile){
    for(int row = 0; row < this.board.getRowSize(); row++){
      for(int col = 0; col < this.board.getColumnSize(); col++){
        if(this.board.getTile(row, col) == tile)
          return new int[]{row, col};
      }
    }

    return null;
  }

  /**
   * Adds an unvisited board position to the flood fill queue if it is inside
   * the board.
   *
   * @param row row to check
   * @param col column to check
   * @param visited matrix of positions already queued or processed
   * @param positionsToCheck queue used by the flood-fill search
   */
  private void addPositionIfValid(int row, int col, boolean[][] visited, ArrayDeque<int[]> positionsToCheck){
    if(!this.board.isValidPosition(row, col) || visited[row][col])
      return;

    visited[row][col] = true;
    positionsToCheck.add(new int[]{row, col});
  }

  /**
   * Checks whether the current board has no legal moves left.
   *
   * @return true if no tile has a connected group of at least two tiles
   */
  public boolean hasNoAvailableMoves(){
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

  /**
   * Checks whether the board is completely empty.
   *
   * @return true if every board position is null
   */
  public boolean checkIfNoTilesLeft(){
    for(int i = 0; i < this.board.getRowSize(); i++){
      for(int j = 0; j < this.board.getColumnSize(); j++){
        if(this.board.getTile(i,j) != null)
          return false;
      }
    }
    return true;
  }

  /**
   * Updates the game state based on the current board.
   * The player wins if no tiles remain, loses if tiles remain but no legal moves
   * are available, and otherwise keeps playing. When the game ends, the best
   * score is updated if the current score is higher.
   */
  public void updateGameState(){
    if(this.checkIfNoTilesLeft()){
      this.gameState = GameState.WON;
      this.notifySoundObservers(SoundEvent.WON);
    } else if(this.hasNoAvailableMoves()){
      this.gameState = GameState.LOST;
      this.notifySoundObservers(SoundEvent.LOST);
    } else {
      return;
    }

    if(this.points > this.maxPoints)
      this.setMaxPoints(this.points);
  }


  /**
   * Adds score for a valid move.
   *
   * @param tilesRemoved the amount of tiles removed in one move
   */
  private void addPoints(int tilesRemoved){
    if(tilesRemoved < 2)
      return;

    this.points += tilesRemoved * 4;
  }

  public MoveSuggestion getBestMoveSuggestion() {
    ArrayList<Tile> visited = new ArrayList<>();
    int groupSize = 1;
    MoveSuggestion bestMove = null;

    for (int row = 0; row < this.getBoard().getRowSize(); row++) {
      for (int col = 0; col < this.getBoard().getColumnSize(); col++) {
        Tile tile = this.getBoard().getTile(row, col);
        if(tile == null || visited.contains(tile))
          continue;

        ArrayList<Tile> connectedTiles = this.findConnectedTiles(tile);
        visited.addAll(connectedTiles);

        if(connectedTiles.size() > groupSize) {
          bestMove = new MoveSuggestion(row,col,connectedTiles.size());
          groupSize = connectedTiles.size();
        }
      }
    }
    return bestMove;
  }
}
