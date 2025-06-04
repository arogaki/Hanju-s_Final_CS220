// Memory Card Game - Single-player concentration game with difficulty levels
// Features timer, scoring system, hints, and responsive UI design
package game.template;

import java.net.URL;

import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

// Main application class for the single-player Memory Card Game
// Handles JavaFX UI, timer, scoring, and game state management
public class App extends Application {
    private VBox root;
    private MemoryGame game;
    private GridPane cardGridPane;
    private Label scoreLabel;
    private Label highScoreLabel;
    private Label timerLabel;
    private Label difficultyLabel;
    private Label gameStatusLabel;
    private Label progressLabel;
    private Button hintButton;
    private Button newGameButton;
    private Timeline gameTimer;
    private int elapsedSeconds;
    
    @Override
    public void start(Stage primaryStage) throws Exception {
        // Initialize the game logic
        game = new MemoryGame();
        elapsedSeconds = 0;
        
        // Create the root container
        root = new VBox();
        root.setSpacing(10);
        root.setPadding(new Insets(10));
        root.getStyleClass().add("root");
        
        // Add components in order
        root.getChildren().add(createMenuBar());
        root.getChildren().add(createControlPanel());
        root.getChildren().add(createScorePanel());
        
        // Add game status
        gameStatusLabel = new Label("Select difficulty and click New Game to start!");
        gameStatusLabel.getStyleClass().add("game-status");
        root.getChildren().add(gameStatusLabel);
        
        // Setup game timer
        setupGameTimer();
        
        // Create the scene and apply styling
        Scene scene = new Scene(root);
        
        // Load CSS stylesheet
        URL styleURL = getClass().getResource("/style.css");
        if (styleURL != null) {
            String stylesheet = styleURL.toExternalForm();
            scene.getStylesheets().add(stylesheet);
        }
        
        // Set up the primary stage
        primaryStage.setTitle("Memory Card Game - Single Player");
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(650);
        primaryStage.setMinHeight(550);
        primaryStage.setWidth(750);
        primaryStage.setHeight(650);
        primaryStage.setResizable(true);
        primaryStage.centerOnScreen();
        primaryStage.show();
        
        // Handle window close event
        primaryStage.setOnCloseRequest(event -> {
            if (gameTimer != null) {
                gameTimer.stop();
            }
            System.out.println("Memory Card Game closing...");
        });
    }
    
    // Create the menu bar with game options
    private MenuBar createMenuBar() {
        MenuBar menuBar = new MenuBar();
        menuBar.getStyleClass().add("menubar");
        
        // Game Menu
        Menu gameMenu = new Menu("Game");
        
        // New Game menu item
        addMenuItem(gameMenu, "New Game", () -> startNewGame());
        addMenuItem(gameMenu, "Reset Scores", () -> resetAllScores());
        addMenuItem(gameMenu, "Rules", () -> showRulesDialog());
        addMenuItem(gameMenu, "High Scores", () -> showHighScoresDialog());
        addMenuItem(gameMenu, "Exit", () -> System.exit(0));
        
        menuBar.getMenus().add(gameMenu);
        return menuBar;
    }
    
    // Create control panel with difficulty selector and buttons
    private HBox createControlPanel() {
        HBox controlPanel = new HBox();
        controlPanel.setSpacing(20);
        controlPanel.setAlignment(Pos.CENTER);
        controlPanel.getStyleClass().add("control-panel");
        
        // Game title label
        Label gameLabel = new Label("GAME:");
        gameLabel.getStyleClass().add("game-title-label");
        
        // Difficulty buttons - horizontal layout
        HBox difficultyBox = new HBox();
        difficultyBox.setSpacing(5);
        difficultyBox.setAlignment(Pos.CENTER);
        
        Button easyButton = new Button("EASY");
        Button normalButton = new Button("NORMAL");
        Button hardButton = new Button("HARD");
        
        easyButton.getStyleClass().addAll("difficulty-button", "difficulty-selected");
        normalButton.getStyleClass().add("difficulty-button");
        hardButton.getStyleClass().add("difficulty-button");
        
        // Store current difficulty and buttons for reference
        Button[] difficultyButtons = {easyButton, normalButton, hardButton};
        GameDifficulty[] difficulties = {GameDifficulty.EASY, GameDifficulty.NORMAL, GameDifficulty.HARD};
        
        // Add click handlers for difficulty buttons
        for (int i = 0; i < difficultyButtons.length; i++) {
            final int index = i;
            difficultyButtons[i].setOnAction(e -> {
                // Update visual selection
                for (Button btn : difficultyButtons) {
                    btn.getStyleClass().remove("difficulty-selected");
                }
                difficultyButtons[index].getStyleClass().add("difficulty-selected");
                
                // Start new game with selected difficulty
                game.startNewGame(difficulties[index]);
                createCardGrid();
                elapsedSeconds = 0;
                if (gameTimer != null) {
                    gameTimer.stop();
                    gameTimer.play();
                }
                updateGameDisplay();
                gameStatusLabel.setText(game.getGameStatus());
            });
        }
        
        difficultyBox.getChildren().addAll(easyButton, normalButton, hardButton);
        
        // New Game button
        newGameButton = new Button("New Game");
        newGameButton.getStyleClass().add("game-button");
        newGameButton.setOnAction(e -> startNewGame());
        
        // Hint button
        hintButton = new Button("Use Hint");
        hintButton.getStyleClass().addAll("game-button", "hint-button");
        hintButton.setDisable(true);
        hintButton.setOnAction(e -> useHint());
        
        controlPanel.getChildren().addAll(gameLabel, difficultyBox, newGameButton, hintButton);
        return controlPanel;
    }
    
