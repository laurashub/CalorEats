package com.example.rose.caloreats;

import android.content.Context;
import android.widget.ImageView;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import android.support.annotation.NonNull;
import android.widget.Toast;

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

    /*
    public void uploadJpg (Food photoObject, byte[] data) {
        StorageReference newImage = fileStorage(photoObject);
        UploadTask uploadTask = newImage.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {

            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

            }
        });
    }*/

    public void displayJpg(Food food, ImageView imageView) {

        //download jpg
        StorageReference getImage = fileStorage(food);

        if (getImage == null){
            System.out.println("IMAGE IS NULL");
        }

        GlideApp.with(context)
                .asBitmap()
                .load(getImage)
                .into(imageView);

    }
}
