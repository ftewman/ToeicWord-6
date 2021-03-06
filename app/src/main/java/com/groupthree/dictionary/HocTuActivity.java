package com.groupthree.dictionary;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.groupthree.dictionary.model.DatabaseWord;
import com.groupthree.dictionary.model.ListWord;
import com.groupthree.dictionary.view.AdapterListWordWithListView;

import java.util.ArrayList;

public class HocTuActivity extends AppCompatActivity implements View.OnClickListener {
    ListView lvHocTu;
    DatabaseWord db;
    ArrayList<ListWord> arrL;
    Button btnHocTu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hoc_tu);
        init();
    }

    private void init() {
        setupActionBar();
        lvHocTu = (ListView) findViewById(R.id.lvHocTu);
        btnHocTu = (Button) findViewById(R.id.btnHocTu);
        btnHocTu.setOnClickListener(this);
        checkNull();
    }

    public ArrayList<ListWord> getListWord() {
        db = new DatabaseWord(getApplicationContext());
        arrL = db.queryListWord("SELECT Id, Word, Mean, FavouriteWord FROM Word WHERE FavouriteWord = '" + 1 + "'");
        return arrL;
    }

    private AdapterListWordWithListView showListWord() {
        arrL = getListWord();
        db = new DatabaseWord(getApplicationContext());
        AdapterListWordWithListView adapter = new AdapterListWordWithListView(HocTuActivity.this, R.layout.adapter_list_word, arrL, db);
        return adapter;
    }

    public AlertDialog.Builder thongBao() {
        AlertDialog.Builder builder = new AlertDialog.Builder(HocTuActivity.this);
        builder.setTitle("Bạn cần có ít nhất 4 từ đánh dấu !");
        builder.setMessage("Bạn có muốn thêm từ để học");
        builder.setNegativeButton("Đồng ý", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(HocTuActivity.this, ListSubjectActivity.class));
                finish();
            }
        });
        builder.setPositiveButton("Hủy", null);
        return builder;
    }

    public void checkNull() {
        if (getListWord().size() == 0) {
        } else {
            lvHocTu.setAdapter(showListWord());
        }
    }

    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                startActivity(new Intent(HocTuActivity.this, MainActivity.class));
                overridePendingTransition(R.anim.xout_from, R.anim.xout_to);
                finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(HocTuActivity.this, MainActivity.class));
        overridePendingTransition(R.anim.xout_from, R.anim.xout_to);
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnHocTu:
                if(getListWord().size() < 4) {
                    thongBao().show();
                }else {
                    CustomDialogClass cdd = new CustomDialogClass(HocTuActivity.this);
                    cdd.show();
                }
        }
    }
}
