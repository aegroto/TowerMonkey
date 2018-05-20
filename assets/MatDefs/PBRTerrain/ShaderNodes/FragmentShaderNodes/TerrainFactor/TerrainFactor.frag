#import "ShaderLib/TriplanarMapping.glsllib"

void main(){
    if(vPosition.y >= LayerMinHeight && vPosition.y <= LayerMaxHeight) {
        if(vPosition.y <= PrevLayerMaxHeight) {
            factor = ((vPosition.y - LayerMinHeight) / (PrevLayerMaxHeight - LayerMinHeight));
        } else if(vPosition.y >= NextLayerMinHeight) {
            factor = 1.0 - ((vPosition.y - NextLayerMinHeight) / (LayerMaxHeight - NextLayerMinHeight));
        } else factor = 1.0;

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
