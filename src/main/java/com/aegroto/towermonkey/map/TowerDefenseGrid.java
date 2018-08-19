package com.aegroto.towermonkey.map;

import java.util.LinkedList;

import com.aegroto.towermonkey.util.FastRandom;
import com.aegroto.towermonkey.util.Vector2i;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 * Describes the map's grid.
 * 
 * @author aegroto
 */

public class TowerDefenseGrid {
    private static final Vector2i
            SHIFT_UP = new Vector2i(-1, 0),
            SHIFT_DOWN = new Vector2i(1, 0),
            SHIFT_RIGHT = new Vector2i(0, 1),
            SHIFT_LEFT = new Vector2i(0, -1);
    
    private static final int GENESIS_TILES_DISTANCE = 5;
    
    private final int gridSize, snakyness;
    
    @Setter private Vector2i startTilePos, endTilePos;
    @Getter private final MapTile[][] grid;
    
    @Getter private final LinkedList<MapTile> pathTiles;

    private FastRandom randomizer;
   
    /**
     * TowerDefenseGrid constructor
     * 
     * @param gridSize Grid size
     * @param snakyness Path's "snakyness", the higher it is, the less linear the path will be
     * @param seed Pseudorandom seed
     * 
     * @author aegroto
     */

    public TowerDefenseGrid(int gridSize, int snakyness, long seed) {
        this.gridSize = gridSize;
        this.snakyness = snakyness;
        grid = new MapTile[gridSize][gridSize];
        pathTiles = new LinkedList<>();
        randomizer = new FastRandom(seed);
    }

    /**
     * TowerDefenseGrid constructor which uses a random choosen seed
     * 
     * @param gridSize Grid size
     * @param snakyness Path's "snakyness", the higher it is, the less linear the path will be
     * 
     * @author aegroto
     */
    public TowerDefenseGrid(int gridSize, int snakyness) {
        this(gridSize, snakyness, 0);
    }
    
    /** 
     * 
     * Generates the grid with random start and end tiles,
     * should not be called manually.
     *
     * @author aegroto
     */

    public void generateGrid() {
        Vector2i sTile = randomSideTile(null, 1);
        Vector2i eTile = randomSideTile(sTile, GENESIS_TILES_DISTANCE);
        
        generateGrid(sTile, eTile);
    }
    
    /** 
     * 
     * Generates the grid, should not be called manually.
     * 
     * @param startTilePos Start tile position
     * @param endTilePos End tile position
     * 
     * @author aegroto
     */

    public void generateGrid(final Vector2i startTilePos, final Vector2i endTilePos) {
        this.startTilePos = startTilePos;
        this.endTilePos = endTilePos;                        
        
        addTileToPath(placeTile(startTilePos, MapTile.TYPE_START));
        placeTile(endTilePos, MapTile.TYPE_END);        
        
        Vector2i lastTilePos = doSnakySteps(); 
        
        fillMidTiles(lastTilePos, endTilePos);
        
        addTileToPath(getTile(endTilePos));
    }

    /**
     * 
     * Applies snaky steps to the path, check the paper
     * for more informations.
     * 
     * @author aegroto
     */
    
    private Vector2i doSnakySteps() {
        Vector2i currentTilePos = startTilePos.clone(), 
                 lastShift = null;
        
        int snakySteps = 0;
        float tuckProbability = 1f;        
        
        while(snakySteps < snakyness) {
            if(randomizer.nextRandomFloat() <= tuckProbability
               || isPosOutOfRange(currentTilePos.add(lastShift))) {
                lastShift = randomPosShift(currentTilePos, lastShift);
                tuckProbability = 0f;
            } else {
                tuckProbability += ((float) snakySteps)/snakyness;
            }
            
            currentTilePos.addLocal(lastShift);
            
            MapTile tile = placeTileIfEmpty(currentTilePos);
            
            if(tile == null) {
                tile = getTile(currentTilePos);
            }
            
            addTileToPath(tile);
            
            ++snakySteps;
        }
        
        return currentTilePos;
    }

    /**
     * 
     * Checks if given position is out of Grid's matrix range.
     * 
     * @param pos Position to check
     * 
     * @author aegroto 
     */
    
