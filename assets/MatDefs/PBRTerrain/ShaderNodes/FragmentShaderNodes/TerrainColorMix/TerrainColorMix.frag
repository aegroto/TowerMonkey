void main(){
    vec4 diffuseColor = tex1 * tex1Factor * light1.x +
                        tex2 * tex2Factor * light2.x +
                        tex3 * tex3Factor * light3.x;

    vec4 specularColor = vec4(1.0);
                        
    outColor = ambientSum * diffuseColor +
               diffuseSum * diffuseColor +
               diffuseSum * specularColor * ((light1.y + light2.y + light3.y) / 3.0);
}

