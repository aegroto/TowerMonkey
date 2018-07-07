package com.aegroto.towermonkey.entity;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;

public class EntityPathMarker extends Entity {  
    // private boolean debug = false;

    public EntityPathMarker(boolean debug, AssetManager assetManager) {
        if(debug) {
            geometry = new Geometry("Path Marker debug shape", new Box(1, 1, 1));
            Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
            mat.setColor("Color", ColorRGBA.Blue);
            geometry.setMaterial(mat);
            this.attachChild(geometry); 
        }
    }

    public void update() {

    }
}