#import "ShaderLib/TriplanarMapping.glsllib"

void main(){
    #if defined(SPECULAR_MAPPING)
        #if defined(TRIPLANAR_MAPPING)
            shininess = triplanarColor(SpecularMapTex, vVertex, TexScale, blending).r;
        #else
            shininess = texture2D(SpecularMapTex, texCoord * TexScale).r;
        #endif
    #else
        shininess = 1.0;
    #endif

    shininess *= fixedShininess;
}
