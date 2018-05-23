void main(){
    worldViewPosition = (gWorldViewMatrix * vec4(inPosition, 1.0)).xyz;
    worldViewNormal = normalize(gNormalMatrix * inNormal);
    worldViewDir = normalize(-worldViewPosition);

    worldViewLightPos = (gViewMatrix * vec4(gLightPosition.xyz, clamp(gLightColor.w, 0.0, 1.0)));
    worldViewLightPos.w = gLightPosition.w;

    worldViewTangent = normalize(gNormalMatrix * inTangent.xyz);
    worldViewBinormal = cross(worldViewNormal, worldViewTangent);

    #if defined(PATH_NORMALMAP) || defined(HILL_NORMALMAP) || defined(MOUNTAIN_NORMALMAP) || defined(PATH_PARALLAXMAP) || defined(HILL_PARALLAXMAP) || defined(MOUNTAIN_PARALLAXMAP)
        tangentSpaceMat = mat3(
            worldViewTangent,
            worldViewBinormal * inTangent.w,
            worldViewNormal
        );
    #endif
}