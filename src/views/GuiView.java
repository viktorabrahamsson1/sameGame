package views;

import controller.GameController;
import model.Board;
import model.GameObserver;
import model.Tile;

import javax.swing.*;
import java.awt.*;

public class GuiView extends JFrame implements GameObserver {
  private final GameController controller;
  private Board board;
  private final BoardPanel boardPanel;

  public GuiView(GameController controller) {
    if (controller == null) {
      throw new IllegalArgumentException("controller cannot be null");
    }

    this.controller = controller;
    this.boardPanel = new BoardPanel();

    setTitle("SameGame");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setSize(800, 500);
    setLocationRelativeTo(null);
    add(boardPanel);
    setVisible(true);
  }

  @Override
  public void updateBoard(Board board) {
    this.board = board;
    boardPanel.repaint();
  }

  private class BoardPanel extends JPanel {
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

      for (int row = 0; row < board.getRowSize(); row++) {
        for (int col = 0; col < board.getColumnSize(); col++) {
          Tile tile = board.getTile(row, col);

          if (tile == null) {
            g.setColor(Color.LIGHT_GRAY);
          } else {
            g.setColor(toAwtColor(tile));
          }

          int x = col * tileSize;
          int y = row * tileSize;

          g.fillRect(x, y, tileSize, tileSize);
          g.setColor(Color.DARK_GRAY);
          g.drawRect(x, y, tileSize, tileSize);
        }
      }
    }

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

          int col = e.getX() / tileSize;
          int row = e.getY() / tileSize;

          controller.playMove(row, col);
        }
      });
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