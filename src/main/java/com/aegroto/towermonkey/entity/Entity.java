package com.aegroto.towermonkey.entity;

import lombok.Getter;
import com.jme3.math.Vector2f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;

public abstract class Entity extends Node {
    protected Geometry geometry;        

    public abstract void update();
}