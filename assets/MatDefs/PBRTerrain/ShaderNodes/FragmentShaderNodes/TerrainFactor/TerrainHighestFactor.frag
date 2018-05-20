#import "ShaderLib/TriplanarMapping.glsllib"

void main(){
    if(vVertex.y >= LayerMinHeight) {
        if(vVertex.y <= PrevLayerMaxHeight) {
            factor = ((vVertex.y - LayerMinHeight) / (PrevLayerMaxHeight - LayerMinHeight));
        } else { 
            factor = 1.0;
        }

        #ifdef TRIPLANAR_MAPPING
            color = triplanarColor(Tex, vVertex, TexScale, blending);
        #else        
            color = texture2D(Tex, texCoord * TexScale);
        #endif
    } else {
        factor = 0.0;
        color = vec4(0.0, 0.0, 0.0, 1.0);
    }
}
