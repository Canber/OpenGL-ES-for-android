/***
 * Excerpted from "OpenGL ES for Android",
 * published by The Pragmatic Bookshelf.
 * Copyrights apply to this code. It may not be used to create training material, 
 * courses, books, articles, and the like. Contact us if you are in doubt.
 * We make no guarantees that this code is fit for any purpose. 
 * Visit http://www.pragmaticprogrammer.com/titles/kbogla for more book information.
***/
package com.particles.android.programs;

import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glUniform3fv;
import static android.opengl.GLES20.glUniform4fv;
import static android.opengl.GLES20.glUniformMatrix4fv;
import android.content.Context;

import com.particles.android.R;

public class HeightmapShaderProgram extends ShaderProgram {           
    private final int uVectorToLightLocation;
    private final int uMVMatrixLocation;
    private final int uIT_MVMatrixLocation;
    private final int uMVPMatrixLocation;
    private final int uPointLightPositionsLocation;
    private final int uPointLightColorsLocation;
    
    private final int aPositionLocation;
    private final int aNormalLocation;

    public HeightmapShaderProgram(Context context) {
        super(context, R.raw.heightmap_vertex_shader,
            R.raw.heightmap_fragment_shader);
                
        uVectorToLightLocation = glGetUniformLocation(program, U_VECTOR_TO_LIGHT);
        uMVMatrixLocation = glGetUniformLocation(program, U_MV_MATRIX);
        uIT_MVMatrixLocation = glGetUniformLocation(program, U_IT_MV_MATRIX);
        uMVPMatrixLocation = glGetUniformLocation(program, U_MVP_MATRIX);
        
        uPointLightPositionsLocation = 
            glGetUniformLocation(program, U_POINT_LIGHT_POSITIONS);
        uPointLightColorsLocation = glGetUniformLocation(program, U_POINT_LIGHT_COLORS);
        aPositionLocation = glGetAttribLocation(program, A_POSITION);
        aNormalLocation = glGetAttribLocation(program, A_NORMAL);
    }

    /*
    public void setUniforms(float[] matrix, Vector vectorToLight) {
        glUniformMatrix4fv(uMatrixLocation, 1, false, matrix, 0);   
        glUniform3f(uVectorToLightLocation, 
            vectorToLight.x, vectorToLight.y, vectorToLight.z);
    }
     */
    
    public void setUniforms(float[] mvMatrix, float[] it_mvMatrix, float[] mvpMatrix, 
                            float[] vectorToDirectionalLight, 
                            float[] pointLightPositions, float[] pointLightColors) {          
        glUniformMatrix4fv(uMVMatrixLocation, 1, false, mvMatrix, 0);   
        glUniformMatrix4fv(uIT_MVMatrixLocation, 1, false, it_mvMatrix, 0);   
        glUniformMatrix4fv(uMVPMatrixLocation, 1, false, mvpMatrix, 0);
        glUniform3fv(uVectorToLightLocation, 1, vectorToDirectionalLight, 0);
        
        glUniform4fv(uPointLightPositionsLocation, 3, pointLightPositions, 0);            
        glUniform3fv(uPointLightColorsLocation, 3, pointLightColors, 0);
    }

    public int getPositionAttributeLocation() {
        return aPositionLocation;
    }
    
    public int getNormalAttributeLocation() {
        return aNormalLocation;
    }
}
