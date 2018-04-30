void main(){
    vec3 coords = vVertex;
    texCoord = coords.xz;

    vec4 triplanarColor1, triplanarColor2, triplanarColor3;
    color1 = color2 = color3 = triplanarColor1 = triplanarColor2 = triplanarColor3 = vec4(0.0, 0.0, 0.0, 1.0);

    /*color1Factor = color2Factor = color3Factor = 1.0;

    if(coords.y >= HillMinHeight) {
        color1Factor -= ((coords.y - HillMinHeight) / (PathMaxHeight - HillMinHeight));
    }

    if(coords.y <= PathMaxHeight) {
        color2Factor = 1.0 - color1Factor;
    } else if(coords.y >= MountainMinHeight) {
        color2Factor -= ((coords.y - MountainMinHeight) / (HillMaxHeight - MountainMinHeight));
    }

    if(coords.y <= HillMaxHeight) {
        color3Factor = 1.0 - color2Factor;
    }

    float pseudoRandomValue = fract((coords.x + coords.z) / 1000.0);

    if(coords.y >= HillMinHeight && coords.y <= PathMaxHeight) {
        color2Factor -= pseudoRandomValue;
        color1Factor += pseudoRandomValue;
    }

    if(coords.y >= MountainMinHeight && coords.y <= HillMaxHeight) {
        color3Factor -= pseudoRandomValue;
        color2Factor += pseudoRandomValue;
    } */

    if(coords.y <= PathMaxHeight) {
        #ifdef TRIPLANAR_MAPPING
            triplanarColor1 = texture2D( PathTex, coords.yz * PathTexScale );
            triplanarColor2 = texture2D( PathTex, coords.xz * PathTexScale );
            triplanarColor3 = texture2D( PathTex, coords.xy * PathTexScale );

            // blend the results of the 3 planar projections.
            color1 = triplanarColor1 * blending.x + triplanarColor2 * blending.y + triplanarColor3 * blending.z;
        #else        
            color1 = texture2D(PathTex, coords.xz * PathTexScale);
        #endif
    } 

    if(coords.y >= HillMinHeight && coords.y <= HillMaxHeight) {
        #ifdef TRIPLANAR_MAPPING
            triplanarColor1 = texture2D( HillTex, coords.yz * HillTexScale );
            triplanarColor2 = texture2D( HillTex, coords.xz * HillTexScale );
            triplanarColor3 = texture2D( HillTex, coords.xy * HillTexScale );

            // blend the results of the 3 planar projections.
            color2 = triplanarColor1 * blending.x + triplanarColor2 * blending.y + triplanarColor3 * blending.z;
        #else
            color2 = texture2D(HillTex, coords.xz * HillTexScale);
        #endif
    }
 

    if(coords.y >= MountainMinHeight) {
        #ifdef TRIPLANAR_MAPPING
            triplanarColor1 = texture2D( MountainTex, coords.yz * MountainTexScale );
            triplanarColor2 = texture2D( MountainTex, coords.xz * MountainTexScale );
            triplanarColor3 = texture2D( MountainTex, coords.xy * MountainTexScale );

            // blend the results of the 3 planar projections.
            color3 = triplanarColor1 * blending.x + triplanarColor2 * blending.y + triplanarColor3 * blending.z;
        #else
            color3 = texture2D(MountainTex, coords.xz * MountainTexScale);
        #endif
    }
}

