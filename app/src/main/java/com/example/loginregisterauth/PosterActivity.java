package com.example.loginregisterauth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class PosterActivity extends AppCompatActivity {

    private final static int CODE = 100;
    private FileOutputStream fileOutputStream;
    private Intent intent;

    private ImageView fullPosterview;
    private Button download,share;

    private ScaleGestureDetector scaleGestureDetector;
    private float mScaleFactor = 1.0f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poster);

        fullPosterview = findViewById(R.id.fullPosterview);

        download = findViewById(R.id.downloadposterbtn);
        share = findViewById(R.id.shareposterbtn);

        scaleGestureDetector = new ScaleGestureDetector(this, new ScaleListener());

        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get permission from user
                if(ContextCompat.checkSelfPermission(PosterActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                    download();
                }
                else {
                    askPermission();
                }
            }
        });

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                share();
            }
        });

        Glide.with(this).load(getIntent().getStringExtra("object@#"))
                .into(fullPosterview);
    }

    //share image/poster to other apps
    private void share() {
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        BitmapDrawable drawable = (BitmapDrawable)fullPosterview.getDrawable();
        Bitmap bitmap = drawable.getBitmap();

        File file = new File(getExternalCacheDir()+ "/" + getResources().getString(R.string.app_name)+ ".jpg");

        try {
            fileOutputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,fileOutputStream);

            fileOutputStream.flush();
            fileOutputStream.close();

            intent = new Intent(Intent.ACTION_SEND);
            intent.setType("image/*");

            intent.putExtra(Intent.EXTRA_STREAM,Uri.fromFile(file));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        startActivity(Intent.createChooser(intent,"Share poster"));
    }

    private void askPermission() {

        ActivityCompat.requestPermissions(PosterActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(CODE == requestCode){

            if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                download();
            }
            else{
                Toast.makeText(PosterActivity.this, "Please provide permission",Toast.LENGTH_SHORT).show();
            }
        }


        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    //download image to user's gallery
    private void download() {
        File dir = new File(Environment.getExternalStorageDirectory(), "Posters");
        if(!dir.exists()){
            dir.mkdir();
        }

        BitmapDrawable drawable = (BitmapDrawable) fullPosterview.getDrawable();
        Bitmap bitmap = drawable.getBitmap();

        File file = new File(dir, System.currentTimeMillis() + ".jpg");

        try {
            fileOutputStream = new FileOutputStream(file);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        bitmap.compress(Bitmap.CompressFormat.JPEG,100,fileOutputStream);
        Toast.makeText(PosterActivity.this,"Download Complete",Toast.LENGTH_SHORT).show();
        try {
            fileOutputStream.flush();
            fileOutputStream.close();

            //this intent will save image/poster to gallery
            intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            intent.setData(Uri.fromFile(file));
            sendBroadcast(intent);
        } catch (IOException e) {
            e.printStackTrace();
        }



    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        scaleGestureDetector.onTouchEvent(motionEvent);
        return true;
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector scaleGestureDetector) {
            mScaleFactor *= scaleGestureDetector.getScaleFactor();
            mScaleFactor = Math.max(0.1f, Math.min(mScaleFactor, 10.0f));
            fullPosterview.setScaleX(mScaleFactor);
            fullPosterview.setScaleY(mScaleFactor);
            mScaleFactor = Math.max(1f, Math.max(mScaleFactor,1.0f));

            return true;
        }
    }
}