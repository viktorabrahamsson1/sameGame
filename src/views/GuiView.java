package views;

import controller.GameController;
import java.awt.*;
import javax.swing.*;
import model.Board;
import model.GameModel;
import model.GameObserver;
import model.GameState;
import model.Tile;

public class GuiView extends JFrame implements GameObserver {
  private final GameModel model;
  private final GameController controller;
  private Board board;
  private final BoardPanel boardPanel;
  private final JLabel scoreLabel;
  private final JLabel bestScoreLabel;
  private final JLabel statusLabel;
  private final JButton playAgainButton;

  public GuiView(GameModel model, GameController controller) {
    if (model == null || controller == null) {
      throw new IllegalArgumentException("model and controller cannot be null");
    }

    this.model = model;
    this.controller = controller;
    this.boardPanel = new BoardPanel();

    JLabel titleLabel = new JLabel("SAMEGAME", SwingConstants.CENTER);
    titleLabel.setFont(new Font("Arial", Font.BOLD, 30));

    scoreLabel = new JLabel("Score: 0", SwingConstants.CENTER);
    bestScoreLabel = new JLabel("Best score: 0", SwingConstants.CENTER);
    statusLabel = new JLabel("", SwingConstants.CENTER);
    statusLabel.setFont(new Font("Arial", Font.BOLD, 24));
    playAgainButton = new JButton("Play again");
    playAgainButton.setVisible(false);
    playAgainButton.addActionListener(e -> controller.startNewGame());

    JPanel topPanel = new JPanel(new GridLayout(5, 1));
    topPanel.add(titleLabel);
    topPanel.add(scoreLabel);
    topPanel.add(bestScoreLabel);
    topPanel.add(statusLabel);
    topPanel.add(playAgainButton);

    setTitle("SameGame");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setSize(800, 600);
    setLocationRelativeTo(null);
    setLayout(new BorderLayout());

    add(topPanel, BorderLayout.NORTH);
    add(boardPanel, BorderLayout.CENTER);

    setVisible(true);
  }

  @Override
  public void updateBoard(Board board) {
    this.board = board;

    scoreLabel.setText("Score: " + model.getPoints());
    bestScoreLabel.setText("Best score: " + model.getMaxPoints());
    updateGameOverControls();

    boardPanel.repaint();
  }

  private void updateGameOverControls() {
    GameState gameState = model.getGameState();

    if (gameState == GameState.LOST) {
      statusLabel.setText("GAME OVER: no avaiable moves left"); 
      statusLabel.setForeground(Color.RED);
      playAgainButton.setVisible(true);
    } else if (gameState == GameState.WON) {
      statusLabel.setText("GAME OVER: YOU WON");
      statusLabel.setForeground(new Color(0, 150, 0));
      playAgainButton.setVisible(true);
    } else {
      statusLabel.setText("");
      playAgainButton.setVisible(false);
    }
  }

  private class BoardPanel extends JPanel {
    private int hoverRow = -1;
    private int hoverCol = -1;
    public BoardPanel() {
      addMouseListener(new java.awt.event.MouseAdapter() {
        @Override
        public void mouseClicked(java.awt.event.MouseEvent e) {
          if (board == null) {
            return;
          }

          int tileSize = Math.min(
              getWidth() / board.getColumnSize(),
              getHeight() / board.getRowSize()
          );

          if (tileSize <= 0) {
            return;
          }

          int offsetX = (getWidth() - tileSize * board.getColumnSize()) / 2;
          int offsetY = (getHeight() - tileSize * board.getRowSize()) / 2;

          int col = (e.getX() - offsetX) / tileSize;
          int row = (e.getY() - offsetY) / tileSize;

          controller.playMove(row, col);
        }
      });

      addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
    @Override
    public void mouseMoved(java.awt.event.MouseEvent e) {
      if (board == null) return;

      int tileSize = Math.min(
          getWidth() / board.getColumnSize(),
          getHeight() / board.getRowSize()
      );

      if (tileSize <= 0) return;

      int offsetX = (getWidth() - tileSize * board.getColumnSize()) / 2;
      int offsetY = (getHeight() - tileSize * board.getRowSize()) / 2;

      hoverCol = (e.getX() - offsetX) / tileSize;
      hoverRow = (e.getY() - offsetY) / tileSize;

      repaint();
    }
  });
    }

    @Override
    protected void paintComponent(Graphics g) {
      super.paintComponent(g);

      if (board == null) {
        return;
      }

      int tileSize = Math.min(
          getWidth() / board.getColumnSize(),
          getHeight() / board.getRowSize()
      );

      int offsetX = (getWidth() - tileSize * board.getColumnSize()) / 2;
      int offsetY = (getHeight() - tileSize * board.getRowSize()) / 2;

      if (tileSize <= 0) {
        return;
      }

      java.util.ArrayList<Tile> highlightTiles = null;

     if (hoverRow >= 0 && hoverCol >= 0) {
       if (board.isValidPosition(hoverRow, hoverCol)) {
      Tile tile = board.getTile(hoverRow, hoverCol);
      if (tile != null) {
      highlightTiles = model.findConnectedTiles(tile);
    }
  }
}

      for (int row = 0; row < board.getRowSize(); row++) {
        for (int col = 0; col < board.getColumnSize(); col++) {
          Tile tile = board.getTile(row, col);

          if (tile == null) {
            g.setColor(Color.LIGHT_GRAY);
          } else {
            g.setColor(toAwtColor(tile));
          }

          int x = offsetX + col * tileSize;
          int y = offsetY + row * tileSize;

          g.fillRect(x, y, tileSize, tileSize);
          g.setColor(Color.DARK_GRAY);
          g.drawRect(x, y, tileSize, tileSize);
        }
      }
    }
  }

  private Color toAwtColor(Tile tile) {
    return switch (tile.getColor()) {
      case Black -> Color.BLACK;
      case Blue -> Color.BLUE;
      case Orange -> Color.ORANGE;
      case Yellow -> Color.YELLOW;
      case Red -> Color.RED;
    };
  }
}
