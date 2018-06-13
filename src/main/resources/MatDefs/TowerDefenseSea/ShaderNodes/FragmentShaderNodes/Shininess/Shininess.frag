#import "ShaderLib/TextureInterlace.glsllib"

void main(){
    #if defined(SPECULAR_MAPPING)
        // shininess = texture2D(SpecularMapTex, texCoord * TexScale).r;
        shininess = interlaceTexture(SpecularMapTex, texCoord, TexScale, randomSeed, InterlacingFactor).r;
    #else
        shininess = 1.0;
    #endif

    shininess *= fixedShininess;
}
