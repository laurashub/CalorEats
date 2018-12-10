package com.example.rose.caloreats;


import android.util.Log;

import android.content.Context;
import android.widget.ImageView;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

//* Modified from FCFireDist Storage file*//

// Store files in firebase storage
public class Storage {
    StorageReference photoStorage;
    Context context;

    private static class Holder {
        public static Storage helper = new Storage();
    }

    // Every time you need the Net object, you must get it via this helper function
    public static Storage getInstance() {
        return Holder.helper;
    }

    // Call init before using instance
    public static synchronized void init(Context context) {
        Holder.helper.context = context;
        // Initialize our reference, which is a photo collection that has per-user subcollections of photo files
        Holder.helper.photoStorage = FirebaseStorage.getInstance().getReference().child("photos");
    }

    protected StorageReference fileStorage(Food food) {
        return photoStorage
                .child(food.getFoodId())
                .child(food.getName() + ".jpg");
    }

    public void displayJpg(Food food, ImageView imageView) {

        //download jpg
        StorageReference getImage = fileStorage(food);

        if (getImage == null){
            Log.e("displayJpg", "Image is null");
        }

        GlideApp.with(context)
                .asBitmap()
                .load(getImage)
                .into(imageView);

    }
}
