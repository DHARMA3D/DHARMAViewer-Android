package org.dharma.Viewer;

import android.content.res.AssetManager;
import android.renderscript.Matrix4f;

import javax.microedition.khronos.opengles.GL10;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * Date: 5/19/13
 * Author: James Sweet
 */
public class Model {
    private final AssetManager mManager;

    public String Title;
    public float Radius;
    public float[] View;
    public float[] Center;
    public String Path;
    public List<Cloud> Data = new ArrayList<Cloud>();

    public Model( AssetManager assets ){
        mManager = assets;
    }

    public void addCloud( float[] transform, float scale, int points, String path ) throws IOException {
        Data.add( new Cloud( transform, scale, points, mManager.open(Path + "/" + path) ));
    }

    public void DrawClouds(GL10 gl) {
        gl.glPushMatrix();
        {
            gl.glRotatef( 270.0f, 1.0f, 0.0f, 0.0f );
            gl.glTranslatef(-Center[0], -Center[1], -Center[2]);

            for( Cloud c : Data ){
                c.Draw(gl);
            }
        }
        gl.glPopMatrix();
    }
}
