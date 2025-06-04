package game.template;

import javafx.scene.control.Button;

// Card class represents a single card in the memory game
// Each card has a value (symbol) and can be face up or face down
public class Card extends Button {
    private final String value;      // The symbol/value of the card
    private boolean isFaceUp;        // Whether the card is currently face up
    private boolean isMatched;       // Whether this card has been matched
    private boolean isHintVisible;   // Whether card is showing due to hint
    
    // Expanded card symbols for larger grids (supports up to 24 pairs = 48 cards)
    private static final String[] CARD_SYMBOLS = {
        // Basic symbols (8 pairs)
        "♠", "♣", "♥", "♦", "★", "♪", "♫", "☀",
        // Weather and nature (8 pairs) 
        "☁", "☂", "❄", "⚡", "🌟", "💫", "🌙", "🌈",
        // Music and entertainment (8 pairs)
        "🎵", "🎶", "🎭", "🎨", "🎯", "🎲", "🎪", "🎸"
    };
    
    // Constructor for Card
    public Card(String value) {
        this.value = value;
        this.isFaceUp = false;
        this.isMatched = false;
        this.isHintVisible = false;
        
        // Set initial appearance (face down)
        setText("?");
        setMinSize(60, 60);  // Smaller size for larger grids
        setMaxSize(60, 60);
        getStyleClass().add("memory-card");
    }
    
    // Flip the card face up, showing its value
    public void flipUp() {
        if (!isMatched) {
            isFaceUp = true;
            setText(value);
            getStyleClass().remove("card-face-down");
            getStyleClass().add("card-face-up");
        }
    }
    
    // Flip the card face down, hiding its value
    public void flipDown() {
        if (!isMatched && !isHintVisible) {
            isFaceUp = false;
            setText("?");
            getStyleClass().remove("card-face-up");
            getStyleClass().add("card-face-down");
        }
    }
    
    // Mark this card as matched (permanently face up)
    public void setMatched() {
        isMatched = true;
        isFaceUp = true;
        isHintVisible = false;
        setText(value);
        getStyleClass().remove("card-face-up");
        getStyleClass().remove("card-face-down");
        getStyleClass().remove("card-hint");
        getStyleClass().add("card-matched");
        setDisable(true);
    }
    
    // Show card temporarily for hint (different styling)
    public void showHint() {
        if (!isMatched && !isFaceUp) {
            isHintVisible = true;
            setText(value);
            getStyleClass().remove("card-face-down");
            getStyleClass().add("card-hint");
        }
    }
    
    // Hide hint and return to face down
    public void hideHint() {
        if (isHintVisible && !isMatched && !isFaceUp) {
            isHintVisible = false;
            setText("?");
            getStyleClass().remove("card-hint");
            getStyleClass().add("card-face-down");
        }
    }
    
    // Get the value/symbol of this card
    // @return The card's symbol
    public String getValue() {
        return value;
    }
    
    // Check if card is currently face up
    // @return true if face up, false if face down
    public boolean isFaceUp() {
        return isFaceUp;
    }
    
    // Check if card has been matched
    // @return true if matched, false otherwise
    public boolean isMatched() {
        return isMatched;
    }
    
    // Check if card is showing hint
    // @return true if hint is visible
    public boolean isHintVisible() {
        return isHintVisible;
    }
    
    // Get array of available card symbols
    // @return Array of card symbols
    public static String[] getCardSymbols() {
        return CARD_SYMBOLS.clone();
    }
} 