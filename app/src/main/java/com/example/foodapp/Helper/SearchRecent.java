package com.example.foodapp.Helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import com.example.foodapp.Adapter.SearchRecommendAdpater;

import com.example.foodapp.Model.Foods;
import com.example.foodapp.Model.searchRecommend;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;

public class SearchRecent {
    private SharedPreferences preferences;
    public SearchRecent(Context appContext) {
        preferences = PreferenceManager.getDefaultSharedPreferences(appContext);
    }
    public ArrayList<String> getListString(String key) {
        return new ArrayList<String>(Arrays.asList(TextUtils.split(preferences.getString(key, ""), "‚‗‚")));
    }
    public ArrayList<searchRecommend> getListObject(String key){
        Gson gson = new Gson();

        ArrayList<String> objStrings = getListString(key);
        ArrayList<searchRecommend> searchRecent =  new ArrayList<>();

        for(String jObjString : objStrings){
            searchRecommend player  = gson.fromJson(jObjString,  searchRecommend.class);
            searchRecent.add(player);
        }
        return searchRecent;
    }
    public void putListObject(String key, ArrayList<searchRecommend> playerList){
        checkForNullKey(key);
        Gson gson = new Gson();
        ArrayList<String> objStrings = new ArrayList<String>();
        for(searchRecommend player: playerList){
            objStrings.add(gson.toJson(player));
        }
        putListString(key, objStrings);
    }
    private void checkForNullKey(String key){
        if (key == null){
            throw new NullPointerException();
        }
    }

    public void putListString(String key, ArrayList<String> stringList) {
        checkForNullKey(key);
        String[] myStringList = stringList.toArray(new String[stringList.size()]);
        preferences.edit().putString(key, TextUtils.join("‚‗‚", myStringList)).apply();
    }
}
