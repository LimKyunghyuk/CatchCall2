package org.devlion.catchCall;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefHelper {

    private static SharedPrefHelper sharedPrefHelper;
    private static SharedPreferences sharedPref;

    final Context context;

    private final String TAG = "CATCH_CALL";
    private final static String DEF_VALUE = "";
    public final static String SERVER_URL = "SERVER_URL";
    public final static String SERVER_KEY = "SERVER_KEY";



    private SharedPrefHelper(Context context){
        this.context = context;
        sharedPref = context.getSharedPreferences(TAG, context.MODE_PRIVATE);
    }

    public static SharedPrefHelper getInstance(Context context){

        if(sharedPrefHelper == null){
            sharedPrefHelper = new SharedPrefHelper(context);
        }

        return sharedPrefHelper;
    }

    public static String getString(String key){
        return sharedPref.getString(key, DEF_VALUE);
    }

    public static void setString(String key, String value){

        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(key, value);
        editor.apply();
    }

}
