package com.groupthree.dictionary.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class DatabaseWord extends SQLiteOpenHelper {
    SQLiteDatabase db;
    ContentValues values;
    Context context;
    public final String DB_PATH = "/data/data/com.groupthree.dictionary/databases/";
    public final String DB_NAME = "db_word.db";

    public final String TBL_WORD = "Word";
    public final String id = "id";
    public final String Subject = "Subject";
    public final String SubjectMean = "SubjectMean";
    public final String Word = "Word";
    public final String Type = "Type";
    public final String Phonetic = "Phonetic";
    public final String Mean = "Mean";
    public final String SortMean = "SortMean";
    public final String Synonyms = "Synonyms";
    public final String Sentence = "Sentence";
    public final String SentenceMean = "SentenceMean";
    public final String FavouriteWord = "FavouriteWord";
    public final String AdequateMean = "AdequateMean";
    public final String Pharagraph = "Pharagraph";
    public final String View = "View";

    public void updateWord(Word w) {
        db = getWritableDatabase();
        values = new ContentValues();
        values.put(Subject, w.Subject);
        values.put(SubjectMean, w.SubjectMean);
        values.put(Word, w.Word);
        values.put(Type, w.Type);
        values.put(Phonetic, w.Phonetic);
        values.put(Mean, w.Mean);
        values.put(SortMean, w.SortMean);
        values.put(Synonyms, w.Synonyms);
        values.put(Sentence, w.Sentence);
        values.put(SentenceMean, w.SentenceMean);
        values.put(FavouriteWord, w.FavouriteWord);
        values.put(AdequateMean, w.AdequateMean);
        values.put(Pharagraph, w.Pharagraph);
        db.update(TBL_WORD, values, id + " = ?",
                new String[]{String.valueOf(w.id)});
    }

    public DatabaseWord(Context context) {
        super(context, "db_word.db", null, 1);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE Word(Id INTEGER PRIMARY KEY, Subject TEXT, SubjectMean TEXT, Word TEXT, Type TEXT, Phonetic TEXT, Mean TEXT, SortMean TEXT, Synonyms TEXT,Sentence TEXT, SentenceMean TEXT, FavouriteWord INTEGER, AdequateMean TEXT, Pharagraph TEXT, View INTEGER DEFAULT 0)");

    }

    public ArrayList<Word> queryWord() {
        db = getReadableDatabase();
        ArrayList<Word> arrW = new ArrayList<>();
        Cursor c = db.rawQuery("SELECT * FROM " + TBL_WORD, null);
        while (c.moveToNext()) {
            arrW.add(new Word(c.getInt(0), c.getString(1), c.getString(2), c
                    .getString(3), c.getString(4), c.getString(5), c
                    .getString(6), c.getString(7), c.getString(8), c
                    .getString(9), c.getString(10), c.getInt(11), c
                    .getString(12), c.getString(13), c.getInt(14)));
        }
        c.close();
        return arrW;
    }

    public Word queryWordWithId(int Id) {
        db = getReadableDatabase();
        Word w = null;
        Cursor c = db.rawQuery("SELECT * FROM " + TBL_WORD + " WHERE Id = '" + Id + "'", null);
        while (c.moveToNext()) {
            w = new Word(c.getInt(0), c.getString(1), c.getString(2), c
                    .getString(3), c.getString(4), c.getString(5), c
                    .getString(6), c.getString(7), c.getString(8), c
                    .getString(9), c.getString(10), c.getInt(11), c
                    .getString(12), c.getString(13), c.getInt(14));
        }
        c.close();
        return w;
    }

    public ArrayList<Subject> querySubject() {
        db = getReadableDatabase();
        ArrayList<Subject> arrS = new ArrayList<>();
        Cursor c = db.rawQuery("SELECT Id, Subject, SubjectMean FROM " + TBL_WORD + " GROUP BY Subject ORDER BY Id Asc", null);
        Cursor c2 = db.rawQuery("SELECT COUNT(case FavouriteWord when '1' then 1 else null end) FROM " + TBL_WORD + " GROUP BY Subject ORDER BY Id Asc", null);

        while (c.moveToNext() && c2.moveToNext()) {
            arrS.add(new Subject(c.getInt(0), c.getString(1), c.getString(2), c2.getInt(0)));
        }
        c.close();
        return arrS;
    }

    public ArrayList<ListWord> queryListWord(String query) {
        db = getReadableDatabase();
        ArrayList<ListWord> arrL = new ArrayList<>();
        Cursor c = db.rawQuery(query, null);
        while (c.moveToNext()) {
            arrL.add(new ListWord(c.getInt(0), c.getString(1), c.getString(2), c.getInt(3)));
        }
        c.close();
        return arrL;
    }

    public void updateFavourite(ListWord lw) {
        db = getWritableDatabase();
        values = new ContentValues();
        values.put(FavouriteWord, lw.FavouriteWord);
        db.update(TBL_WORD, values, "Id = ?", new String[]{String.valueOf(lw.Id)});
    }

    public void updateFavouriteWithSubject(int value, String subject) {
        db = getWritableDatabase();
        values = new ContentValues();
        values.put(FavouriteWord, value);
        db.update(TBL_WORD, values, Subject + " = ?", new String[]{String.valueOf(subject)});
    }

    public boolean checkFavourite(String subject) {
        db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT FavouriteWord FROM Word WHERE Subject = '" + subject + "' AND FavouriteWord = 1", null);
        if (c.moveToFirst()) {
            return true;
        }
        return false;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void createDb() {
        if (!checkDb()) {
            this.getWritableDatabase();
            try {
                copyDb();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                this.close();
            }
        }
    }

    public boolean checkDb() {
        try {
            final File file = new File(DB_PATH + DB_NAME);
            if (file.exists()) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void copyDb() throws IOException {
        try {
            this.getWritableDatabase();
            InputStream is = context.getAssets().open("database/db_word.db");
            OutputStream os = new FileOutputStream(DB_PATH + DB_NAME);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
            os.flush();
            os.close();
            is.close();
            this.close();
        } catch (Exception e) {

        }
    }

}
