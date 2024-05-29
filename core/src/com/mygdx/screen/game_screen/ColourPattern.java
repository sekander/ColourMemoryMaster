package com.mygdx.screen.game_screen;

import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.util.common.EntityFactory;
import com.mygdx.util.common.GameManager;
import com.mygdx.util.game.GameBase;
import com.mygdx.util.screen.ScreenBaseAdapter;
import com.mygdx.util.system.HudRenderSystem;
import com.mygdx.util.system.RenderSystem;

import java.util.Random;

import sun.swing.StringUIClientPropertyKey;


public class ColourPattern extends ScreenBaseAdapter {

    private final GameBase game;
    private final AssetManager assetManager;
    private OrthographicCamera camera;
    private Viewport viewport;
    private PooledEngine engine;
    private EntityFactory factory;
    private ShapeRenderer debugRender;

    boolean bottomLeft, cBottomLeft;
    boolean bottomRight, cBottomRight;
    boolean TopLeft, cTopLeft;
    boolean TopRight, cTopRight;
    boolean workerThread;
    boolean match, newPattern;
    boolean drawLevel;

    public String pattern;
    public String cpuPattern;
    private String loseText;
    private String levelText;

    private int level;

    private final int CPUDRAW = 1000;
    private double CPUCYLE = 0.1;

    //Need to draw start lose draw
    boolean loseGame;
    //Need to draw levels


    int random;
    int tap;
    int score;

    float r, g, b;
    float selection_R, selection_G, selection_B;

    Random rand;
    BitmapFont font, levelFont;

    double updatedTime;
    double capturedTime;
    double startTime;

    private GlyphLayout layout;

    Sound noteA, noteB, noteC, noteD;

    public ColourPattern(GameBase game){
        this.game = game;
        this.assetManager = game.getAssetManager();
        startTime = System.currentTimeMillis();
    }

    //TO-DO LIST
    //Create reset button to reset the game back to level 1
    //Create mute button
    //Build Main menu
    //Move project to clean version remove anything unnecessary
    //Remove all print statements

    @Override
    public void show() {

        //Setup viewport
        camera = new OrthographicCamera();
        viewport = new FitViewport(Gdx.graphics.getWidth()/10, Gdx.graphics.getHeight()/10, camera);
        debugRender = new ShapeRenderer();

        //Load in fonts
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("ui/fonts/Minecraft.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 20;

        font = generator.generateFont(parameter);
        levelFont = generator.generateFont(parameter);

        font.getData().setScale(0.25f, 0.25f);
        levelFont.getData().setScale(0.5f, 0.5f);

        //Sounds
        noteA = Gdx.audio.newSound(Gdx.files.internal("sounds/noteA.wav"));
        noteB = Gdx.audio.newSound(Gdx.files.internal("sounds/noteB.wav"));
        noteC = Gdx.audio.newSound(Gdx.files.internal("sounds/noteC.wav"));
        noteD = Gdx.audio.newSound(Gdx.files.internal("sounds/noteD.wav"));


        //Entity System
        engine = new PooledEngine();
        factory = new EntityFactory(engine, assetManager, null);

        engine.addSystem(new RenderSystem(viewport, game.getBatch()));
        engine.addSystem(new HudRenderSystem(viewport, game.getBatch(), font));

        //Initialize required attributes
        bottomLeft = false;
        bottomRight= false;
        TopLeft = false;
        TopRight = false;

        cBottomLeft = false;
        cBottomRight = false;
        cTopRight = false;
        cTopLeft = false;
        workerThread = true;
        //factory.addBackground("background/Clouds_1/1.png", 50, 129, 128, 192);

        loseGame = false;
        drawLevel = true;


        rand = new Random();
        layout = new GlyphLayout();


        match = true;
        newPattern = false;
        loseText = "YOU LOSE!!!";
        levelText = "LEVEL ";
        level = 1;

        pattern = "";
        cpuPattern = "";
        tap = 0;
        r = 1.0f;
        g = 0.0f;
        b = 0.0f;
        //score = 0;
        score = GameManager.INSTANCE.getScore();

        //Cpu update thread
        new Thread(new Runnable() {
            @Override
            public void run() {
                    cpuUpdate();
            }
        }).start();
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.BLACK);
        //Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        //Update time
        updatedTime = (System.currentTimeMillis() - startTime) / 1000;
        GameManager.INSTANCE.setGameTime(updatedTime);

        game.getBatch().enableBlending();

        double swave = 1 * Math.cos(2 * 3.14 * 0.01 * (System.currentTimeMillis() - startTime)/1000  * 100);
        //double swave = 1 * Math.sin(2 * 3.14 * 0.0001 * (System.currentTimeMillis() - startTime)/1000  * 100);
        //double swave = 1 * Math.sin(2 * 3.14 * 0.001 * (System.currentTimeMillis() - startTime)/1000  * 100);
        //double cwave = 1 * Math.cos(2 * 3.14 * 0.001 * (System.currentTimeMillis() - startTime)/1000  * 100);

        matchCondition();
        playerUpdate();
        comparePattern();

        //Draw lose condition
        if(loseGame)
            //Change this
            drawDebug(swave);

        //Draw current level
        if(drawLevel) {
            game.getBatch().begin();
            layout.setText(levelFont,levelText + level);
            levelFont.setColor(1, 1, 1, (float) Math.abs(swave));

            //levelFont.draw(game.getBatch(), layout, 36, 130);
            levelFont.draw(game.getBatch(), layout, 36, 160);
            game.getBatch().end();
        }

        game.getBatch().begin();
        font.draw(game.getBatch(), "Score : " + GameManager.INSTANCE.getScore(), 5, 30 );
        font.draw(game.getBatch(), "High Score : " + GameManager.INSTANCE.getHighScoreString(), 5, 20 );
        game.getBatch().end();

        engine.update(delta);
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }
    @Override
    public void pause() {
        super.pause();
    }
    @Override
    public void resume() {
        super.resume();
    }

