package com.mygdx.screen.game_screen;

import com.mygdx.assets.AssetDescriptors;
import com.mygdx.util.common.EntityFactory;
import com.mygdx.util.components.PlayerComponent;
import com.mygdx.util.controller.GamePAD;
import com.mygdx.util.game.GameBase;
import com.mygdx.util.physics.B2ContactListener;
import com.mygdx.util.physics.BodyFactory;
import com.mygdx.util.screen.ScreenBaseAdapter;
import com.mygdx.util.system.HudRenderSystem;
import com.mygdx.util.system.ParallaxSystem;
import com.mygdx.util.system.PhysicsSystem;
import com.mygdx.util.system.PlayerSystem;
import com.mygdx.util.system.RenderSystem;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class Box2DScreen extends ScreenBaseAdapter {

    private final GameBase game;

    //    private final ObstacleAvoidGame game;
    private final AssetManager assetManager;

    private OrthographicCamera camera;
    private Viewport viewport;
    private ShapeRenderer renderer;
    private PooledEngine engine;
    private EntityFactory factory;
    private Sound hit;

    //Box 2D
    private Box2DDebugRenderer debugRenderer;
    //private B2Model b2Model;

    private World world;
    private BodyFactory bodyFactory;



    ComponentMapper<PlayerComponent> pComp;

    private GamePAD gamePAD;


    private Viewport hudViewport;

    public Box2DScreen(GameBase game ) {
        this.game = game;
        this.assetManager = game.getAssetManager();
    }

    @Override
    public void show() {

        //Essential tools

        // Create our new rendering system

        camera = new OrthographicCamera();
        viewport = new FitViewport(Gdx.graphics.getWidth()/10, Gdx.graphics.getHeight()/10, camera);
        //viewport = new FitViewport(GameConfig.WORLD_WIDTH, GameConfig.WORLD_HEIGHT, camera);
        hudViewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        renderer = new ShapeRenderer();
        gamePAD = new GamePAD();

        //Box2d
        //b2Model = new B2Model(world);
        debugRenderer = new Box2DDebugRenderer(true, true,
                                                true, true,
                                              true, true);

        world = new World(new Vector2(0,-50f), true);
        world.setContactListener(new B2ContactListener());
        bodyFactory = BodyFactory.getInstance(world);

        //Entity System
        engine = new PooledEngine();
        factory = new EntityFactory(engine, assetManager, bodyFactory);


        BitmapFont font = assetManager.get(AssetDescriptors.FONT);
        factory.addBackground("background/Clouds_1/1.png", 8, 122, 108, 192);

        engine.addSystem(new PlayerSystem(gamePAD, camera ));
        engine.addSystem(new PhysicsSystem(world));
        engine.addSystem(new RenderSystem(viewport, game.getBatch()));
        engine.addSystem(new HudRenderSystem(viewport, game.getBatch(), font));
        //engine.addSystem(new HudRenderSystem(hudViewport, game.getBatch(), font));
        engine.addSystem(new ParallaxSystem());

        //factory.addParallax("ui/back.png", 2, 2);

        factory.addPlayer(10, 50, 10, 10);
        factory.addFloor();



        pComp = ComponentMapper.getFor(PlayerComponent.class);

    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.BLACK);
        engine.update(delta);
        //b2Model.update(delta);

        //camera.translate();
        debugRenderer.render(world, viewport.getCamera().combined);

        //camera.position.x = 0;
        //camera.position.y = 0;

        for(Entity entity : engine.getEntitiesFor(Family.all().get()))
        {
            //camera.position.x = pComp.get(entity).xpos;
        }
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        hudViewport.update(width, height, true);
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
        renderer.dispose();
    }
}
