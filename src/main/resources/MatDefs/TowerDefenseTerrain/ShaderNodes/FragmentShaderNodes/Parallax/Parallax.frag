#import "ShaderLib/ParallaxMapping.glsllib"

#ifndef TP_FRAG_H
    #define TP_FRAG_H 1
#endif

void main(){
    #if defined(PARALLAX_MAPPING)
        #if defined(TRIPLANAR_MAPPING)
            vVertexParallax = vVertex; // Unsupported
        #else
            #if defined(STEEP_PARALLAX)
                texCoordParallax = steepParallaxOffset(parallaxMapTex, texScale, vViewDirParallax, texCoord, parallaxHeight);
            #else 
                texCoordParallax = parallaxOffset(parallaxMapTex, texScale, vViewDirParallax, texCoord, parallaxHeight);
            #endif
        #endif
    #else
        #if defined(TRIPLANAR_MAPPING)
            vVertexParallax = vVertex;
        #else            
            texCoordParallax = texCoord;
        #endif
    #endif
}