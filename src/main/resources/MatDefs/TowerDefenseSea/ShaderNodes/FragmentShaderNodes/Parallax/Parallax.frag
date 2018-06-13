#import "ShaderLib/ParallaxMapping.glsllib"
#import "ShaderLib/TextureInterlace.glsllib"

void main(){
    #if defined(PARALLAX_MAPPING)
        #if defined(STEEP_PARALLAX)
            texCoordParallax = steepParallaxOffset(parallaxMapTex, texScale, vViewDirParallax, texCoord, parallaxHeight);
        #else 
            // texCoordParallax = interlacedParallaxOffset(parallaxMapTex, texScale, vViewDirParallax, texCoord, parallaxHeight, randomSeed, interlacingFactor);
            float h = interlaceTexture(parallaxMapTex, texCoord, texScale, randomSeed, InterlacingFactor).r;
            float heightBias = parallaxHeight * 0.5;
            vec3 normalizedViewDir = normalize(vViewDirParallax); 
            h = (h * parallaxHeight + heightBias) * normalizedViewDir.z;
            texCoordParallax = texCoord + (h * normalizedViewDir.xy);
        #endif
    #else      
        texCoordParallax = texCoord;
    #endif
}