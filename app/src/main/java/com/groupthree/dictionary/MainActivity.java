package com.groupthree.dictionary;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.groupthree.dictionary.controller.InternetReceiver;
import com.groupthree.dictionary.controller.khoamanhinh.LockScreenService;
import com.groupthree.dictionary.controller.nhactu.NhacTuService;
import com.groupthree.dictionary.model.DatabaseWord;
import com.groupthree.dictionary.model.ListWord;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, SharedPreferences.OnSharedPreferenceChangeListener {
    ListView lvMain;
    ArrayList<NavigationMain> arrN;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    final String[] btnNhacTu = {"Bật nhắc từ", "Tắt nhắc từ"};
    final String[] btnKhoaManHinh = {"Bật khóa màn hình", "Tắt khóa màn hình"};
    CallbackManager callbackManager;
    ShareDialog shareDialog;
    AdapterMain adapter;
    LinearLayout rootMain;
    DatabaseWord db;
    ArrayList<ListWord> arrL;
    Intent itLockScreenService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupCheckNetwork();
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        setContentView(R.layout.activity_main);
        init();
        itLockScreenService = new Intent(MainActivity.this, LockScreenService.class);
        if(getListWord().size() < 2){
            arrN.set(2, new NavigationMain(Image[2], "Bật khóa màn hình"));
            editor.putBoolean("khoa_man_hinh", false);
            stopService(itLockScreenService);
//            Toast.makeText(MainActivity.this, "abc", Toast.LENGTH_SHORT).show();
        }else if(getListWord().size() >= 2 && pref.getBoolean("khoa_man_hinh", false) == true) {
            editor.putBoolean("khoa_man_hinh", true);
            startService(itLockScreenService);
        }
        editor.commit();


    }

    public void init() {
        pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        pref.registerOnSharedPreferenceChangeListener(this);
        editor = pref.edit();
        lvMain = (ListView) findViewById(R.id.lvMain);
        rootMain = (LinearLayout) findViewById(R.id.rootMain);
        setTypeNhacTu(0);
        setTypeKhoaManHinh(0);
        setupNav();
        openOrcreateDatabase();
    }

    public void setTypeKhoaManHinh(int type) {
        boolean khoa_man_hinh = pref.getBoolean("khoa_man_hinh", false);
        if (type == 0) {
            if (khoa_man_hinh) {
                Title[2] = btnKhoaManHinh[1];
            } else {
                Title[2] = btnKhoaManHinh[0];
            }
        } else {
            Intent itLockScreenService = new Intent(MainActivity.this, LockScreenService.class);
            if (khoa_man_hinh) {
                Title[2] = btnKhoaManHinh[1];
                startService(itLockScreenService);
            } else {
                Title[2] = btnKhoaManHinh[0];
                stopService(itLockScreenService);
            }
        }
    }

    public void setTypeNhacTu(int type) {
        boolean nhac_tu = pref.getBoolean("nhac_tu", false);
        if (type == 0) {
            if (nhac_tu) {
                Title[3] = btnNhacTu[1];
            } else {
                Title[3] = btnNhacTu[0];
            }
        } else {
            Intent itNhacTuService = new Intent(MainActivity.this, NhacTuService.class);
            if (nhac_tu) {
                Title[3] = btnNhacTu[1];
                startService(itNhacTuService);
            } else {
                Title[3] = btnNhacTu[0];
                stopService(itNhacTuService);
            }
        }
    }

    String Title[] = {
            "Từ vựng theo chủ đề",
            "Học từ đã đánh dấu",
            "Bật khóa màn hình",
            "Bật nhắc từ",
            "Cài đặt",
            "Chia sẻ",
            "Hướng dẫn"};
    final int Image[] = {
            R.mipmap.ic_hoctuvungtheochude,
            R.mipmap.ic_hoctukho,
            R.mipmap.ic_khoamanhinh,
            R.mipmap.ic_nhactu,
            R.mipmap.ic_setting,
            R.mipmap.ic_facebook,
            R.mipmap.ic_huongdan};

    public void setupNav() {
        arrN = new ArrayList<>();
        for (int i = 0; i < Title.length; i++) {
            arrN.add(new NavigationMain(Image[i], Title[i]));
        }
        adapter = new AdapterMain(MainActivity.this, R.layout.adapter_main, arrN);
        lvMain.setAdapter(adapter);
        lvMain.setOnItemClickListener(this);
    }

    public void openOrcreateDatabase() {
        DatabaseWord db = new DatabaseWord(getApplicationContext());
        db.createDb();
    }

    public void handleStartActivity(Class<?> cls) {
        startActivity(new Intent(MainActivity.this, cls));
        overridePendingTransition(R.anim.xin_from, R.anim.xin_to);
    }

    public Snackbar handlerMessage(String msg) {
        return Snackbar.make(rootMain, msg, Snackbar.LENGTH_LONG);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 0:
                handleStartActivity(ListSubjectActivity.class);
                finish();
                break;
            case 1:
                handleStartActivity(HocTuActivity.class);
                finish();
                break;
            case 2:
                TextView tvKhoaManHinh = (TextView) view.findViewById(R.id.lvm_title);
                if (getListWord().size() < 2) {
                    stopService(itLockScreenService);
                    editor.putBoolean("khoa_man_hinh", false);
                    thongBao().show();
                } else {
                    String kmh = tvKhoaManHinh.getText().toString();
                    if (kmh.equals(btnKhoaManHinh[0])) {
                        tvKhoaManHinh.setText(btnKhoaManHinh[1]);
                        startService(itLockScreenService);
                        editor.putBoolean("khoa_man_hinh", true);
                    }
                    if (kmh.equals(btnKhoaManHinh[1])) {
                        tvKhoaManHinh.setText(btnKhoaManHinh[0]);
                        stopService(itLockScreenService);
                        editor.putBoolean("khoa_man_hinh", false);
                    }
                    editor.commit();

                }
                break;
            case 3:
                TextView tvNhacTu = (TextView) view.findViewById(R.id.lvm_title);
                String batOrTat = tvNhacTu.getText().toString();
                Intent itNhacTuService = new Intent(MainActivity.this, NhacTuService.class);
                if (batOrTat.equals(btnNhacTu[0])) {
                    tvNhacTu.setText(btnNhacTu[1]);
                    startService(itNhacTuService);
                    editor.putBoolean("nhac_tu", true);
                }
                if (batOrTat.equals(btnNhacTu[1])) {
                    tvNhacTu.setText(btnNhacTu[0]);
                    stopService(itNhacTuService);
                    editor.putBoolean("nhac_tu", false);
                }
                editor.commit();
                break;
            case 4:
                handleStartActivity(SettingsActivity.class);
                finish();
                break;
            case 5:
                if (InternetReceiver.network) {
                    showShare();
                } else {
                    handlerMessage("Kiểm tra kết nối mạng.").show();
                }
                break;
            case 6:
                handleStartActivity(HelpActivity.class);
                finish();
                break;
        }
    }


    public ArrayList<ListWord> getListWord() {
        db = new DatabaseWord(getApplicationContext());
        arrL = db.queryListWord("SELECT Id, Word, Mean, FavouriteWord FROM Word WHERE FavouriteWord = '" + 1 + "'");
        return arrL;
    }

    public AlertDialog.Builder thongBao() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Học từ đã đánh dấu");
        builder.setMessage("Bạn vui lòng đánh dấu ít nhất 2 từ để thực hiện chức năng này !");
        builder.setNegativeButton("Đồng ý", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(MainActivity.this, ListSubjectActivity.class));
                finish();
            }
        });
        builder.setPositiveButton("Hủy", null);
        return builder;
    }

    public void setupCheckNetwork() {
        InternetReceiver internetReceiver = new InternetReceiver();
        IntentFilter filter = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(internetReceiver, filter);
    }

    public void showShare() {
        shareDialog = new ShareDialog(this);
        ShareLinkContent content = new ShareLinkContent.Builder()
                .setContentTitle("Một ứng dụng học từ vựng tiếng anh thật tuyệt vời")
                .setImageUrl(Uri.parse("https://scontent-hkg3-1.xx.fbcdn.net/hphotos-xfp1/v/t1.0-9/1530451_594845904003981_2510383800762108996_n.jpg?oh=39a9e6b6c53578b9378af38fcd5db749&oe=5787DE5D"))
                .setContentDescription("Tải và cài đặt ngay hôm nay")
                .setContentUrl(Uri.parse("https://www.facebook.com/hoc600tuvungtienganh/"))
                .build();
        shareDialog.show(content);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onResume() {
        super.onResume();
        AppEventsLogger.activateApp(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        AppEventsLogger.deactivateApp(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        setTypeNhacTu(1);
        setTypeKhoaManHinh(1);
    }

    class AdapterMain extends ArrayAdapter<NavigationMain> {
        Activity activity;
        int idLayout;
        ArrayList<NavigationMain> arrN;
        ImageView ivMain;
        TextView tvMain;

        public AdapterMain(Activity activity, int idLayout, ArrayList<NavigationMain> arrN) {
            super(activity, idLayout, arrN);
            this.activity = activity;
            this.idLayout = idLayout;
            this.arrN = arrN;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = LayoutInflater.from(parent.getContext()).inflate(idLayout, parent, false);
            ivMain = (ImageView) v.findViewById(R.id.lvm_ico);
            tvMain = (TextView) v.findViewById(R.id.lvm_title);
            NavigationMain n = arrN.get(position);
            ivMain.setImageResource(n.image);
            tvMain.setText(n.title);
            return v;
        }
    }

    class NavigationMain {
        public int image;
        public String title;

        public NavigationMain(int image, String title) {
            this.image = image;
            this.title = title;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
