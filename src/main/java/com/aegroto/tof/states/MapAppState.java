/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aegroto.tof.states;

import com.aegroto.tof.map.TowerDefenseGrid;
import com.aegroto.tof.map.TowerDefenseHeightMap;
import com.aegroto.tof.mesh.TowerDefenseGridMesh;
import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.jme3.light.AmbientLight;
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
import com.jme3.scene.shape.Box;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture.WrapMode;
import com.jme3.util.mikktspace.MikktspaceTangentGenerator;

import lombok.Getter;

/**
 *
 * @author lorenzo
 */
public class MapAppState extends BaseAppState {
    @Getter private Geometry map;
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
            generateMapGeometry(64, 32, (32 - 16) / 2);
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
        TowerDefenseHeightMap heightmapGenerator = new TowerDefenseHeightMap(new TowerDefenseGrid(gridSize - ditchSize * 2, 15));
        
        heightmapGenerator.setSize(size);
        heightmapGenerator.setGridSize(gridSize);
        heightmapGenerator.setDitchSize(ditchSize);
        
        heightmapGenerator.setMinDitchHeight(-8.0f);
        heightmapGenerator.setDitchVariation(.5f);

        heightmapGenerator.setPathTileBorderFactor(.15f);
        heightmapGenerator.setPathTileBorderNeckFactor(.05f);
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
        AmbientLight ambient = new AmbientLight();
        ambient.setColor(ColorRGBA.White);
        rootNode.addLight(ambient);
        
        DirectionalLight sun = new DirectionalLight();
        sun.setDirection((new Vector3f(-0.5f, -0.5f, -0.5f)).normalizeLocal());
        sun.setColor(ColorRGBA.White);
        rootNode.addLight(sun);
        
        // map = new TerrainQuad("Map mesh", (int) FastMath.pow(2, 9) + 1, size + 1, heightmapGenerator.getHeightMap());
        map = new Geometry("Map mesh", new TowerDefenseGridMesh((short) size, (short) size, 1f));
        map.setLocalTranslation(-size/2f, 0f, -size/2f);

        // mapMaterial = getApplication().getAssetManager().loadMaterial("Materials/TowerDefenseTerrain.j3m");        
        mapMaterial = new Material(getApplication().getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
        // mapMaterial.getAdditionalRenderState().setWireframe(true);
        Texture testTexture = getApplication().getAssetManager().loadTexture("Textures/test.png");
        testTexture.setWrap(WrapMode.Repeat);
        mapMaterial.setTexture("ColorMap", testTexture);

        map.setMaterial(mapMaterial);

        seaGeom = new Geometry("Sea", new Quad(size, size));
        seaMaterial = getApplication().getAssetManager().loadMaterial("Materials/TowerDefenseSea.j3m"); 
        seaMaterial.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);     
        seaGeom.setMaterial(seaMaterial);

        seaGeom.setLocalTranslation(-size/2f, -1f, size/2f);
        seaGeom.setLocalRotation(new Quaternion().fromAngles(-FastMath.HALF_PI, 0f, 0f));
        MikktspaceTangentGenerator.generate(seaGeom);
        // rootNode.attachChild(seaGeom);
    }
}