    @Override
    public void hide() {
        super.hide();
    }

    @Override
    public void dispose() {
        super.dispose();
        //renderer.dispose();
        noteA.dispose();
        debugRender.dispose();

    }

    public void DrawDebugLine(Vector2 start, Vector2 end, int lineWidth, Color color, Matrix4 projectionMatrix)
    {
        Gdx.gl.glLineWidth(lineWidth);
        debugRender.setProjectionMatrix(projectionMatrix);
        debugRender.begin(ShapeRenderer.ShapeType.Line);
        debugRender.setColor(color);
        debugRender.line(start, end);
        debugRender.end();
        Gdx.gl.glLineWidth(1);
    }
    public void DrawDebugSquare(Vector2 start, Vector2 end, Color color, Matrix4 projectionMatrix)
    {
        //Gdx.gl.glLineWidth(lineWidth);
        debugRender.setProjectionMatrix(projectionMatrix);
        debugRender.begin(ShapeRenderer.ShapeType.Filled);
        debugRender.setColor(color);
        debugRender.rect(start.x, start.y, end.x, end.y);
        debugRender.end();
        //Gdx.gl.glLineWidth(1);
    }

    public void gameInput(){
        //Enable user input after cpu is done updating
        if(!workerThread) {
            selection_R = 0.0f;
            selection_G = 1.0f;
            selection_B = 1.0f;
            if (Gdx.input.justTouched()) {
            //if (Gdx.input.isTouched() ) {
                float screenX = Gdx.input.getX(); // in pixels
                float screenY = Gdx.input.getY(); // in pixels



                System.out.println("Touch Input X: " + screenX);
                System.out.println("Touch Input Y: " + screenY);

                //BLUE
                if (screenY < Gdx.graphics.getHeight() - (Gdx.graphics.getHeight() * 0.50)
                        && screenX > Gdx.graphics.getWidth() - (Gdx.graphics.getWidth() * 0.50)
                        && screenY > Gdx.graphics.getHeight() * 0.05) {
                    TopRight = true;
                    String topRightString = "2";
                    pattern = pattern + topRightString;
                    tap += 1;
                    noteA.play();
                }

                //RED
                if (screenY < Gdx.graphics.getHeight() - (Gdx.graphics.getHeight() * 0.50)
                        && screenX < Gdx.graphics.getWidth() - (Gdx.graphics.getWidth() * 0.50)
                        && screenY > Gdx.graphics.getHeight() * 0.05) {
                    TopLeft = true;
                    String topLeftString = "1";
                    pattern = pattern + topLeftString;
                    tap += 1;
                    noteB.play();
                }

                //GREEN
                if (screenY > Gdx.graphics.getHeight() - (Gdx.graphics.getHeight() * 0.50)
                        && screenX > Gdx.graphics.getWidth() - (Gdx.graphics.getWidth() * 0.50)
                        && screenY < Gdx.graphics.getHeight() - (Gdx.graphics.getHeight() * 0.15)) {
                    bottomRight = true;
                    String bottomRightString = "4";
                    pattern = pattern + bottomRightString;
                    tap += 1;
                    noteC.play();
                }

                //YELLOW
                if (screenY > Gdx.graphics.getHeight() - (Gdx.graphics.getHeight() * 0.50)
                        && screenX < Gdx.graphics.getWidth() - (Gdx.graphics.getWidth() * 0.50)
                        && screenY < Gdx.graphics.getHeight() - (Gdx.graphics.getHeight() * 0.15)) {
                    bottomLeft = true;
                    String bottomLeftString = "3";
                    pattern = pattern + bottomLeftString;
                    tap += 1;
                    noteD.play();
                }

            } else {
                bottomLeft = false;
                bottomRight = false;
                TopLeft = false;
                TopRight = false;
            }
        }
    }

