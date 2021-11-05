package com.example.a33_18078881_nguyenviettien_dhktpm14ctt;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import androidx.annotation.Nullable;

public class MyService extends Service {
    private MyBinder mBinder = new MyBinder();

    public class MyBinder extends Binder {
        MyService getService(){
            return MyService.this;
        }
    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public String create(){
        return "Created";
    }
    public String read(){
        return "Read";
    }
    public String update(){
        return "Updated";
    }
    public String delete(){
        return "Deleted";
    }
}
