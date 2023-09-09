package com.betelgeuse.corp.flashlight;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {
    Bitmap originalImage, newImage;
    ImageView imageView;
    Boolean isSwitcherOn;
    int newWidth, newHeight;
    CameraManager cameraManager;
    String cameraId;
    SharedPreferences sharedPreferences;
    String typeOfSwitcher;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.menuitem1){
            uploadSwitcherImage();

            typeOfSwitcher = "Version 1";
            saveTypeOfSwitcher(typeOfSwitcher);

            if (isSwitcherOn){
                viewSwitchOnImage();
            }else {
                viewSwitchOffImage();
            }
        }else if(itemId == R.id.menuitem2){
            uploadButtonImage();

            typeOfSwitcher = "Version 2";
            saveTypeOfSwitcher(typeOfSwitcher);

            if (isSwitcherOn){
                viewSwitchOnImage();
            }else {
                viewSwitchOffImage();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveTypeOfSwitcher(String typeOfSwitcher) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("typeOfSwitcher", typeOfSwitcher);
        editor.apply();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = getSharedPreferences("my_preferences", Context.MODE_PRIVATE);
        typeOfSwitcher = sharedPreferences.getString("typeOfSwitcher", "Version 1");

        cameraManager = (CameraManager) getSystemService(CAMERA_SERVICE);
        try {
            cameraId = cameraManager.getCameraIdList()[0];
        } catch (CameraAccessException e) {
            throw new RuntimeException(e);
        }

        setStartPosition();
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isSwitcherOn){
                    isSwitcherOn = false;

                    viewSwitchOffImage();
                    try {
                        cameraManager.setTorchMode(cameraId, isSwitcherOn);
                    } catch (CameraAccessException e) {
                        throw new RuntimeException(e);
                    }
                }else{
                    isSwitcherOn = true;

                    viewSwitchOnImage();
                    try {
                        cameraManager.setTorchMode(cameraId, isSwitcherOn);
                    } catch (CameraAccessException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
    }

    private void setStartPosition() {
        if(typeOfSwitcher.equals("Version 1")){
            uploadSwitcherImage();
        } else if (typeOfSwitcher.equals("Version 2")) {
            uploadButtonImage();
        }

        viewSwitchOffImage();

        isSwitcherOn = false;

    }
    private void viewSwitchOnImage() {
        newWidth = originalImage.getWidth()/2;
        newHeight = originalImage.getHeight();
        newImage = Bitmap.createBitmap(originalImage, 0,0,newWidth,newHeight);
        imageView.setImageBitmap(newImage);
    }
    private void viewSwitchOffImage() {
        newWidth = originalImage.getWidth()/2;
        newHeight = originalImage.getHeight();
        newImage = Bitmap.createBitmap(originalImage, newWidth,0,newWidth,newHeight);
        imageView.setImageBitmap(newImage);
    }
    private void uploadSwitcherImage() {
        imageView = findViewById(R.id.imageView);
        originalImage = BitmapFactory.decodeResource(getResources(),R.drawable.switcher);
    }
    private void uploadButtonImage() {
        imageView = findViewById(R.id.imageView);
        originalImage = BitmapFactory.decodeResource(getResources(),R.drawable.onof);
    }
}