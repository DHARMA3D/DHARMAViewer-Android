package org.dharma.Viewer;

import android.content.res.AssetManager;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;

/**
 * Date: 5/19/13
 * Author: James Sweet
 */
public class Main extends Activity {
    private AssetManager mAssets;

    // UI Fields
    private GLSurfaceView mRendererView;
    private Renderer mRenderer = new Renderer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Setup View
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Store Asset Manager
        mAssets = this.getAssets();

        // Setup UI
        mRendererView = (GLSurfaceView)findViewById( R.id.renderer );
        mRendererView.setRenderer(mRenderer);
    }

    @Override
    protected void onResume(){
        super.onResume();

        try{
            // Look at all files in the base asset directory
            for( String file: mAssets.list("") ){

                // Test if they are xml descriptor files
                if( file.contains(".xml") ){
                    Log.i("Main", "Model File: " + file );

                    for( Model m : DharmaXmlParser.Parse( mAssets, file ) ){
                        Log.i("Model", "Title: " + m.Title);
                        Log.i("Model", "Radius: " + m.Radius);
                        Log.i("Model", "View: " + m.View);
                        Log.i("Model", "Center: " + m.Center);
                        Log.i("Model", "Path: " + m.Path);

                        mRenderer.mModels.add( m );
                    }
                }
            }
        }catch( Exception e ){
            Log.e("Main", e.toString());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
}
