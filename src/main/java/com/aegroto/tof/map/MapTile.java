/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aegroto.tof.map;

import com.aegroto.tof.utils.Vector2i;
import lombok.Getter;

/**
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
    // @Getter @Setter private MapTile previousTile;

    MapTile(Vector2i pos, byte type) {
        this.pos = pos;
        this.type = type;
    }
    
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
    
    @Override
    public String toString() {
        return "{Tile " + type + ", " + pos + "}";
    }
}
