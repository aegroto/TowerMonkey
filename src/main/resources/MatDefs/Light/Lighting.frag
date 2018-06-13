#import "Common/ShaderLib/GLSLCompat.glsllib"
#import "Common/ShaderLib/Parallax.glsllib"
#import "Common/ShaderLib/Optics.glsllib"
#import "Common/ShaderLib/BlinnPhongLighting.glsllib"
#import "Common/ShaderLib/Lighting.glsllib"

varying vec2 texCoord;

varying vec3 AmbientSum;
varying vec4 DiffuseSum;
varying vec3 SpecularSum;


uniform vec4 g_LightDirection;
varying vec3 vViewDir;
varying vec4 vLightDir;
varying vec3 lightVec;

uniform sampler2D m_DiffuseMap;

#ifdef PARALLAXMAP
  uniform sampler2D m_ParallaxMap;  
#endif

#if (defined(PARALLAXMAP)
    uniform float m_ParallaxHeight;
    varying vec3 vViewDirPrlx;
#endif

#ifdef NORMALMAP
  uniform sampler2D m_NormalMap;   
#else
  varying vec3 vNormal;
#endif

#ifndef VERTEX_LIGHTING
    uniform float m_Shininess;
#endif

void main(){
    vec2 newTexCoord;
     
    #if (defined(PARALLAXMAP)     
       #ifdef STEEP_PARALLAX
            newTexCoord = steepParallaxOffset(m_ParallaxMap, vViewDirPrlx, texCoord, m_ParallaxHeight);
       #else
            newTexCoord = classicParallaxOffset(m_ParallaxMap, vViewDirPrlx, texCoord, m_ParallaxHeight);
       #endif
    #else
       newTexCoord = texCoord;    
    #endif
    
    vec4 diffuseColor = texture2D(m_DiffuseMap, newTexCoord);

    float alpha = DiffuseSum.a * diffuseColor.a;

    // ***********************
    // Read from textures
    // ***********************
    #if defined(NORMALMAP)
        vec4 normalHeight = texture2D(m_NormalMap, newTexCoord);
        //Note the -2.0 and -1.0. We invert the green channel of the normal map, 
        //as it's complient with normal maps generated with blender.
        //see http://hub.jmonkeyengine.org/forum/topic/parallax-mapping-fundamental-bug/#post-256898
        //for more explanation.
        vec3 normal = normalize((normalHeight.xyz * vec3(2.0,-2.0,2.0) - vec3(1.0,-1.0,1.0)));
    #elif
        vec3 normal = vNormal;
        normal = normalize(normal);
    #endif


    vec4 lightDir = vLightDir;
    lightDir.xyz = normalize(lightDir.xyz);
    vec3 viewDir = normalize(vViewDir);
    float spotFallOff = 1.0;

    #if __VERSION__ >= 110
    if(g_LightDirection.w != 0.0){
    #endif
        spotFallOff =  computeSpotFalloff(g_LightDirection, lightVec);
    #if __VERSION__ >= 110
        if(spotFallOff <= 0.0) {
            gl_FragColor.rgb = AmbientSum * diffuseColor.rgb;
            gl_FragColor.a   = alpha;
            return;
        }
    }        
    #endif

    vec2 light = computeLighting(normal, viewDir, lightDir.xyz, lightDir.w * spotFallOff, m_Shininess);

    vec4 SpecularSum2 = vec4(SpecularSum, 1.0);

    gl_FragColor.rgb =  AmbientSum       * diffuse.rgb  +
                        DiffuseSum.rgb   * diffuseColor.rgb  * vec3(light.x) +
                        SpecularSum2.rgb * specularColor.rgb * vec3(light.y);
    gl_FragColor.a = alpha;
}
