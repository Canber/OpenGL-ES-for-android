/***
 * Excerpted from "OpenGL ES for Android",
 * published by The Pragmatic Bookshelf.
 * Copyrights apply to this code. It may not be used to create training material, 
 * courses, books, articles, and the like. Contact us if you are in doubt.
 * We make no guarantees that this code is fit for any purpose. 
 * Visit http://www.pragmaticprogrammer.com/titles/kbogla for more book information.
***/
package com.particles.android.objects;

import static android.opengl.GLES20.GL_POINTS;
import static android.opengl.GLES20.glDrawArrays;
import static com.particles.android.Constants.BYTES_PER_FLOAT;
import android.graphics.Color;

import com.particles.android.data.VertexArray;
import com.particles.android.programs.ParticleShaderProgram;
import com.particles.android.util.Geometry.Point;
import com.particles.android.util.Geometry.Vector;

public class ParticleSystem {
    private static final int POSITION_COMPONENT_COUNT = 3;
    private static final int COLOR_COMPONENT_COUNT = 3;
    private static final int VECTOR_COMPONENT_COUNT = 3;    
    private static final int PARTICLE_START_TIME_COMPONENT_COUNT = 1;

    private static final int TOTAL_COMPONENT_COUNT = 
        POSITION_COMPONENT_COUNT
      + COLOR_COMPONENT_COUNT 
      + VECTOR_COMPONENT_COUNT      
      + PARTICLE_START_TIME_COMPONENT_COUNT;

    private static final int STRIDE = TOTAL_COMPONENT_COUNT * BYTES_PER_FLOAT;

    private final float[] particles;
    private final VertexArray vertexArray;
    private final int maxParticleCount;

    private int currentParticleCount;
    private int nextParticle;

    public ParticleSystem(int maxParticleCount) {
        particles = new float[maxParticleCount * TOTAL_COMPONENT_COUNT];
        vertexArray = new VertexArray(particles);
        this.maxParticleCount = maxParticleCount;
    }
    
    public void addParticle(Point position, int color, Vector direction,
        float particleStartTime) {                
        final int particleOffset = nextParticle * TOTAL_COMPONENT_COUNT;
		
        int currentOffset = particleOffset;        
        nextParticle++;
        
        if (currentParticleCount < maxParticleCount) {
            currentParticleCount++;
        }
        
        if (nextParticle == maxParticleCount) {
            // Start over at the beginning, but keep currentParticleCount so
            // that all the other particles still get drawn.
            nextParticle = 0;
        }  
        
        particles[currentOffset++] = position.x;
        particles[currentOffset++] = position.y;
        particles[currentOffset++] = position.z;
        
        particles[currentOffset++] = Color.red(color) / 255f;
        particles[currentOffset++] = Color.green(color) / 255f;
        particles[currentOffset++] = Color.blue(color) / 255f;
        
        particles[currentOffset++] = direction.x;
        particles[currentOffset++] = direction.y;
        particles[currentOffset++] = direction.z;             
        
        particles[currentOffset++] = particleStartTime;
              
        vertexArray.updateBuffer(particles, particleOffset, TOTAL_COMPONENT_COUNT);
    }

    public void bindData(ParticleShaderProgram particleProgram) {
        int dataOffset = 0;
        vertexArray.setVertexAttribPointer(dataOffset,
            particleProgram.getPositionAttributeLocation(),
            POSITION_COMPONENT_COUNT, STRIDE);
        dataOffset += POSITION_COMPONENT_COUNT;
        
        vertexArray.setVertexAttribPointer(dataOffset,
            particleProgram.getColorAttributeLocation(),
            COLOR_COMPONENT_COUNT, STRIDE);        
        dataOffset += COLOR_COMPONENT_COUNT;
        
        vertexArray.setVertexAttribPointer(dataOffset,
            particleProgram.getDirectionVectorAttributeLocation(),
            VECTOR_COMPONENT_COUNT, STRIDE);
        dataOffset += VECTOR_COMPONENT_COUNT;       
        
        vertexArray.setVertexAttribPointer(dataOffset,
            particleProgram.getParticleStartTimeAttributeLocation(),
            PARTICLE_START_TIME_COMPONENT_COUNT, STRIDE);
    }

    public void draw() {
        glDrawArrays(GL_POINTS, 0, currentParticleCount);
    }
}
