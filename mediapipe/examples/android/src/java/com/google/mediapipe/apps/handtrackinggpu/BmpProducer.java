package com.google.mediapipe.apps.handtrackinggpu;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.util.Log;
import com.google.ar.core.Session;
import com.google.ar.core.Frame;

import java.nio.ByteBuffer;


public class BmpProducer extends Thread {

    CustomFrameAvailableListner customFrameAvailableListner;

    public int height = 513,width = 513;
    Bitmap bmp;

    BmpProducer(Context context, Frame frame){
        try {
            Image image = frame.acquireCameraImage();
            ByteBuffer buffer = image.getPlanes()[0].getBuffer();
            byte[] bytes = new byte[buffer.capacity()];
            buffer.get(bytes);
            bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length, null);
            //bmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.img2);
            bmp = Bitmap.createScaledBitmap(bmp,480,640,true);
            height = bmp.getHeight();
            width = bmp.getWidth();
            start();
        } catch (Throwable t) {
            // Avoid crashing the application due to unhandled exceptions.
            Log.e(TAG, "Exception on the OpenGL thread BMP PRODUCER", t);
        }
    }

    public void setCustomFrameAvailableListner(CustomFrameAvailableListner customFrameAvailableListner){
        this.customFrameAvailableListner = customFrameAvailableListner;
    }

    public static final String TAG="BmpProducer";
    @Override
    public void run() {
        super.run();
        while ((true)){
            if(bmp==null || customFrameAvailableListner == null)
                continue;
            Log.d(TAG,"Writing frame");
            customFrameAvailableListner.onFrame(bmp);
            /*OTMainActivity.imageView.post(new Runnable() {
                @Override
                public void run() {
                    OTMainActivity.imageView.setImageBitmap(bg);
                }
            });*/
            try{
                Thread.sleep(10);
            }catch (Exception e){
                Log.d(TAG,e.toString());
            }
        }
    }
}
