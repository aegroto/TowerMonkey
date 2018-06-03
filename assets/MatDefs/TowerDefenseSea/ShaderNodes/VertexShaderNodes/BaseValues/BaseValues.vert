void main(){
    texCoord = inTexCoord;
    vPosition = inPosition;
    vNormal = inNormal;

    randomSeed = fract(Time * RandomSeedVariation);
}