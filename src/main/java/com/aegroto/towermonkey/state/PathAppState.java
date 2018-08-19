package com.aegroto.towermonkey.state;

import java.util.LinkedList;

import com.aegroto.towermonkey.util.PathMarker;
import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.jme3.math.Vector2f;
import com.jme3.scene.Node;

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

    @Setter private LinkedList<Vector2f> pathPoints;
    private Node sceneRootNode, rootNode;

    public PathAppState(LinkedList<Vector2f> pathPoints, Node sceneRootNode) {
        this.sceneRootNode = sceneRootNode;
        this.rootNode = new Node();
        this.pathPoints = pathPoints;
    }

    @Override
    protected void initialize(Application aplctn) {
        if(DEBUG) {
            pathPoints.forEach(point -> {
                PathMarker marker = new PathMarker(getApplication().getAssetManager());
                rootNode.attachChild(marker);
                marker.setLocalTranslation(point.y, 2.5f, point.x); 
            });
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
}