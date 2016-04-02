package com.groupthree.dictionary.controller.listen;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;

import java.io.IOException;

/**
 * Created by Kent on 3/28/2016.
 */
public class OpenMp3 {
    static OpenMp3 a;
    Context context;
    MediaPlayer player1;

    public OpenMp3(Context context) {
        this.context = context;
    }

    public static OpenMp3 a(Context context) {
        if (a != null) {
            return a;
        }
        a = new OpenMp3(context);

        return a;
    }

    public void playMp3(String Id) {
        MediaPlayer player2 = new MediaPlayer();
        player1 = player2;

        if (player2.isPlaying()) {
            player2.stop();
            player2.release();
            player1 = new MediaPlayer();
        }

        try {
            AssetFileDescriptor descriptor = context.getAssets().openFd("audiofile/" + GetFileName.fileName(EncodeName.encodeFileName(Id)) + ".mp3");
            player1.setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset(), descriptor.getLength());
            descriptor.close();
            player1.prepare();
            player1.start();
            player1.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    player1.stop();
                    player1.release();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
