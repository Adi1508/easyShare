package com.example.aditya.easyshare;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

public class ImageCompression extends AppCompatActivity {

    private static ImageCompression imageCompress=null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imageCompress=this;
    }
    public static Context context(){
        return imageCompress.getApplicationContext();
    }

    public static Bitmap decodeImage(int resourceId){
        try{
            BitmapFactory.Options o=new BitmapFactory.Options();
            o.inJustDecodeBounds=true;
            BitmapFactory.decodeResource(imageCompress.getResources(), resourceId, o);

            final int REQUIRED_SIZE=100;

            int scale=1;
            while(o.outWidth/scale/2 >= REQUIRED_SIZE && o.outHeight/scale/2>=REQUIRED_SIZE){
                scale*=2;
            }

            BitmapFactory.Options o2=new BitmapFactory.Options();
            o2.inSampleSize=scale;
            return BitmapFactory.decodeResource(imageCompress.getResources(), resourceId, o2);

        }catch ( Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
