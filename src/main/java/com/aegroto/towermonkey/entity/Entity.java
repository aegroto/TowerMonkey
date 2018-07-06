package com.aegroto.towermonkey.entity;

import lombok.Getter;
import com.jme3.math.Vector2f;
import com.jme3.scene.Geometry;

public abstract class Entity {
    @Getter protected Vector2f position;
    protected Geometry geometry;        
}