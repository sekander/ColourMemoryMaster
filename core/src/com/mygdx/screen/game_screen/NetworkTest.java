package com.mygdx.screen.game_screen;

import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.net.SocketHints;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.assets.AssetDescriptors;
import com.mygdx.util.common.EntityFactory;
import com.mygdx.util.game.GameBase;
import com.mygdx.util.screen.ScreenBaseAdapter;
import com.mygdx.util.system.HudRenderSystem;
import com.mygdx.util.system.RenderSystem;

public class NetworkTest extends ScreenBaseAdapter {

    private final GameBase game;
    private final AssetManager assetManager;

    private OrthographicCamera camera;
    private Viewport viewport;

    private PooledEngine engine;
    private EntityFactory factory;
    BitmapFont font;


    SocketHints hints = new SocketHints();
//    Socket client = Gdx.net.newClientSocket(Net.Protocol.TCP, "192.168.0.179", 8080, hints);


    private ShapeRenderer debugRender;
    public NetworkTest(GameBase game){

        this.game = game;
        this.assetManager = game.getAssetManager();
    }

    @Override
    public void show() {
        super.show();

        camera = new OrthographicCamera();
        viewport = new FitViewport(Gdx.graphics.getWidth()/10, Gdx.graphics.getHeight()/10, camera);
        debugRender = new ShapeRenderer();

        font = assetManager.get(AssetDescriptors.FONT);
        font.getData().setScale(0.25f, 0.25f);
        //Entity System
        engine = new PooledEngine();
        engine.addSystem(new RenderSystem(viewport, game.getBatch()));
        engine.addSystem(new HudRenderSystem(viewport, game.getBatch(), font));
        factory = new EntityFactory(engine, assetManager, null);

        /*
        try {
            client.getOutputStream().write("PING\n".getBytes());
            String response = new BufferedReader(new InputStreamReader(client.getInputStream())).readLine();
            Gdx.app.log("PingPongSocketExample", "got server message: " + response);
        } catch (IOException e) {
            Gdx.app.log("PingPongSocketExample", "an error occured", e);
        }
        */

        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true)
                    System.out.println("Worker Thread!!");
            }
        }).start();
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        ScreenUtils.clear(Color.BLACK);
            System.out.println("Main Thread!!");

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
    }
}
