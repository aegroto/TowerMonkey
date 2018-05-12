#ifndef TERRAIN_LIGHT_FUNCTIONS
#define TERRAIN_LIGHT_FUNCTIONS 1
    float lightComputeDiffuse(in vec3 norm, in vec3 lightdir){
        return max(0.0, dot(norm, lightdir));
    }

    float lightComputeSpecular(in vec3 norm, in vec3 viewdir, in vec3 lightdir, in float shiny){
        vec3 H = normalize(viewdir + lightdir);
        float HdotN = max(0.0, dot(H, norm));
        return pow(HdotN, shiny);
    }

    vec2 computeLighting(in vec3 norm, in vec3 viewDir, in vec3 lightDir, in float attenuation, in float shininess){
        float diffuseFactor = lightComputeDiffuse(norm, lightDir);
        float specularFactor = lightComputeSpecular(norm, viewDir, lightDir, shininess);      
        if (shininess <= 1.0) {
            specularFactor = 0.0;
        }
        specularFactor *= diffuseFactor;
        return vec2(diffuseFactor, specularFactor) * vec2(attenuation);
    }
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
        #if defined(TRIPLANAR_MAPPING)
            normal = vec3(
                    texture2D(normalMapTex, vVertex.yz * texScale) * blending.x +
                    texture2D(normalMapTex, vVertex.xz * texScale) * blending.y +
                    texture2D(normalMapTex, vVertex.xy * texScale) * blending.z);
        #else
            normal = vec3(0.0, 0.0, 0.0);
            normal += texture2D(normalMapTex, vVertex.xz * texScale).xyz;
        #endif

        normal = normalize(normal.xyz * vec3(2.0) - vec3(1.0));
    #else
        normal = vNormal;
    #endif

    lightDir = vec4(normalize(vLightDir.xyz), vLightDir.w);

    light = computeLighting(normal, vViewDir.xyz, lightDir.xyz, lightDir.w * spotFallOff, shininess);
}
