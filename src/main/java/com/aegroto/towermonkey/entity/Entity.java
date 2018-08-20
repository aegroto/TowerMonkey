package com.aegroto.towermonkey.entity;

import com.jme3.asset.AssetManager;
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

    public Entity(AssetManager assetManager) {
        initializeGeometry(assetManager);

        this.attachChild(geometry);
    }

    protected abstract void initializeGeometry(AssetManager assetManager);
    /**
     * 
     * update() method is called once per frame during
     * the update loop.
     * 
     * @author aegroto
     */
    public abstract void update(float tpf);
}