# ColourMemoryMaster
Android Game built using libGDX


MenuScreen : 
This MenuScreen class effectively manages the UI and user interactions for navigating through menu options in your game. It leverages LibGDX's Stage and Table for UI layout, manages input with InputProcessor, and handles screen transitions seamlessly. The use of AssetManager ensures efficient resource loading, enhancing performance and maintainability.

Main Game Loop:
    Game Initialization (show method):
        Sets up the game environment including camera, viewport, fonts, and asset loading.
        Initializes game state variables (pattern, cpuPattern, tap, level, etc.).
        Starts the CPU update thread (cpuUpdate) for simulating patterns asynchronously.

    Rendering (render method):
        Clears the screen and enables blending for smooth rendering.
        Updates game time and processes player input (gameInput) to interact with game elements based on screen touch.
        Animates visual effects (swave) based on elapsed time.
        Draws game elements including grids, current score, high score, and debug information (drawGrid, drawDebug).

    Game Loop Management:
        Controls the game flow by managing states (match, newPattern, loseGame) and progression (level).
        Compares player input (pattern) with CPU-generated pattern (cpuPattern) to determine matches (comparePattern).
        Updates game logic (matchCondition) to handle transitions between levels and reset conditions.

    Input Handling (gameInput method):
        Processes touch input to detect user interaction with specific game areas (color squares).
        Plays corresponding sound effects (noteA, noteB, noteC, noteD) based on the selected color.

    Debug Drawing (DrawDebugLine, DrawDebugSquare methods):
        Utilizes ShapeRenderer to draw lines and filled squares for debugging purposes.
        Renders visual cues such as lines and squares in different colors (RED, BLUE, GREEN) to highlight game elements.

    Resource Management and Disposal (dispose method):
        Properly disposes of resources like sounds (noteA, noteB, noteC, noteD) and debug renderers (debugRender) to free up memory.
        Ensures clean shutdown and prevents resource leaks.

    Thread Management (cpuUpdate method):
        Controls the CPU pattern generation and simulation in a separate thread (workerThread).
        Updates the game state asynchronously to maintain responsiveness while simulating CPU actions (simulatePattern).

These key features collectively define the functionality of the ColourPattern class, encompassing game initialization, rendering, input handling, state management, and thread synchronization to deliver an interactive and responsive gameplay experience.
