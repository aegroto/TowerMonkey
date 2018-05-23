#import "Common/ShaderLib/Lighting.glsllib"

void main(){
    vec3 worldViewPosition = (gWorldViewMatrix * vec4(inPosition, 1.0)).xyz;
    vec3 worldViewNormal = normalize(gNormalMatrix * inNormal);
    vec3 worldViewDir = normalize(-worldViewPosition);

    vec4 worldViewLightPos = (gViewMatrix * vec4(gLightPosition.xyz, clamp(gLightColor.w, 0.0, 1.0)));
    worldViewLightPos.w = gLightPosition.w;

    #if defined(PATH_NORMALMAP) || defined(HILL_NORMALMAP) || defined(MOUNTAIN_NORMALMAP)
        vec3 worldViewTangent = normalize(gNormalMatrix * inTangent.xyz);
        vec3 worldViewBinormal = cross(worldViewNormal, worldViewTangent);

        mat3 tangentSpaceMat = mat3(
            worldViewTangent,
            worldViewBinormal * inTangent.w,
            worldViewNormal
        );

        vViewDir = worldViewDir * tangentSpaceMat;

        lightComputeDir(worldViewPosition, gLightColor.w, worldViewLightPos, vLightDir, lightVec);
        vLightDir.xyz = vLightDir.xyz * tangentSpaceMat;
    #else
        wNormal = worldViewNormal;
        vViewDir = worldViewDir;

        lightComputeDir(worldViewPosition, gLightColor.w, worldViewLightPos, vLightDir, lightVec);
    #endif

    #if defined(PATH_PARALLAXMAP) || defined(HILL_PARALLAXMAP) || defined(MOUNTAIN_PARALLAXMAP)
        vViewDirParallax = -worldViewPosition * tangentSpaceMat;
    #endif

    texCoord = inTexCoord;
    vPosition = inPosition;
    vNormal = inNormal;
}