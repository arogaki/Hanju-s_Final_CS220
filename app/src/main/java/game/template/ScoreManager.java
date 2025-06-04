package game.template;

import java.util.HashMap;
import java.util.Map;

// ScoreManager handles scoring logic and high score tracking
// Scores are calculated based on time, matches, and difficulty level
public class ScoreManager {
    private Map<GameDifficulty, Integer> highScores;  // Best scores for each difficulty
    private int currentScore;                         // Current game score
    private int matchesFound;                         // Number of matches in current game
    private long gameStartTime;                       // When current game started
    private int moveCount;                            // Number of moves (card flips) made
    private GameDifficulty currentDifficulty;        // Current game difficulty
    
    // Scoring constants for calculation
    private static final int BASE_SCORE_PER_MATCH = 100;
    private static final int TIME_BONUS_MULTIPLIER = 10;
    private static final int MOVE_PENALTY = 5;
    private static final int DIFFICULTY_MULTIPLIER_EASY = 1;
    private static final int DIFFICULTY_MULTIPLIER_NORMAL = 2;
    private static final int DIFFICULTY_MULTIPLIER_HARD = 3;
    
    // Constructor
    public ScoreManager() {
        highScores = new HashMap<>();
        // Initialize high scores to 0 for all difficulties
        for (GameDifficulty difficulty : GameDifficulty.values()) {
            highScores.put(difficulty, 0);
        }
        resetCurrentGame();
    }
    
    // Start a new game with specified difficulty
    public void startNewGame(GameDifficulty difficulty) {
        currentDifficulty = difficulty;
        currentScore = 0;
        matchesFound = 0;
        moveCount = 0;
        gameStartTime = System.currentTimeMillis();
    }
    
    // Reset current game stats
    public void resetCurrentGame() {
        currentScore = 0;
        matchesFound = 0;
        moveCount = 0;
        gameStartTime = 0;
        currentDifficulty = GameDifficulty.EASY;
    }
    
    // Record a successful match
    public void recordMatch() {
        matchesFound++;
        updateScore();
    }
    
    // Record a move (card flip)
    public void recordMove() {
        moveCount++;
    }
    
    // Calculate and update current score based on performance
    private void updateScore() {
        if (gameStartTime == 0) return;
        
        // Base score from matches
        int baseScore = matchesFound * BASE_SCORE_PER_MATCH;
        
        // Time bonus (faster completion = higher bonus)
        long gameTimeSeconds = (System.currentTimeMillis() - gameStartTime) / 1000;
        int timeBonus = Math.max(0, (int)(1000 - gameTimeSeconds) * TIME_BONUS_MULTIPLIER);
        
        // Move penalty (fewer moves = higher score)
        int movePenalty = moveCount * MOVE_PENALTY;
        
        // Difficulty multiplier
        int difficultyMultiplier = getDifficultyMultiplier(currentDifficulty);
        
        // Calculate final score
        currentScore = Math.max(0, (baseScore + timeBonus - movePenalty) * difficultyMultiplier);
    }
    
    // Get difficulty multiplier for scoring
    private int getDifficultyMultiplier(GameDifficulty difficulty) {
        switch (difficulty) {
            case EASY: return DIFFICULTY_MULTIPLIER_EASY;
            case NORMAL: return DIFFICULTY_MULTIPLIER_NORMAL;
            case HARD: return DIFFICULTY_MULTIPLIER_HARD;
            default: return 1;
        }
    }
    
    // Finish current game and check for high score
    public boolean finishGame() {
        updateScore();
        
        if (currentDifficulty != null) {
            int previousHigh = highScores.get(currentDifficulty);
            if (currentScore > previousHigh) {
                highScores.put(currentDifficulty, currentScore);
                return true;  // New high score!
            }
        }
        return false;  // No new high score
    }
    
    // Get current score
    public int getCurrentScore() {
        updateScore();  // Ensure score is up to date
        return currentScore;
    }
    
    // Get high score for specific difficulty
    public int getHighScore(GameDifficulty difficulty) {
        return highScores.getOrDefault(difficulty, 0);
    }
    
    // Get current number of matches found
    public int getMatchesFound() {
        return matchesFound;
    }
    
    // Get current move count
    public int getMoveCount() {
        return moveCount;
    }
    
    // Get elapsed game time in seconds
    public long getElapsedTime() {
        if (gameStartTime == 0) return 0;
        return (System.currentTimeMillis() - gameStartTime) / 1000;
    }
    
    // Get current difficulty
    public GameDifficulty getCurrentDifficulty() {
        return currentDifficulty;
    }
    
    // Get formatted score display
    public String getScoreDisplay() {
        return String.format("Score: %d", getCurrentScore());
    }
    
    // Get formatted high score display for current difficulty
    public String getHighScoreDisplay() {
        if (currentDifficulty == null) return "High Score: 0";
        return String.format("High Score: %d", getHighScore(currentDifficulty));
    }
    
    // Get formatted stats display
    public String getStatsDisplay() {
        return String.format("Matches: %d | Moves: %d | Time: %d:%02d", 
                           matchesFound, moveCount, 
                           getElapsedTime() / 60, getElapsedTime() % 60);
    }
    
    // Get all high scores as formatted string
    public String getAllHighScores() {
        StringBuilder sb = new StringBuilder();
        sb.append("High Scores:\n");
        for (GameDifficulty difficulty : GameDifficulty.values()) {
            sb.append(String.format("%s: %d\n", 
                     difficulty.getDisplayName(), 
                     getHighScore(difficulty)));
        }
        return sb.toString();
    }
} 