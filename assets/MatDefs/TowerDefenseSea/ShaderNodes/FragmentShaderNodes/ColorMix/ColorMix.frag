void main(){
    vec4 diffuseColor = texture2D(diffuseMapTex, texCoord * TexScale) * light.x;
    diffuseColor.a = alpha;

    float specularFactor = light.y;
                        
    outColor = ambientSum * diffuseColor +
               diffuseSum * diffuseColor + 
               specularSum * specularFactor;
}

