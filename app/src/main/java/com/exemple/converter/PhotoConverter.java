package com.exemple.converter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.exemple.converter.utils.PhotoUtils;

import java.io.FileNotFoundException;


public class PhotoConverter extends Activity {

    TextView textTargetUri;
    ImageView targetImage;
    static TextView suma;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photo_converter);


        Button buttonLoadImage = (Button)findViewById(R.id.loadimage);
        textTargetUri = (TextView)findViewById(R.id.targeturi);
        targetImage = (ImageView)findViewById(R.id.targetimage);

        buttonLoadImage.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 0);
            }});

        dispatchTakePictureIntent();
    }

    static final int REQUEST_IMAGE_CAPTURE = 1;

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    public static TextView getSuma() {
        return suma;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK){
            Uri targetUri = data.getData();
            //textTargetUri.setText(targetUri.toString());
            try {
                Bitmap bitmapGallery = BitmapFactory.decodeStream(getContentResolver().openInputStream(targetUri));
                Bitmap bitmapRON1 = BitmapFactory.decodeResource(getResources(), R.drawable.ron1);
                targetImage.setImageBitmap(bitmapGallery);

                //Mat matGallery = PhotoUtils.bitmapToGreyMat(bitmapGallery);
                //Mat matRON1 = PhotoUtils.bitmapToGreyMat(bitmapRON1);
                //String result = PhotoUtils.getCompareResult(matGallery, matRON1);
                //textTargetUri.setText(result);

                String res = PhotoUtils.compareHist(bitmapGallery, bitmapRON1);
                textTargetUri.setText(res);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}