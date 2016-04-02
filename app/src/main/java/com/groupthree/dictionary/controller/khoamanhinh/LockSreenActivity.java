package com.groupthree.dictionary.controller.khoamanhinh;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DigitalClock;
import android.widget.TextView;
import android.widget.Toast;

import com.groupthree.dictionary.R;
import com.groupthree.dictionary.model.DatabaseWord;
import com.groupthree.dictionary.model.ListWord;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class LockSreenActivity extends Activity implements View.OnClickListener {
    DatabaseWord db;
    ArrayList<ListWord> arrL;
    TextView tvWord, tvDay;
    Button btnAns1;
    Button btnAns2;
    String nghia;
    Calendar calendar;
    DigitalClock digital;
    int year_x, month_x, day_x;
    int check;
    int test = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lockscreen);
        init();
        setupTime();
        question(0);
    }

    public void setupTime() {
        calendar = Calendar.getInstance();
        year_x = calendar.get(Calendar.YEAR);
        month_x = calendar.get(Calendar.MONTH);
        day_x = calendar.get(Calendar.DAY_OF_MONTH);
        tvDay.setText(day_x + "/" + (month_x + 1) + "/" + year_x);
    }

    public void question(int id) {
        ArrayList<String> listNghia = new ArrayList<String>();
        int i = 0;
        if(test == arrL.size()){
            Toast.makeText(LockSreenActivity.this, "Da hoan thanh xong", Toast.LENGTH_SHORT).show();
            test =0;
        }
        if(id == 0){
            Random random = new Random();
            i = random.nextInt(arrL.size());
            check = i;
            test++;
        }else{
            if(check >= 0 && check < (arrL.size() - 1)){
                i = check + 1;
                check++;
            }else if(check == (arrL.size() - 1)){
                i = 0;
                check = i;
            }
            test++;
        }
        Random rd = new Random();
        int i2 = rd.nextInt(arrL.size());
//        Toast.makeText(LockSreenActivity.this, i + "-" + i2, Toast.LENGTH_SHORT).show();
        tvWord.setText(arrL.get(i).Word);
        nghia = arrL.get(i).Mean;
        String nghia2 = "";
        if (i2 != i) {
            nghia2 = arrL.get(i2).Mean;
        } else {
            if (i == 0) {
                nghia2 = arrL.get(i + 1).Mean;
            } else {
                nghia2 = arrL.get(i - 1).Mean;
            }
        }
        listNghia.add(nghia);
        listNghia.add(nghia2);
        Random rd1 = new Random();
        int i3 = rd1.nextInt(2);
        String n1 = listNghia.get(i3);
        btnAns1.setText(n1);
        if (i3 == 0) {
            btnAns2.setText(listNghia.get(1));
        } else {
            btnAns2.setText(listNghia.get(0));
        }
    }

    public ArrayList<ListWord> getListWord() {
        db = new DatabaseWord(getApplicationContext());
        arrL = db.queryListWord("SELECT Id, Word, Mean, FavouriteWord FROM Word WHERE FavouriteWord = '" + 1 + "'");
        return arrL;
    }

    public void init() {
        makeFullScreen();
        getListWord();
        tvWord = (TextView) findViewById(R.id.tvWord);
        btnAns1 = (Button) findViewById(R.id.btnAns1);
        btnAns2 = (Button) findViewById(R.id.btnAns2);
        tvDay = (TextView) findViewById(R.id.tvDay);
        digital = (DigitalClock) findViewById(R.id.digital_clock);
        btnAns1.setOnClickListener(this);
        btnAns2.setOnClickListener(this);
    }


    public void makeFullScreen() {
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if (Build.VERSION.SDK_INT < 19) {
            //View.SYSTEM_UI_FLAG_IMMERSIVE is only on API 19+
            this.getWindow().getDecorView()
                    .setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        } else {
            this.getWindow().getDecorView()
                    .setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE);
        }
    }

    @Override
    public void onBackPressed() {
        return;
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        switch (keyCode) {
            case KeyEvent.KEYCODE_HOME:
                Toast.makeText(LockSreenActivity.this, "Lêu lêu", Toast.LENGTH_SHORT).show();
                return true;
            case KeyEvent.ACTION_MULTIPLE:
                Toast.makeText(LockSreenActivity.this, "Lêu lêu", Toast.LENGTH_SHORT).show();
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void handleMessage(boolean chuancmnr) {
        if (chuancmnr) {
            new SweetAlertDialog(LockSreenActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                    .setTitleText("Chính xác!")
                    .setConfirmText("Mở khóa")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            finish();
                        }
                    }).show();
        } else {
            new SweetAlertDialog(LockSreenActivity.this, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("Bạn chọn sai rồi")
                    .setContentText("Thử lại với từ khác chứ ?")
                    .setCancelText("Không, mở khóa ngay")
                    .setConfirmText("OK")
                    .showCancelButton(true)
                    .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            finish();
                        }
                    })
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            question(1);
                            sweetAlertDialog.cancel();
                        }
                    })
                    .show();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnAns1:
                if (btnAns1.getText().toString().equals(nghia)) {
                    handleMessage(true);
                } else {
                    handleMessage(false);
                }
                break;
            case R.id.btnAns2:
                if (btnAns2.getText().toString().equals(nghia)) {
                    handleMessage(true);
                } else {
                    handleMessage(false);
                }
                break;
        }
    }
}
