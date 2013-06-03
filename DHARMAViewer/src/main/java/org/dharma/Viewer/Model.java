package org.dharma.Viewer;

import android.content.res.AssetManager;
import android.util.Log;

import javax.microedition.khronos.opengles.GL10;
import java.io.IOException;
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
    public List<Mesh> mMesh = new ArrayList<Mesh>();

    public Model( AssetManager assets ){
        mManager = assets;
    }

    public void addCloud( float[] transform, float scale, int points, String path ) throws IOException {
        Data.add( new Cloud( transform, scale, points, mManager.open(Path + "/" + path) ));
    }

    public void addMesh(float[] transformation, String pointsPath, int points, String indexPath, int indicies) throws IOException {
        Log.i("Mesh", "Adding Mesh: " + pointsPath + " " + indexPath );
        mMesh.add( new Mesh( transformation, mManager.open(Path + "/" + pointsPath), points, mManager.open(Path + "/" + indexPath), indicies ) );
    }

    public void DrawClouds(GL10 gl) {
        gl.glPushMatrix();
        {
            gl.glRotatef( 270.0f, 1.0f, 0.0f, 0.0f );
            gl.glTranslatef(-Center[0], -Center[1], -Center[2]);

            for( Mesh m : mMesh ){
                m.Draw(gl);
            }

            for( Cloud c : Data ){
                c.Draw(gl);
            }
        }
        gl.glPopMatrix();
    }
}
