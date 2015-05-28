package com.gotwingm.my.screenshoter;

import android.app.Activity;
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


public class MainActivity extends Activity implements View.OnClickListener {

    private Bitmap mBitmap;
    private View mView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public Bitmap makeScreenShot(View v) {

        Bitmap screenshot = null;

        if (v != null) {

            screenshot = Bitmap.createBitmap(v.getMeasuredWidth(), v.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(screenshot);
            v.draw(canvas);
        }

        return screenshot;
    }

    public void savePic(Bitmap b) {

        final String fileLocation = Environment.getExternalStorageDirectory() + "/screenshot";

        final String fileName = System.currentTimeMillis() + ".jpg";

        File dir = new File(fileLocation);

        if(!dir.exists()) {
            dir.mkdir();
        }


        try {

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            b.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);

            File file = new File(fileLocation, fileName);
            file.createNewFile();

            FileOutputStream fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(byteArrayOutputStream.toByteArray());
            fileOutputStream.close();


        } catch (NullPointerException | IOException e) {
            e.printStackTrace();
        }

        Toast.makeText(getApplicationContext(), "Screenshot saved", Toast.LENGTH_LONG).show();

    }

    @Override
    public void onClick(View v) {

        mView = getWindow().getDecorView().getRootView();

        mView.post(new Runnable() {
            @Override
            public void run() {

                mBitmap = makeScreenShot(mView);

                savePic(mBitmap);
            }
        });
    }
}
