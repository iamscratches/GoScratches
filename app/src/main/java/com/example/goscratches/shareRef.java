package com.example.goscratches;


import android.content.Context;
import android.content.SharedPreferences;

public class shareRef {
    SharedPreferences SharedRef;
    static Context context;
    public shareRef(Context context){
        this.context = context;
        SharedRef = context.getSharedPreferences("myRef", Context.MODE_PRIVATE);
        if(SharedRef.contains("Name")==false){
            SharedPreferences.Editor editor = SharedRef.edit();
            editor.putString("Name", "Guest");
            editor.putInt("Age", 21);
            editor.putInt("Cherry", 0);
            editor.putFloat("Distance", 0.0f);
            editor.putInt("SuperCherry", 0);
            editor.commit();
        }
    }

    public void saveData(String name, int age, int cherry, float distance, int supercherry){
        SharedPreferences.Editor editor = SharedRef.edit();
        editor.putString("Name", name);
        editor.putInt("Age", age);
        editor.putInt("Cherry", cherry);
        editor.putFloat("Distance", distance);
        editor.putInt("SuperCherry", supercherry);
        editor.commit();
    }
    public void saveData(String name, int age){
        SharedPreferences.Editor editor = SharedRef.edit();
        editor.putString("Name", name);
        editor.putInt("Age", age);
        editor.commit();
    }
    public StoredData loadData(){
        StoredData sd = new StoredData();
        sd.Name = SharedRef.getString("Name", "Guest");
        sd.Age = SharedRef.getInt("Age", 21);
        sd.Cherry = SharedRef.getInt("Cherry", 0);
        sd.SuperCherry = SharedRef.getInt("SuperCherry", 0);
        sd.Distance = SharedRef.getFloat("Distance", 0);

        return sd;
    }
    public void saveData(int cherryValue){
        SharedPreferences.Editor editor = SharedRef.edit();
        StoredData sd = new StoredData();
        cherryValue += SharedRef.getInt("Cherry", 0);
        editor.putInt("Cherry", cherryValue);
        editor.commit();
    }
    public void saveData(int superCherry, int cherryValue){
        SharedPreferences.Editor editor = SharedRef.edit();
        StoredData sd = new StoredData();
        cherryValue += SharedRef.getInt("Cherry", 0);
        superCherry += SharedRef.getInt("SuperCherry", 0);
        editor.putInt("Cherry", cherryValue);
        editor.putInt("SuperCherry", superCherry);
        editor.commit();
    }
    public  void saveData(double distance){// distance is calculated in metres in the variable distance so convert it into km
        SharedPreferences.Editor editor = SharedRef.edit();
        StoredData sd = new StoredData();
        distance += (SharedRef.getFloat("Distance", 0) * 1000);
        editor.putFloat("Distance", (float)(distance/1000));
        editor.commit();
    }
}
