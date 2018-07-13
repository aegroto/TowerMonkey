package com.aegroto.towermonkey.map;

import java.util.LinkedList;

import com.aegroto.towermonkey.util.FastRandom;
import com.aegroto.towermonkey.util.Vector2i;
import com.jme3.math.FastMath;
import com.jme3.math.Vector2f;
import com.jme3.terrain.heightmap.AbstractHeightMap;

import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author lorenzo
 */

public class TowerDefenseHeightMap extends AbstractHeightMap {    
    @Getter @Setter private int
              xArrayOffset,
              totalMountains,
              mountainMinSize,
              mountainMaxSize,
              mountainMinLevels, mountainMaxLevels,
              mountainMaxGenerationTries = 5;
    
    private int 
              gridSize, ditchSize,
              pathTileSize, pathTileBorder, pathTileBorderNeck;

    @Setter private Vector2i battlegroundOffset = new Vector2i(0, 0);
    
    @Getter @Setter private float 
            ditchVariation, pathVariation, hillVariation, mountainVariation,
            minDitchHeight,
            minHillHeight,
            minMountainHeight = 2f,
            mountainBorderFragmentation,
            mountainBaseTerrainThreshold = minMountainHeight * .75f;
    
    @Getter private TowerDefenseGrid grid;

    private FastRandom randomizer;
    
    public TowerDefenseHeightMap(TowerDefenseGrid grid, long seed) {
        this.grid = grid;
        randomizer = new FastRandom(seed);
    }

    public TowerDefenseHeightMap(TowerDefenseGrid grid) {
        this(grid, 0);
    }
    
    @Override
    public void setSize(int size) {
        try {
            super.setSize(size);
            this.xArrayOffset = size * 2;
        } catch (Exception e) {
            System.err.println(e);
        }
    }
    
    public void setGridSize(int gridSize) {
        this.gridSize = gridSize;
        updatePathTileSize();
    }

    public void setDitchSize(int ditchSize) {
        this.ditchSize = ditchSize;
    }
    
    public void setPathTileBorderFactor(float borderFactor) {
        this.pathTileBorder = (int) (pathTileSize * borderFactor);
    }
    
    public void setPathTileBorderNeckFactor(float borderNeckFactor) {
        this.pathTileBorderNeck = (int) (pathTileBorder * borderNeckFactor);
    }

    private void updatePathTileSize() {
        this.pathTileSize = size / gridSize;
    }
    
    private void initRawHeightmap() {
        int heightmapLSize = (size * 2);
        int heightmapSize = heightmapLSize * heightmapLSize;
        heightData = new float[heightmapSize];
    }

    private void generateBattleground() {
        int battlegroundSize = gridSize - ditchSize * 2;
        int xStart = battlegroundOffset.getX(),
            zStart = battlegroundOffset.getY();

        for(int x = xStart; x < xStart + battlegroundSize; ++x) {
            for(int z = zStart; z < zStart + battlegroundSize; ++z) {
                generateHillTile(x, z);
            }
        }
    }

    private void generateBattlegroundBorder() {
        int battlegroundSize = gridSize - ditchSize * 2;
        int xStart = tileToMeshScale(battlegroundOffset.getX() - 1),
            zStart = tileToMeshScale(battlegroundOffset.getY() - 1);

        for(int x = xStart; x < xStart + tileToMeshScale(battlegroundSize) + pathTileSize * 2; ++x) {
            for(int z = zStart; z < zStart + pathTileSize; ++z) {
                heightData[arrayIndex(x, z)] = randomBattlegroundBorderPointHeight();
                heightData[arrayIndex(z, x)] = randomBattlegroundBorderPointHeight();
                heightData[arrayIndex(x, tileToMeshScale(battlegroundSize) + pathTileSize + z)] = randomBattlegroundBorderPointHeight();
                heightData[arrayIndex(tileToMeshScale(battlegroundSize) + pathTileSize + z, x)] = randomBattlegroundBorderPointHeight();
            }
        }
    }

