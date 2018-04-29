void main(){
    if(vVertex.y >= LayerMinHeight) {
        if(vVertex.y <= PrevLayerMaxHeight) {
            factor = ((vVertex.y - LayerMinHeight) / (PrevLayerMaxHeight - LayerMinHeight));
        } else if(vVertex.y >= NextLayerMinHeight) {
            factor = 1.0 - ((vVertex.y - NextLayerMinHeight) / (LayerMaxHeight - NextLayerMinHeight));
        } else factor = 1.0;
    }
}
