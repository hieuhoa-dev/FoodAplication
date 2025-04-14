package com.example.foodapp.Helper;

import android.content.Context;
import android.content.SharedPreferences;

public class DarkMode {
    private final SharedPreferences sharedPreferences;

    public DarkMode(Context context) {
        sharedPreferences = context.getSharedPreferences("DarkMode", Context.MODE_PRIVATE);
    }

    public void setDarkModeEnabled(boolean enabled) {
        sharedPreferences.edit().putBoolean("isDarkModeEnabled", enabled).apply();
    }

    public boolean isDarkModeEnabled() {
        return sharedPreferences.getBoolean("isDarkModeEnabled", false);
    }
}
