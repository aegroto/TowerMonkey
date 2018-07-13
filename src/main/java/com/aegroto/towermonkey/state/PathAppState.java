package com.aegroto.towermonkey.state;

import java.util.ArrayList;
import java.util.LinkedList;

import com.aegroto.towermonkey.entity.EntityPathMarker;
import com.aegroto.towermonkey.map.MapTile;
import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.jme3.math.Vector2f;
import com.jme3.scene.Node;

public class PathAppState extends BaseAppState {
    protected static boolean DEBUG = true;

    private LinkedList<Vector2f> pathPoints;
    private ArrayList<EntityPathMarker> pathMarkers;
    private Node rootNode;

    public PathAppState(LinkedList<Vector2f> pathPoints, Node rootNode) {
        this.pathPoints = pathPoints;
        this.rootNode = rootNode;
    }

    @Override
    protected void initialize(Application aplctn) {
        this.pathMarkers = new ArrayList<>();
        pathPoints.forEach(point -> {
                    EntityPathMarker marker = new EntityPathMarker(DEBUG, getApplication().getAssetManager());
                    marker.setLocalTranslation(point.y, 2.5f, point.x); 
                    pathMarkers.add(marker);
                });
    }
    
    @Override
    protected void cleanup(Application aplctn) {
    }

    @Override
    protected void onEnable() {
        if(DEBUG) {
            pathMarkers.stream().forEach(marker -> rootNode.attachChild(marker));
        }
    }

    @Override
    protected void onDisable() {
    
    }
}