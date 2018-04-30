void main(){
    blending = abs( vNormal );
    blending = (blending -0.2) * 0.7;
    blending = normalize(max(blending, 0.00001));
    float b = (blending.x + blending.y + blending.z);
    blending /= vec3(b, b, b);
}
