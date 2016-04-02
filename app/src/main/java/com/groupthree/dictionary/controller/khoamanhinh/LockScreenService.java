package com.groupthree.dictionary.controller.khoamanhinh;

import android.app.KeyguardManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.preference.PreferenceManager;

public class LockScreenService extends Service implements SharedPreferences.OnSharedPreferenceChangeListener {

    BroadcastReceiver receiver;
    SharedPreferences pref;
    SharedPreferences.Editor editor;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        pref = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        pref.registerOnSharedPreferenceChangeListener(this);
        KeyguardManager.KeyguardLock key;
        KeyguardManager km = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);
        key = km.newKeyguardLock("IN");
        key.disableKeyguard();
        editor = PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit();

        receiver = new LockScreenReceiver();

        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Intent.ACTION_BOOT_COMPLETED);

        registerReceiver(receiver, filter);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

    }
}
