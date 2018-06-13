/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aegroto.towermonkey.utils;

import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author lorenzo
 */
public class Vector2i implements Cloneable {
    @Getter @Setter private int x, y;
    
    public Vector2i(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    public int distanceFrom(Vector2i otherVec) {
        int xDist = x - otherVec.x, yDist = y - otherVec.y;

        if(xDist < 0) xDist = -xDist;
        if(yDist < 0) yDist = -yDist;
        
        return xDist + yDist;
    }
    
    public Vector2i add(Vector2i otherVec) {
        return new Vector2i(x + otherVec.x, y + otherVec.y);
    }

    public Vector2i add(int ax, int ay) {
        return new Vector2i(x + ax, y + ay);
    }
    
    public Vector2i addLocal(Vector2i otherVec) {
        x += otherVec.x;
        y += otherVec.y;
        return this;
    }
    
    public boolean equals(Vector2i otherVec) {
        return otherVec != null && x == otherVec.x && y == otherVec.y;
    }
    
    public boolean isInverse(Vector2i otherVec) {
        return (x == otherVec.x && y == -otherVec.y) || (x == -otherVec.x && y == otherVec.y);
    }
    
    @Override
    public String toString() {
        return "(" + x + "," + y + ")";
    }
    
    @Override
    public Vector2i clone() {
        try {
            return (Vector2i) super.clone();
        } catch(CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
