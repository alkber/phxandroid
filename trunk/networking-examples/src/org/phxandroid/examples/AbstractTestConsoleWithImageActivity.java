package org.phxandroid.examples;

import java.io.InputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

public abstract class AbstractTestConsoleWithImageActivity extends AbstractTestConsoleActivity {
    protected ImageView imgView;

    @Override
    protected int getLayoutId() {
        return R.layout.testconsole_withimage;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.imgView = (ImageView) findViewById(R.id.image);
        Log.i("PhxAndroid", "ImageView = " + imgView);
        this.imgView.setImageResource(R.drawable.placeholder);
    }

    protected void setImage(Bitmap bitmap) {
        if (bitmap == null) {
            this.imgView.setImageResource(R.drawable.placeholder);
            return;
        }
        this.imgView.setImageBitmap(bitmap);
    }

    protected void setImage(byte buf[]) {
        Bitmap bitmap = BitmapFactory.decodeByteArray(buf, 0, buf.length);
        setImage(bitmap);
    }

    protected void setImage(InputStream stream) {
        Bitmap bitmap = BitmapFactory.decodeStream(stream);
        setImage(bitmap);
    }
}
