#import "Common/ShaderLib/BlinnPhongLighting.glsllib"

vec3 calculateNormal(in vec2 texCoord) {
  vec3 normal = vec3(0,0,1);
  vec3 n = vec3(0,0,0);

  #ifdef NORMALMAP
    n = texture2D(hillNormalMap, texCoord * m_DiffuseMap_0_scale).xyz;
    normal += n;
  #else
    normal += vec3(0.5,0.5,1);
  #endif

  normal = (normal.xyz * vec3(2.0) - vec3(1.0));
  return normalize(normal);
}

vec3 calculateNormalTriPlanar(in vec3 wNorm, in vec4 wVert,in vec2 texCoord) {
    vec3 blending = abs( wNorm );
    blending = (blending -0.2) * 0.7;
    blending = normalize(max(blending, 0.00001));
    float b = (blending.x + blending.y + blending.z);
    blending /= vec3(b, b, b);

    // texture coords
    vec4 coords = wVert;

    vec3 normal = vec3(0,0,1);
    vec3 n = vec3(0,0,0);

    #ifdef NORMALMAP
        n = getTriPlanarBlend(coords, blending, hillNormalMap, m_DiffuseMap_0_scale).xyz;
        normal += n;
    #else
        normal += vec3(0.5,0.5,1);
    #endif

    normal = (normal.xyz * vec3(2.0) - vec3(1.0));
    return normalize(normal);
}

void main(){
    float spotFallOff;

    if(gLightDirection.w != 0.0) {
        vec3 L = normalize(lightVec.xyz);
        vec3 spotdir = normalize(gLightDirection.xyz);
        float curAngleCos = dot(-L, spotdir);             
        float innerAngleCos = floor(gLightDirection.w) * 0.001;
        float outerAngleCos = fract(gLightDirection.w);
        float innerMinusOuter = innerAngleCos - outerAngleCos;

        spotFallOff = (curAngleCos - outerAngleCos) / innerMinusOuter;

        if(spotFallOff <= 0.0){
            light = vec2(0, 0);
            return;
        } else {
            spotFallOff = clamp(spotFallOff, 0.0, 1.0);
        }
    } else {
        spotFallOff = 1.0;
    }

    #if defined(PATH_NORMALMAP) || defined(HILL_NORMALMAP) || defined(MOUNTAIN_NORMALMAP)
      #ifdef TRI_PLANAR_MAPPING
        vec3 normal = calculateNormalTriPlanar(wNormal, wVertex, texCoord);
      #else
        vec3 normal = calculateNormal(texCoord);
      #endif
    #else
      vec3 normal = vNormal;
    #endif

    vec4 lightDir = vLightDir;
    lightDir.xyz = normalize(lightDir.xyz);

    light = computeLighting(normal, vViewDir.xyz, lightDir.xyz, lightDir.w*spotFallOff, m_Shininess);
}
