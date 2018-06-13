void main(){
    vec4 diffuseColor = ditchColor * ditchFactor * ditchLight.x +
                        pathColor * pathFactor * pathLight.x +
                        hillColor * hillFactor * hillLight.x +
                        mountainColor * mountainFactor * mountainLight.x;

    float specularFactor = ditchLight.y * ditchFactor +
                           pathLight.y * pathFactor + 
                           hillLight.y * hillFactor +
                           mountainLight.y * mountainFactor;
                        
    outColor = ambientSum * diffuseColor +
               diffuseSum * diffuseColor + 
               specularSum * specularFactor;
}