    private void generateDitch() {       
        for(int x = 0; x < gridSize; ++x) {
            for(int z = 0; z < gridSize; ++z) {
                generateDitchTile(x, z);
            }
        }
    }
    
    
    private void generateMountains() {
        int mountains, mountainSizeX, mountainSizeZ, x, z, generationTries;
        mountains = generationTries = 0;
        
        while(mountains < totalMountains) {
            mountainSizeX = randomizer.randomIntInRange(mountainMinSize, mountainMaxSize);
            mountainSizeZ = randomizer.randomIntInRange(mountainMinSize, mountainMaxSize);
            x = randomizer.randomIntInRange(0, size);
            z = randomizer.randomIntInRange(0, size);
            
            if(isMountainGenerableIn(x, z, mountainSizeX, mountainSizeZ)) {
                generateMountain(x, z, mountainSizeX, mountainSizeZ);
                
                ++mountains;
                generationTries = 0;
            } else  {
                ++generationTries;
                
                if(generationTries >= mountainMaxGenerationTries) {
                    System.err.printf("WARNING: There was no space to generate a mountain (%d)\n", mountains);
                    ++mountains;
                    generationTries = 0;
                }
            }
        }
    } 
    
    private boolean isMountainGenerableIn(int x, int z, int xRange, int zRange) {
        int zoneXStart = x - xRange > 0 ? x - xRange : 0,
            zoneZStart = z - zRange > 0 ? z - zRange : 0,
            zoneXRange = xRange * 2 < size ? xRange * 2 : size,
            zoneZRange = zRange * 2 < size ? zRange * 2 : size;
        
        boolean pathFree = isRangeFreeOfPath(zoneXStart, zoneZStart, zoneXRange, zoneZRange);
                
        boolean lowMediumHeight = 
                getRangeMediumHeight(zoneXStart, zoneZStart, zoneXRange, zoneZRange) < mountainBaseTerrainThreshold;
        
        return pathFree && lowMediumHeight;
    }
    
    private float getRangeMediumHeight(int x, int z, int xRange, int zRange) {
        float totalHeight = 0f;
        
        int iStart = x,
            jStart = z,
            iLimit = x + xRange,
            jLimit = z + zRange;
        
        for(int i = iStart; i <= iLimit; ++i) {
            for(int j = jStart; j <= jLimit; ++j) {
                totalHeight += getPointHeight(i, j);
            }
        }
        
        return totalHeight / (xRange * zRange);
    }
    
    private boolean isRangeFreeOfPath(int x, int z, int xRange, int zRange) {        
        int iStart = meshToTileScale(x),
            jStart = meshToTileScale(z),
            iLimit = meshToTileScale(x + xRange),
            jLimit = meshToTileScale(z + zRange);
        
        for(int i = iStart; i <= iLimit; ++i) {
            for(int j = jStart; j <= jLimit; ++j) {
                if(grid.isPathTile(i, j)) {
                    return false;
                }
            }
        }
        
        return true;
    }
    
    private void generateMountain(int x, int z, int mountainSizeX, int mountainSizeZ) {
        int mountainLevels = randomizer.randomIntInRange(mountainMinLevels, mountainMaxLevels);        
        int k = 0;
        
        int xMin = x - mountainSizeX / 2,
            xMax = x + mountainSizeX,
            zMin = z - mountainSizeZ / 2,
            zMax = z + mountainSizeZ;
        
        xMin = xMin >= 0 ? xMin : 0;
        zMin = zMin >= 0 ? zMin : 0;
        
        while(k < mountainLevels) {            
            x += randomizer.randomIntInRange(-2, 2);
            x = (int) FastMath.clamp(x, xMin, xMax);
            
            z += randomizer.randomIntInRange(-2, 2);
            z = (int) FastMath.clamp(z, zMin, zMax);
            
            mountainSizeX -= randomizer.nextRandomFloat() - .95f > 0 ? 1 : 0;
            if(mountainSizeX < 3) mountainSizeX = 3;
            
            mountainSizeZ -= randomizer.nextRandomFloat() - .95f > 0 ? 1 : 0;
            if(mountainSizeZ < 3) mountainSizeZ = 3;
            
            applyMountainBorder(x, z, mountainSizeX, mountainSizeZ);
            applyMountainLevel(x, z, mountainSizeX, mountainSizeZ);
            
            ++k;
        }
    }
    
    private void applyMountainLevel(int x, int z, int mountainSizeX, int mountainSizeZ) {
        int iLimit = x + mountainSizeX - 1,
            jLimit = z + mountainSizeZ - 1;
        
        for(int i = x + 1; i < iLimit; ++i) {
            for(int j = z + 1; j < jLimit; ++j) {
                applyRandomMountainPoint(i, j);
            }
        }
    }
    