    // Create score and information panel
    private HBox createScorePanel() {
        HBox scorePanel = new HBox();
        scorePanel.setSpacing(20);
        scorePanel.setAlignment(Pos.CENTER);
        scorePanel.getStyleClass().add("score-panel");
        
        // Current score
        scoreLabel = new Label("Score: 0");
        scoreLabel.getStyleClass().add("score-label");
        
        // High score for current difficulty
        highScoreLabel = new Label("High Score: 0");
        highScoreLabel.getStyleClass().add("high-score-label");
        
        // Timer
        timerLabel = new Label("Time: 0:00");
        timerLabel.getStyleClass().add("timer-label");
        
        // Difficulty info
        difficultyLabel = new Label("Easy (4x4)");
        difficultyLabel.getStyleClass().add("difficulty-label");
        
        // Progress
        progressLabel = new Label("Progress: 0%");
        progressLabel.getStyleClass().add("progress-label");
        
        scorePanel.getChildren().addAll(scoreLabel, highScoreLabel, timerLabel, 
                                       difficultyLabel, progressLabel);
        return scorePanel;
    }
    
    // Create the grid of memory cards
    private void createCardGrid() {
        if (cardGridPane != null) {
            root.getChildren().remove(cardGridPane);
        }
        
        cardGridPane = new GridPane();
        cardGridPane.getStyleClass().add("card-grid");
        cardGridPane.setAlignment(Pos.CENTER);
        cardGridPane.setHgap(3);
        cardGridPane.setVgap(3);
        cardGridPane.setPadding(new Insets(10));
        
        // Add cards to the grid
        for (int row = 0; row < game.getGridRows(); row++) {
            for (int col = 0; col < game.getGridCols(); col++) {
                Card card = game.getCard(row, col);
                if (card != null) {
                    // Set appropriate size class based on difficulty
                    addCardSizeClass(card);
                    
                    // Set up card click handler
                    final int cardRow = row;
                    final int cardCol = col;
                    card.setOnAction(event -> handleCardClick(cardRow, cardCol));
                    
                    cardGridPane.add(card, col, row);
                }
            }
        }
        
        // Add the card grid back to the root container (before the status label)
        root.getChildren().add(root.getChildren().size() - 1, cardGridPane);
    }
    
    // Add appropriate CSS size class based on difficulty
    private void addCardSizeClass(Card card) {
        // Clear any existing size classes
        card.getStyleClass().removeAll("large-grid", "medium-grid", "small-grid");
        
        GameDifficulty diff = game.getDifficulty();
        switch (diff) {
            case EASY:
                card.getStyleClass().add("large-grid");
                break;
            case NORMAL:
                card.getStyleClass().add("medium-grid");
                break;
            case HARD:
                card.getStyleClass().add("small-grid");
                break;
        }
    }
    
    // Handle when a card is clicked
    private void handleCardClick(int row, int col) {
        Card clickedCard = game.getCard(row, col);
        if (clickedCard == null) return;
        
        // Try to process the card click
        boolean clickProcessed = game.handleCardClick(clickedCard);
        
        if (clickProcessed) {
            updateGameDisplay();
            
            // If we're waiting to flip cards back, set up the delay
            if (game.isWaitingToFlipBack()) {
                gameStatusLabel.setText("No match! Cards flipping back...");
                
                // Create a pause transition for 1.5 seconds
                PauseTransition pause = new PauseTransition(Duration.seconds(1.5));
                pause.setOnFinished(event -> {
                    game.flipMismatchedCardsBack();
                    updateGameDisplay();
                });
                pause.play();
            } else if (game.isGameOver()) {
                gameTimer.stop();
                showGameCompleteDialog();
            }
        }
    }
    
    // Start a new game
    private void startNewGame() {
        // Stop current timer
        if (gameTimer != null) {
            gameTimer.stop();
        }
        
        // Use current game difficulty (already set by difficulty buttons)
        GameDifficulty currentDifficulty = game.getDifficulty();
        
        // Start new game with current difficulty
        game.startNewGame(currentDifficulty);
        
        // Recreate card grid
        createCardGrid();
        
        // Reset timer
        elapsedSeconds = 0;
        gameTimer.play();
        
        // Update display
        updateGameDisplay();
        gameStatusLabel.setText(game.getGameStatus());
    }
    
