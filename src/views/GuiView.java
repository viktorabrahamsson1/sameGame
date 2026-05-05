package views;

import controller.GameController;
import model.Board;
import model.GameModel;
import model.MoveSuggestion;
import model.Tile;
import model.enums.GameState;
import model.observers.GameObserver;

import javax.swing.*;
import java.awt.*;

public class GuiView extends JFrame implements GameObserver {
  private final GameModel model;
  private final GameController controller;
  private Board board;
  private final BoardPanel boardPanel;
  private final JLabel scoreLabel;
  private final JLabel bestScoreLabel;
  private final JLabel statusLabel;
  private final JLabel difficultyLevel;
  private final JButton playAgainButton;

  // Suggested move
  private final JButton bestMoveButton;
  private final JTextField bestMoveRowField;
  private final JTextField bestMoveColField;
  private final JTextField bestMovePointsField;

  public GuiView(GameModel model, GameController controller) {
    if (model == null || controller == null) {
      throw new IllegalArgumentException("model and controller cannot be null");
    }

    this.model = model;
    this.controller = controller;
    this.boardPanel = new BoardPanel();
    this.boardPanel.setPreferredSize(new Dimension(1000, 520));

    JLabel titleLabel = new JLabel("SAMEGAME", SwingConstants.CENTER);
    titleLabel.setFont(new Font("Arial", Font.BOLD, 30));

    scoreLabel = new JLabel("Score: 0", SwingConstants.CENTER);
    bestScoreLabel = new JLabel("Best score: 0", SwingConstants.CENTER);
    statusLabel = new JLabel("", SwingConstants.CENTER);
    difficultyLevel = new JLabel("Difficulty level: ", SwingConstants.CENTER);
    statusLabel.setFont(new Font("Arial", Font.BOLD, 24));
    playAgainButton = new JButton("Play again");
    playAgainButton.setVisible(false);
    playAgainButton.addActionListener(e -> startNewGameWithSelectedDifficulty());

    bestMoveButton = new JButton("Get best move");
    bestMoveButton.addActionListener(e -> showBestMoveSuggestion());
    bestMoveRowField = createSuggestionField();
    bestMoveColField = createSuggestionField();
    bestMovePointsField = createSuggestionField();

    JPanel suggestionPanel = createSuggestionPanel();

    JPanel topPanel = new JPanel(new GridLayout(7, 1));
    topPanel.add(titleLabel);
    topPanel.add(difficultyLevel);
    topPanel.add(scoreLabel);
    topPanel.add(bestScoreLabel);
    topPanel.add(suggestionPanel);
    topPanel.add(statusLabel);
    topPanel.add(playAgainButton);

    setTitle("SameGame");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setSize(1100, 750);
    setLocationRelativeTo(null);
    setLayout(new BorderLayout());

    add(topPanel, BorderLayout.NORTH);
    add(boardPanel, BorderLayout.CENTER);

    setVisible(true);
  }

  @Override
  public void updateBoard(Board board) {
    this.board = board;

    difficultyLevel.setText("Difficulty " + model.getDifficultyLevel());
    scoreLabel.setText("Score: " + model.getPoints());
    bestScoreLabel.setText("Best score: " + model.getMaxPoints());
    clearBestMoveSuggestion();
    updateGameOverControls();

    boardPanel.repaint();
  }

  private void updateGameOverControls() {
    GameState gameState = model.getGameState();

    if (gameState == GameState.LOST) {
      statusLabel.setText("GAME OVER: No more moves!");
      statusLabel.setForeground(Color.RED);
      playAgainButton.setVisible(true);
    } else if (gameState == GameState.WON) {
      statusLabel.setText("YOU WON !!");
      statusLabel.setForeground(new Color(0, 150, 0));
      playAgainButton.setVisible(true);
    } else {
      statusLabel.setText("");
      playAgainButton.setVisible(false);
    }
  }

  private class BoardPanel extends JPanel {
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

