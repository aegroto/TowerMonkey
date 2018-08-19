package com.aegroto.towermonkey.state;

import java.util.ArrayList;
import java.util.LinkedList;

import com.aegroto.towermonkey.entity.Entity;
import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.jme3.math.Vector2f;
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
    }

    public void addEntity(Entity entity) {
        entities.add(entity);

        rootNode.attachChild(entity);

        Vector2f position = pathAppState.getHeadPathPoint().getPosition();
        entity.setLocalTranslation(position.x, 2.5f, position.y);
    }
}