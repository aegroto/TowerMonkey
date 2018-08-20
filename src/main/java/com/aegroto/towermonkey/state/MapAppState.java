package com.aegroto.towermonkey.state;

import com.aegroto.towermonkey.map.TowerDefenseGrid;
import com.aegroto.towermonkey.map.TowerDefenseHeightMap;
import com.aegroto.towermonkey.path.PathPoint;
import com.aegroto.towermonkey.util.Vector2i;
import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Quad;
import com.jme3.terrain.geomipmap.TerrainQuad;
import com.jme3.util.TangentBinormalGenerator;

import lombok.Getter;
import lombok.Setter;

/**
 *
 * App state used to setup and generate a tower defense map.
 * 
 * @author aegroto 
 */

public class MapAppState extends BaseAppState {
    @Getter protected Node mapGeom;
    protected Geometry seaGeom;
    @Setter private Material mapMaterial, seaMaterial;
    
    private final Node rootNode;

    @Getter private PathPoint headPathPoint; 
    private PathAppState pathAppState;

    @Getter @Setter private int mapSize, gridSize, ditchSize,
                    battlegroundOffsetX, battlegroundOffsetY;
    @Getter @Setter private long seed;

    @Getter @Setter private float minDitchHeight, ditchVariation,
                  pathTileBorderFactor, pathTileBorderNeckFactor, pathVariation,
                  minHillHeight, hillVariation, mountainBorderFragmentation,
                  minMountainHeight, mountainVariation, mountainBaseTerrainThreshold;

    @Getter @Setter private int totalMountains, 
                                mountainMinSize, mountainMaxSize,
                                mountainMinLevels, mountainMaxLevels;

    /**
     * 
     * MapAppState constructor.
     * 
     * @param rootNode Scene root node
     * 
     * @author aegroto
     * 
     */
    
    public MapAppState(Node rootNode) {
        this.rootNode = rootNode;
    }
    
    @Override
    protected void initialize(Application aplctn) {
        try {
            generateMapGeometry(mapSize, gridSize, ditchSize, new Vector2i(battlegroundOffsetX, battlegroundOffsetY), seed);
        } catch(Exception e) {
            System.err.println("Error generating map:");
            e.printStackTrace();
        }

        pathAppState = new PathAppState(headPathPoint, rootNode);
    }
    
    @Override
    protected void cleanup(Application aplctn) { }

    @Override
    protected void onEnable() {
        rootNode.attachChild(mapGeom);
        rootNode.attachChild(seaGeom);

        getStateManager().attach(pathAppState);
    }

    @Override
    protected void onDisable() { }

    private void generateMapGeometry(final int size, final int gridSize, final int ditchSize, final Vector2i battlegroundOffset, long seed) throws Exception {
        TowerDefenseGrid grid = new TowerDefenseGrid(gridSize - ditchSize * 2, 15, seed);
        TowerDefenseHeightMap heightmapGenerator = new TowerDefenseHeightMap(grid, seed);
        
        heightmapGenerator.setSize(size);
        heightmapGenerator.setGridSize(gridSize);
        heightmapGenerator.setDitchSize(ditchSize);
        heightmapGenerator.setBattlegroundOffset(battlegroundOffset);

        heightmapGenerator.setMinDitchHeight(minDitchHeight);
        heightmapGenerator.setDitchVariation(ditchVariation);

        heightmapGenerator.setPathTileBorderFactor(pathTileBorderFactor);
        heightmapGenerator.setPathTileBorderNeckFactor(pathTileBorderNeckFactor);
        heightmapGenerator.setPathVariation(pathVariation);
        
        heightmapGenerator.setMinHillHeight(minHillHeight);        
        heightmapGenerator.setHillVariation(hillVariation);
                
        heightmapGenerator.setTotalMountains(totalMountains);
        heightmapGenerator.setMountainMinSize(mountainMinSize);
        heightmapGenerator.setMountainMaxSize(mountainMaxSize);
        heightmapGenerator.setMountainMinLevels(mountainMinLevels);
        heightmapGenerator.setMountainMaxLevels(mountainMaxLevels);
        heightmapGenerator.setMountainBorderFragmentation(mountainBorderFragmentation);
        heightmapGenerator.setMinMountainHeight(minMountainHeight);     
        heightmapGenerator.setMountainVariation(mountainVariation);
        heightmapGenerator.setMountainBaseTerrainThreshold(mountainBaseTerrainThreshold);
        
        heightmapGenerator.load();
        
        headPathPoint = heightmapGenerator.calculatePathPoints();
        
        AmbientLight ambient = new AmbientLight();
        ambient.setColor(new ColorRGBA(.7f, .7f, .7f, 1f));
        rootNode.addLight(ambient);
        
        DirectionalLight sun = new DirectionalLight();
        sun.setDirection((new Vector3f(-0.5f, -0.5f, -0.5f)).normalizeLocal());
        sun.setColor(ColorRGBA.White);
        rootNode.addLight(sun);
        
        mapGeom = new TerrainQuad("Map mesh", (int) FastMath.pow(2, 9) + 1, size + 1, heightmapGenerator.getHeightMap());

        seaGeom = new Geometry("Sea", new Quad(size, size));
        seaGeom.setLocalTranslation(-size/2f, -1f, size/2f);
        seaGeom.setLocalRotation(new Quaternion().fromAngles(-FastMath.HALF_PI, 0f, 0f));
        TangentBinormalGenerator.generate(seaGeom);

        updateMaterials();
    }

    /**
     * 
     * Updates map materials.
     * 
     * @author aegroto
     * 
     */

    public void updateMaterials() {
        mapGeom.setMaterial(mapMaterial);
        seaGeom.setMaterial(seaMaterial);
    }
}