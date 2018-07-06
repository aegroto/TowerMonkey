package com.aegroto.towermonkey.entity;

import com.jme3.math.Vector2f;

public abstract class EntityPathMarker extends Entity{  
    public EntityPathMarker(Vector2f position) {
        this.position = position;
    }
}