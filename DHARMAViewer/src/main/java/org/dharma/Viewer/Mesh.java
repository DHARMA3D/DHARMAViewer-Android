package org.dharma.Viewer;

import android.util.Log;

import javax.microedition.khronos.opengles.GL10;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

/**
 * Date: 5/21/13
 * Author: James Sweet
 */
public class Mesh {
    public int Verticies;
    public int Indicies;

    public FloatBuffer Transformation;
    private ByteBuffer DataBuffer = null;
    private ByteBuffer IndexBuffer = null;

    public Mesh( float[] transform, InputStream vertex, int points, InputStream index, int indicies){
        Verticies = points;
        Indicies = indicies;

        try{
            // Setup Vertex Data
            ByteArrayOutputStream vData = new ByteArrayOutputStream();
            copy(vertex, vData);

            DataBuffer = ByteBuffer.allocateDirect(vData.size());
            DataBuffer.order(ByteOrder.nativeOrder());
            DataBuffer.put(vData.toByteArray());
            DataBuffer.rewind();

            // Setup Index Data
            ByteArrayOutputStream vIndex = new ByteArrayOutputStream();
            copy(index, vIndex);

            ByteBuffer Temp = ByteBuffer.allocateDirect(vIndex.size());
            Temp.order(ByteOrder.nativeOrder());
            Temp.put(vIndex.toByteArray());
            Temp.rewind();

            IndexBuffer = ByteBuffer.allocateDirect(vIndex.size() / 2);
            IndexBuffer.order(ByteOrder.nativeOrder());

            IntBuffer iTemp = Temp.asIntBuffer();
            while( iTemp.remaining() > 0 ){
                IndexBuffer.putShort( (short)iTemp.get() );
            }
            IndexBuffer.rewind();

            // Setup Transformation
            ByteBuffer TempBuffer = ByteBuffer.allocateDirect(16 * 4);
            TempBuffer.order(ByteOrder.nativeOrder());

            Transformation = TempBuffer.asFloatBuffer();
            Transformation.put(transform);
            Transformation.rewind();
        }catch( Exception e ){
            Log.e("Mesh", e.toString());
        }
    }

    public void Draw( GL10 gl ){
        gl.glPushMatrix();
        {
            gl.glMultMatrixf(Transformation);

            gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
            gl.glEnableClientState(GL10.GL_NORMAL_ARRAY);

            ByteBuffer vertex = DataBuffer.duplicate();
            gl.glVertexPointer(3, GL10.GL_FLOAT, 32, vertex);

            ByteBuffer normal = DataBuffer.duplicate();
            normal.position(12);
            gl.glNormalPointer(GL10.GL_FLOAT, 32, normal);

            gl.glDrawElements(GL10.GL_TRIANGLES, Indicies, GL10.GL_UNSIGNED_SHORT, IndexBuffer);

            gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
            gl.glDisableClientState(GL10.GL_NORMAL_ARRAY);
        }
        gl.glPopMatrix();
    }

    public static int copy(InputStream in, OutputStream out) throws IOException {
        int count = 0;
        int read = 0;
        byte[] buffer = new byte[1024];
        while (read != -1) {
            read = in.read(buffer);
            if (read != -1) {
                count += read;
                out.write(buffer,0,read);
            }
        }
        return count;
    }
}
