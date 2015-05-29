package com.gotwingm.my.screenshoter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;


public class MainActivity extends Activity implements View.OnClickListener {

    private final String FILE_DIR = Environment.getExternalStorageDirectory() + "/screenshoter";

    public Bitmap mBitmap;
    public View mView;

    Toast dirInfoToast;
    File dir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dir = new File(FILE_DIR);

        dirInfoToast = Toast.makeText(getApplicationContext(), "Screenshot saved: " +
                FILE_DIR, Toast.LENGTH_LONG);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case (R.id.buttonMakeScreenshot):

                mView = getWindow().getDecorView().getRootView();
                mView.post(new Runnable() {
                    @Override
                    public void run() {
                        mBitmap = makeActivityScreenshot(mView);
                        savePic(mBitmap);
                    }
                });

                break;
            case (R.id.buttonStartService):

                startService(new Intent(this, MyService.class));

                break;
            case (R.id.buttonStopService):

                stopService(new Intent(this, MyService.class));

                break;
            case (R.id.button):

                makeFullscreenScreenshot();

                break;
        }
    }

    public Bitmap makeActivityScreenshot(View v) {

        Bitmap screenshot;
        v.setDrawingCacheEnabled(true);
        v.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

        screenshot = Bitmap.createBitmap(v.getDrawingCache());
        Canvas canvas = new Canvas(screenshot);
        v.draw(canvas);

        v.setDrawingCacheEnabled(false);

        return screenshot;
    }

    public void savePic(Bitmap b) {

        final String fileName = System.currentTimeMillis() + ".jpg";

        try {

            if (!dir.exists()) {
                dir.mkdir();
            }

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            b.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);

            File file = new File(FILE_DIR, fileName);
            file.createNewFile();

            FileOutputStream fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(byteArrayOutputStream.toByteArray());
            fileOutputStream.close();


        } catch (NullPointerException | IOException e) {
            e.printStackTrace();
        }

        dirInfoToast.show();
    }

    private void makeFullscreenScreenshot() {

        String command = "/system/bin/screencap -p ";
        String name = "fullscreen.png";

        Process process;
        OutputStream outputStream;

        if (!dir.exists()) {
            dir.mkdir();
        }

        try {

            process = Runtime.getRuntime().exec("su", null, null);
            outputStream = process.getOutputStream();

            outputStream.write((command + FILE_DIR + File.separator +
                    System.currentTimeMillis() + name)
                    .getBytes("ASCII"));
            outputStream.flush();
            outputStream.close();
            process.waitFor();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        dirInfoToast.show();
    }

}