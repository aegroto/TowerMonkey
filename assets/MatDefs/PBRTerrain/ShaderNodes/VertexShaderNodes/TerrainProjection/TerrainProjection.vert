void main(){
    vPosition = inPosition;
    vNormal = inNormal;

    projPosition = worldViewProjectionMatrix * vec4(inPosition, 1.0);
}