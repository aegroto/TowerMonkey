package com.aegroto.tof;

import com.aegroto.tof.states.InGameAppState;
import com.jme3.app.SimpleApplication;
import com.jme3.renderer.RenderManager;
import com.jme3.system.AppSettings;

public class Main extends SimpleApplication {
    
    private InGameAppState inGameAppState;
    
    public static void main(String[] args) {
        Main app = new Main();
        AppSettings appSettings = new AppSettings(true);
        app.setSettings(appSettings);
        app.setShowSettings(false);
        app.start();
    }

    @Override
    public void simpleInitApp() {
        // getFlyByCamera().setEnabled(false);
        getFlyByCamera().setMoveSpeed(100f);
        this.setDisplayStatView(false);
        
        inGameAppState = new InGameAppState(rootNode);
        
        stateManager.attach(inGameAppState);
    }

    @Override
    public void simpleUpdate(float tpf) {
         
    }

    @Override
    public void simpleRender(RenderManager rm) {
        
    }
}
