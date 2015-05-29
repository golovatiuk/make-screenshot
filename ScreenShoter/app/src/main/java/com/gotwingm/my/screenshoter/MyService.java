package com.gotwingm.my.screenshoter;

import android.app.Service;
import android.content.Intent;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;

public class MyService extends Service {

    private final String DIR = Environment.getExternalStorageDirectory() + "/screenshoter/";

    public MyService() {
    }

    public int onStartCommand(Intent intent, int flags, int startId) {

        for (int i = 0; i < 3; i++) {

            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            makeScreenshot();
            stopSelf();
            Toast.makeText(getApplicationContext(), "Service finished", Toast.LENGTH_SHORT).show();
        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void makeScreenshot() {

        String command = "/system/bin/screencap -p ";
        String name = "fullscreen.png";
        File dir = new File(DIR);

        Process process;
        OutputStream outputStream;

        if (!dir.exists()) {
            dir.mkdir();
        }

        try {
            process = Runtime.getRuntime().exec("su", null, null);
            outputStream = process.getOutputStream();

            outputStream.write((command + DIR + System.currentTimeMillis() + name)
                    .getBytes("ASCII"));
            outputStream.flush();
            outputStream.close();
            process.waitFor();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Toast.makeText(getApplicationContext(), "3 screenshots saved to: " +
                DIR, Toast.LENGTH_SHORT).show();
    }
}
