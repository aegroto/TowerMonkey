#import "ShaderLib/TextureInterlace.glsllib"

void main(){
    #if defined(NORMAL_MAPPING)
        // normal = normalize((texture2D(NormalMapTex, texCoord * TexScale).xyz * vec3(2.0,-2.0,2.0) - vec3(1.0,-1.0,1.0)));
        normal = normalize(interlaceTexture(NormalMapTex, texCoord, TexScale, randomSeed, InterlacingFactor).rgb * vec3(2.0,-2.0,2.0) - vec3(1.0,-1.0,1.0));
    #else
        normal = vNormal;
    #endif
}
