package com.mygdx.util.screen;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;

public interface ScreenBase extends Screen {
    InputProcessor getInputProccessor();
}
