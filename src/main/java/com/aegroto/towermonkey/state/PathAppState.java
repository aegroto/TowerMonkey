package com.aegroto.towermonkey.state;

import java.util.LinkedList;

import com.aegroto.towermonkey.path.PathPoint;
import com.aegroto.towermonkey.util.PathMarker;
import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.jme3.math.Vector2f;
import com.jme3.scene.Node;

import lombok.Getter;
import lombok.Setter;
/**
 * 
 * App state used to manage a tower defense path.
 * 
 * @author aegroto
 * 
 */

public class PathAppState extends BaseAppState {
    private static boolean DEBUG = true;

    @Getter private PathPoint headPathPoint; 
    private Node sceneRootNode, rootNode;

    public PathAppState(PathPoint headPathPoint, Node sceneRootNode) {
        this.sceneRootNode = sceneRootNode;
        this.rootNode = new Node();
        this.headPathPoint = headPathPoint;
    }

    @Override
    protected void initialize(Application aplctn) {
        if(DEBUG) {
            PathPoint point = headPathPoint;
            while(point != null) { 
                PathMarker marker = new PathMarker(getApplication().getAssetManager());
                rootNode.attachChild(marker);
                marker.setLocalTranslation(point.getPosition().x, 2.5f, point.getPosition().y); 

                point = point.getNextPathPoint();
            };
        }
    }
    
    @Override
    protected void cleanup(Application aplctn) {
    }

    @Override
    protected void onEnable() {
        sceneRootNode.attachChild(rootNode);

    }

    @Override
    protected void onDisable() {
        rootNode.removeFromParent();
    }

    @Override
    public void update(float tpf) { }
}