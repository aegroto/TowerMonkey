#ifndef TP_FRAG_H
    #define TP_FRAG_H 1
    vec2 parallaxOffset(sampler2D parallaxMap, vec3 vViewDir,vec2 texCoord,float parallaxScale){
        vec2 vParallaxDirection = normalize(  vViewDir.xy );

        // The length of this vector determines the furthest amount of displacement: (Ati's comment)
        float fLength         = length( vViewDir );
        float fParallaxLength = sqrt( fLength * fLength - vViewDir.z * vViewDir.z ) / vViewDir.z; 

        // Compute the actual reverse parallax displacement vector: (Ati's comment)
        vec2 vParallaxOffsetTS = vParallaxDirection * fParallaxLength;

        // Need to scale the amount of displacement to account for different height ranges
        // in height maps. This is controlled by an artist-editable parameter: (Ati's comment)              
        parallaxScale *=0.3;
        vParallaxOffsetTS *= parallaxScale;

       vec3 eyeDir = normalize(vViewDir).xyz;   

        float nMinSamples = 6.0;
        float nMaxSamples = 1000.0 * parallaxScale;   
        float nNumSamples = mix( nMinSamples, nMaxSamples, 1.0 - eyeDir.z );   //In reference shader: int nNumSamples = (int)(lerp( nMinSamples, nMaxSamples, dot( eyeDirWS, N ) ));
        float fStepSize = 1.0 / nNumSamples;   
        float fCurrHeight = 0.0;
        float fPrevHeight = 1.0;
        float fNextHeight = 0.0;
        float nStepIndex = 0.0;
        vec2 vTexOffsetPerStep = fStepSize * vParallaxOffsetTS;
        vec2 vTexCurrentOffset = texCoord;
        float  fCurrentBound     = 1.0;
        float  fParallaxAmount   = 0.0;   

        while ( nStepIndex < nNumSamples && fCurrHeight <= fCurrentBound ) {
            vTexCurrentOffset -= vTexOffsetPerStep;
            fPrevHeight = fCurrHeight;
            
           
           #ifdef NORMALMAP_PARALLAX
               //parallax map is stored in the alpha channel of the normal map         
               fCurrHeight = texture2D( parallaxMap, vTexCurrentOffset).a; 
           #else
               //parallax map is a texture
               fCurrHeight = texture2D( parallaxMap, vTexCurrentOffset).r;                
           #endif
           
            fCurrentBound -= fStepSize;
            nStepIndex+=1.0;
        } 
        vec2 pt1 = vec2( fCurrentBound, fCurrHeight );
        vec2 pt2 = vec2( fCurrentBound + fStepSize, fPrevHeight );

        float fDelta2 = pt2.x - pt2.y;
        float fDelta1 = pt1.x - pt1.y;

        float fDenominator = fDelta2 - fDelta1;

        fParallaxAmount = (pt1.x * fDelta2 - pt2.x * fDelta1 ) / fDenominator;

        vec2 vParallaxOffset = vParallaxOffsetTS * (1.0 - fParallaxAmount );
        return texCoord - vParallaxOffset; 
    }
#endif

void main(){
    #if defined(PATH_PARALLAXMAP) || defined(HILL_PARALLAXMAP) || defined(MOUNTAIN_PARALLAXMAP)
        #if defined(TRIPLANAR_MAPPING)
            vVertexParallax.xy = parallaxOffset(parallaxMapTex, vViewDirParallax, vVertex.xy, parallaxHeight) * blending.x;
            vVertexParallax.xz = parallaxOffset(parallaxMapTex, vViewDirParallax, vVertex.xz, parallaxHeight) * blending.y;
            vVertexParallax.yz = parallaxOffset(parallaxMapTex, vViewDirParallax, vVertex.yz, parallaxHeight) * blending.z;
        #else
            vVertexParallax = vVertex;
            vVertexParallax.xz = parallaxOffset(parallaxMapTex, vViewDirParallax, vVertex.xz, parallaxHeight);
        #endif
    #endif
}
