package com.aegroto.tof.states;

import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;

/**
 *
 * @author lorenzo
 */
public class InGameAppState extends BaseAppState {
    private final MapAppState mapAppState;
    
    public InGameAppState(Node rootNode) {
        this.mapAppState = new MapAppState(rootNode);
    }

    @Override
    protected void initialize(Application aplctn) { }

    @Override
    protected void cleanup(Application aplctn) { }

    @Override
    protected void onEnable() {
        getApplication().getCamera().setLocation(new Vector3f(0, 60, 65));
        getApplication().getCamera().setRotation(new Quaternion().fromAngles(FastMath.QUARTER_PI, FastMath.PI, 0f));      
        
        getStateManager().attach(mapAppState);
    }

    @Override
    protected void onDisable() { }    
}
