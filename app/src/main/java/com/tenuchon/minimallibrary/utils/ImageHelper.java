package com.tenuchon.minimallibrary.utils;

import android.content.Context;
import android.net.Uri;
import android.widget.ImageView;


import androidx.core.content.FileProvider;

import com.squareup.picasso.Picasso;
import com.tenuchon.minimallibrary.R;

import java.io.File;

public class ImageHelper {
    private static final String path = "com.tenuchon.android.minimallibrary.fileprovider";

    public static Uri getUriImage(Context context, File file) {
        return FileProvider.getUriForFile(context, path, file);
    }

    public static void deleteImage(Context context, File file) {
        Uri uri = getUriImage(context, file);
        context.getContentResolver().delete(uri, null, null);
        Picasso.get().invalidate(file);
    }

    public static void loadImageAndSetView(File file, ImageView view, boolean fit) {
        if (fit)
            Picasso.get().load(file).placeholder(R.drawable.ic_book_cover_default).fit().into(view);
        else Picasso.get().load(file).placeholder(R.drawable.ic_book_cover_default).into(view);
    }
}
