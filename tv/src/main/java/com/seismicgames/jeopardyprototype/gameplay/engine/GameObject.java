package com.seismicgames.jeopardyprototype.gameplay.engine;

/**
 * Created by jduffy on 6/29/16.
 */
public abstract class GameObject {

    protected GameEngine engine;

    public GameObject(GameEngine engine){
        this.engine = engine;
    }

    public abstract void pump();
}
