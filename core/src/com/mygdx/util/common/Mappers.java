package com.mygdx.util.common;

import com.mygdx.util.components.BoundsComponent;
import com.mygdx.util.components.MovementComponent;
import com.mygdx.util.components.ObstacleComponent;
import com.mygdx.util.components.ParallaxComponenet;
import com.mygdx.util.components.TextureComponent;
import com.mygdx.util.components.TransformComponent;
import com.badlogic.ashley.core.ComponentMapper;

public class Mappers {

    public static final ComponentMapper<BoundsComponent> BOUNDS =
            ComponentMapper.getFor(BoundsComponent.class);

    public static final ComponentMapper<MovementComponent> MOVEMENT =
            ComponentMapper.getFor(MovementComponent.class);

    public static final ComponentMapper<TransformComponent> TRANSFORM =
            ComponentMapper.getFor(TransformComponent.class);

    public static final ComponentMapper<ObstacleComponent> OBSTACLE =
            ComponentMapper.getFor(ObstacleComponent.class);

    public static final ComponentMapper<TextureComponent> TEXTURE =
            ComponentMapper.getFor(TextureComponent.class);

    public static final ComponentMapper<ParallaxComponenet> PARALLAX =
            ComponentMapper.getFor(ParallaxComponenet.class);

    private Mappers(){}
}
