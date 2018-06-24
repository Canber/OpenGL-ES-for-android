/***
 * Excerpted from "OpenGL ES for Android",
 * published by The Pragmatic Bookshelf.
 * Copyrights apply to this code. It may not be used to create training material, 
 * courses, books, articles, and the like. Contact us if you are in doubt.
 * We make no guarantees that this code is fit for any purpose. 
 * Visit http://www.pragmaticprogrammer.com/titles/kbogla for more book information.
***/
package com.particles.android.wallpaper;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.opengl.GLSurfaceView;
import android.os.Build;
import android.service.wallpaper.WallpaperService;
import android.util.Log;
import android.view.SurfaceHolder;
import android.widget.Toast;

import com.particles.android.ParticlesRenderer;
import com.particles.android.util.LoggerConfig;

public class GLWallpaperService extends WallpaperService {
    @Override
    public Engine onCreateEngine() {
        return new GLEngine();
    }
    public class GLEngine extends Engine {
        private static final String TAG = "GLEngine";
        
        private WallpaperGLSurfaceView glSurfaceView;
        private ParticlesRenderer particlesRenderer;
        private boolean rendererSet;
        
        
        @Override
        public void onCreate(SurfaceHolder surfaceHolder) {
            super.onCreate(surfaceHolder);            
            if (LoggerConfig.ON) {
                Log.d(TAG, "onCreate(" + surfaceHolder + ")");
            }
            glSurfaceView = new WallpaperGLSurfaceView(GLWallpaperService.this);
            
            // Check if the system supports OpenGL ES 2.0.
            ActivityManager activityManager = 
                (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
            ConfigurationInfo configurationInfo = activityManager
                .getDeviceConfigurationInfo();
            // Even though the latest emulator supports OpenGL ES 2.0,
            // it has a bug where it doesn't set the reqGlEsVersion so
            // the above check doesn't work. The below will detect if the
            // app is running on an emulator, and assume that it supports
            // OpenGL ES 2.0.
            
            final boolean supportsEs2 =
                configurationInfo.reqGlEsVersion >= 0x20000
                    // Check for emulator.
                    || (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1
                     && (Build.FINGERPRINT.startsWith("generic")
                      || Build.FINGERPRINT.startsWith("unknown")
                      || Build.MODEL.contains("google_sdk")
                      || Build.MODEL.contains("Emulator")
                      || Build.MODEL.contains("Android SDK built for x86")));

            /*
            final ParticlesRenderer particlesRenderer = 
                new ParticlesRenderer(GLWallpaperService.this);
            */
            particlesRenderer = new ParticlesRenderer(GLWallpaperService.this);
                
            if (supportsEs2) {
                glSurfaceView.setEGLContextClientVersion(2);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    glSurfaceView.setPreserveEGLContextOnPause(true);
                }
                glSurfaceView.setRenderer(particlesRenderer);
                rendererSet = true;
            } else {
                /*
                 * This is where you could create an OpenGL ES 1.x compatible
                 * renderer if you wanted to support both ES 1 and ES 2. Since 
                 * we're not doing anything, the app will crash if the device 
                 * doesn't support OpenGL ES 2.0. If we publish on the market, we 
                 * should also add the following to AndroidManifest.xml:
                 * 
                 * <uses-feature android:glEsVersion="0x00020000"
                 * android:required="true" />
                 * 
                 * This hides our app from those devices which don't support OpenGL
                 * ES 2.0.
                 */
                Toast.makeText(GLWallpaperService.this, 
                    "This device does not support OpenGL ES 2.0.", 
                    Toast.LENGTH_LONG).show();
                return;
            }              
        }
        
        @Override
        public void onVisibilityChanged(boolean visible) {
            super.onVisibilityChanged(visible);
            if (LoggerConfig.ON) {
                Log.d(TAG, "onVisibilityChanged(" + visible + ")");
            }                                   
            if (rendererSet) {
                if (visible) {
                    glSurfaceView.onResume();
                } else {                   
                    glSurfaceView.onPause();
                }
            }
        }   
        @Override
        public void onOffsetsChanged(final float xOffset, final float yOffset,
            float xOffsetStep, float yOffsetStep, int xPixelOffset, int yPixelOffset) {
            glSurfaceView.queueEvent(new Runnable() {                
                @Override
                public void run() {
                    particlesRenderer.handleOffsetsChanged(xOffset, yOffset);                    
                }
            });            
        }
        @Override
        public void onDestroy() {
            super.onDestroy();
            if (LoggerConfig.ON) {
                Log.d(TAG, "onDestroy()");
            }
            glSurfaceView.onWallpaperDestroy();
        }
        class WallpaperGLSurfaceView extends GLSurfaceView {
            private static final String TAG = "WallpaperGLSurfaceView";
            WallpaperGLSurfaceView(Context context) {
                super(context);

                if (LoggerConfig.ON) {
                    Log.d(TAG, "WallpaperGLSurfaceView(" + context + ")");
                }
            }            
            @Override
            public SurfaceHolder getHolder() {
                if (LoggerConfig.ON) {
                    Log.d(TAG, "getHolder(): returning " + getSurfaceHolder());
                }
                return getSurfaceHolder();
            }
            public void onWallpaperDestroy() {
                if (LoggerConfig.ON) {
                    Log.d(TAG, "onWallpaperDestroy()");
                }
                super.onDetachedFromWindow();
            }
        }
    }
}

