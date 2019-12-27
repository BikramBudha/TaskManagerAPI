package com.bikram.taskmanagerapi;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.CursorLoader;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bikram.taskmanagerapi.serverResponse.ImageResponse;
import com.bikram.taskmanagerapi.urlapi.ApiInterface;
import com.bikram.taskmanagerapi.urlapi.Url;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Response;

public class SignupActivity extends AppCompatActivity {
        EditText etfirstName,etlastName,etusernameSign,etPasswordSign,etConfirPW;
        ImageView imageView;
        Button btnSignup;
        String imagePath;


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_signup);

            imageView = findViewById(R.id.ImageView);
            etfirstName = findViewById(R.id.firstName);
            etlastName = findViewById(R.id.lastName);
            etusernameSign = findViewById(R.id.username);
            etPasswordSign = findViewById(R.id.password);

            etConfirPW = findViewById(R.id.conformpassword);

            btnSignup = findViewById(R.id.Signup);
            btnSignup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(SignupActivity.this, "Register Successfully", Toast.LENGTH_SHORT).show();
                }
            });

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BrowseImage();
                }
            });




        }

        private void BrowseImage(){
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent,0);

        }

        @Override
        protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
            super.onActivityResult(requestCode, resultCode, data);

            if (resultCode == RESULT_OK){
                if (data == null){
                    Toast.makeText(this,"Please select an image", Toast.LENGTH_SHORT).show();
                }
            }

            Uri uri = data.getData();
            imageView.setImageURI(uri);
            String imagePath = getRealPathFromUri(uri);
           // previewImage(imagePath);
        }



        private String getRealPathFromUri(Uri uri) {
            String[] projection = {MediaStore.Images.Media.DATA};
            CursorLoader loader = new CursorLoader(getApplicationContext(), uri, projection, null, null, null
            );
            Cursor cursor = loader.loadInBackground();
            int colIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            String result = cursor.getString(colIndex);
            cursor.close();
            return result;

        }

        private void previewImage(String imagePath) {
            File imgFile = new File(imagePath);
            if(imgFile.exists()){
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                imageView.setImageBitmap(myBitmap);
           }

        }

        private void StrictMode() {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        private void SaveImageOnly() {

            File file = new File(imagePath);

            RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            MultipartBody.Part body = MultipartBody.Part.createFormData("imageFile", file.getName(), requestBody);

            ApiInterface apiInterface = Url.getInstance().create(ApiInterface.class);
            Call<ImageResponse> responseCall = apiInterface.uploadImage(body);

            StrictMode();

            try {
                Response<ImageResponse> imageResponseResponse = responseCall.execute();
                //After saving image, retrieve the current name of the image
                imagePath = imageResponseResponse.body().getFilename();
            }


            catch (Exception e){
                Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
            }

        }