          int boardWidth = tileSize * board.getColumnSize();
          int xOffset = (getWidth() - boardWidth) / 2;
          int adjustedX = e.getX() - xOffset;

          if (adjustedX < 0 || adjustedX >= boardWidth) {
            return;
          }

          int col = adjustedX / tileSize;
          int row = e.getY() / tileSize;

          controller.playMove(row, col);
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

      if (tileSize <= 0) {
        return;
      }

      for (int row = 0; row < board.getRowSize(); row++) {
        for (int col = 0; col < board.getColumnSize(); col++) {
          Tile tile = board.getTile(row, col);

          if (tile == null) {
            g.setColor(Color.LIGHT_GRAY);
          } else {
            g.setColor(toAwtColor(tile));
          }

          int boardWidth = tileSize * board.getColumnSize();
          int xOffset = (getWidth() - boardWidth) / 2;
          int x = xOffset + col * tileSize;
          int y = row * tileSize;

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


  private JTextField createSuggestionField() {
    JTextField field = new JTextField("-");
    field.setEditable(false);
    field.setHorizontalAlignment(SwingConstants.CENTER);
    return field;
  }

  private JPanel createSuggestionPanel() {
    JPanel suggestionPanel = new JPanel(new GridBagLayout());
    GridBagConstraints constraints = new GridBagConstraints();
    constraints.insets = new Insets(0, 6, 0, 6);
    constraints.fill = GridBagConstraints.HORIZONTAL;
    constraints.gridy = 0;

    constraints.gridx = 0;
    constraints.weightx = 0.25;
    suggestionPanel.add(bestMoveButton, constraints);

    constraints.gridx = 1;
    constraints.weightx = 0;
    suggestionPanel.add(new JLabel("Row:", SwingConstants.RIGHT), constraints);

    constraints.gridx = 2;
    constraints.weightx = 0.2;
    suggestionPanel.add(bestMoveRowField, constraints);

    constraints.gridx = 3;
    constraints.weightx = 0;
    suggestionPanel.add(new JLabel("Col:", SwingConstants.RIGHT), constraints);

    constraints.gridx = 4;
    constraints.weightx = 0.2;
    suggestionPanel.add(bestMoveColField, constraints);

    constraints.gridx = 5;
    constraints.weightx = 0;
    suggestionPanel.add(new JLabel("Points:", SwingConstants.RIGHT), constraints);

    constraints.gridx = 6;
    constraints.weightx = 0.2;
    suggestionPanel.add(bestMovePointsField, constraints);

    return suggestionPanel;
  }

  private void showBestMoveSuggestion() {
    MoveSuggestion suggestion = model.getBestMoveSuggestion();

    if (suggestion == null) {
      bestMoveRowField.setText("-");
      bestMoveColField.setText("-");
      bestMovePointsField.setText("0");
      statusLabel.setText("No valid move suggestion");
      return;
    }

    bestMoveRowField.setText(String.valueOf(suggestion.getRow() + 1));
    bestMoveColField.setText(String.valueOf(suggestion.getCol() + 1));
    bestMovePointsField.setText(String.valueOf(suggestion.getPoints()));
  }

  private void startNewGameWithSelectedDifficulty() {
    Integer difficultyLevel = chooseDifficultyLevel();

    if(difficultyLevel != null)
      controller.startNewGame(difficultyLevel);
  }

  private Integer chooseDifficultyLevel() {
    Integer[] difficultyLevels = {2, 3, 4, 5};
    Integer currentDifficulty = model.getDifficultyLevel();

    return (Integer) JOptionPane.showInputDialog(
            this,
            "Choose difficulty level",
            "New game",
            JOptionPane.QUESTION_MESSAGE,
            null,
            difficultyLevels,
            currentDifficulty
    );
  }

  private void clearBestMoveSuggestion() {
    bestMoveRowField.setText("-");
    bestMoveColField.setText("-");
    bestMovePointsField.setText("-");
  }
}
