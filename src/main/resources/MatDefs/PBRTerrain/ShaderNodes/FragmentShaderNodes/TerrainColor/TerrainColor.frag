void main(){
    vec4 diffuseColor = tex1 * tex1Factor + tex2 * tex2Factor + tex3 * tex3Factor;
    outColor =  ambientSum * diffuseColor +
                diffuseSum * diffuseColor * light.x;
}