    private void applyMountainBorder(int x, int z, int mountainSizeX, int mountainSizeZ) {
        for(int i = 1; i < mountainSizeX / 2 + 1; ++i) {
            if(randomizer.nextRandomFloat() < mountainBorderFragmentation) {
                mountainBorderPass(x, z, i, 0, mountainSizeX, mountainSizeZ);
            }
        }
        
        for(int j = 1; j < mountainSizeZ / 2 + 1; ++j) {
            if(randomizer.nextRandomFloat() < mountainBorderFragmentation) {
                mountainBorderPass(x, z, 0, j, mountainSizeX, mountainSizeZ);
            }
        }
    }
    
    private void mountainBorderPass(
            int x, int z, 
            int i, int j, 
            int mountainSizeX, int mountainSizeZ) {                
                applyRandomMountainPoint(x + i, z + j);
                
                if(mountainSizeX > 0) 
                    applyRandomMountainPoint(x + mountainSizeX - i, z + j);                       
                
                if(mountainSizeZ > 0)
                    applyRandomMountainPoint(x + i, z + mountainSizeZ - j);
                
                if(mountainSizeX * mountainSizeZ > 0)
                    applyRandomMountainPoint(x + mountainSizeX - i, z + mountainSizeZ - j);
    }
    
    private void applyRandomMountainPoint(int x, int z) {
        heightData[arrayIndex(x, z)] = getPointHeight(x, z) + randomMountainPointHeight();
    }
    
    private void applyPathToHeightmap() {        
        MapTile previousTile = null;
        for(MapTile tile : grid.getPathTiles()) {
            generatePathTile(tile.getPos().getX(), tile.getPos().getY());
            
            if(previousTile != null)
                applyPathConjunction(previousTile, tile);
            
            previousTile = tile;
        }
    }
    
    private void generatePathTile(int x, int z) {
        x += battlegroundOffset.getX();
        z += battlegroundOffset.getY();

        int iStart = x * pathTileSize + pathTileBorder,
            jStart = z * pathTileSize + pathTileBorder,
            iLimit = x * pathTileSize + pathTileSize - pathTileBorder,
            jLimit = z * pathTileSize + pathTileSize - pathTileBorder;
        
        for(int i = iStart; i < iLimit; ++i) {
            for(int j = jStart; j < jLimit; ++j) {
                heightData[arrayIndex(i, j)] = randomPathPointHeight();
            }
        }
    }

    private void generateDitchTile(int x, int z) {
        int iStart = x * pathTileSize,
            jStart = z * pathTileSize,
            iLimit = x * pathTileSize + pathTileSize,
            jLimit = z * pathTileSize + pathTileSize;
        
        for(int i = iStart; i < iLimit; ++i) {
            for(int j = jStart; j < jLimit; ++j) {
                heightData[arrayIndex(i, j)] = randomDitchPointHeight();
            }
        }
    }

    private void generateHillTile(int x, int z) {
        int iStart = x * pathTileSize,
            jStart = z * pathTileSize,
            iLimit = x * pathTileSize + pathTileSize,
            jLimit = z * pathTileSize + pathTileSize;
        
        for(int i = iStart; i < iLimit; ++i) {
            for(int j = jStart; j < jLimit; ++j) {
                heightData[arrayIndex(i, j)] = randomHillPointHeight();
            }
        }
    }

    private void applyPathConjunction(MapTile firstTile, MapTile secondTile) {
        byte angle = firstTile.angleWithTile(secondTile);
        
        switch(angle) {
            case MapTile.TILE_UP:
                applyPathUpConjunction(firstTile); 
                applyPathDownConjunction(secondTile);
                break;
            case MapTile.TILE_DOWN:
                applyPathDownConjunction(firstTile);
                applyPathUpConjunction(secondTile);
                break;
            case MapTile.TILE_LEFT:
                applyPathLeftConjunction(firstTile); 
                applyPathRightConjunction(secondTile);
                break;
            case MapTile.TILE_RIGHT:
                applyPathRightConjunction(firstTile); 
                applyPathLeftConjunction(secondTile);
                break;
            default: System.err.println("ERROR: Unrecognized map tile angle: "
                    + firstTile + "," + secondTile);
        }
    }
    
    private void applyPathUpConjunction(MapTile firstTile) {
        Vector2i firstPos = firstTile.getPos().add(battlegroundOffset);
        
        int iStart = tileToMeshScale(firstPos.getX()),
            iLimit = tileToMeshScale(firstPos.getX()) + pathTileBorder,
                
            jStart = tileToMeshScale(firstPos.getY()) + pathTileBorder + pathTileBorderNeck,
            jLimit = tileToMeshScale(firstPos.getY()) + pathTileSize - pathTileBorder - pathTileBorderNeck;
            
        for(int i = iStart; i < iLimit; ++i) {
            for(int j = jStart; j < jLimit; ++j) {
                heightData[arrayIndex(i, j)] = randomPathPointHeight();
            }
        }
    }
    
