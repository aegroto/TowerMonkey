void main(){
    vec4 diffuseColor = pathColor * pathFactor * pathLight.x +
                        hillColor * hillFactor * hillLight.x +
                        mountainColor * mountainFactor * mountainLight.x;

    float specularFactor = pathLight.y * pathFactor + 
                           hillLight.y * hillFactor +
                           mountainLight.y * mountainFactor;
                        
    outColor = ambientSum * diffuseColor +
               diffuseSum * diffuseColor + 
               specularSum * specularFactor;
}

