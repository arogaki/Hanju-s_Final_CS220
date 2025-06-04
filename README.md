# Memory Card Game - Single Player Version

A JavaFX memory card game that I developed for my CS220 class. This started as a simple two-player game but I expanded it into a full single-player experience with multiple difficulty levels, scoring system, timer, and hint features.

## What This Game Is About

This is a concentration/memory game where you flip cards to find matching pairs. The goal is to match all pairs as quickly as possible with as few moves as you can. I designed it to be challenging but fair, with features that help you improve your memory skills.

## Main Features

### Three Difficulty Levels
- **Easy Mode**: 4×4 grid with 16 cards (8 pairs) - Good for beginners
- **Normal Mode**: 5×6 grid with 30 cards (15 pairs) - More challenging  
- **Hard Mode**: 6×8 grid with 48 cards (24 pairs) - For memory experts

The cards automatically resize based on difficulty so everything fits nicely on screen.

### Smart Scoring System
The scoring isn't just about finding matches - it rewards good memory and speed:

- **100 points** for each pair you match
- **Time bonus** - finish faster to get more points
- **Move penalty** - extra moves reduce your score
- **Difficulty multiplier** - Hard mode scores count for more

Your best score for each difficulty level gets saved automatically.

### Real-Time Timer
A timer runs while you play and shows in MM:SS format. The timer affects your final score, so there's good motivation to play efficiently. It starts when you begin a game and stops when you finish.

### Hint System That You Earn
Instead of giving unlimited hints, you have to earn them:
- Make 2 successful matches in a row to earn a hint
- Use the hint to see all unmatched cards for 2 seconds
- Cards glow orange during hint time so you know it's active

This system helps when you're stuck but doesn't make the game too easy.

### Professional Looking Interface
I spent time making the UI look polished:
- Clean layout that's easy to understand
- Horizontal difficulty buttons (EASY/NORMAL/HARD) next to "GAME:"
- Real-time display of score, high score, timer, and progress
- Smooth hover effects and visual feedback
- Good color contrast for accessibility

## How to Play

### Getting Started
1. When you open the game, you'll see the main interface
2. Click one of the difficulty buttons (EASY, NORMAL, HARD) to start
3. Or click "New Game" if you want to restart the current difficulty
4. Cards appear face-down with "?" symbols

### Playing the Game
1. Click any card to flip it and see the symbol
2. Click a second card to try to make a match
3. If they match: Cards stay flipped and you get points
4. If they don't match: Cards flip back after 1.5 seconds
5. Keep going until you've matched all pairs

### Earning and Using Hints
- Make 2 matches in a row without missing to earn a hint
- The "Use Hint" button becomes clickable when you have one
- Click it to see all unmatched cards for 2 seconds
- Use this time to memorize positions for your next moves

## Technical Details

### Project Structure
```
app/src/main/java/game/template/
├── App.java              # Main application and UI
├── MemoryGame.java       # Game logic and rules
├── Card.java             # Individual card behavior
├── ScoreManager.java     # Scoring and high score tracking
└── GameDifficulty.java   # Difficulty settings

app/src/main/resources/
└── style.css             # Visual styling
```

### How I Built This

**App.java** - This is the main class that handles all the JavaFX UI components. It creates the layout, handles button clicks, updates the display, and manages the timer using JavaFX Timeline.

**MemoryGame.java** - Contains all the game logic like checking for matches, managing game state, handling the hint system, and coordinating with the scoring system. This class doesn't know anything about the UI.

**Card.java** - Represents each individual card. It extends JavaFX Button and handles the different visual states (face down, face up, matched, hint). Each card knows its symbol and current state.

**ScoreManager.java** - Calculates scores using the multi-factor formula, tracks high scores for each difficulty, and saves them to files so they persist between game sessions.

**GameDifficulty.java** - An enum that defines the three difficulty levels with their grid sizes and other settings. Makes it easy to add new difficulties later.

### Programming Concepts I Used
- **Model-View separation**: Game logic is completely separate from UI code
- **Enum for configuration**: Type-safe way to handle difficulty settings
- **Event-driven programming**: Uses JavaFX event handling and timers
- **CSS styling**: Professional appearance with responsive design
- **File I/O**: Saving and loading high scores

