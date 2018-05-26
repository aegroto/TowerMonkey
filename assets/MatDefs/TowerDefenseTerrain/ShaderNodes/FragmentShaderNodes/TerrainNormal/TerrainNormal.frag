#import "ShaderLib/TriplanarMapping.glsllib"

void main(){
    #if defined(NORMAL_MAPPING)
        #if defined(TRIPLANAR_MAPPING)
            normal = triplanarColor(NormalMapTex, vVertex, TexScale, blending).xyz;

            normal = normalize(normal) * vec3(2.0,2.0,2.0) - vec3(1.0,1.0,1.0);
        #else
            normal = normalize((texture2D(NormalMapTex, texCoord * TexScale).xyz * vec3(2.0,-2.0,2.0) - vec3(1.0,-1.0,1.0)));
        #endif
    #else
        normal = vNormal;
    #endif
}
