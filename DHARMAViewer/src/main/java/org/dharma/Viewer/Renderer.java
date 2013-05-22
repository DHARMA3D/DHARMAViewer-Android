package org.dharma.Viewer;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLSurfaceView;
import android.opengl.GLU;

public class Renderer implements GLSurfaceView.Renderer {
    public List<Model> mModels = new ArrayList<Model>();
    private float X = 0.0f;
    private float Y = 0.0f;

    public Renderer(){
		
	}
	
	@Override
	public void onDrawFrame(GL10 gl) {
        X += 0.1f;
        Y += 0.1f;
        
		gl.glClear( GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT );

        gl.glPushMatrix();
        {
            gl.glTranslatef( 0.0f, 0.0f, -mModels.get(0).Radius );
            gl.glRotatef(X, 0.0f, 1.0f, 0.0f);
            //gl.glRotatef(Y, 1.0f, 0.0f, 0.0f);

            for( Model c : mModels ){
                c.DrawClouds(gl);
            }
        }
        gl.glPopMatrix();
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		gl.glViewport(0, 0, width, height);

		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glLoadIdentity();

		GLU.gluPerspective( gl, 45.0f, (float)width / (float)height, 0.1f, 1000.0f);

		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();
	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		gl.glClearColor( 0.0f, 0.0f, 0.0f, 1.0f );
		gl.glShadeModel( GL10.GL_SMOOTH );
		gl.glClearDepthf( 1.0f );
		gl.glEnable( GL10.GL_DEPTH_TEST );
		gl.glDepthFunc( GL10.GL_LEQUAL );
		gl.glHint( GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST );

        gl.glEnable(GL10.GL_LIGHTING);
        gl.glEnable(GL10.GL_COLOR_MATERIAL);

        float[] ambient = {
                0.5f, 0.5f, 0.5f, 1.0f
        };

        gl.glLightModelfv(GL10.GL_LIGHT_MODEL_AMBIENT, ambient, 0 );

        gl.glClearColor( 0.0f, 0.0f, 0.0f, 0.0f);
        gl.glClearDepthf(1.0f);

        gl.glEnable(GL10.GL_BLEND);
        gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
	}
	
	private FloatBuffer arrayToBuffer( float[] array, int size ) {
		ByteBuffer buffer = ByteBuffer.allocateDirect( size * 4 );
		buffer.order( ByteOrder.nativeOrder() );

		FloatBuffer retVal = buffer.asFloatBuffer();
		retVal.put( array );
		retVal.position(0);

		return retVal;
	}
}

