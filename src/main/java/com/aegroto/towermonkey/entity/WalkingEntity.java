package com.aegroto.towermonkey.entity;

import com.aegroto.towermonkey.path.PathPoint;
import com.jme3.asset.AssetManager;
import com.jme3.math.Vector3f;

public abstract class WalkingEntity extends Entity {
    private final float POINT_STEPS_THRESHOLD = .96f;

    protected PathPoint currentPathPoint, nextPathPoint;

    protected float currentPointSteps;
    protected final float walkingSpeed;

    public WalkingEntity(AssetManager assetManager, float walkingSpeed) {
        super(assetManager);

        this.walkingSpeed = walkingSpeed;
    }

    public void initializePathWalk(PathPoint headPathPoint) {
        currentPathPoint = headPathPoint;
        nextPathPoint = headPathPoint.getNextPathPoint();
        currentPointSteps = 0f;

        setLocalTranslation(currentPathPoint.getPosition());
    }

    public void move(float moveQuantity) {
        if(nextPathPoint != null) {
            currentPointSteps += moveQuantity;

            if(currentPointSteps >= POINT_STEPS_THRESHOLD) {
                currentPointSteps = 0f;

                setLocalTranslation(nextPathPoint.getPosition());
                currentPathPoint = nextPathPoint;
                nextPathPoint = nextPathPoint.getNextPathPoint();
            } else {
                Vector3f newPos = currentPathPoint.getPosition().clone();
                setLocalTranslation(newPos.interpolateLocal(newPos, nextPathPoint.getPosition(), currentPointSteps));
            }
        } else {
            onPathEnd();
        }
    }

    public void update(float tpf) {
        move(walkingSpeed * tpf);
    }

    protected abstract void onPathEnd();
}