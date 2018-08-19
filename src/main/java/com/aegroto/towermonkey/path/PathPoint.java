package com.aegroto.towermonkey.path;

import com.jme3.math.Vector2f;

import lombok.Getter;
import lombok.Setter;

public class PathPoint {
    protected final Vector2f position;
    @Getter @Setter protected PathPoint nextPathPoint;

    public PathPoint(Vector2f position) {
        this.position = position;
    }
}