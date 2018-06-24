/***
 * Excerpted from "OpenGL ES for Android",
 * published by The Pragmatic Bookshelf.
 * Copyrights apply to this code. It may not be used to create training material, 
 * courses, books, articles, and the like. Contact us if you are in doubt.
 * We make no guarantees that this code is fit for any purpose. 
 * Visit http://www.pragmaticprogrammer.com/titles/kbogla for more book information.
***/
package com.nextstep.android;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ConfigurationInfo;
import android.opengl.GLSurfaceView;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.Toast;

import com.nextstep.android.ChooseFilterMode.ChooseFilterModeListener;
import com.nextstep.android.util.TextureHelper.FilterMode;

public class ParticlesActivity extends FragmentActivity implements
    ChooseFilterModeListener {
    private static final String ENABLE_AA = "ENABLE_AA";
    
    /**
     * Hold a reference to our GLSurfaceView
     */
    private GLSurfaceView glSurfaceView;
    private ParticlesRenderer particlesRenderer;
    private boolean rendererSet = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        final boolean aaEnabledForThisRun = getIntent() != null && getIntent().getAction() != null && getIntent().getAction().equals(ENABLE_AA);
        
        setContentView(R.layout.main);
        glSurfaceView = (GLSurfaceView) findViewById(R.id.glSurfaceView);

        findViewById(R.id.textureFilterChooser).setOnClickListener(
            new OnClickListener() {
                @Override
                public void onClick(View v) {
                    ChooseFilterMode.show(getSupportFragmentManager());
                }
            });
        
        if (aaEnabledForThisRun) {
            ((Button)findViewById(R.id.toggleAA)).setText(R.string.disableAa);
        }
        
        findViewById(R.id.toggleAA).setOnClickListener(new OnClickListener() {            
            @Override
            public void onClick(View v) {
                final Intent intent = new Intent(ParticlesActivity.this, ParticlesActivity.class);
                
                if (!aaEnabledForThisRun) {
                    intent.setAction(ENABLE_AA);
                }
                
                startActivity(intent);
                finish();                
            }
        });

        // Check if the system supports OpenGL ES 2.0.
        ActivityManager activityManager =
            (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        ConfigurationInfo configurationInfo =
            activityManager.getDeviceConfigurationInfo();
        // Even though the latest emulator supports OpenGL ES 2.0,
        // it has a bug where it doesn't set the reqGlEsVersion so
        // the above check doesn't work. The below will detect if the
        // app is running on an emulator, and assume that it supports
        // OpenGL ES 2.0.
        final boolean supportsEs2 =
            configurationInfo.reqGlEsVersion >= 0x20000
                || (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1 && (Build.FINGERPRINT
                    .startsWith("generic")
                    || Build.FINGERPRINT.startsWith("unknown")
                    || Build.MODEL.contains("google_sdk")
                    || Build.MODEL.contains("Emulator") || Build.MODEL
                        .contains("Android SDK built for x86")));

        particlesRenderer = new ParticlesRenderer(this);

        if (supportsEs2) {
            // Request an OpenGL ES 2.0 compatible context.
            glSurfaceView.setEGLContextClientVersion(2);                        
            
            if (aaEnabledForThisRun) {
                glSurfaceView.setEGLConfigChooser(new MultisampleConfigChooser());
            }

            // Assign our renderer.
            glSurfaceView.setRenderer(particlesRenderer);
            rendererSet = true;
        } else {
            /*
             * This is where you could create an OpenGL ES 1.x compatible
             * renderer if you wanted to support both ES 1 and ES 2. Since we're
             * not doing anything, the app will crash if the device doesn't
             * support OpenGL ES 2.0. If we publish on the market, we should
             * also add the following to AndroidManifest.xml:
             * 
             * <uses-feature android:glEsVersion="0x00020000"
             * android:required="true" />
             * 
             * This hides our app from those devices which don't support OpenGL
             * ES 2.0.
             */
            Toast.makeText(this, "This device does not support OpenGL ES 2.0.",
                Toast.LENGTH_LONG).show();
            return;
        }

        glSurfaceView.setOnTouchListener(new OnTouchListener() {
            float previousX, previousY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event != null) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        previousX = event.getX();
                        previousY = event.getY();
                    } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
                        final float deltaX = event.getX() - previousX;
                        final float deltaY = event.getY() - previousY;

                        previousX = event.getX();
                        previousY = event.getY();

                        glSurfaceView.queueEvent(new Runnable() {
                            @Override
                            public void run() {
                                particlesRenderer.handleTouchDrag(deltaX,
                                    deltaY);
                            }
                        });
                    }

                    return true;
                } else {
                    return false;
                }
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (rendererSet) {
            glSurfaceView.onPause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (rendererSet) {
            glSurfaceView.onResume();
        }
    }

    @Override
    public void onChooseFilter(final FilterMode filterMode) {
        if (!particlesRenderer.supportsAnisotropicFiltering()
            && filterMode == FilterMode.ANISOTROPIC) {
            Toast.makeText(this, getString(R.string.noAnisotropicFiltering), Toast.LENGTH_LONG).show();
        } else {

            glSurfaceView.queueEvent(new Runnable() {
                @Override
                public void run() {
                    particlesRenderer.handleFilterModeChange(filterMode);
                }
            });
        }
    }
}