package com.aegroto.towermonkey.util;

import java.util.Random;

public class FastRandom {
    private Random random;

    public FastRandom(long seed) {
        if(seed > 0)
            random = new Random(seed);
        else
            random = new Random();
    }

    public float nextRandomFloat() {
        return random.nextFloat();
    }

    public int randomIntInRange(int min, int max) {
        return min + (int) ((max - min + 1) * random.nextFloat());
    }
}