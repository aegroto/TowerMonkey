package com.aegroto.towermonkey.state;

import java.util.ArrayList;

import com.aegroto.towermonkey.entity.Entity;
import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.jme3.scene.Node;

public class EntityAppState extends BaseAppState {
    private static final boolean DEBUG = true;

    private Node sceneRootNode, rootNode;
    private ArrayList<Entity> entities;

    public EntityAppState(Node sceneRootNode) {
        this.sceneRootNode = sceneRootNode;
        this.rootNode = new Node();
    }

    protected void initialize(Application application) {
        this.entities = new ArrayList<>();
    }

    protected void cleanup(Application application) { }

    protected void onEnable() { 
        sceneRootNode.attachChild(rootNode);
    }

    protected void onDisable() {
        rootNode.removeFromParent();
    } 
}