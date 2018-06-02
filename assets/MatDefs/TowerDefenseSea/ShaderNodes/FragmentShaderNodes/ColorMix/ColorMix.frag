#import "ShaderLib/TextureInterlace.glsllib"

void main(){
    // vec4 diffuseColor = texture2D(diffuseMapTex, texCoord * TexScale) * light.x;
    vec4 diffuseColor = interlaceTexture(diffuseMapTex, texCoord, TexScale, 2);

    diffuseColor *= light.x;
    diffuseColor.a = alpha;

    float specularFactor = light.y;
                        
    outColor = ambientSum * diffuseColor +
               diffuseSum * diffuseColor + 
               specularSum * specularFactor;
}

