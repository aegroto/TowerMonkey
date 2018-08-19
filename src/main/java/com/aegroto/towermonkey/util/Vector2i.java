package com.aegroto.towermonkey.util;

import lombok.Getter;
import lombok.Setter;

/**
 *
 * A vector containing 2 integers.
 * 
 * @author aegroto 
 * 
 */

public class Vector2i implements Cloneable {
    @Getter @Setter private int x, y;
   
    /**
     * 
     * Vector2i constructor.
     * 
     * @param x X
     * @param y Y
     * 
     * @author aegroto
     */

    public Vector2i(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    /**
     * 
     * Calculates and returns distance from the given Vector2i.
     * 
     * @param otherVec Vector2i to calculate the distance from.
     * 
     * @return Distance from the given Vector2i.
     * 
     * @author aegroto.
     */

    public int distanceFrom(Vector2i otherVec) {
        int xDist = x - otherVec.x, yDist = y - otherVec.y;

        if(xDist < 0) xDist = -xDist;
        if(yDist < 0) yDist = -yDist;
        
        return xDist + yDist;
    }
    
    /**
     * Returns the sum of the current vector and the given vector.
     * 
     * @param otherVec Vector to sum.
     * 
     * @return Sum of the two vectors.
     * 
     * @author aegroto
     * 
     */

    public Vector2i add(Vector2i otherVec) {
        return new Vector2i(x + otherVec.x, y + otherVec.y);
    }

    /**
     * Returns the sum of the current vector and the given vector.
     * 
     * @param ax X to sum.
     * @param ay Y to sum.
     * 
     * @return Sum of the two vectors.
     * 
     * @author aegroto
     * 
     */

    public Vector2i add(int ax, int ay) {
        return new Vector2i(x + ax, y + ay);
    }

    /**
     * 
     * Sums the given vector's components to the current vector.
     * 
     * @param otherVec Vector to sum.
     * 
     * @return Current vector.
     * 
     * @author aegroto
     *  
     */
    
    public Vector2i addLocal(Vector2i otherVec) {
        x += otherVec.x;
        y += otherVec.y;
        return this;
    }

    /**
     * 
     * Checks if the current vector is equal to the given one.
     * 
     * @param otherVec Vector to be compared.
     * 
     * @author aegroto
     * 
     */
    
    public boolean equals(Vector2i otherVec) {
        return otherVec != null && x == otherVec.x && y == otherVec.y;
    }

    /**
     * 
     * Checks if the current vector has a component which is equal
     * and a component which is opposite respect to the given 
     * vector's components.
     * 
     * @param otherVec Vector to be compared.
     * 
     * @author aegroto
     * 
     */

    public boolean isInverse(Vector2i otherVec) {
        return (x == otherVec.x && y == -otherVec.y) || (x == -otherVec.x && y == otherVec.y);
    }

    /** 
     * 
     * Converts vector to string
     * 
     * @return String representing the vector.
     * 
     * @author aegroto
     * 
     */
    
    @Override
    public String toString() {
        return "(" + x + "," + y + ")";
    }
    
    /**
     * 
     * Clones the current vector.
     * 
     * @return Cloned current vector.
     * 
     * @author aegroto
     * 
     */

    @Override
    public Vector2i clone() {
        try {
            return (Vector2i) super.clone();
        } catch(CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