    //Simulate cpu pattern
    public void simulatePattern(){
        random = 1 + rand.nextInt((4 -1) + 1);
        cpuPattern += Integer.toString(random);
        capturedTime = (System.currentTimeMillis() - startTime)/1000;
    }

    public void drawDebug(double wave){

        //DrawDebugSquare(new Vector2(5, 38), new Vector2(44, 86), new Color(0.5f, 1, 0, 1), camera.combined);
        DrawDebugLine(new Vector2(0, 225), new Vector2(108, 225), 2, Color.RED, camera.combined);
        DrawDebugLine(new Vector2(0, 129), new Vector2(108, 129), 2, Color.BLUE, camera.combined);
        DrawDebugLine(new Vector2(54, 225), new Vector2(54, 33), 2, Color.GREEN, camera.combined);
        DrawDebugLine(new Vector2(0, 33), new Vector2(108, 33), 2, Color.RED, camera.combined);

        DrawDebugSquare(new Vector2(27, 3), new Vector2(20,20), new Color(r, g, b, 1.0f), camera.combined);


        DrawDebugSquare(new Vector2(34, 118), new Vector2(40, 20), new Color(0, 1, 1, 0.2f), camera.combined);
        DrawDebugSquare(new Vector2(36, 120), new Vector2(36, 16), new Color(1, 0, 1, 0.5f), camera.combined);
        //DrawDebugSquare(new Vector2(0, 300), new Vector2(108, 225),Color.BLACK, camera.combined );
        game.getBatch().begin();

            if(loseGame)
                layout.setText(font, loseText);

            font.draw(game.getBatch(), layout, 37 , 125);

        game.getBatch().end();
    }
    //Draw the gameboard
    public void drawGrid(){
        //YELLOW
        if(bottomLeft || cBottomLeft)
            DrawDebugSquare(new Vector2(0, 33), new Vector2(54, 96), new Color(selection_R, selection_G, selection_B, 1), camera.combined);
        DrawDebugSquare(new Vector2(2, 35), new Vector2(50, 92), new Color(1.0f , 1.0f , 0, 1), camera.combined);

        //green
        if(bottomRight || cBottomRight)
            DrawDebugSquare(new Vector2(54, 33), new Vector2(108, 96), new Color(selection_R, selection_G, selection_B, 1), camera.combined);
        DrawDebugSquare(new Vector2(56, 35), new Vector2(50, 92), new Color(0.0f , 1.0f , 0, 1), camera.combined);

        //red
        if(TopLeft || cTopLeft)
            DrawDebugSquare(new Vector2(0, 129), new Vector2(54, 96), new Color(selection_R, selection_G, selection_B, 1), camera.combined);
        DrawDebugSquare(new Vector2(2, 131), new Vector2(50, 92), new Color(1.0f , 0.0f , 0, 1), camera.combined);

        //blue
        if(TopRight || cTopRight)
            DrawDebugSquare(new Vector2(54, 129), new Vector2(108, 96), new Color(selection_R, selection_G, selection_B, 1), camera.combined);
        DrawDebugSquare(new Vector2(56, 131), new Vector2(50, 92), new Color(0f , 0.0f , 1.0f, 1), camera.combined);
    }

    //Player update input
    public void playerUpdate(){
        drawGrid();
        gameInput();
    }

