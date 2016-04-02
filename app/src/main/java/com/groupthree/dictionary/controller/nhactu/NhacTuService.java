package com.groupthree.dictionary.controller.nhactu;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.groupthree.dictionary.R;
import com.groupthree.dictionary.model.DatabaseWord;
import com.groupthree.dictionary.model.Word;

import java.util.ArrayList;
import java.util.Random;

public class NhacTuService extends Service implements SharedPreferences.OnSharedPreferenceChangeListener {
    WindowManager windowManager;
    LinearLayout lnNhacTu;
    TextView tvWord;
    TextView tvPhonetic;
    TextView tvSortMean;
    DatabaseWord db;
    ArrayList<Word> arrW;
    View view;
    SharedPreferences pref;

    int random = 0;
    int color;
    String phien_am = "";
    int[] arrColor;
    Word w;

    int soChay;
    public long time_start = System.currentTimeMillis();
    int soNext;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        getWord();
        init();
        return START_STICKY;
    }

    public void getWord() {
        db = new DatabaseWord(getBaseContext());
        arrW = db.queryWord();
    }

    public void init() {
        pref = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        pref.registerOnSharedPreferenceChangeListener(this);
        if (view == null) {
            windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
            view = View.inflate(getBaseContext(), R.layout.layout_nhactu, null);
            WindowParams parmW = new WindowParams();
            windowManager.addView(view, parmW);
            view.setOnTouchListener(new Touch(view, windowManager,
                    parmW));
        }
        lnNhacTu = (LinearLayout) view.findViewById(R.id.lnNhacTu);
        tvWord = (TextView) view.findViewById(R.id.tvWord);
        tvPhonetic = (TextView) view.findViewById(R.id.tvPhonetic);
        tvSortMean = (TextView) view.findViewById(R.id.tvSortMean);
        setupShow();

    }

    public boolean getColorRandom() {
        return pref.getBoolean("color_nhac_tu", false);
    }

    public boolean getPhienAm() {
        return pref.getBoolean("phien_am_nhac_tu", false);
    }

    public int getTime() {
        return Integer.parseInt(pref.getString("time_nhac_tu", "3"));
    }

    public int getColor() {
        return Color.parseColor(pref.getString("list_color", "#3F51B5"));
    }

    public void setupShow() {
        arrColor = getResources().getIntArray(R.array.arrColor);
        int time_down = getTime() * 600000;

        CountDownTimer timer = new CountDownTimer(time_down, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                double time = (System.currentTimeMillis() - time_start) / 1000d;
                soChay = (int) time;
                if (soChay % getTime() == 1) {
                    soNext = soChay / getTime();
                }
                w = arrW.get(soNext);
                if (getPhienAm()) {
                    phien_am = w.Phonetic;
                } else {
                    phien_am = "";
                }

                if (getColorRandom()) {
                    random = new Random().nextInt(arrColor.length) + 0;
                    color = arrColor[random];
                } else {
                    color = getColor();
                }

                if (soNext < arrW.size()) {
                    tvWord.setText(w.Word);
                    tvPhonetic.setText(phien_am);
                    tvSortMean.setText(w.SortMean);
                    lnNhacTu.setBackgroundColor(color);
                }
            }

            @Override
            public void onFinish() {

            }
        };
        timer.start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        windowManager.removeView(view);

    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

    }
}