    private boolean isPosOutOfRange(Vector2i pos) {
        return pos.getX() < 0 || pos.getX() >= gridSize || pos.getY() < 0 || pos.getY() >= gridSize;
    }

    /**
     * 
     * Checks if given position is near enough end tile.
     * 
     * @param pos Position to check
     * @param minDistance Distance to be considered
     * 
     * @author aegroto 
     */

    private boolean isPosNearEndTile(Vector2i pos, int minDistance) {
        return pos.distanceFrom(endTilePos) < minDistance;
    }
   
    /**
     * 
     * Fills tiles between two tiles given, check paper for
     * more information on the algorithm used.
     * 
     * @author aegroto
     */

    private void fillMidTiles(final Vector2i firstTilePos, final Vector2i secondTilePos) {
        int distance = firstTilePos.distanceFrom(secondTilePos);
        
        if(distance > 1) {
            int minX, maxX, minY, maxY;
            
            if(firstTilePos.getX() <= secondTilePos.getX()) {
                minX = firstTilePos.getX();
                maxX = secondTilePos.getX();
            } else {
                minX = secondTilePos.getX();
                maxX = firstTilePos.getX();
            }
            
            if(firstTilePos.getY() <= secondTilePos.getY()) {
                minY = firstTilePos.getY();
                maxY = secondTilePos.getY();
            } else {
                minY = secondTilePos.getY();
                maxY = firstTilePos.getY();
            }
            
            Vector2i midTile = new Vector2i(
                    randomizer.randomIntInRange(minX, maxX),
                    randomizer.randomIntInRange(minY, maxY));        
            
            placeTileIfEmpty(midTile);
            
            fillMidTiles(firstTilePos, midTile);
            fillMidTiles(midTile, secondTilePos);            
        } else if (distance == 1) {
            MapTile firstTile = getTile(firstTilePos);
            MapTile secondTile = getTile(secondTilePos); 

            addTileToPath(firstTile);            
            addTileToPath(secondTile);
        }
    }
   
    /**
     * Getter for a map tile in a given position.
     * 
     * @param x Tile's X coordinate
     * @param y Tile's Y coordinate
     * 
     * @author aegroto
     */

    public MapTile getTile(int x, int y) {
        if(x < gridSize && y < gridSize && x >= 0 && y >= 0)
            return grid[x][y];
        
        return null;
    }

    /**
     * Getter for a map tile in a given position.
     * 
     * @param pos Tile's coordinates
     * 
     * @author aegroto
     */

    public MapTile getTile(Vector2i pos) {
        return getTile(pos.getX(), pos.getY());
    }

    /**
     * Places a tile of given type to a given position.
     * 
     * @param tilePos Tile's coordinates
     * @param tileType Tile's type
     */
    
    private MapTile placeTile(Vector2i tilePos, byte tileType) {
        MapTile newMapTile = new MapTile(tilePos, tileType);
        grid[tilePos.getX()][tilePos.getY()] = newMapTile;
        return newMapTile;
    }

    /**
     * Places a tile of given type to a given position (if it's empty).
     * 
     * @param tilePos Tile's coordinates
     * @param tileType Tile's type
     */

    private MapTile placeTileIfEmpty(Vector2i tilePos) {
        MapTile tile = getTile(tilePos);

        if(tile == null) {
            return placeTile(tilePos.clone(), MapTile.TYPE_PATH);
        }
        
        return null;
    }
   
    /**
     * Adds tiles to path's tile list.
     * 
     * @param tile Tile to be added
     * 
     * @author aegroto
     */

    private void addTileToPath(MapTile tile) {
        if(tile != null) {
            if(pathTiles.isEmpty() || pathTiles.getLast() != tile)
                pathTiles.add(tile);
        }
    }
   
    /**
     * Returns random shifted position, used for path generation,
     * check paper for more informations.
     * 
     * @param pos Tile current coordinates.
     * @param lastShift Last shift applied to position, used to avoid inversions.
     * 
     * @author aegroto
     */

