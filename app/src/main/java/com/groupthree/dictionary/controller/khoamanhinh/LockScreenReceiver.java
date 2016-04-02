package com.groupthree.dictionary.controller.khoamanhinh;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.groupthree.dictionary.model.DatabaseWord;
import com.groupthree.dictionary.model.ListWord;

import java.util.ArrayList;

public class LockScreenReceiver extends BroadcastReceiver implements SharedPreferences.OnSharedPreferenceChangeListener {

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    DatabaseWord db;
    ArrayList<ListWord> arrL;
    Intent itLockScreenService;

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Intent i = new Intent(context, LockSreenActivity.class);
        pref = PreferenceManager.getDefaultSharedPreferences(context);
        pref.registerOnSharedPreferenceChangeListener(this);
        editor = pref.edit();
        if (action.equals(Intent.ACTION_SCREEN_OFF) || action.equals(Intent.ACTION_BOOT_COMPLETED)) {
            Intent iOff = new Intent(context, LockSreenActivity.class);
            iOff.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            iOff.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

            itLockScreenService = new Intent(context, LockScreenService.class);
            if(getListWord(context).size() < 2){
                editor.putBoolean("khoa_man_hinh", false);
//            Toast.makeText(MainActivity.this, "abc", Toast.LENGTH_SHORT).show();
            }else if(getListWord(context).size() >= 2 && pref.getBoolean("khoa_man_hinh", false) == true) {
                editor.putBoolean("khoa_man_hinh", true);
                context.startActivity(iOff);
            }
            editor.commit();
        } else if (action.equals(Intent.ACTION_SCREEN_ON)) {

        }


    }



    public ArrayList<ListWord> getListWord(Context context) {
        db = new DatabaseWord(context);
        arrL = db.queryListWord("SELECT Id, Word, Mean, FavouriteWord FROM Word WHERE FavouriteWord = '" + 1 + "'");
        return arrL;
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

    }
}
