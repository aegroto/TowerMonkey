package com.aegroto.towermonkey.state;

import java.util.LinkedList;

import com.aegroto.towermonkey.map.TowerDefenseGrid;
import com.aegroto.towermonkey.map.TowerDefenseHeightMap;
import com.aegroto.towermonkey.util.Vector2i;
import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Quad;
import com.jme3.terrain.geomipmap.TerrainQuad;
import com.jme3.util.TangentBinormalGenerator;

import lombok.Getter;

/**
 *
 * @author lorenzo
 */
public class MapAppState extends BaseAppState {
    @Getter private Node map;
    private Geometry seaGeom;
    private Material mapMaterial, seaMaterial;
    
    private final Node rootNode;

    private final int mapSize, gridSize, ditchSize, battlegroundOffsetX, battlegroundOffsetY;

    private LinkedList<Vector2f> pathPoints;
    private PathAppState pathAppState;
    
    public MapAppState(Node rootNode, int mapSize, int gridSize, int ditchSize, int battlegroundOffsetX, int battlegroundOffsetY) {
        this.rootNode = rootNode;
        this.mapSize = mapSize;
        this.gridSize = gridSize;
        this.ditchSize = ditchSize;
        this.battlegroundOffsetX = battlegroundOffsetX;
        this.battlegroundOffsetY = battlegroundOffsetY;
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
            generateMapGeometry(mapSize, gridSize, ditchSize, new Vector2i(battlegroundOffsetX, battlegroundOffsetY));
        } catch(Exception e) {
            System.err.println("Error generating map:");
            e.printStackTrace();
        }
        
        rootNode.attachChild(map);

        pathAppState = new PathAppState(pathPoints, rootNode);
        this.getApplication().getStateManager().attach(pathAppState);
    }

    @Override
    protected void onDisable() {
    
    }
    
    private void generateMapGeometry(final int size, final int gridSize, final int ditchSize, final Vector2i battlegroundOffset) throws Exception {
        TowerDefenseGrid grid = new TowerDefenseGrid(gridSize - ditchSize * 2, 15);
        TowerDefenseHeightMap heightmapGenerator = new TowerDefenseHeightMap(grid);
        
        heightmapGenerator.setSize(size);
        heightmapGenerator.setGridSize(gridSize);
        heightmapGenerator.setDitchSize(ditchSize);
        heightmapGenerator.setBattlegroundOffset(battlegroundOffset);

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
        
        pathPoints = heightmapGenerator.getPathPoints();
        
        AmbientLight ambient = new AmbientLight();
        ambient.setColor(new ColorRGBA(.7f, .7f, .7f, 1f));
        rootNode.addLight(ambient);
        
        DirectionalLight sun = new DirectionalLight();
        sun.setDirection((new Vector3f(-0.5f, -0.5f, -0.5f)).normalizeLocal());
        sun.setColor(ColorRGBA.White);
        rootNode.addLight(sun);
        
        map = new TerrainQuad("Map mesh", (int) FastMath.pow(2, 9) + 1, size + 1, heightmapGenerator.getHeightMap());
        
        mapMaterial = getApplication().getAssetManager().loadMaterial("Materials/TowerDefenseTerrain.j3m");        

        map.setMaterial(mapMaterial);

        seaGeom = new Geometry("Sea", new Quad(size, size));
        seaMaterial = getApplication().getAssetManager().loadMaterial("Materials/TowerDefenseSea.j3m"); 
        seaMaterial.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);     
        seaGeom.setMaterial(seaMaterial);

        seaGeom.setLocalTranslation(-size/2f, -1f, size/2f);
        seaGeom.setLocalRotation(new Quaternion().fromAngles(-FastMath.HALF_PI, 0f, 0f));
        TangentBinormalGenerator.generate(seaGeom);
        rootNode.attachChild(seaGeom);
    }
}