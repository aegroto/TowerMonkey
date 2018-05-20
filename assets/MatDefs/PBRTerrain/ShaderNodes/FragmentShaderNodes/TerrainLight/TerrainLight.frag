#import "Common/ShaderLib/BlinnPhongLighting.glsllib"
#ifndef TL_FRAG_H
    #define TL_FRAG_H 1
#endif

void main(){
    if(gLightDirection.w == 0.0) {
        spotFallOff = 1.0;
    } else {
        float curAngleCos = dot(-normalize(lightVec.xyz), normalize(gLightDirection.xyz));
        spotFallOff = (curAngleCos - (fract(gLightDirection.w) * 0.001)) / floor(gLightDirection.w);

        if(spotFallOff <= 0.0){
            light = vec2(0, 0);
            return;
        } else {
            spotFallOff = clamp(spotFallOff, 0.0, 1.0);
        }
    }

    #if defined(PATH_NORMALMAP) || defined(HILL_NORMALMAP) || defined(MOUNTAIN_NORMALMAP)
        normal = vec3(0, 0, 1);

        #if defined(TRIPLANAR_MAPPING)
            normal += (texture2D(normalMapTex, vVertex.yz * texScale) * blending.x +
                      texture2D(normalMapTex, vVertex.xz * texScale) * blending.y +
                      texture2D(normalMapTex, vVertex.xy * texScale) * blending.z).xyz;
        #else
            normal += texture2D(normalMapTex, vVertex.xz * texScale).xyz;
        #endif

        normal = normal.xyz * vec3(2.0) - vec3(1.0);
        normal = normalize(normal);
    #else
        normal = vNormal;
    #endif

    lightDir = vec4(normalize(vLightDir.xyz), vLightDir.w);

    light = computeLighting(normal, vViewDir.xyz, lightDir.xyz, lightDir.w * spotFallOff, shininess);
}
