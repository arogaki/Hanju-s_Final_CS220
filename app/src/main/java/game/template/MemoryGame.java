package game.template;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// MemoryGame class handles single-player game logic with difficulty levels
// Includes hint system, scoring, and timer functionality
public class MemoryGame {
    private GameDifficulty difficulty;           // Current difficulty level
    private Card[][] cardGrid;                  // 2D array to hold the cards
    private List<Card> flippedCards;            // Currently flipped cards
    private int matchedPairs;                   // Number of pairs matched
    private boolean gameOver;                   // Whether the game is finished
    private boolean waitingToFlipBack;          // Whether waiting to flip mismatched cards
    private ScoreManager scoreManager;          // Handles scoring and statistics
    private int consecutiveMatches;             // Track consecutive successful matches
    private boolean hintAvailable;              // Whether hint can be used
    private boolean hintActive;                 // Whether hint is currently showing
    
    // Constructor initializes a new single-player memory game
    public MemoryGame() {
        this.difficulty = GameDifficulty.EASY;  // Default to easy
        this.scoreManager = new ScoreManager();
        resetGame();
    }
    
    // Start a new game with specified difficulty
    public void startNewGame(GameDifficulty newDifficulty) {
        this.difficulty = newDifficulty;
        scoreManager.startNewGame(difficulty);
        resetGame();
        initializeCards();
    }
    
    // Reset game state without changing difficulty
    public void resetGame() {
        flippedCards = new ArrayList<>();
        matchedPairs = 0;
        gameOver = false;
        waitingToFlipBack = false;
        consecutiveMatches = 0;
        hintAvailable = false;
        hintActive = false;
        
        // Initialize card grid based on current difficulty
        cardGrid = new Card[difficulty.getRows()][difficulty.getCols()];
    }
    
    // Initialize the card grid with random pairs of symbols
    private void initializeCards() {
        String[] symbols = Card.getCardSymbols();
        List<String> cardValues = new ArrayList<>();
        
        // Create pairs of cards (each symbol appears twice)
        int pairsNeeded = difficulty.getTotalPairs();
        for (int i = 0; i < pairsNeeded; i++) {
            String symbol = symbols[i % symbols.length];
            cardValues.add(symbol);
            cardValues.add(symbol);  // Add the same symbol twice for a pair
        }
        
        // Shuffle the card values randomly
        Collections.shuffle(cardValues);
        
        // Create Card objects and place them in the grid
        int cardIndex = 0;
        for (int row = 0; row < difficulty.getRows(); row++) {
            for (int col = 0; col < difficulty.getCols(); col++) {
                String value = cardValues.get(cardIndex);
                cardGrid[row][col] = new Card(value);
                cardIndex++;
            }
        }
    }
    
    // Handle a card being clicked
    public boolean handleCardClick(Card card) {
        // Don't allow clicks during certain states
        if (waitingToFlipBack || gameOver || card.isFaceUp() || 
            card.isMatched() || hintActive || card.isHintVisible()) {
            return false;
        }
        
        // Don't allow more than 2 cards to be flipped at once
        if (flippedCards.size() >= 2) {
            return false;
        }
        
        // Record the move
        scoreManager.recordMove();
        
        // Flip the card and add to flipped list
        card.flipUp();
        flippedCards.add(card);
        
        // Check if we have two cards flipped
        if (flippedCards.size() == 2) {
            checkForMatch();
        }
        
        return true;
    }
    
    // Check if the two flipped cards match
    private void checkForMatch() {
        Card card1 = flippedCards.get(0);
        Card card2 = flippedCards.get(1);
        
        if (card1.getValue().equals(card2.getValue())) {
            // Cards match!
            card1.setMatched();
            card2.setMatched();
            matchedPairs++;
            consecutiveMatches++;
            
            // Record the match for scoring
            scoreManager.recordMatch();
            
            // Check if hint should be available (2 consecutive matches)
            if (consecutiveMatches >= 2) {
                hintAvailable = true;
                consecutiveMatches = 0;  // Reset counter
            }
            
            // Clear flipped cards list
            flippedCards.clear();
            
            // Check if game is over
            if (matchedPairs == difficulty.getTotalPairs()) {
                gameOver = true;
                scoreManager.finishGame();
            }
        } else {
            // Cards don't match - reset consecutive matches
            consecutiveMatches = 0;
            // Cards will be flipped back after a delay
            waitingToFlipBack = true;
        }
    }
    
