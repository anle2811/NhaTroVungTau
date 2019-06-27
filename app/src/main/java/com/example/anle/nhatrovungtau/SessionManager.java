package com.example.anle.nhatrovungtau;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.ResourceBundle;

public class SessionManager {
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context context;
    private static final int PRIVATE_MODE=0;
    private static final String IS_LOGIN = "IsLoggedIn";
    private static final String PREF_NAME="KEEP_LOGIN";
    public static final String TAI_KHOAN="TAIKHOAN";
    public SessionManager(Context context){
        this.context = context;
        pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
        editor.apply();
    }

    public void createLoginSession(String name){
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(TAI_KHOAN, name);
        editor.commit();
    }

    public String getTAIKHOAN(){
        String taikhoan="";
        taikhoan=pref.getString(TAI_KHOAN,"");
        return taikhoan;
    }

    public void checkLogin(){
        if (!this.isLoggedIn()){
            try{
                Intent intent=new Intent(context,DangNhapActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
                Log.d("KT",": aaa");
            }catch (Exception e){
                Log.d("KT",": aaa"+e.getMessage());
            }

        }
    }

    public boolean isLoggedIn(){
        Log.d("KT",": "+pref.getBoolean(IS_LOGIN,false));
        return pref.getBoolean(IS_LOGIN,false);
    }

    public void logOut(){
        editor.clear();
        editor.commit();
        Intent intent=new Intent(context,DangNhapActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}
