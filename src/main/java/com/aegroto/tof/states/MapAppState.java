/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aegroto.tof.states;

import com.aegroto.tof.map.TowerDefenseHeightMap;
import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Quad;
import com.jme3.terrain.geomipmap.TerrainQuad;

import lombok.Getter;

/**
 *
 * @author lorenzo
 */
public class MapAppState extends BaseAppState {
    @Getter private TerrainQuad map;
    private Material mapMaterial;
    
    private final Node rootNode;
    
    public MapAppState(Node rootNode) {
        this.rootNode = rootNode;
    }
    
    @Override
    protected void initialize(Application aplctn) {
    }
    
    @Override
    protected void cleanup(Application aplctn) {
    }

    @Override
    protected void onEnable() {
        try {
            generateMapGeometry(64, 16);
        } catch(Exception e) {
            System.err.println("Error generating map:");
            e.printStackTrace();
        }
        
        rootNode.attachChild(map);
    }

    @Override
    protected void onDisable() {
    
    }
    
    private void generateMapGeometry(final int size, final int gridSize) throws Exception {
        TowerDefenseHeightMap heightmapGenerator = new TowerDefenseHeightMap();
        
        heightmapGenerator.setSize(size);
        heightmapGenerator.setGridSize(gridSize);
        
        heightmapGenerator.setPathTileBorderFactor(.2f);
        heightmapGenerator.setPathTileBorderNeckFactor(.01f);
        heightmapGenerator.setPathSnakyness(15);
        heightmapGenerator.setPathVariation(.25f);
        
        heightmapGenerator.setMinHillHeight(2f);        
        heightmapGenerator.setHillVariation(0f);
                
        heightmapGenerator.setTotalMountains(5);
        heightmapGenerator.setMountainMinSize((int) (size * .1f));
        heightmapGenerator.setMountainMaxSize((int) (size * .15f));
        heightmapGenerator.setMountainMinLevels(size * 50);
        heightmapGenerator.setMountainMaxLevels(size * 60);
        heightmapGenerator.setMountainBorderFragmentation(.5f);
        heightmapGenerator.setMinMountainHeight(2f);     
        heightmapGenerator.setMountainVariation(.1f);
        heightmapGenerator.setMountainBaseTerrainThreshold(3f);
        
        heightmapGenerator.load();   
        
        /** A white ambient light source. */ 
        /*AmbientLight ambient = new AmbientLight();
        ambient.setColor(ColorRGBA.White);
        rootNode.addLight(ambient); */
        
        DirectionalLight sun = new DirectionalLight();
        sun.setDirection((new Vector3f(-0.5f, -0.5f, -0.5f)).normalizeLocal());
        sun.setColor(ColorRGBA.White);
        rootNode.addLight(sun);
        
        map = new TerrainQuad("Map mesh", (int) FastMath.pow(2, 9) + 1, size + 1, heightmapGenerator.getHeightMap());
        // TangentBinormalGenerator.generate(map);
        
        mapMaterial = getApplication().getAssetManager().loadMaterial("Materials/PBRTerrain.j3m");
        map.setMaterial(mapMaterial);

        TerrainQuad simpleMap = map.clone();
        simpleMap.setLocalTranslation(70f, 0f, 0f);
        Material simpleMapMaterial = getApplication().getAssetManager().loadMaterial("Materials/LightTerrain.j3m");  
        simpleMap.setMaterial(simpleMapMaterial);
        rootNode.attachChild(simpleMap);
    }
}