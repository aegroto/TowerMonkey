void main(){
    // vec3 coords = vVertex;
    // texCoord = coords.xz;

    vec4 triplanarColor1, triplanarColor2, triplanarColor3;
    // color1 = color2 = color3 = triplanarColor1 = triplanarColor2 = triplanarColor3 = vec4(0.0, 0.0, 0.0, 1.0);

    if(vVertex.y <= PathMaxHeight) {
        #ifdef TRIPLANAR_MAPPING
            triplanarColor1 = texture2D( PathTex, vVertex.yz * PathTexScale );
            triplanarColor2 = texture2D( PathTex, vVertex.xz * PathTexScale );
            triplanarColor3 = texture2D( PathTex, vVertex.xy * PathTexScale );

            // blend the results of the 3 planar projections.
            color1 = triplanarColor1 * blending.x + triplanarColor2 * blending.y + triplanarColor3 * blending.z;
        #else        
            color1 = texture2D(PathTex, vVertex.xz * PathTexScale);
        #endif
    } 

    if(vVertex.y >= HillMinHeight && vVertex.y <= HillMaxHeight) {
        #ifdef TRIPLANAR_MAPPING
            triplanarColor1 = texture2D( HillTex, vVertex.yz * HillTexScale );
            triplanarColor2 = texture2D( HillTex, vVertex.xz * HillTexScale );
            triplanarColor3 = texture2D( HillTex, vVertex.xy * HillTexScale );

            // blend the results of the 3 planar projections.
            color2 = triplanarColor1 * blending.x + triplanarColor2 * blending.y + triplanarColor3 * blending.z;
        #else
            color2 = texture2D(HillTex, vVertex.xz * HillTexScale);
        #endif
    }
 

    if(vVertex.y >= MountainMinHeight) {
        #ifdef TRIPLANAR_MAPPING
            triplanarColor1 = texture2D( MountainTex, vVertex.yz * MountainTexScale );
            triplanarColor2 = texture2D( MountainTex, vVertex.xz * MountainTexScale );
            triplanarColor3 = texture2D( MountainTex, vVertex.xy * MountainTexScale );

            // blend the results of the 3 planar projections.
            color3 = triplanarColor1 * blending.x + triplanarColor2 * blending.y + triplanarColor3 * blending.z;
        #else
            color3 = texture2D(MountainTex, vVertex.xz * MountainTexScale);
        #endif
    }
}

