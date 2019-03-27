package lv.lita.stauvere.imagecaptureapp;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.os.Environment.getExternalStoragePublicDirectory;


public class MainActivity extends AppCompatActivity {

    Button btnTakePic;
    ImageView imageView;
    String pathToFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Poga
        btnTakePic = (Button) findViewById(R.id.btnTakePic);
        if (Build.VERSION.SDK_INT >= 23) {
            requestPermissions(new String[] {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
        }
        btnTakePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //palaist kameru
                dispatchPictureTakerAction();

            }
        });

        //Bilde
        imageView = findViewById(R.id.image);


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_OK) {
            if (requestCode == 1) {
                //Bitmap bitmap = BitmapFactory.decodeFile(pathToFile);
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                imageView.setImageBitmap(bitmap);
                Toast.makeText(this, "Bitmap: " + bitmap, Toast.LENGTH_SHORT).show();
            }
        }
    }


    public void dispatchPictureTakerAction() {

        //genere instanci
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (intent.resolveActivity(getPackageManager()) != null ) {

            //uzstada output failu
            File photoFile = null;
            photoFile = createPhotoFile();

            if (photoFile != null) {
                String pathToFile = photoFile.getAbsolutePath();
                Uri photoURI = FileProvider.getUriForFile(MainActivity.this,"lv.lita.stauvere.imagecaptureapp.fileprovider", photoFile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                //intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
                startActivityForResult(intent, 1);
            }

        }

    }

    private File createPhotoFile() {
        String name = new SimpleDateFormat("yyyy-MM-dd HHmm").format(new Date());
        File storageDir = getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = null;
        try {
            image = File.createTempFile(name, ".jpg", storageDir);
        } catch (IOException e) {
            Log.d("mylog", "Excep: " + e.toString());
        }
        return image;
    }


}

