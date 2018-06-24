uniform mat4 u_MVMatrix;
uniform mat4 u_IT_MVMatrix;
uniform mat4 u_MVPMatrix;

uniform vec3 u_VectorToLight;             // In eye space
uniform vec4 u_PointLightPositions[3];    // In eye space
uniform vec3 u_PointLightColors[3];

attribute vec4 a_Position;
attribute vec3 a_Normal;
attribute vec2 a_TextureCoordinates;

varying vec3 v_Color;
varying vec2 v_TextureCoordinates;
varying float v_Ratio;

vec3 materialColor;
vec4 eyeSpacePosition;
vec3 eyeSpaceNormal;

vec3 getAmbientLighting();
vec3 getDirectionalLighting();
vec3 getPointLighting();
  
void main()                    
{           
	v_TextureCoordinates = a_TextureCoordinates;                   
	v_Ratio = a_Position.y;

    materialColor = vec3(1.0, 1.0, 1.0); // Will be modified by the texture later.        
    eyeSpacePosition = u_MVMatrix * a_Position;                             
                                    
    // The model normals need to be adjusted as per the transpose 
    // of the inverse of the modelview matrix.    
    eyeSpaceNormal = normalize(vec3(u_IT_MVMatrix * vec4(a_Normal, 0.0)));   
                                                                                  
    v_Color = getAmbientLighting();
    v_Color += getDirectionalLighting();                                                                                                                  
    v_Color += getPointLighting();
        
    gl_Position = u_MVPMatrix * a_Position;
}  

vec3 getAmbientLighting() 
{    
    return materialColor * 0.2;      
}

vec3 getDirectionalLighting()
{   
    return materialColor * max(dot(eyeSpaceNormal, u_VectorToLight), 0.0);       
}

vec3 getPointLighting()
{
    vec3 lightingSum = vec3(0.0);
    
    for (int i = 0; i < 3; i++) {                  
        vec3 toPointLight = vec3(u_PointLightPositions[i]) - vec3(eyeSpacePosition);          
        float distance = length(toPointLight);
        toPointLight = normalize(toPointLight);
        
        float cosine = max(dot(eyeSpaceNormal, toPointLight), 0.0); 
        lightingSum += (materialColor * u_PointLightColors[i] * 5.0 * cosine) 
                       / distance;
    }  
    
    return lightingSum;       
} 