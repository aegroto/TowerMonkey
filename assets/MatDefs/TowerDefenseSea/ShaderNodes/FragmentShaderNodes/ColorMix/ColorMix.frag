#import "ShaderLib/TextureInterlace.glsllib"

void main(){
    // vec4 diffuseColor = texture2D(DiffuseMapTex, texCoord * TexScale) * light.x;
    vec4 diffuseColor = interlaceTexture(DiffuseMapTex, texCoord, TexScale, randomSeed, InterlacingFactor);

    diffuseColor *= light.x;
    diffuseColor.a = alpha;

    float specularFactor = light.y;
                        
    outColor = ambientSum * diffuseColor +
               diffuseSum * diffuseColor + 
               specularSum * specularFactor;
}

