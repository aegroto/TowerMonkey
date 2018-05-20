void main(){
    vPosition = inPosition.xyz;
    vNormal = inNormal;

    projPosition = worldViewProjectionMatrix * vec4(inPosition.xyz, 1.0);
}