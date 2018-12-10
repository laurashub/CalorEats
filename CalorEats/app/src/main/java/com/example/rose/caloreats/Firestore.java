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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.SetOptions;

import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Context;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.Map;

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
        c.put("resname", food.getRestaurant());

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


    public void getFoods(final String date, final DiaryAdapter da,
                                    final Graph weekly, final TextView totalTV,
                                    final HashMap<String, Integer> totals) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

            final ArrayList<Food> dailyFood = new ArrayList<>();

            db.collection("users/" + user.getUid() + "/" + date)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                int total = 0;
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Log.d("QUERY SUCCESS", document.getId() + " => " + document.getData());
                                    Food food = new Food(document.get("name").toString(),
                                            document.get("cals").toString(),
                                            document.get("price").toString());
                                    if (document.get("resname") != null) {
                                        food.setRestaurant(document.get("resname").toString());
                                    }
                                    total += Integer.parseInt(food.getCals());
                                    dailyFood.add(food);
                                }

                                if (da != null) {
                                    da.setItems(dailyFood);
                                }

                                if (weekly != null){
                                    weekly.updateDiary(date, dailyFood);
                                }

                                if (totalTV != null){
                                    totalTV.setText(Integer.toString(total));
                                }

                                if (totals != null){
                                    totals.put(date, total);
                                }

                            } else {
                                Log.d("QUERY ERROR", "Error getting documents");
                            }
                        }
                    });

    }

    public void todaysCounts(HashMap<String, Integer> data){
        getCalLimit (null, null, data);
        getFoods(getDateArray().get(0), null, null, null, data);

    }

    public ArrayList<String> getDateArray(){

        ArrayList<String> dates = new ArrayList<>();

        long millis = System.currentTimeMillis();

        //construct diary
        for (int i = 0; i < 7; i++) {

            java.sql.Date date = new java.sql.Date(millis - (i * (24*60*60*1000)) );
            String dateQuery = date.toString();
            dates.add(dateQuery);
        }

        return dates;
    }

    public void getCalLimit(final EditText cal_limit, final TextView textView, final HashMap<String, Integer> totals){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        DocumentReference docRef = db.collection("users").document(user.getUid());

        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()){
                    if (cal_limit != null) {
                        cal_limit.setText(documentSnapshot.get("limit").toString());
                    }

                    if (textView != null){
                        textView.setText(documentSnapshot.get("limit").toString());
                    }

                    if (totals != null){
                        totals.put("limit", Integer.parseInt(documentSnapshot.get("limit").toString()));
                    }

                } else {
                    Log.d("getCalLimit", "Unable to retrieve calorie limit");
                }
            }
        });

    }

    public void setCalLimit(Context context, int limit){
        if (limit < 1200 || limit > 4000){
            Toast.makeText(context,
                    "Unable to set calorie limit to this value " +
                            "- please pick a value between 1200 and 4000", Toast.LENGTH_LONG).show();
        } else {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            Map<String, Integer> map = new HashMap<>();
            map.put("limit", limit);
            db.collection("users").document(user.getUid()).set(map, SetOptions.merge());
        }
    }


}