package org.phxandroid.logos;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SpinnerAdapter;

public class AssetAdapter extends BaseAdapter implements SpinnerAdapter {
    private static final String TAG = "AssetAdapter";
    
    public class AssetRef {
        private Bitmap bitmap;
        private String name;

        public AssetRef(String name, Bitmap bitmap) {
            super();
            this.name = name;
            this.bitmap = bitmap;
        }

        public Bitmap getBitmap() {
            return bitmap;
        }

        public String getName() {
            return name;
        }
    }
    
    private List<AssetRef> logos = new ArrayList<AssetRef>();

    public AssetAdapter(Context context) throws IOException {
        Pattern pat = Pattern.compile("phxandroid-(.*)-logo.png");
        Matcher mat;
        
        // Discover the available logos in the assets directory, load them as bitmaps, and create logos array.
        for(String filename: context.getAssets().list("")) {
            Log.i(TAG, "Found asset name: " + filename);
            mat = pat.matcher(filename);
            if(mat.matches()) {
                
            }
        }
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        return null;
    }

}
