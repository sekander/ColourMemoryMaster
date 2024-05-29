package com.mygdx.screen.loading_screen;

import com.badlogic.gdx.Net;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.utils.Array;
import com.mygdx.screen.game_screen.Box2DScreen;
import com.mygdx.screen.game_screen.ColourPattern;
import com.mygdx.screen.game_screen.NetworkTest;
import com.mygdx.screen.menu_screen.MenuScreen;
import com.mygdx.screen.transition.ScreenTransitions;
import com.mygdx.util.game.GameBase;
import com.mygdx.util.screen.loading.LoadingScreenBase;
import com.mygdx.assets.AssetDescriptors;
import com.mygdx.util.screen.transitions.ScreenTransition;

public class LoadingScreen extends LoadingScreenBase {
    // == constructors ==
    public LoadingScreen(GameBase game) {
        super(game);
    }

    // == protected methods ==
    @Override
    protected Array<AssetDescriptor> getAssetDescriptors() {
        return AssetDescriptors.ALL;
    }


    @Override
    protected void onLoadDone() {

        //game.setScreen(new MenuScreen(game), ScreenTransitions.FADE);
        //game.setScreen(new NetworkTest(game), ScreenTransitions.FADE);
        game.setScreen(new ColourPattern(game), ScreenTransitions.FADE);
        //game.setScreen(new Box2DScreen(game));
        //game.setScreen(new GameScrenn(game));
    }


}
