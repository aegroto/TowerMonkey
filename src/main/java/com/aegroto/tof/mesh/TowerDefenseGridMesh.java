/*
 * Copyright (c) 2009-2012 jMonkeyEngine
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * * Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 *
 * * Neither the name of 'jMonkeyEngine' nor the names of its contributors
 *   may be used to endorse or promote products derived from this software
 *   without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.aegroto.tof.mesh;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

import com.jme3.util.BufferUtils;
import com.jme3.scene.Mesh;
import com.jme3.scene.VertexBuffer.Type;

/**
 * Simple grid shape.
 * 
 * @author Kirill Vainer
 */
public class TowerDefenseGridMesh extends Mesh {
    private short xSize, zSize;
    private float quadSize;

    private FloatBuffer positions, texCoords, normals;
    private IntBuffer indices;
    
    public TowerDefenseGridMesh(short xSize, short zSize, float quadSize) {
        this.xSize = xSize;
        this.zSize = zSize;
        this.quadSize = quadSize;

        updateGeometry();
    }

    public void updateGeometry() {
        positions = BufferUtils.createFloatBuffer(xSize * zSize * 16);
        texCoords = BufferUtils.createFloatBuffer(xSize * zSize * 8);
        normals = BufferUtils.createFloatBuffer(xSize * zSize * 12);
        indices = BufferUtils.createIntBuffer(xSize * zSize * 6);

        addVertices();
        addTexCoords();
        addNormals();
        addIndices();

        setBuffer(Type.Position, 3, positions);
        setBuffer(Type.TexCoord, 2, texCoords);
        setBuffer(Type.Normal,   3, normals);
        setBuffer(Type.Index,    3, indices);

        updateBound();
        setStatic();
    }    

    private void addVertices() {
        for(short x = 0; x <= xSize; ++x) {
            for(short z = 0; z <= zSize; ++z) {
                positions.put(new float[] { x * quadSize, 0, z * quadSize} );
            }
        }
    }

    private void addTexCoords() {
        for(short i = 0; i < xSize * zSize; ++i)
            texCoords.put( new float[] {
                1, 0, 0, 0, 0, 1, 1, 1,            
            });
    }

    private void addNormals() {
        float[] quadNormal = new float[] { 0, 1, 0 };
        for(short i = 0; i < xSize * zSize; ++i)
            normals.put(quadNormal);
    }


    private void addIndices() {
        short offset = 0;

        for(short x = 0; x < xSize; ++x) {
            for(short z = 0; z < zSize; ++z) {
                offset = (short) (x * (zSize + 1) + z);
                indices.put(new int[] { 
                    offset,  (short) (offset + 1),          (short) (offset + zSize + 2),
                    offset,  (short) (offset + zSize + 2),  (short) (offset + zSize + 1),
                });
            }
        }
    }

    public void addZQuad(short z) {
        float zOffset = z * quadSize;
        short indexOffset = (short) (z * 3);

        // Adding vertices
        positions.put(new float[]{  0,  0, zOffset + quadSize,
                                    zOffset + quadSize,  0, quadSize
        });

        // Adding Tex coords
        texCoords.put( new float[]{ z + 1, z,
                                    z + 1, z + 1                                    
        });

        // Adding normals
        normals.put(new float[]{ 0, 1, 0,
                                 0, 1, 0,
                                 0, 1, 0,
                                 0, 1, 0
        });

        // Adding vertices order
        indices.put(new int[]{ z, (short) (indexOffset + 2), (short) (indexOffset + 1),
                                 z, (short) (z + 1),           (short) (indexOffset + 2)
        });
    }

    public void addGenesisQuad() {
        // Adding vertices
        positions.put(new float[]{  0,         0,        0,
                                    quadSize,  0,        0,
                                    quadSize,  0,        quadSize,
                                    0,         0,        quadSize
        });

        // Adding Tex coords
        texCoords.put( new float[]{ 0, 0,
                                    1, 0,
                                    1, 1,
                                    0, 1
        });

        // Adding normals
        normals.put(new float[]{ 0, 1, 0,
                                 0, 1, 0,
                                 0, 1, 0,
                                 0, 1, 0
        });

        // Adding vertices order
        indices.put(new int[]{ 0, 2, 1,
                                 0, 3, 2
        });
    }
}
