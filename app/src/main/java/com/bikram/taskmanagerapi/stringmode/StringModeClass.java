package com.bikram.taskmanagerapi.stringmode;

public class StringModeClass {
    public static void StrictMode(){
        android.os.StrictMode.ThreadPolicy policy = new android.os.
                StrictMode.ThreadPolicy.Builder().permitAll().build();

        android.os.StrictMode.setThreadPolicy(policy);
    }
}
