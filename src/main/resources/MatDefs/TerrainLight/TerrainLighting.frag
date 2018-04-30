#import "Common/ShaderLib/BlinnPhongLighting.glsllib"
#import "Common/ShaderLib/Lighting.glsllib"
#import "Common/ShaderLib/Parallax.glsllib"

uniform float m_Shininess;
uniform vec4 g_LightDirection;

varying vec4 AmbientSum;
varying vec4 DiffuseSum;
varying vec4 SpecularSum;

varying vec3 vNormal;
varying vec2 texCoord;
varying vec3 vPosition;
varying vec3 vViewDir;
varying vec4 vLightDir;
varying vec3 lightVec;


#ifdef DIFFUSEMAP
  uniform sampler2D m_DiffuseMap;
#endif

#ifdef DIFFUSEMAP_0_SCALE
  uniform float m_DiffuseMap_0_scale;
#endif

#ifdef NORMALMAP
  uniform sampler2D m_NormalMap;
#endif


#ifdef TRI_PLANAR_MAPPING
  varying vec4 wVertex;
  varying vec3 wNormal;
#endif

vec4 calculateDiffuseBlend(in vec2 texCoord) {
  vec4 diffuseColor = vec4(1.0);

  #ifdef DIFFUSEMAP
      diffuseColor = texture2D(m_DiffuseMap, texCoord * m_DiffuseMap_0_scale);
  #endif

  return diffuseColor;
}

vec3 calculateNormal(in vec2 texCoord) {
  vec3 normal = vec3(0,0,1);
  vec3 n = vec3(0,0,0);

  #ifdef NORMALMAP
    n = texture2D(m_NormalMap, texCoord * m_DiffuseMap_0_scale).xyz;
    normal += n;
  #else
    normal += vec3(0.5,0.5,1);
  #endif

  normal = (normal.xyz * vec3(2.0) - vec3(1.0));
  return normalize(normal);
}

vec4 getTriPlanarBlend(in vec4 coords, in vec3 blending, in sampler2D map, in float scale) {
  vec4 col1 = texture2D( map, coords.yz * scale);
  vec4 col2 = texture2D( map, coords.xz * scale);
  vec4 col3 = texture2D( map, coords.xy * scale); 
  // blend the results of the 3 planar projections.
  vec4 tex = col1 * blending.x + col2 * blending.y + col3 * blending.z;
  return tex;
}

vec4 calculateTriPlanarDiffuseBlend(in vec3 wNorm, in vec4 wVert, in vec2 texCoord) {
    // tri-planar texture bending factor for this fragment's normal
    vec3 blending = abs( wNorm );
    blending = (blending -0.2) * 0.7;
    blending = normalize(max(blending, 0.00001));      // Force weights to sum to 1.0 (very important!)
    float b = (blending.x + blending.y + blending.z);
    blending /= vec3(b, b, b);

    // texture coords
    vec4 coords = wVert;

    // blend the results of the 3 planar projections.
    vec4 diffuseColor = getTriPlanarBlend(coords, blending, m_DiffuseMap, m_DiffuseMap_0_scale);

    return diffuseColor;
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
        n = getTriPlanarBlend(coords, blending, m_NormalMap, m_DiffuseMap_0_scale).xyz;
        normal += n;
    #else
        normal += vec3(0.5,0.5,1);
    #endif

    normal = (normal.xyz * vec3(2.0) - vec3(1.0));
    return normalize(normal);
}

void main(){

    //----------------------
    // diffuse calculations
    //----------------------
    #ifdef DIFFUSEMAP
        #ifdef TRI_PLANAR_MAPPING
            vec4 diffuseColor = calculateTriPlanarDiffuseBlend(wNormal, wVertex, texCoord);
        #else
            vec4 diffuseColor = calculateDiffuseBlend(texCoord);
        #endif
        // vec4 diffuseColor = texture2D(m_DiffuseMap, texCoord);
    #else
      vec4 diffuseColor = vec4(1.0);
    #endif

        float spotFallOff = 1.0;
        if(g_LightDirection.w!=0.0){
              vec3 L=normalize(lightVec.xyz);
              vec3 spotdir = normalize(g_LightDirection.xyz);
              float curAngleCos = dot(-L, spotdir);             
              float innerAngleCos = floor(g_LightDirection.w) * 0.001;
              float outerAngleCos = fract(g_LightDirection.w);
              float innerMinusOuter = innerAngleCos - outerAngleCos;

              spotFallOff = (curAngleCos - outerAngleCos) / innerMinusOuter;

              if(spotFallOff <= 0.0){
                  gl_FragColor = AmbientSum * diffuseColor;
                  return;
              }else{
                  spotFallOff = clamp(spotFallOff, 0.0, 1.0);
              }
        }

    //---------------------
    // normal calculations
    //---------------------
    #if defined(NORMALMAP)
      #ifdef TRI_PLANAR_MAPPING
        vec3 normal = calculateNormalTriPlanar(wNormal, wVertex, texCoord);
      #else
        vec3 normal = calculateNormal(texCoord);
      #endif
    #else
      vec3 normal = vNormal;
    #endif


    //-----------------------
    // lighting calculations
    //-----------------------
    vec4 lightDir = vLightDir;
    lightDir.xyz = normalize(lightDir.xyz);

    vec2 light = computeLighting(normal, vViewDir.xyz, lightDir.xyz,lightDir.w*spotFallOff,m_Shininess);

    vec4 specularColor = vec4(1.0);

    //--------------------------
    // final color calculations
    //--------------------------
    gl_FragColor =  AmbientSum * diffuseColor +
                    DiffuseSum * diffuseColor  * light.x +
                    SpecularSum * specularColor * light.y;

    //gl_FragColor.a = alpha;
}
