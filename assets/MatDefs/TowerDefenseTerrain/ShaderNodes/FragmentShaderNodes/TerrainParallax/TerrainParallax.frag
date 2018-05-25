#import "ShaderLib/ParallaxMapping.glsllib"

#ifndef TP_FRAG_H
    #define TP_FRAG_H 1
#endif

void main(){
    #if defined(PATH_PARALLAXMAP) || defined(HILL_PARALLAXMAP) || defined(MOUNTAIN_PARALLAXMAP)
        #if defined(TRIPLANAR_MAPPING)
            vVertexParallax = vVertex;
        #else
            #if defined(STEEP_PARALLAX)
                // texCoordParallax = steepParallaxOffset(parallaxMapTex, texScale, vViewDirParallax, texCoord, parallaxHeight);
            #else 
                vVertexParallax = triplanarParallaxOffset(parallaxMapTex, texScale, vViewDirParallax, vVertex, parallaxHeight, blending);
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