    //Cpu update
    public void cpuUpdate() {
        while(true)
        {
            if(match) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {//do stuff
                }
            }

            if(TopLeft || TopRight || bottomLeft || bottomRight ||
            cTopLeft || cTopLeft || cBottomRight || cBottomLeft){
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {//do stuff
                }
                TopLeft = false;
                TopRight = false;
                bottomRight = false;
                bottomLeft = false;
                cTopLeft = false;
                cTopRight = false;
                cBottomLeft = false;
                cBottomRight = false;
            }


            if (workerThread) {
                selection_R = 1.0f;
                selection_G = 1.0f;
                selection_B = 1.0f;

                try{
                    Thread.sleep(500);
                }catch(InterruptedException ex) {//do stuff
                }

                //Might not need this
                TopRight = false;
                TopLeft = false;
                bottomRight = false;
                bottomLeft = false;


                if(loseGame) {
                    loseGame = false;
                    drawLevel = true;
                }
                try{
                    Thread.sleep(500);
                }catch(InterruptedException ex) {//do stuff
                }


                //Start cpu draw pattern
                for (int i = 0; i < cpuPattern.length(); i++) {

                    drawLevel = false;

                    try{
                        Thread.sleep(500);
                    }catch(InterruptedException ex) {//do stuff
                    }
                    char c = cpuPattern.charAt(i);
                    cTopLeft = false;
                    cTopRight = false;
                    cBottomRight = false;
                    cBottomLeft = false;
                    System.out.println("DRawing cpu pattern");


                    if (c == '1') {
                        noteB.play();
                        cTopLeft = true;
                        System.out.println("Top Left ON");
                        try{
                            Thread.sleep(CPUDRAW - (int)(CPUDRAW * CPUCYLE));
                        }catch(InterruptedException ex){//do stuff
                        }
                        cTopLeft = false;
                        System.out.println("Top Left OFF");
                    }
                    else if (c == '2') {
                        noteA.play();
                        cTopRight = true;
                        System.out.println("Top Right ON");
                        try{
                            Thread.sleep(CPUDRAW - (int)(CPUDRAW * CPUCYLE));
                        }catch(InterruptedException ex){//do stuff
                        }
                        cTopRight = false;
                        System.out.println("Top Right OFF");
                    }
                    else if (c == '3') {
                        noteD.play();
                        cBottomLeft = true;
                        System.out.println("Bottom Left ON");
                        try{
                            Thread.sleep(CPUDRAW - (int)(CPUDRAW * CPUCYLE));
                        }catch(InterruptedException ex){//do stuff
                        }
                        cBottomLeft = false;
                        System.out.println("Bottom Left OFF");
                    }
                    else if (c == '4') {
                        noteC.play();
                        cBottomRight = true;
                        System.out.println("Bottom Right ON");
                        try{
                            Thread.sleep(CPUDRAW - (int)(CPUDRAW * CPUCYLE));
                        }catch(InterruptedException ex){//do stuff
                        }
                        cBottomRight = false;
                        System.out.println("Bottom Right OFF");
                    }
                }
                workerThread = false;
            }
        }
    }

    public void comparePattern(){
        boolean matched = false;
        for(int i = 0; i < cpuPattern.length(); i++)
        {
            for(int j = 0; j < pattern.length(); j++){
                //Might not need this
                if(!pattern.equals("")) {
                    //if(tap == )
                    if (i == j && pattern.charAt(j) == cpuPattern.charAt(i)) {
                        System.out.println("Player pattern index " + i + " matches cpu pattern index");
                        r = 0.0f;
                        g = 1.0f;
                        matched = true;
                    }else if(i == j && pattern.charAt(j) != cpuPattern.charAt(i))
                    {
                        System.out.println("WRONG  !!!!\n WRONG!!!!! Player patetern does not match with CPU pattern");
                        pattern = "";
                        cpuPattern = "";
                        r = 1.0f;
                        g = 0.0f;
                        matched = false;
                        newPattern = true;
                        GameManager.INSTANCE.updateHighScore();
                        GameManager.INSTANCE.reset();
                        level = 1;
                        CPUCYLE = 0.1;
                        loseGame = true;
                    }
                }
            }
        }

        if(matched) {
            if (pattern.equals(cpuPattern)) {
                System.out.println("Winner Winner!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                level++;
                if(CPUCYLE < 0.5)
                    CPUCYLE += 0.05;
                else if(CPUCYLE > 0.8)
                    CPUCYLE += 0.001;
                else
                    CPUCYLE += 0.01;
                match = true;
                GameManager.INSTANCE.updateScore(100);
                //score += 100;
            }
        }
    }

    public void matchCondition(){
        if(match || newPattern) {
            if(!loseGame)
                drawLevel = true;

            simulatePattern();
            //Clear player patter when match
            pattern = "";
            tap = 0;
            workerThread = true;
            match = false;
            newPattern = false;
        }
    }


}