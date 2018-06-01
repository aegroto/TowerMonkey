#import "ShaderLib/TriplanarMapping.glsllib"

void main(){
    #if defined(SPECULAR_MAPPING)
        shininess = texture2D(SpecularMapTex, texCoord * TexScale).r;
    #else
        shininess = 1.0;
    #endif

    shininess *= fixedShininess;
}
