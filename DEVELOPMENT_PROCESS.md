# Memory Card Game Development Process

**CS220 Spring 2025 Final Project**  
**Student**: Computer Science Undergraduate (2nd Year)

## Introduction

This document explains how I developed my memory card game from a simple two-player version into a complete single-player game with advanced features. The project was quite challenging but I learned a lot about JavaFX programming and software design.

## What I Started With

The original game was pretty basic - just two players clicking cards to find matches. It worked fine but didn't have many features. For my CS220 project, I wanted to make something more impressive that would show what I learned this semester.

## Planning Phase

I spent time thinking about what features would make the game better:

- **Single player mode** - Most people play these games alone anyway
- **Different difficulty levels** - Easy, Normal, Hard with different grid sizes
- **Scoring system** - To make it competitive and track progress
- **Timer functionality** - For time pressure and scoring bonuses
- **Hint system** - To help when players get stuck
- **Better UI design** - Make it look professional

I also needed to keep the code clean and well-commented since that's important for the grade.

## Development Steps

### Step 1: Creating the Difficulty System

This was my first major addition. I created a `GameDifficulty` enum to handle different difficulty levels:

```java
public enum GameDifficulty {
    EASY(4, 4),      // 4x4 grid = 16 cards
    NORMAL(5, 6),    // 5x6 grid = 30 cards  
    HARD(6, 8);      // 6x8 grid = 48 cards
}
```

