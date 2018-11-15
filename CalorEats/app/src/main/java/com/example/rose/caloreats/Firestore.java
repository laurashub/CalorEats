package com.example.rose.caloreats;

//modified from FC7 code

import android.util.Log;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.OnCompleteListener;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.HashMap;
import java.util.ArrayList;

public class Firestore {

    protected FirebaseFirestore db;

    private static class Holder {
        public static Firestore helper = new Firestore();
    }
    // Every time you need the Net object, you must get it via this helper function
    public static Firestore getInstance() {
        return Holder.helper;
    }

    // Call init before using instance
    public static synchronized void init() {
        Holder.helper.db = FirebaseFirestore.getInstance();
        if( Holder.helper.db == null ) {
            Log.e("Firestore Init", "FirebaseFirestore is null!");
        }
        //Holder.helper.auth = auth;
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setTimestampsInSnapshotsEnabled(true)
                .build();
        Holder.helper.db.setFirestoreSettings(settings);
    }

    void saveFood(Food food, String date) {
        Log.d("Food",
                food.print());

        HashMap<String, Object> c = new HashMap<>();
        c.put("name", food.getName());
        c.put("cals", food.getCals());
        c.put("price", food.getPrice());

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        Holder.helper.db.collection("users/" + user.getUid() + "/" + date)
                .add(c)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("saveFood", "Food added successfully");
                    }
                });
    }


    ArrayList<Food> getFoods(String date) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final ArrayList<Food> dailyFood = new ArrayList<>();
        System.out.println("Query on: users/" + user.getUid() + "/" + date);

        db.collection("users/" + user.getUid() + "/" + date)
            .get()
            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.d("QUERY SUCCESS", document.getId() + " => " + document.getData());
                        Food food = new Food(document.get("name").toString(),
                                document.get("cals").toString(),
                                document.get("price").toString());

                        dailyFood.add(food);
                    }
                } else {
                    Log.d("QUERY ERROR", "Error getting documents");
                }
            }
        });

        return dailyFood;
    }

    ArrayList<ArrayList<Food>> getDiary(ArrayList<String> dates) {

        ArrayList<ArrayList<Food>> temp = new ArrayList<>();

        long millis = System.currentTimeMillis();

        //construct diary
        for (int i = 0; i < 7; i++) {

            java.sql.Date date = new java.sql.Date(millis - (i * (24*60*60*1000)) );
            String dateQuery = date.toString();
            dates.add(dateQuery);
            temp.add(i, Firestore.getInstance().getFoods(dateQuery));
        }

        return temp;
    }
}