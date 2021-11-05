package com.example.a33_18078881_nguyenviettien_dhktpm14ctt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class TrangChinh extends AppCompatActivity {

    private static final int NOTIFICATION = 1;
    ImageView imgEmail, imgAni;
    TextView tvTen, tvMess;
    Button btnSignOut;
    GoogleSignInClient mGoogleSignInClient;
    Button create, read, update, delete, btnNo;
    private MyService mMyService;
    private Boolean isConnect = false;
    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MyService.MyBinder binder = (MyService.MyBinder) service;
            mMyService =binder.getService();
            isConnect = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isConnect= false;
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trang_chinh);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        Intent intent = new Intent(TrangChinh.this, MyService.class);
        bindService(intent,mConnection,BIND_AUTO_CREATE);

        create = findViewById(R.id.btnCreate);
        read = findViewById(R.id.btnRead);
        update = findViewById(R.id.btnUpdate);
        delete = findViewById(R.id.btnDelete);
        tvMess = findViewById(R.id.tvMess);
        imgEmail = findViewById(R.id.imgEmail);
        tvTen = findViewById(R.id.textView4);
        btnSignOut = findViewById(R.id.button);
        imgAni = findViewById(R.id.imageView2);
        btnNo = findViewById(R.id.btnNo);

        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                senNotification();
            }
        });

        Animation a = AnimationUtils.loadAnimation(this,R.anim.rotate);
        imgAni.startAnimation(a);
        btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.button:
                        signOut();
                        break;
                }
            }
        });

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(TrangChinh.this, "Created", Toast.LENGTH_SHORT).show();
                tvMess.setText(mMyService.create()+"");
            }
        });
        read.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(TrangChinh.this, "Read", Toast.LENGTH_SHORT).show();
                tvMess.setText(mMyService.read()+"");
            }
        });
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(TrangChinh.this, "Updated", Toast.LENGTH_SHORT).show();
                tvMess.setText(mMyService.update()+"");
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(TrangChinh.this, "Deleted", Toast.LENGTH_SHORT).show();
                tvMess.setText(mMyService.delete()+"");
            }
        });

        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if (acct != null) {
            String personName = acct.getDisplayName();
            Uri personPhoto = acct.getPhotoUrl();

            tvTen.setText(personName);
            Glide.with(this).load(String.valueOf(personPhoto)).into(imgEmail);
        }
    }

    private void senNotification() {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);

        Notification notification = new Notification.Builder(this)
                .setContentTitle("Create, Read, Update, Delete")
                .setContentText("Phần notification")
                .setSmallIcon(R.drawable.common_full_open_on_phone)
                .setLargeIcon(bitmap).build();
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if(notificationManager != null) {
            notificationManager.notify(NOTIFICATION, notification);
        }
    }

    private void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(TrangChinh.this, "Đăng xuất thành công", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
    }
}