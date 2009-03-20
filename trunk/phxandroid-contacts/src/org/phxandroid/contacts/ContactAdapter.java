package org.phxandroid.contacts;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.Contacts;
import android.provider.Contacts.People;
import android.provider.Contacts.Photos;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

public class ContactAdapter extends BaseAdapter implements ListAdapter {
    private static final String TAG        = "ContactAdapter";
    private static String[]     PROJECTION = new String[] { People._ID, People.NAME };
    private List<ContactInfo>   contacts   = new ArrayList<ContactInfo>();
    private LayoutInflater      mInflater;

    public ContactAdapter(Context context) {
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        Cursor c = context.getContentResolver()
                .query(People.CONTENT_URI, PROJECTION, "name not null", null, "name ASC");

        try {
            int idxId = c.getColumnIndexOrThrow(People._ID);
            int idxName = c.getColumnIndexOrThrow(People.NAME);

            c.moveToFirst();
            do {
                ContactInfo info = new ContactInfo();
                info.id = c.getInt(idxId);
                info.name = c.getString(idxName);
                info.avatar = getContactPhotoLong(context, info.id);

                contacts.add(info);
            } while (c.moveToNext());
        } catch (Throwable t) {
            Log.e(TAG, "Cursor error", t);
        } finally {
            c.close();
        }
    }

    private Bitmap getContactPhoto(Context context, int personId) {
        Uri personURI = Uri.withAppendedPath(People.CONTENT_URI, String.valueOf(personId));
        return People.loadContactPhoto(context, personURI, R.drawable.icon, null);
    }

    private Bitmap getContactPhotoLong(Context context, int personId) {
        Uri person = Uri.withAppendedPath(People.CONTENT_URI, String.valueOf(personId));
        if (person == null) {
            return toBitmap(context, R.drawable.icon);
        }

        Uri photoUri = Uri.withAppendedPath(person, Contacts.Photos.CONTENT_DIRECTORY);
        Cursor cursor = context.getContentResolver().query(photoUri, new String[] { Photos.DATA }, null, null, null);
        try {
            if (!cursor.moveToNext()) {
                return null;
            }
            byte[] data = cursor.getBlob(0);
            if (data == null) {
                return null;
            }
            InputStream stream = new ByteArrayInputStream(data);
            if (stream == null) {
                return toBitmap(context, R.drawable.icon);
            }
            Bitmap bm = BitmapFactory.decodeStream(stream, null, null);
            if (bm == null) {
                return toBitmap(context, R.drawable.icon);
            }
            return bm;
        } finally {
            cursor.close();
        }
    }

    private Bitmap toBitmap(Context context, int drawableId) {
        if (drawableId == 0) {
            return null;
        }
        return BitmapFactory.decodeResource(context.getResources(), drawableId, null);
    }

    @Override
    public int getCount() {
        return contacts.size();
    }

    @Override
    public Object getItem(int position) {
        return contacts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;

        if (convertView == null) {
            view = mInflater.inflate(R.layout.contact, parent, false);
        } else {
            view = convertView;
        }

        ImageView img = (ImageView) view.findViewById(R.id.avatar);
        TextView name = (TextView) view.findViewById(R.id.name);

        ContactInfo info = contacts.get(position);
        img.setImageBitmap(info.avatar);
        name.setText(info.name);

        return view;
    }

    class ContactInfo {
        int    id;
        String name;
        Bitmap avatar;
    }
}
