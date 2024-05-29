package com.mygdx.game;

import com.mygdx.screen.loading_screen.LoadingScreen;
import com.mygdx.util.game.GameBase;

public class LibGdxFramework extends GameBase {

    public LibGdxFramework(){super();}

    @Override
    public void postCreate() {
        setScreen(new LoadingScreen(this));



    }
}
