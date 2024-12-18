package com.example.chatcs460.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.chatcs460.R;
import com.example.chatcs460.databinding.ActivitySignUpBinding;
import com.example.chatcs460.utilities.Constants;
import com.example.chatcs460.utilities.PreferenceManager;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;

public class SignUpActivity extends AppCompatActivity {

    private ActivitySignUpBinding binding;
    private PreferenceManager preferenceManager;

    private String encodeImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preferenceManager = new PreferenceManager(getApplicationContext());
        setListeners();
    }

    private void setListeners() {
        binding.textSignIn.setOnClickListener(v -> onBackPressed());

        binding.buttonSignUp.setOnClickListener(view -> {
            if(isValidSignUpDetails()){
                SignUp();
            }
        });

        binding.layoutImage.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

            intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
            pickImage.launch(intent);
        });
    }


    private void showToast(String message){
        Toast.makeText(getApplicationContext(), message,Toast.LENGTH_SHORT).show();
    }

    private void SignUp(){
        //Check Loading
        loading(true);


        //Post to Firebase
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        HashMap<String,String> user = new HashMap<>();
        user.put(Constants.KEY_NAME,binding.inputName.getText().toString());
        user.put(Constants.KEY_EMAIL,binding.inputEmail.getText().toString());
        user.put(Constants.KEY_PASSWORD,binding.inputPassword.getText().toString());

        user.put(Constants.KEY_IMAGE, encodeImage);

        database.collection(Constants.KEY_COLLECTION_USERS).add(user).addOnSuccessListener(documentReference -> {
            loading(false);

            preferenceManager.putBoolean(Constants.KEY_IS_SIGNED_IN,true);
            preferenceManager.putString(Constants.KEY_NAME,binding.inputName.getText().toString());
            preferenceManager.putString(Constants.KEY_IMAGE,encodeImage);

            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);

        }).addOnFailureListener(exception ->{
         loading(false);
         showToast(exception.getMessage());
     });



    }

    private String encodeImage(Bitmap bitmap) {
        int previewWidth = 150;
        int previewHeight = bitmap.getHeight() * previewWidth / bitmap.getWidth();

        Bitmap previewBitmap = Bitmap.createScaledBitmap(bitmap, previewWidth, previewHeight, false);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        previewBitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);

        byte[] bytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }

    private final ActivityResultLauncher<Intent> pickImage = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),result ->{
                if(result.getResultCode() == RESULT_OK){
                    Uri imageUri = result.getData().getData();
                    try{
                        InputStream inputStream = getContentResolver().openInputStream(imageUri);
                      Bitmap bitmap = BitmapFactory.decodeStream(inputStream);


                      binding.imageProfile.setImageBitmap(bitmap);
                      binding.textAddImage.setVisibility(View.GONE);
                      encodeImage = encodeImage(bitmap);

                    }catch(FileNotFoundException e){
                        e.printStackTrace();
                    }
                }
            }
    );



   private Boolean isValidSignUpDetails() {
       if(encodeImage == null){
           showToast("Please select your image");
           return false;
       }
       if (binding.inputName.getText().toString().isEmpty()) {
           showToast("Please Enter Your Name");
           return false;
       } else if (binding.inputEmail.getText().toString().isEmpty()) {
           showToast("Please Enter Your Email");
           return false;
       } else if (!Patterns.EMAIL_ADDRESS.matcher(binding.inputEmail.getText().toString()).matches()) {
           showToast("Please Enter vail Email");
           return false;
       } else if (binding.inputPassword.getText().toString().isEmpty()) {
           showToast("Please Enter Your Password");
           return false;

       }else if (binding.inputConfirmPassword.getText().toString().isEmpty()) {
           showToast("Please Confirm Your Password");
           return false;

       }else if (!binding.inputPassword.getText().toString().equals(binding.inputConfirmPassword.getText().toString())) {
           showToast("Password & Confirm Password must be the same");
           return false;

       }
       else{
            return true;
        }
   }

   private void loading (Boolean isLoading){
       if(isLoading){
           binding.buttonSignUp.setVisibility(View.INVISIBLE);
           binding.progressBar.setVisibility(View.VISIBLE);
       } else{
           binding.progressBar.setVisibility(View.INVISIBLE);
           binding.buttonSignUp.setVisibility(View.VISIBLE);

       }
   }

}