The enum approach was good because:
- Type-safe (can't accidentally use wrong values)
- Easy to add new difficulties later
- Contains all the grid size calculations in one place

**Challenge I faced**: Making sure the card grid would resize properly when switching difficulties. I had to rebuild the entire grid each time, which took some debugging.

### Step 2: Building the Scoring System

I wanted a scoring system that rewards both speed and accuracy. After some experimentation, I settled on this formula:

```
Final Score = (Base Score + Time Bonus - Move Penalty) × Difficulty Multiplier
```

Where:
- Base Score = 100 points per match
- Time Bonus = depends on how fast you finish
- Move Penalty = punishment for extra moves
- Difficulty Multiplier = 1x for Easy, 2x for Normal, 3x for Hard

I created a separate `ScoreManager` class to handle all the scoring logic. This keeps the game logic clean and makes it easier to modify scoring later.

**What I learned**: Breaking complex calculations into separate classes makes debugging much easier. When my scoring wasn't working right, I could test just the ScoreManager without worrying about the rest of the game.

### Step 3: Adding the Timer

The timer was trickier than expected. I needed it to:
- Update every second without freezing the UI
- Integrate with the scoring system
- Stop/start/reset properly

I used JavaFX's `Timeline` class:

```java
gameTimer = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
    elapsedSeconds++;
    updateTimerDisplay();
    updateGameDisplay(); // This updates the score too
}));
```

**Problem I ran into**: The timer kept running even after the game ended, which messed up the final score calculation. I had to make sure to stop it properly in the game completion logic.

### Step 4: Implementing the Hint System

I wanted hints to be earned, not just freely available. My solution:
- Player earns a hint after 2 consecutive matches
- Hint shows all unmatched cards for 2 seconds
- Cards get special orange styling during hint time

```java
if (consecutiveMatches >= 2) {
    hintAvailable = true;
    consecutiveMatches = 0; // Reset counter
}
```

This balance feels right - hints help when you're doing well, but you can't spam them.

### Step 5: UI Redesign

The original UI was designed for two players, so I had to redesign everything:

**Before**: Player turn indicators, simple layout
**After**: Control panel with difficulty selector, score display, timer, progress bar

I replaced the dropdown difficulty selector with horizontal buttons because:
- More visual and obvious
- Shows current selection clearly  
- Looks more professional

Layout structure:
```
MenuBar
Control Panel: GAME: [EASY] [NORMAL] [HARD] [New Game] [Use Hint]
Score Panel: [Score] [High Score] [Timer] [Difficulty] [Progress]
Card Grid (changes size based on difficulty)
Status Message
```

### Step 6: CSS Styling and Visual Polish

I spent a lot of time making the game look professional. Key improvements:

- **Color scheme**: Dark blue gradient background with good contrast
- **Card states**: Different colors for face-down, face-up, matched, and hint cards
- **Hover effects**: Cards slightly grow when you hover over them
- **Responsive sizing**: Cards are bigger for Easy mode, smaller for Hard mode

The CSS was challenging because I needed:
- Different card sizes for each difficulty
- Clear visual feedback for all interactive elements
- Good contrast for accessibility
- Professional appearance

### Step 7: Bug Fixing and Testing

Several major bugs I had to fix:

**Bug 1: Grid Layout Issue**
- Problem: Cards appeared in a single column instead of proper grid
- Cause: Difficulty changes didn't trigger complete game restart
- Fix: Added automatic game recreation when difficulty buttons are clicked

**Bug 2: CSS Class Conflicts**
- Problem: Card size classes would conflict when switching difficulties
- Fix: Clear existing size classes before applying new ones

**Bug 3: Timer Integration**
- Problem: Timer kept running after game completion
- Fix: Proper timer lifecycle management in game state changes

## Technical Challenges and Solutions

### Challenge 1: State Management
Managing timer, scoring, hints, and game state simultaneously was complex. I solved this by creating a central `updateGameDisplay()` method that updates everything consistently.

### Challenge 2: Real-time Updates
Getting smooth timer updates without blocking the UI required understanding JavaFX's animation framework. The Timeline approach worked well once I figured it out.

### Challenge 3: Responsive Design
Making the card grid work for different difficulty levels needed careful CSS planning and dynamic class assignment.

## Code Organization

I tried to follow good software engineering practices:

**Model Classes** (No UI dependencies):
- `MemoryGame` - Core game logic
- `Card` - Individual card behavior  
- `ScoreManager` - Scoring calculations
- `GameDifficulty` - Configuration management

**View Classes** (UI only):
- `App` - JavaFX application and event handling
- `style.css` - All visual styling

This separation made development easier because I could test the game logic without worrying about the UI.

## What I Learned

### Technical Skills
- **JavaFX**: Advanced UI programming with timers and animations
- **CSS**: Responsive design and professional styling
- **Object-oriented design**: Clean class structure and separation of concerns
- **Enum usage**: Type-safe configuration management
- **Event handling**: Complex user interaction workflows

### Software Engineering
- **Planning**: Thinking through features before coding saves time
- **Testing**: Small, frequent tests catch bugs early
- **Documentation**: Good comments make debugging much easier
- **Architecture**: Clean separation makes code easier to modify

### Problem Solving
- **Breaking down complex problems** into smaller pieces
- **Debugging systematically** rather than randomly changing things
- **User experience thinking** - what makes the game fun to play?

## Reflection

This project was much more challenging than I expected, but also more rewarding. The jump from a simple two-player game to a feature-rich single-player experience required learning several new concepts.

The most difficult part was coordinating all the different systems (timer, scoring, hints, UI updates) to work together smoothly. I had to refactor my code several times when I realized my initial approach wasn't working.

The most satisfying part was seeing the final product come together - the responsive design, smooth animations, and professional appearance make it feel like a real application rather than just a school project.

## Files Created/Modified

**New Files:**
- `GameDifficulty.java` - Difficulty level configuration
- `ScoreManager.java` - Scoring system and high score tracking
- `DEVELOPMENT_PROCESS.md` - This document

**Modified Files:**
- `MemoryGame.java` - Converted to single-player with hint system
- `Card.java` - Added hint functionality and more symbols
- `App.java` - Complete UI redesign for single-player
- `style.css` - Professional styling with responsive design
- `README.md` - Updated instructions for single-player mode

**Removed Files:**
- `GameController.java` - Simplified architecture didn't need it
- `GameStats.java` - Functionality moved to ScoreManager

## Running the Game

```bash
# Build and run
./gradlew build
./gradlew run

# Or from app directory
cd app
./gradlew run
```

The game should work on Windows, Mac, and Linux with Java 21+ installed.

## Future Improvements

If I had more time, I would add:
- Sound effects for card flips and matches
- Animation effects for card flipping
- More difficulty levels or custom grid sizes
- Online high score leaderboard
- Different card symbol themes

## Conclusion

This project taught me a lot about building complete applications rather than just individual features. The integration challenges, user experience considerations, and code organization skills will definitely help in future projects.

The final result is something I'm proud to show as an example of my programming abilities - it demonstrates both technical skills and attention to user experience. 