precision mediump float; 
uniform sampler2D u_TextureUnit;
varying vec3 v_Color;
varying float v_ElapsedTime;     	 							    	   								
void main()                    		
{
    /*            
    float xDistance = 0.5 - gl_PointCoord.x;
    float yDistance = 0.5 - gl_PointCoord.y;
    float distanceFromCenter = 
        sqrt(xDistance * xDistance + yDistance * yDistance);
    gl_FragColor = vec4(v_Color / v_ElapsedTime, 1.0);
    
    if (distanceFromCenter > 0.5) {
        discard;
    } else {            
        gl_FragColor = vec4(v_Color / v_ElapsedTime, 1.0);        
    }
    */     
    gl_FragColor = vec4(v_Color / v_ElapsedTime, 1.0)
                 * texture2D(u_TextureUnit, gl_PointCoord);
}
