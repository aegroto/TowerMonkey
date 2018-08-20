package com.aegroto.towermonkey.state;

import java.util.ArrayList;

import com.aegroto.towermonkey.entity.Entity;
import com.aegroto.towermonkey.entity.WalkingEntity;
import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.jme3.scene.Node;

/**
 * 
 * App state used to manage entities spawned on the map
 * 
 * @author aegroto
 * 
 */

public class EntityAppState extends BaseAppState {
    private static final boolean DEBUG = true;

    private Node sceneRootNode, rootNode;
    private ArrayList<Entity> entities;

    private PathAppState pathAppState; 

    /**
     * EntityAppState constructor
     * 
     * @param sceneRootNode Scene root node
     * 
     * @author aegroto
     * 
     */
    
    public EntityAppState(Node sceneRootNode) {
        this.sceneRootNode = sceneRootNode;
        this.rootNode = new Node();
    }

    protected void initialize(Application application) {
        this.entities = new ArrayList<>();
        pathAppState = getApplication().getStateManager().getState(PathAppState.class);
    }

    protected void cleanup(Application application) { }

    protected void onEnable() { 
        sceneRootNode.attachChild(rootNode);
    }

    protected void onDisable() {
        rootNode.removeFromParent();
    } 

    @Override
    public void update(float tpf) {
        entities.forEach(entity -> {

        });
    }

    public void addWalkingEntity(WalkingEntity entity) {
        entities.add(entity);
        entity.initializePathWalk(pathAppState.getHeadPathPoint());

        rootNode.attachChild(entity);        
    }
}