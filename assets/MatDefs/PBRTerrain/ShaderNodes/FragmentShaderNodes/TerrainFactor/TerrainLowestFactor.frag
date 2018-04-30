void main(){
    if(vVertex.y <= LayerMaxHeight) {
        if(vVertex.y >= NextLayerMinHeight) {
            factor = 1.0 - ((vVertex.y - NextLayerMinHeight) / (LayerMaxHeight - NextLayerMinHeight));
        } else factor = 1.0;

        #ifdef TRIPLANAR_MAPPING
            color = texture2D(Tex, vVertex.yz * TexScale) * blending.x +
                    texture2D(Tex, vVertex.xz * TexScale) * blending.y +
                    texture2D(Tex, vVertex.xy * TexScale) * blending.z;
        #else        
            color = texture2D(Tex, vVertex.xz * TexScale);
        #endif
    } else {
        factor = 0.0;
        color = vec4(0.0, 0.0, 0.0, 1.0);
    }
}