package com.mygdx.util.system;

import com.mygdx.util.common.Mappers;
import com.mygdx.util.components.MovementComponent;
import com.mygdx.util.components.ParallaxComponenet;
import com.mygdx.util.components.TransformComponent;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;

public class ParallaxSystem extends IteratingSystem {
    public static final Family FAMILY = Family.all(
            TransformComponent.class,
            MovementComponent.class,
            ParallaxComponenet.class
    ).get();

    public ParallaxSystem(){super((FAMILY));}


    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        TransformComponent transform = Mappers.TRANSFORM.get(entity);
        MovementComponent movement = Mappers.MOVEMENT.get(entity);
        ParallaxComponenet parallax = Mappers.PARALLAX.get(entity);
        //ParallaxComponenet parallax = Mappers.

        movement.ySpeed = -0.01f;

        if(transform.y < -10.0f)
            transform.y = 0;
    }
}
