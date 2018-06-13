uniform sampler2D m_Tex1;
uniform sampler2D m_Tex2;
uniform sampler2D m_Tex3;
uniform float m_Tex1Scale;
uniform float m_Tex2Scale;
uniform float m_Tex3Scale;

uniform float m_Tex1MaxHeight;

uniform float m_Tex2MinHeight;
uniform float m_Tex2MaxHeight;

uniform float m_Tex3MinHeight;

varying vec4 vVertex;
varying vec3 vNormal;

void main(void) {
    // tri-planar texture bending factor for this fragment's normal
    vec3 blending = abs( vNormal );
    blending = (blending -0.2) * 0.7;
    blending = normalize(max(blending, 0.00001));      // Force weights to sum to 1.0 (very important!)
    float b = (blending.x + blending.y + blending.z);
    blending /= vec3(b, b, b);

    // texture coords
    vec4 coords = vVertex;
    vec4 tex1, tex2, tex3, col1, col2, col3;
    tex1 = tex2 = tex3 = col1 = col2 = col3 = vec4(0.0, 0.0, 0.0, 1.0);

    float tex1Factor, tex2Factor, tex3Factor;

    tex1Factor = tex2Factor = tex3Factor = 1.0;

    if(coords.y >= m_Tex2MinHeight) {
        tex1Factor -= ((coords.y - m_Tex2MinHeight) / (m_Tex1MaxHeight - m_Tex2MinHeight));
    }

    if(coords.y <= m_Tex1MaxHeight) {
        tex2Factor = 1.0 - tex1Factor;
    } else if(coords.y >= m_Tex3MinHeight) {
        tex2Factor -= ((coords.y - m_Tex3MinHeight) / (m_Tex2MaxHeight - m_Tex3MinHeight));
    }

    if(coords.y <= m_Tex2MaxHeight) {
        tex3Factor = 1.0 - tex2Factor;
    }

    float pseudoRandomValue = fract((coords.x + coords.z) / 1000.0);

    if(coords.y >= m_Tex2MinHeight && coords.y <= m_Tex1MaxHeight) {
        tex2Factor -= pseudoRandomValue;
        tex1Factor += pseudoRandomValue;
    }

    if(coords.y >= m_Tex3MinHeight && coords.y <= m_Tex2MaxHeight) {
        tex3Factor -= pseudoRandomValue;
        tex2Factor += pseudoRandomValue;
    }

    if(coords.y <= m_Tex1MaxHeight) {
        col1 = texture2D( m_Tex1, coords.yz * m_Tex1Scale );
        col2 = texture2D( m_Tex1, coords.xz * m_Tex1Scale );
        col3 = texture2D( m_Tex1, coords.xy * m_Tex1Scale );

        // blend the results of the 3 planar projections.
        tex1 = col1 * blending.x + col2 * blending.y + col3 * blending.z;
    } 

    if(coords.y >= m_Tex2MinHeight && coords.y <= m_Tex2MaxHeight) {
        col1 = texture2D( m_Tex2, coords.yz * m_Tex2Scale );
        col2 = texture2D( m_Tex2, coords.xz * m_Tex2Scale );
        col3 = texture2D( m_Tex2, coords.xy * m_Tex2Scale );

        // blend the results of the 3 planar projections.
        tex2 = col1 * blending.x + col2 * blending.y + col3 * blending.z;
    }
 

    if(coords.y >= m_Tex3MinHeight) {
        col1 = texture2D( m_Tex3, coords.yz * m_Tex3Scale );
        col2 = texture2D( m_Tex3, coords.xz * m_Tex3Scale );
        col3 = texture2D( m_Tex3, coords.xy * m_Tex3Scale );

        // blend the results of the 3 planar projections.
        tex3 = col1 * blending.x + col2 * blending.y + col3 * blending.z;
    }

    gl_FragColor = tex1 * tex1Factor + tex2 * tex2Factor + tex3 * tex3Factor;
}

