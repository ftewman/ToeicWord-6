package com.groupthree.dictionary;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;

/**
 * Created by QuyetChu on 3/31/16.
 */
public class CustomDialogClass extends Dialog implements
        android.view.View.OnClickListener {

    public Activity c;
    public LinearLayout llNgheLap, llLatTu, llTracNghiem, llLuyenNoi, llLuyenNghe;

    public CustomDialogClass(Activity a) {
        super(a);
        // TODO Auto-generated constructor stub
        this.c = a;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.custom_dialog);
        llNgheLap = (LinearLayout) findViewById(R.id.llNgheLap);
        llLatTu = (LinearLayout) findViewById(R.id.llLatTu);
        llTracNghiem = (LinearLayout) findViewById(R.id.llTracNghiem);
        llNgheLap.setOnClickListener(this);
        llLatTu.setOnClickListener(this);
        llTracNghiem.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.llTracNghiem:

                c.startActivity(new Intent(c, TracNghiemActivity.class));

                break;

            default:
                break;
        }
        dismiss();
    }
}
