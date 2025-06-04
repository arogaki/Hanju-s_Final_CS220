package game.template;

// Enum defining the three difficulty levels for the memory card game
// Each level has different grid dimensions for varying challenge
public enum GameDifficulty {
    EASY(4, 4),      // 4x4 grid = 16 cards (8 pairs)
    NORMAL(5, 6),    // 5x6 grid = 30 cards (15 pairs) 
    HARD(6, 8);      // 6x8 grid = 48 cards (24 pairs)
    
    private final int rows;
    private final int cols;
    
    // Constructor for difficulty enum
    GameDifficulty(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
    }
    
    // Get number of rows for this difficulty
    public int getRows() {
        return rows;
    }
    
    // Get number of columns for this difficulty
    public int getCols() {
        return cols;
    }
    
    // Get total number of cards for this difficulty
    public int getTotalCards() {
        return rows * cols;
    }
    
    // Get number of pairs for this difficulty
    public int getTotalPairs() {
        return getTotalCards() / 2;
    }
    
    // Get display name for this difficulty
    public String getDisplayName() {
        return this.name().charAt(0) + this.name().substring(1).toLowerCase();
    }
    
    // Get grid size description
    public String getGridDescription() {
        return String.format("%dx%d (%d cards)", rows, cols, getTotalCards());
    }
} 