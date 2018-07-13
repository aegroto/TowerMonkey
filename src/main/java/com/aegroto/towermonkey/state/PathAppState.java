package com.aegroto.towermonkey.state;

import java.util.ArrayList;
import java.util.LinkedList;

import com.aegroto.towermonkey.entity.Entity;
import com.aegroto.towermonkey.entity.EntityPathMarker;
import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.jme3.math.Vector2f;
import com.jme3.scene.Node;

public class PathAppState extends BaseAppState {
    protected static boolean DEBUG = true;

    private LinkedList<Vector2f> pathPoints;
    private ArrayList<Entity> entities;
    private Node rootNode;

    public PathAppState(LinkedList<Vector2f> pathPoints, Node sceneRootNode) {
        this.pathPoints = pathPoints;
        this.rootNode = new Node();
        sceneRootNode.attachChild(rootNode);
    }

    @Override
    protected void initialize(Application aplctn) {
        this.entities = new ArrayList<>();
        pathPoints.forEach(point -> {
                    EntityPathMarker marker = new EntityPathMarker(DEBUG, getApplication().getAssetManager());
                    marker.setLocalTranslation(point.y, 2.5f, point.x); 
                    entities.add(marker);
                });
    }
    
    @Override
    protected void cleanup(Application aplctn) {
    }

    @Override
    protected void onEnable() {
        if(DEBUG) {
            entities.forEach(entity -> rootNode.attachChild(entity));
        }
    }

    @Override
    protected void onDisable() {
        if(DEBUG) {
            entities.forEach(entity -> entity.removeFromParent());
        }
    }
}