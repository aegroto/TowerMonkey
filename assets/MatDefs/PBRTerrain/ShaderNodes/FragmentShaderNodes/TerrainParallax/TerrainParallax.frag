#import "ShaderLib/ParallaxMapping.glsllib"

#ifndef TP_FRAG_H
    #define TP_FRAG_H 1
#endif

void main(){
    #if defined(PATH_PARALLAXMAP) || defined(HILL_PARALLAXMAP) || defined(MOUNTAIN_PARALLAXMAP)
        #if defined(TRIPLANAR_MAPPING)

        #else
            vVertexParallax = vVertex;
            vVertexParallax.xz = parallaxOffset(parallaxMapTex, vViewDirParallax, vVertex.xz, parallaxHeight);
        #endif
    #endif
}
