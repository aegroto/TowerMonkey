void main(){
    if(vVertex.y >= NextLayerMinHeight) {
        factor = 1.0 - ((vVertex.y - NextLayerMinHeight) / (LayerMaxHeight - NextLayerMinHeight));
    } else factor = 1.0;
}