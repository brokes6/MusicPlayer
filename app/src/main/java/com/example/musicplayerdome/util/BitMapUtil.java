package com.example.musicplayerdome.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;


public class BitMapUtil {
    private static final String TAG = "BitMapUtil";
    private static Bitmap bitmap;

    public Bitmap getBitmap(ImageView imageView) {
        return ((BitmapDrawable) imageView.getDrawable()).getBitmap();
    }

    public static Bitmap getBitmaps(final String url) {
        URL imageurl = null;
        try {
            imageurl = new URL(url);
            Log.e(TAG, "run: 内部当前的URL为====>" + imageurl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try {
            HttpsURLConnection conn = (HttpsURLConnection) imageurl.openConnection();
            conn.setDoInput(true);
            conn.connect();
            InputStream is = conn.getInputStream();
            Log.e(TAG, "run: " + is.available());
            int len = -1;
            byte[] bytes = new byte[1024];
            while ((len = is.read(bytes)) != -1) {
                String str = new String(bytes, 0, len);
                Log.e(TAG, "run: " + str);
            }
            bitmap = BitmapFactory.decodeStream(is);
            Log.e(TAG, "getBitmaps: 当前转换的Bitmap为===>" + bitmap);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

}
