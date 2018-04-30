void main(){
    if(vVertex.y >= LayerMinHeight) {

        if(vVertex.y <= PrevLayerMaxHeight) {
            factor = ((vVertex.y - LayerMinHeight) / (PrevLayerMaxHeight - LayerMinHeight));
        } else { 
            factor = 1.0;
        }
    }
}