    private void applyPathDownConjunction(MapTile firstTile) {
        Vector2i firstPos = firstTile.getPos().add(battlegroundOffset);
        
        int iStart = tileToMeshScale(firstPos.getX()) + pathTileSize - pathTileBorder,
            iLimit = tileToMeshScale(firstPos.getX()) + pathTileSize,
                
            jStart = tileToMeshScale(firstPos.getY()) + pathTileBorder + pathTileBorderNeck,
            jLimit = tileToMeshScale(firstPos.getY()) + pathTileSize - pathTileBorder - pathTileBorderNeck;
            
        for(int i = iStart; i < iLimit; ++i) {
            for(int j = jStart; j < jLimit; ++j) {
                heightData[arrayIndex(i, j)] = randomPathPointHeight();
            }
        }
    }
    
    private void applyPathLeftConjunction(MapTile firstTile) {
        Vector2i firstPos = firstTile.getPos().add(battlegroundOffset);
        
        int iStart = tileToMeshScale(firstPos.getX()) + pathTileBorder + pathTileBorderNeck,
            iLimit = tileToMeshScale(firstPos.getX()) + pathTileSize - pathTileBorder - pathTileBorderNeck,
                
            jStart = tileToMeshScale(firstPos.getY()) + pathTileSize - pathTileBorder,
            jLimit = tileToMeshScale(firstPos.getY()) + pathTileSize;
            
        for(int i = iStart; i < iLimit; ++i) {
            for(int j = jStart; j < jLimit; ++j) {
                heightData[arrayIndex(i, j)] = randomPathPointHeight();
            }
        }  
    }
    
    private void applyPathRightConjunction(MapTile firstTile) {
        Vector2i firstPos = firstTile.getPos().add(battlegroundOffset);
        
        int iStart = tileToMeshScale(firstPos.getX()) + pathTileBorder + pathTileBorderNeck,
            iLimit = tileToMeshScale(firstPos.getX()) + pathTileSize - pathTileBorder - pathTileBorderNeck,
                
            jStart = tileToMeshScale(firstPos.getY()),
            jLimit = tileToMeshScale(firstPos.getY()) + pathTileBorder;
            
        for(int i = iStart; i < iLimit; ++i) {
            for(int j = jStart; j < jLimit; ++j) {
                heightData[arrayIndex(i, j)] = randomPathPointHeight();
            }
        }    
    }
    
    private float getPointHeight(int x, int z) {
        return heightData[arrayIndex(x, z)];
    }
    
    private int tileToMeshScale(int coord) {
        return coord * pathTileSize;
    }
    
    private int meshToTileScale(int coord) {
        return coord / pathTileSize;
    }
    
    private int arrayIndex(int x, int z) {
        return (x * xArrayOffset) + (z);
    }

    private float randomDitchPointHeight() {
        return minDitchHeight + randomizer.nextRandomFloat() * ditchVariation;
    }

    private float randomBattlegroundBorderPointHeight() {
        float balance = randomizer.nextRandomFloat() * .6f;
        return randomDitchPointHeight() * (1f - balance) * .4f + randomHillPointHeight() * balance * .6f;
    }
    
    private float randomPathPointHeight() {
        return randomizer.nextRandomFloat() * pathVariation;
    }
    
    private float randomHillPointHeight() {
        return minHillHeight + randomizer.nextRandomFloat() * hillVariation;
    }
    
    private float randomMountainPointHeight() {
        return randomizer.nextRandomFloat() * mountainVariation;
    }

    @Override
    public boolean load() { 
        grid.generateGrid();
        
        initRawHeightmap();

        generateDitch();
        generateBattleground();
        generateBattlegroundBorder();
        generateMountains();
        
        applyPathToHeightmap();

        return true;
    }

    public LinkedList<Vector2f> getPathPoints() {
        LinkedList<Vector2f> pathPoints = new LinkedList<>();
        int battlegroundSize = gridSize - ditchSize * 2;
        grid.getPathTiles().forEach(tile -> pathPoints.add(new Vector2f(
                               tileToMeshScale(tile.getPos().getX() - battlegroundSize/2),
                               tileToMeshScale(tile.getPos().getY() - battlegroundSize/2))));
        return pathPoints;
    }
}
