package com.example.rose.caloreats;

import java.util.ArrayList;

public class UserData {

    int max;
    ArrayList<ArrayList<Food>> diary;
    ArrayList<String> dates;
    int[] totals;

    public UserData(){
        dates = new ArrayList<>();
        totals = new int[7];
    }

    public void calculateTotal(int i){
            totals[i] = 0;
            for (int j = 0; j < diary.get(i).size(); j++){

                Food f = diary.get(i).get(j);
                totals[i] += Integer.parseInt(f.getCals());

            }
        }

}
