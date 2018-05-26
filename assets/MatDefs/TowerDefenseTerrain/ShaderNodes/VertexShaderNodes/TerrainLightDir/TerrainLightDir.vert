#import "Common/ShaderLib/Lighting.glsllib"

void main(){
    #if defined(NORMAL_MAPPING)
        vViewDir = worldViewDir * tangentSpaceMat;

        lightComputeDir(worldViewPosition, gLightColor.w, worldViewLightPos, vLightDir, lightVec);
        vLightDir.xyz = vLightDir.xyz * tangentSpaceMat;
    #else
        vViewDir = worldViewDir;

        lightComputeDir(worldViewPosition, gLightColor.w, worldViewLightPos, vLightDir, lightVec);
    #endif

    #if defined(PARALLAX_MAPPING)
        vViewDirParallax = -worldViewPosition * tangentSpaceMat;
    #endif
}