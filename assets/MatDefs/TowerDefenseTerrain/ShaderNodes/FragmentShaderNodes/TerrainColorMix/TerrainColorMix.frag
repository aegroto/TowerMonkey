void main(){
    vec4 diffuseColor = tex1 * tex1Factor * light1.x +
                        tex2 * tex2Factor * light2.x +
                        tex3 * tex3Factor * light3.x;
                        
    outColor = ambientSum * diffuseColor +
               diffuseSum * diffuseColor;
}

