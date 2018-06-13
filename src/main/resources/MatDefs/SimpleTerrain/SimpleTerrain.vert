uniform mat4 g_WorldViewProjectionMatrix;

attribute vec3 inPosition;
attribute vec3 inNormal;

varying vec4 vVertex;
varying vec3 vNormal;

void main(){
    gl_Position = g_WorldViewProjectionMatrix * vec4(inPosition, 1.0);

    vVertex = vec4(inPosition,0.0);
    vNormal = inNormal;
}