    // Flip mismatched cards back down
    public void flipMismatchedCardsBack() {
        if (flippedCards.size() == 2 && waitingToFlipBack) {
            flippedCards.get(0).flipDown();
            flippedCards.get(1).flipDown();
            flippedCards.clear();
            waitingToFlipBack = false;
        }
    }
    
    // Activate hint - show all unmatched cards for 2 seconds
    public boolean activateHint() {
        if (!hintAvailable || hintActive || gameOver) {
            return false;
        }
        
        hintActive = true;
        hintAvailable = false;
        
        // Show all unmatched, face-down cards
        for (int row = 0; row < difficulty.getRows(); row++) {
            for (int col = 0; col < difficulty.getCols(); col++) {
                Card card = cardGrid[row][col];
                if (!card.isMatched() && !card.isFaceUp()) {
                    card.showHint();
                }
            }
        }
        
        return true;
    }
    
    // Hide hint and return cards to face down
    public void hideHint() {
        if (!hintActive) return;
        
        hintActive = false;
        
        // Hide all hint cards
        for (int row = 0; row < difficulty.getRows(); row++) {
            for (int col = 0; col < difficulty.getCols(); col++) {
                Card card = cardGrid[row][col];
                if (card.isHintVisible()) {
                    card.hideHint();
                }
            }
        }
    }
    
    // Get the card at the specified grid position
    // @param row Row index
    // @param col Column index
    // @return The card at that position
    public Card getCard(int row, int col) {
        if (row >= 0 && row < difficulty.getRows() && 
            col >= 0 && col < difficulty.getCols()) {
            return cardGrid[row][col];
        }
        return null;
    }
    
    // Check if the game is over
    // @return true if all pairs have been matched
    public boolean isGameOver() {
        return gameOver;
    }
    
    // Check if we're waiting to flip cards back
    // @return true if waiting to flip mismatched cards back
    public boolean isWaitingToFlipBack() {
        return waitingToFlipBack;
    }
    
    // Check if hint is available
    // @return true if hint can be used
    public boolean isHintAvailable() {
        return hintAvailable;
    }
    
    // Check if hint is currently active
    // @return true if hint is showing
    public boolean isHintActive() {
        return hintActive;
    }
    
    // Get current difficulty level
    // @return Current difficulty
    public GameDifficulty getDifficulty() {
        return difficulty;
    }
    
    // Get number of rows in the grid
    // @return Number of rows
    public int getGridRows() {
        return difficulty.getRows();
    }
    
    // Get number of columns in the grid
    // @return Number of columns
    public int getGridCols() {
        return difficulty.getCols();
    }
    
    // Get current number of matched pairs
    // @return Number of matched pairs
    public int getMatchedPairs() {
        return matchedPairs;
    }
    
    // Get total pairs needed to win
    // @return Total number of pairs
    public int getTotalPairs() {
        return difficulty.getTotalPairs();
    }
    
    // Get the score manager
    // @return ScoreManager instance
    public ScoreManager getScoreManager() {
        return scoreManager;
    }
    
    // Get current game progress as percentage
    // @return Progress percentage (0-100)
    public int getProgressPercentage() {
        if (difficulty.getTotalPairs() == 0) return 0;
        return (matchedPairs * 100) / difficulty.getTotalPairs();
    }
    
    // Get game status message
    // @return Current game status
    public String getGameStatus() {
        if (gameOver) {
            return "Congratulations! Game completed!";
        } else if (waitingToFlipBack) {
            return "No match! Cards flipping back...";
        } else if (hintActive) {
            return "Hint active - all cards revealed!";
        } else if (hintAvailable) {
            return "Hint available! Get 2 more matches to earn another.";
        } else {
            return String.format("Find the matching pairs! (%d/%d)", 
                               matchedPairs, difficulty.getTotalPairs());
        }
    }
} 