/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aegroto.tof.states;

import com.aegroto.tof.map.TowerDefenseHeightMap;
import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
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
    private Geometry seaGeom;
    private Material mapMaterial, seaMaterial;
    
    private final Node rootNode;

    private static final float MAP_WORLD_SIZE = 256f;
    
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
            generateMapGeometry(256, 64, (64 - 16) / 2);
        } catch(Exception e) {
            System.err.println("Error generating map:");
            e.printStackTrace();
        }
        
        rootNode.attachChild(map);
    }

    @Override
    protected void onDisable() {
    
    }
    
    private void generateMapGeometry(final int size, final int gridSize, final int ditchSize) throws Exception {
        TowerDefenseHeightMap heightmapGenerator = new TowerDefenseHeightMap();
        
        heightmapGenerator.setSize(size);
        heightmapGenerator.setGridSize(gridSize);
        heightmapGenerator.setDitchSize(ditchSize);
        
        heightmapGenerator.setMinDitchHeight(-8.0f);
        heightmapGenerator.setDitchVariation(.5f);

        heightmapGenerator.setPathTileBorderFactor(.15f);
        heightmapGenerator.setPathTileBorderNeckFactor(.05f);
        heightmapGenerator.setPathSnakyness(15);
        heightmapGenerator.setPathVariation(.25f);
        
        heightmapGenerator.setMinHillHeight(2f);        
        heightmapGenerator.setHillVariation(.25f);

        int battlegroundGridSize = size - ditchSize * 2;
                
        heightmapGenerator.setTotalMountains(10);
        heightmapGenerator.setMountainMinSize((int) (battlegroundGridSize * .05f));
        heightmapGenerator.setMountainMaxSize((int) (battlegroundGridSize * .1f));
        heightmapGenerator.setMountainMinLevels(battlegroundGridSize * 40);
        heightmapGenerator.setMountainMaxLevels(battlegroundGridSize * 50);
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
        
        mapMaterial = getApplication().getAssetManager().loadMaterial("Materials/TowerDefenseTerrain.j3m");        

        // Scaling mesh, adapting to a lower or higher size
        // float scale = size / MAP_WORLD_SIZE;
        // map.setLocalScale(scale);
        /*mapMaterial.setFloat("PathTexScale",     ((float) mapMaterial.getParam("PathTexScale").getValue()) * scale);
        mapMaterial.setFloat("HillTexScale",     ((float) mapMaterial.getParam("HillTexScale").getValue()) * scale);
        mapMaterial.setFloat("MountainTexScale", ((float) mapMaterial.getParam("MountainTexScale").getValue()) * scale);*/

        map.setMaterial(mapMaterial);

        seaGeom = new Geometry("Sea", new Quad(size, size));
        seaMaterial = getApplication().getAssetManager().loadMaterial("Materials/TowerDefenseSea.j3m"); 
        seaMaterial.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);     
        seaGeom.setMaterial(seaMaterial);

        seaGeom.setLocalTranslation(-size/2f, -1f, size/2f);
        seaGeom.setLocalRotation(new Quaternion().fromAngles(-FastMath.HALF_PI, 0f, 0f));
        rootNode.attachChild(seaGeom);

        /*TerrainQuad simpleMap = map.clone();
        simpleMap.setLocalTranslation(70f, 0f, 0f);
        Material simpleMapMaterial = getApplication().getAssetManager().loadMaterial("Materials/LightTerrain.j3m");  
        simpleMap.setMaterial(simpleMapMaterial);
        rootNode.attachChild(simpleMap);*/
    }
}