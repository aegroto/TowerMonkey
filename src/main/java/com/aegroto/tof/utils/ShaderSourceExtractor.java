/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aegroto.tof.utils;

import com.jme3.material.Material;
import java.lang.reflect.Field;

/**
 *
 * @author lorenzo
 */
public class ShaderSourceExtractor {
    private String source;
    private Material material;
    
    public ShaderSourceExtractor(Material material) {
        this.material = material;
        
        source = extractSource();
    }
    
    private String extractSource() {
        String src = "";
        Field rendererField;
        // rendererField = material.getClass().getField("")
        return src;
    }
}
