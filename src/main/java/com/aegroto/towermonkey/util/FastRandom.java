package com.aegroto.towermonkey.util;

import java.util.Random;

/**
 * 
 * Fast random utility class, used as a sort of adapter
 * to provide a simpler interface to clients.
 * 
 * @author aegroto
 * 
 */

public class FastRandom {
    private Random random;

    /**
     * 
     * FastRandom constructor.
     * 
     * @param seed Pseudorandom seed
     * 
     * @author aegroto
     * 
     */

    public FastRandom(long seed) {
        if(seed > 0)
            random = new Random(seed);
        else
            random = new Random();
    }

    /**
     * 
     * Returns a random float.
     * 
     * @return Next random float number.
     * 
     * @author aegroto
     */

    public float nextRandomFloat() {
        return random.nextFloat();
    }

    /**
     * 
     * Return a random int in the given range.
     * 
     * @return Random int in a given range.
     * 
     * @author aegroto
     * 
     */

    public int randomIntInRange(int min, int max) {
        return min + (int) ((max - min + 1) * random.nextFloat());
    }
}