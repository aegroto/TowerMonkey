package com.aegroto.towermonkey.entity;

import lombok.Getter;
import com.jme3.math.Vector2f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;

/**
 *  
 * Entity abstract class, used to define
 * entities most general features and
 * actions. 
 * 
 * @author aegroto
 */

public abstract class Entity extends Node {
    protected Geometry geometry;        

    /**
     * 
     * update() method is called once per frame during
     * the update loop.
     * 
     * @author aegroto
     */
    public abstract void update();
}