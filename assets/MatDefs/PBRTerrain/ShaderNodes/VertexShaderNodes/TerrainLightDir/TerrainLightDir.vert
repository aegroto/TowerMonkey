#import "Common/ShaderLib/Lighting.glsllib"

void main(){
    #if defined(PATH_NORMALMAP) || defined(HILL_NORMALMAP) || defined(MOUNTAIN_NORMALMAP)
        vViewDir = worldViewDir * tangentSpaceMat;

        lightComputeDir(worldViewPosition, gLightColor.w, worldViewLightPos, vLightDir, lightVec);
        vLightDir.xyz = vLightDir.xyz * tangentSpaceMat;
    #else
        vViewDir = worldViewDir;

        lightComputeDir(worldViewPosition, gLightColor.w, worldViewLightPos, vLightDir, lightVec);
    #endif

    #if defined(PATH_PARALLAXMAP) || defined(HILL_PARALLAXMAP) || defined(MOUNTAIN_PARALLAXMAP)
        vViewDirParallax = -worldViewPosition * tangentSpaceMat;
    #endif
}