## Installation and Running

### What You Need
- Java 21 or newer with JavaFX support
- Gradle 8.0+ (included with the project)
- Windows, Mac, or Linux

### How to Run
```bash
# Option 1: From project root
./gradlew build
./gradlew run

# Option 2: From app folder
cd app
./gradlew run

# Windows users can use
.\gradlew.bat run
```

If you get a "gradlew not recognized" error on Windows, use `.\gradlew` instead of just `gradlew`.

## Game Controls

### Main Interface Layout
```
GAME: [EASY] [NORMAL] [HARD] [New Game] [Use Hint]
[Score: 0] [High Score: 1250] [Time: 0:45] [Easy (4x4)] [Progress: 75%]
```

### Menu Options
- **New Game**: Restart with current difficulty
- **Reset Scores**: Clear all high scores (asks for confirmation)
- **Rules**: Shows detailed instructions
- **High Scores**: View best scores for each difficulty
- **Exit**: Close the game

## Scoring Strategy and Tips

### How to Get High Scores
1. **Work on memory**: Try to remember where cards are from previous flips
2. **Play systematically**: Don't just click randomly
3. **Use hints wisely**: Save them for when you're really stuck
4. **Speed matters**: Faster completion gives better time bonuses
5. **Minimize mistakes**: Each extra move hurts your score

### Score Calculation Details
```
Final Score = (Matches×100 + Time Bonus - Move Penalty) × Difficulty Multiplier

Time Bonus = max(0, (300 - seconds_taken) × 5)
Move Penalty = max(0, (total_moves - optimal_moves) × 10)
Difficulty Multiplier = Easy:1, Normal:2, Hard:3
```

So if you finish Easy mode in 60 seconds with 20 moves (optimal is 16):
- Base score: 8 matches × 100 = 800
- Time bonus: (300-60) × 5 = 1200  
- Move penalty: (20-16) × 10 = 40
- Final: (800 + 1200 - 40) × 1 = 1960 points

## Common Problems and Solutions

### Game Won't Start
- Make sure you have Java 21+ installed
- Try running `./gradlew clean build` first
- Check that you're in the right directory

### Cards Not Showing Up Right
- Try clicking a different difficulty level
- Click "New Game" to refresh the layout
- Make sure your window is big enough

### Performance Issues
- Close other applications to free up memory
- Try Easy mode if Hard mode is laggy
- Restart the application if it gets slow

### High Scores Not Saving
- Check that the application can write files in its folder
- Make sure you have enough disk space
- Try the "Reset Scores" option if scores seem corrupted

## Development Notes

### What I Learned Building This
- **JavaFX Programming**: How to build desktop applications with good UI
- **Software Architecture**: Separating game logic from display code
- **Real-time Programming**: Using timers without freezing the interface
- **User Experience**: Making software that's actually enjoyable to use
- **Problem Solving**: Debugging complex interactions between different systems

### Challenges I Faced
- Getting the timer to work smoothly with score updates
- Making the card grid resize properly for different difficulties
- Balancing the hint system so it's helpful but not overpowered
- CSS styling to make everything look professional
- Managing complex state when multiple systems interact

### Code Quality
- All comments use single-line format (//) as required
- Clean separation between game logic and UI code
- Error handling for edge cases and invalid states
- Performance optimized for smooth real-time updates
- Well-organized file structure with clear responsibilities

## Future Ideas

If I continue working on this project, I might add:
- Sound effects for flips and matches
- Card flip animations
- Custom themes with different symbols
- Statistics tracking over multiple games
- Multiplayer mode over network
- Mobile version using JavaFX Mobile

## Conclusion

This project taught me a lot about building complete applications rather than just solving individual programming problems. The integration challenges, user interface design, and software architecture considerations were all valuable learning experiences.

I'm proud of how it turned out - it feels like a real application that someone would actually want to play, not just a school assignment. The clean code structure and professional appearance demonstrate the programming skills I've developed this semester.

---

**Have fun playing!** Let me know if you find any bugs or have suggestions for improvements. 