package org.phxandroid.logos;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class AssetAdapter extends BaseAdapter {
    @SuppressWarnings("unused")
    private static final String TAG      = "AssetAdapter";
    private static AssetAdapter INSTANCE = new AssetAdapter();

    public static AssetAdapter getInstance() {
        return INSTANCE;
    }

    public class AssetRef {
        private Bitmap icon;
        private Bitmap bitmap;
        private String name;

        public AssetRef(String name, Bitmap bitmap, Bitmap icon) {
            super();
            this.name = name;
            this.bitmap = bitmap;
            this.icon = icon;
        }

        public Bitmap getBitmap() {
            return bitmap;
        }

        public String getName() {
            return name;
        }

        public Bitmap getIcon() {
            return icon;
        }
    }

    private List<AssetRef> logos       = new ArrayList<AssetRef>();
    private LayoutInflater inflater;
    private boolean        initialized = false;

    public boolean isInitialized() {
        return initialized;
    }

    public void init(Context context) throws IOException {
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        initialized = true;
        AssetManager assets = context.getAssets();

        Pattern pat = Pattern.compile("phxandroid-(.*)-logo.png");
        Matcher mat;

        // Discover the available logos in the assets directory, load them as bitmaps, and create logos array.
        for (String filename : assets.list("")) {
            mat = pat.matcher(filename);
            if (mat.matches()) {
                InputStream in = null;
                try {
                    String name = Character.toUpperCase(mat.group(1).charAt(0)) + mat.group(1).substring(1);
                    in = assets.open(filename);
                    Bitmap bm = BitmapFactory.decodeStream(in);
                    Bitmap icon = Bitmap.createScaledBitmap(bm, 60, 60, true);

                    logos.add(new AssetRef(name, bm, icon));
                } finally {
                    close(in);
                }
            }
        }
    }

    private void close(InputStream in) {
        if (in == null) {
            return;
        }
        try {
            in.close();
        } catch (IOException ignore) {
            /* ignore */
        }
    }

    @Override
    public int getCount() {
        return logos.size();
    }

    @Override
    public Object getItem(int position) {
        return logos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;

        if (convertView == null) {
            view = inflater.inflate(R.layout.logo_icon, parent, false);
        } else {
            view = convertView;
        }

        ImageView img = (ImageView) view.findViewById(R.id.icon);
        TextView name = (TextView) view.findViewById(R.id.name);

        AssetRef ref = logos.get(position);
        img.setImageBitmap(ref.icon);
        name.setText(ref.name);
        view.setTag(ref);

        return view;
    }

    public AssetRef getAssetRef(String name) {
        for (AssetRef ref : logos) {
            if (ref.getName().equals(name)) {
                return ref;
            }
        }

        return null;
    }

    public AssetRef getAssetRef(int position) {
        return logos.get(position);
    }
}
