package com.aegroto.towermonkey.path;

import com.jme3.math.Vector3f;

import lombok.Getter;
import lombok.Setter;

public class PathPoint {
    @Getter protected final Vector3f position;
    @Getter @Setter protected PathPoint nextPathPoint;

    public PathPoint(Vector3f position) {
        this.position = position;
    }
}