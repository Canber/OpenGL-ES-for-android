precision mediump float; 

uniform sampler2D u_TextureUnit;

varying vec3 v_Color;
varying float v_ElapsedTime;
      	 							    	   								
void main()                    		
{
    gl_FragColor = vec4(v_Color / v_ElapsedTime, 1.0)
                 * texture2D(u_TextureUnit, gl_PointCoord);
}