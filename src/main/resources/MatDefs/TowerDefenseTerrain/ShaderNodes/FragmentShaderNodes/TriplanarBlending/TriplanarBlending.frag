#import "ShaderLib/TriplanarMapping.glsllib"

void main(){
    blending = calculateBlending(vNormal, triplanarMaxBlending);
}
