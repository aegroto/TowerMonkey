package com.aegroto.towermonkey.map;

import com.aegroto.towermonkey.util.Vector2i;
import lombok.Getter;

/**
 *
 * Map tiles are used by TowerDefenseGrid to identify
 * and describing a single map block, including
 * its position and its type.
 *  
 * @author lorenzo
 */

public class MapTile {
    public static final byte
        TYPE_PATH = 0,
        TYPE_START = 1,
        TYPE_END = 2,
        TYPE_BUILDING = 3,
            
        TILE_UP = 0,
        TILE_DOWN  = 1,
        TILE_RIGHT = 2,
        TILE_LEFT = 3;
    
    @Getter private final byte type;
    @Getter private final Vector2i pos;

    /**
     * 
     * MapTile constructor.
     * 
     * @param pos Tile's position
     * @param type Tile's type (check MapTile's TILE_* fields)
     * 
     * @author aegroto
     */

    MapTile(Vector2i pos, byte type) {
        this.pos = pos;
        this.type = type;
    }

    /**
     * Returns a byte identifying how the parameter
     * is oriented respect to the tile the method
     * is called on.
     * 
     * @param otherTile Tile of which to check orientation
     * 
     * @author aegroto 
     */

    public byte angleWithTile(MapTile otherTile) {
        if(pos.getY() == otherTile.pos.getY()) {
            if(pos.getX() == otherTile.pos.getX() + 1) {
                return TILE_UP;
            } else if(pos.getX() == otherTile.pos.getX() - 1) {
                return TILE_DOWN;
            } 
        }
        
        if(pos.getX() == otherTile.pos.getX()) {
            if(pos.getY() == otherTile.pos.getY() + 1) {
                return TILE_RIGHT;
            } else if(pos.getY() == otherTile.pos.getY() - 1) {
                return TILE_LEFT;
            }
        }
        return -1;
    }
   
    /**
     * 
     * String conversion method for MapTile,
     * mostly useful for debug purposes.
     * 
     * @author aegroto
     */
    @Override
    public String toString() {
        return "{Tile " + type + ", " + pos + "}";
    }
}
