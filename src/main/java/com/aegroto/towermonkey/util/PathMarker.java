package com.aegroto.towermonkey.util;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;

/** 
 * 
 * PathMarker class, used to highlights path points.
 * Useful for debug
 * 
 * @author aegroto
 * 
 */

public class PathMarker extends Node {
    /**
     * 
     * PathMarker constructor.
     * 
     * @param assetManager Asset manager
     * 
     * @author aegroto
     * 
     */

    public PathMarker(AssetManager assetManager) {
        Geometry geometry = new Geometry("Path Marker debug shape", new Box(1, 1, 1));
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", ColorRGBA.Blue);
        geometry.setMaterial(mat);
        this.attachChild(geometry); 
    }
}