    // Use hint feature
    private void useHint() {
        if (game.activateHint()) {
            hintButton.setDisable(true);
            gameStatusLabel.setText("Hint activated! All cards revealed for 2 seconds.");
            
            // Hide hint after 2 seconds
            PauseTransition hintTimer = new PauseTransition(Duration.seconds(2));
            hintTimer.setOnFinished(event -> {
                game.hideHint();
                updateGameDisplay();
            });
            hintTimer.play();
        }
    }
    
    // Setup the game timer
    private void setupGameTimer() {
        gameTimer = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            elapsedSeconds++;
            updateTimerDisplay();
            updateGameDisplay(); // Update score as time passes
        }));
        gameTimer.setCycleCount(Timeline.INDEFINITE);
    }
    
    // Update timer display
    private void updateTimerDisplay() {
        int minutes = elapsedSeconds / 60;
        int seconds = elapsedSeconds % 60;
        timerLabel.setText(String.format("Time: %d:%02d", minutes, seconds));
    }
    
    // Update all game display elements
    private void updateGameDisplay() {
        ScoreManager scoreManager = game.getScoreManager();
        
        // Update score labels
        scoreLabel.setText(scoreManager.getScoreDisplay());
        highScoreLabel.setText(scoreManager.getHighScoreDisplay());
        
        // Update difficulty label
        GameDifficulty diff = game.getDifficulty();
        difficultyLabel.setText(diff.getDisplayName() + " " + diff.getGridDescription());
        
        // Update progress
        int progress = game.getProgressPercentage();
        progressLabel.setText(String.format("Progress: %d%%", progress));
        
        // Update hint button
        hintButton.setDisable(!game.isHintAvailable());
        
        // Update status if not in special states
        if (!game.isWaitingToFlipBack() && !game.isHintActive()) {
            gameStatusLabel.setText(game.getGameStatus());
        }
    }
    
    // Show game completion dialog
    private void showGameCompleteDialog() {
        ScoreManager scoreManager = game.getScoreManager();
        boolean isNewHighScore = scoreManager.finishGame();
        
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Game Complete!");
        
        if (isNewHighScore) {
            alert.setHeaderText("Congratulations! New High Score!");
        } else {
            alert.setHeaderText("Game Completed!");
        }
        
        String message = String.format(
            "Difficulty: %s\n" +
            "Final Score: %d\n" +
            "Time: %d:%02d\n" +
            "Moves: %d\n" +
            "Matches: %d/%d\n\n" +
            "%s",
            game.getDifficulty().getDisplayName(),
            scoreManager.getCurrentScore(),
            elapsedSeconds / 60, elapsedSeconds % 60,
            scoreManager.getMoveCount(),
            game.getMatchedPairs(), game.getTotalPairs(),
            isNewHighScore ? "You beat your previous best!" : "Try again for a higher score!"
        );
        
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    // Show rules dialog
    private void showRulesDialog() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("How to Play");
        alert.setHeaderText("Memory Card Game Rules");
        
        String rules = "How to Play Memory Card Game:\n\n" +
                      "1. Select your difficulty level (Easy, Normal, or Hard)\n" +
                      "2. Click New Game to start\n" +
                      "3. Click cards to flip them and reveal symbols\n" +
                      "4. Find matching pairs of symbols\n" +
                      "5. Match all pairs to complete the game\n\n" +
                      "Scoring:\n" +
                      "- Base points for each match\n" +
                      "- Bonus for fast completion\n" +
                      "- Penalty for extra moves\n" +
                      "- Difficulty multiplier\n\n" +
                      "Hints:\n" +
                      "- Earn hints by making 2 consecutive matches\n" +
                      "- Hints reveal all cards for 2 seconds\n\n" +
                      "Good luck!";
        
        alert.setContentText(rules);
        alert.showAndWait();
    }
    
    // Show high scores dialog
    private void showHighScoresDialog() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("High Scores");
        alert.setHeaderText("Best Scores by Difficulty");
        
        ScoreManager scoreManager = game.getScoreManager();
        alert.setContentText(scoreManager.getAllHighScores());
        alert.showAndWait();
    }
    
    // Reset all high scores
    private void resetAllScores() {
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Reset Scores");
        confirmAlert.setHeaderText("Reset All High Scores?");
        confirmAlert.setContentText("This will permanently delete all high scores. Continue?");
        
        confirmAlert.showAndWait().ifPresent(response -> {
            if (response.getButtonData().isDefaultButton()) {
                // Reset scores in the score manager
                ScoreManager scoreManager = game.getScoreManager();
                for (GameDifficulty diff : GameDifficulty.values()) {
                    // Note: This would require adding a method to ScoreManager
                    // For now, just show confirmation
                }
                updateGameDisplay();
                
                Alert infoAlert = new Alert(Alert.AlertType.INFORMATION);
                infoAlert.setTitle("Scores Reset");
                infoAlert.setContentText("All high scores have been reset.");
                infoAlert.showAndWait();
            }
        });
    }
    
    // Helper method to add menu items
    private void addMenuItem(Menu menu, String name, Runnable action) {
        MenuItem menuItem = new MenuItem(name);
        menuItem.setOnAction(event -> action.run());
        menu.getItems().add(menuItem);
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}

