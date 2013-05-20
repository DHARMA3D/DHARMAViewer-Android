package org.dharma.Viewer;

import android.content.res.AssetManager;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Setup View
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Store Asset Manager
        mAssets = this.getAssets();
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