    private Vector2i randomPosShift(Vector2i pos, Vector2i lastShift) {
        float randomFactor = randomizer.nextRandomFloat();
        
        if(randomFactor < .25f && canDownShift(pos, lastShift)) {
            return SHIFT_DOWN;
        } else if(randomFactor < .5f && canUpShift(pos, lastShift)) {
            return SHIFT_UP;
        } else if(randomFactor < .75f && canLeftShift(pos, lastShift)) {
            return SHIFT_LEFT;
        } else if (canRightShift(pos, lastShift)) {
            return SHIFT_RIGHT;
        } else return randomPosShift(pos, lastShift);
    }

    /**
     * 
     * Checks if given position can down shift.
     * 
     * @param pos Tile current coordinates.
     * @param lastShift Last shift applied to position, used to avoid inversions.
     * 
     * @author aegroto
     * 
     */
    
    private boolean canDownShift(Vector2i pos, Vector2i lastShift) {        
        return pos.getX() < gridSize - 1 && !SHIFT_UP.equals(lastShift);
    }
    
    /**
     * 
     * Checks if given position can up shift.
     * 
     * @param pos Tile current coordinates.
     * @param lastShift Last shift applied to position, used to avoid inversions.
     * 
     * @author aegroto
     * 
     */

    private boolean canUpShift(Vector2i pos, Vector2i lastShift) {        
        return pos.getX() > 0 && !SHIFT_DOWN.equals(lastShift);
    }

    /**
     * 
     * Checks if given position can right shift.
     * 
     * @param pos Tile current coordinates.
     * @param lastShift Last shift applied to position, used to avoid inversions.
     * 
     * @author aegroto
     * 
     */

    private boolean canRightShift(Vector2i pos, Vector2i lastShift) {
        return pos.getY() < gridSize - 1 && !SHIFT_LEFT.equals(lastShift);
    }

    /**
     * 
     * Checks if given position can left shift.
     * 
     * @param pos Tile current coordinates.
     * @param lastShift Last shift applied to position, used to avoid inversions.
     * 
     * @author aegroto
     * 
     */

    private boolean canLeftShift(Vector2i pos, Vector2i lastShift) {        
        return pos.getY() > 0 && !SHIFT_RIGHT.equals(lastShift);
    }
   
    /**
     * Returns a random tile position, anchored to the Grid's limits.
     * 
     * @param referenceTile Last spawned tile, used to force minimum distance
     * @param minDistance Minimum distance from last spawned tile to be respected
     * 
     * @author aegroto
     */

    private Vector2i randomSideTile(Vector2i referenceTile, int minDistance) {
        if(referenceTile == null)
            referenceTile = new Vector2i(-minDistance - 1, -minDistance - 1);

        final float sideRandomFactor = randomizer.nextRandomFloat();
        final int randomOffset = randomizer.randomIntInRange(0, gridSize - 1);
        Vector2i coords = null;

        do {
            if(sideRandomFactor <= .25f) {
                coords = new Vector2i(0, randomOffset);
            } else if(sideRandomFactor <= .5f) {
                coords =  new Vector2i(randomOffset, 0);
            } else if(sideRandomFactor <= .75f) {
                coords = new Vector2i(gridSize - 1, randomOffset);
            } else {
                coords = new Vector2i(randomOffset, gridSize - 1);
            }
        } while(coords.distanceFrom(referenceTile) < minDistance--);

        return coords;
    }

    /**
     * 
     * Checks if given position contains a path tile.
     * 
     * @param x X coordinate
     * @param y Y coordinate
     * 
     * @author aegroto
     */
    
    public boolean isPathTile(int x, int y) {
        MapTile tile = getTile(x, y);
                
        return tile != null && (tile.getType() == MapTile.TYPE_PATH
                  || tile.getType() == MapTile.TYPE_START
                  || tile.getType() == MapTile.TYPE_END);
    }

    /**
     * Prints grid, mostly useful for debug purposes.
     * 
     * @author aegroto
     */

    public void printGrid() {        
        for(int x = 0; x < gridSize; ++x) {
            StringBuilder line = new StringBuilder();
            for(int y = 0; y < gridSize; ++y) {
                line.append("[").append(grid[x][y]).append("] ");
            }
            System.out.println(line);
        }
    }

    /**
     * Prints path, mostly useful for debug purposes.
     * 
     * @author aegroto
     */

    public void printPath() {
        System.out.println("PATH: " + pathTiles);
    }
}
