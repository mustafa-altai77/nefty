package org.sudadev.nefty.ui.anonymouse;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;

import org.sudadev.nefty.R;
import org.sudadev.nefty.common.Constants;
import org.sudadev.nefty.storage.SharedPreferencesLocalStorage;
import org.sudadev.nefty.ui.MainActivity;
import org.sudadev.nefty.ui.custom.BaseActivity;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SplashActivity extends BaseActivity {

    private String deepLinkEventId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        init();
    }

    private void init() {
        createNotificationChannel();

        SharedPreferencesLocalStorage localStorage = new SharedPreferencesLocalStorage(this);
        final boolean isLogged = localStorage.isLogged();

        Runnable task = new Runnable() {
            @Override
            public void run() {
                Intent intent = null;
                if (isLogged) {
                    intent = new Intent(SplashActivity.this, MainActivity.class);
                } else {
                    intent = new Intent(SplashActivity.this, LoginActivity.class);
                }

                startActivity(intent);
                finish();
            }
        };


        new Handler().postDelayed(task, 3000);
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "ZoFirm";
            String description = "ZoFirm Channel";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(Constants.CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}

