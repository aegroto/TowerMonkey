#import "Common/ShaderLib/Lighting.glsllib"

void main(){
    texCoord = inTexCoord;

    vec3 wvPosition = (gWorldViewMatrix * vec4(inPosition, 1.0)).xyz;
    vec3 wvNormal = normalize(gNormalMatrix * inNormal);
    vec3 viewDir = normalize(-wvPosition);

    vec4 wvLightPos = (gViewMatrix * vec4(gLightPosition.xyz, clamp(gLightColor.w,0.0,1.0)));
    wvLightPos.w = gLightPosition.w;

    #if defined(PATH_NORMALMAP) || defined(HILL_NORMALMAP) || defined(MOUNTAIN_NORMALMAP)
        vec3 wvTangent = normalize(gNormalMatrix * inTangent.xyz);
        vec3 wvBinormal = cross(wvNormal, wvTangent);

        mat3 tbnMat = mat3(wvTangent, wvBinormal * inTangent.w, wvNormal);

        wPosition = wvPosition * tbnMat;
        wViewDir  = viewDir * tbnMat;

        lightComputeDir(wvPosition, gLightColor.w, wvLightPos, vLightDir, lightVec);
        vLightDir.xyz = (vLightDir.xyz * tbnMat).xyz;

        #if defined(PATH_PARALLAXMAP) || defined(HILL_PARALLAXMAP) || defined(MOUNTAIN_PARALLAXMAP)
            vViewDirParallax = -wvPosition * tbnMat;
        #endif
    #else
        wNormal = wvNormal;

        wPosition = wvPosition;
        wViewDir = viewDir;

        lightComputeDir(wvPosition, gLightColor.w, wvLightPos, vLightDir, lightVec);
    #endif

    vPosition = vec4(inPosition, 1.0);
    vNormal = inNormal;
}