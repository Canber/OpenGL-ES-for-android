precision mediump float;

uniform sampler2D u_TextureUnit1; 
uniform sampler2D u_TextureUnit2;

varying vec3 v_Color;
varying vec2 v_TextureCoordinates;
varying float v_Ratio;
	    	   								
void main()                    		
{	
    gl_FragColor = texture2D(u_TextureUnit1, v_TextureCoordinates) * (1.0 - v_Ratio);
    // Divide the texture coordinates by 2 to make the stone texture repeat half as often.
    gl_FragColor += texture2D(u_TextureUnit2, v_TextureCoordinates / 2.0) * v_Ratio;
    gl_FragColor *= vec4(v_Color, 1.0);
}