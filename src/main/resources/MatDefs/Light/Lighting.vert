#import "Common/ShaderLib/GLSLCompat.glsllib"
#import "Common/ShaderLib/Instancing.glsllib"
#import "Common/ShaderLib/Skinning.glsllib"
#import "Common/ShaderLib/Lighting.glsllib"
#import "Common/ShaderLib/MorphAnim.glsllib"


uniform vec4 m_Ambient;
uniform vec4 m_Diffuse;
uniform float m_Shininess;

uniform vec4 g_LightColor;
uniform vec4 g_LightPosition;
uniform vec4 g_AmbientLightColor;

varying vec2 texCoord;

varying vec3 AmbientSum;
varying vec4 DiffuseSum;
varying vec3 SpecularSum;

attribute vec3 inPosition;
attribute vec2 inTexCoord;
attribute vec3 inNormal;

varying vec3 lightVec;

attribute vec4 inTangent;

#ifndef NORMALMAP
  varying vec3 vNormal;
#endif  

varying vec3 vViewDir;
varying vec4 vLightDir

#if (defined(PARALLAXMAP)
    varying vec3 vViewDirPrlx;
#endif


void main(){
    vec4 modelSpacePos = vec4(inPosition, 1.0);
    vec3 modelSpaceNorm = inNormal;

    gl_Position = TransformWorldViewProjection(modelSpacePos);

    texCoord = inTexCoord;

    vec3 wvPosition = TransformWorldView(modelSpacePos).xyz;
    vec3 wvNormal  = normalize(TransformNormal(modelSpaceNorm));
    vec3 viewDir = normalize(-wvPosition);

    vec4 wvLightPos = (g_ViewMatrix * vec4(g_LightPosition.xyz,clamp(g_LightColor.w,0.0,1.0)));
    wvLightPos.w = g_LightPosition.w;
    vec4 lightColor = g_LightColor;

    #if (defined(NORMALMAP) || defined(PARALLAXMAP))
      vec3 wvTangent = normalize(TransformNormal(modelSpaceTan));
      vec3 wvBinormal = cross(wvNormal, wvTangent);
      mat3 tbnMat = mat3(wvTangent, wvBinormal * inTangent.w,wvNormal);
    #endif
 
    #if defined(NORMALMAP)
      vViewDir  = -wvPosition * tbnMat;    
      #if (defined(PARALLAXMAP)
          vViewDirPrlx = vViewDir;
      #endif
      lightComputeDir(wvPosition, lightColor.w, wvLightPos, vLightDir, lightVec);
      vLightDir.xyz = (vLightDir.xyz * tbnMat).xyz;
    #elif
      vNormal = wvNormal;
      vViewDir = viewDir;
      #if defined(PARALLAXMAP)
         vViewDirPrlx  =  -wvPosition * tbnMat;
      #endif
      lightComputeDir(wvPosition, lightColor.w, wvLightPos, vLightDir, lightVec);
    #endif

    AmbientSum  = g_AmbientLightColor.rgb;
    DiffuseSum  =  vec4(lightColor.rgb, 1.0);
    SpecularSum = vec3(0.0);
}