package com.arslan6015.clubherofitadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.arslan6015.clubherofitadmin.Model.Post;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ArticleActivity extends AppCompatActivity {
    TextView article_title,article_desp,article_upload_time;
    ImageView article_img;
    Button Edit_Article;

    Dialog popAddPost ;
    ImageView popupPostImage,popupAddBtn;
    TextView popupTitle,popupDescription;
    ProgressBar popupClickProgress;
    private Uri pickedImgUri = null;
    private static final int REQUESCODE = 2 ;
    String intImg,intTitle,intDesp,intPostkey,intTimeStamp;
    static String saveCurrentDate;
    static String saveCurrentTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);

        iniPopup();
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        intTitle = intent.getStringExtra("intentArtTitle");
        intDesp = intent.getStringExtra("intentArtDesp");
        intImg = intent.getStringExtra("intentArtImg");
        intPostkey = intent.getStringExtra("intentArtPostKey");
        intTimeStamp = intent.getStringExtra("intentArtTimeStamp");
        getSupportActionBar().setTitle(intTitle);

        article_title = findViewById(R.id.article_title);
        article_desp = findViewById(R.id.article_desp);
        article_img = findViewById(R.id.article_img);
        article_upload_time = findViewById(R.id.article_upload_time);
        Edit_Article = findViewById(R.id.Edit_Article);

        article_title.setText(intTitle);
        article_desp.setText(intDesp);
        Picasso.get().load(intImg).into(article_img);
        article_upload_time.setText(intTimeStamp);

        Edit_Article.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popAddPost.show();
                popupTitle.setText(intTitle);
                Picasso.get().load(intImg).into(popupPostImage);
        //        pickedImgUri = intImg;
                popupDescription.setText(intDesp);
            }
        });

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    public void iniPopup() {

        popAddPost = new Dialog(ArticleActivity.this);
        popAddPost.setContentView(R.layout.popup_add_post);
        popAddPost.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popAddPost.getWindow().setLayout(Toolbar.LayoutParams.MATCH_PARENT,Toolbar.LayoutParams.WRAP_CONTENT);
        popAddPost.getWindow().getAttributes().gravity = Gravity.TOP;

        // ini popup widgets
        popupPostImage = popAddPost.findViewById(R.id.popup_img);
        popupTitle = popAddPost.findViewById(R.id.popup_title);
        popupDescription = popAddPost.findViewById(R.id.popup_description);
        popupAddBtn = popAddPost.findViewById(R.id.popup_add);
        popupClickProgress = popAddPost.findViewById(R.id.popup_progressBar);

        popupPostImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // here when image clicked we need to open the gallery
                // before we open the gallery we need to check if our app have the access to user files
//                checkAndRequestForPermission();
                openGallery();
            }
        });

//
//        // Add post click Listener
//
        popupAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                popupAddBtn.setVisibility(View.INVISIBLE);
                popupClickProgress.setVisibility(View.VISIBLE);

                // we need to test all input fields (Title and description ) and post image

                if (!popupTitle.getText().toString().isEmpty()
                        && !popupDescription.getText().toString().isEmpty()
                        && pickedImgUri != null ) {

                    //everything is okey no empty or null value
                    // TODO Create Post Object and add it to firebase database
                    // first we need to upload post Image
                    // access firebase storage
                    StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("Article_images");
                    final StorageReference imageFilePath = storageReference.child(pickedImgUri.getLastPathSegment());
                    imageFilePath.putFile(pickedImgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            imageFilePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String imageDownlaodLink = uri.toString();
                                    // create post Object
                                    Post post = new Post(
                                            popupTitle.getText().toString(),
                                            popupDescription.getText().toString(),
                                            imageDownlaodLink,
                                            FirebaseAuth.getInstance().getCurrentUser().getUid()
                                    );

                                    // Add post to firebase database

                                    updatePost(post);

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    // something goes wrong uploading picture

                                    showMessage(e.getMessage());
                                    popupClickProgress.setVisibility(View.INVISIBLE);
                                    popupAddBtn.setVisibility(View.VISIBLE);
                                }
                            });
                        }
                    });
                }

                else if (!popupTitle.getText().toString().isEmpty()
                        && !popupDescription.getText().toString().isEmpty()
                        && intImg != null){

                        //everything is okey no empty or null value
                        // TODO Create Post Object and add it to firebase database
                        // first we need to upload post Image
                        // access firebase storage
//                        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("Article_images");
//                        final StorageReference imageFilePath = storageReference.child(pickedImgUri.getLastPathSegment());
//                        imageFilePath.putFile(pickedImgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                            @Override
//                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//
//                                imageFilePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                                    @Override
//                                    public void onSuccess(Uri uri) {
//                                        String imageDownlaodLink = uri.toString();
//                                        // create post Object
                                        Post post = new Post(
                                                popupTitle.getText().toString(),
                                                popupDescription.getText().toString(),
                                                intImg,
                                                FirebaseAuth.getInstance().getCurrentUser().getUid()
                                        );

                                        // Add post to firebase database

                                    updatePost(post);

                                    }
//                                }).addOnFailureListener(new OnFailureListener() {
//                                    @Override
//                                    public void onFailure(@NonNull Exception e) {
//                                        // something goes wrong uploading picture
//
//                                        showMessage(e.getMessage());
//                                        popupClickProgress.setVisibility(View.INVISIBLE);
//                                        popupAddBtn.setVisibility(View.VISIBLE);
//                                    }
//                                });
//                            }
//                        });
//                    }
                else{
                    showMessage("Please verify all input fields and choose Post Image") ;
                    popupAddBtn.setVisibility(View.VISIBLE);
                    popupClickProgress.setVisibility(View.INVISIBLE);

                }



            }
        });



    }

    private void showMessage(String message) {
        Toast.makeText(this,message,Toast.LENGTH_LONG).show();
    }

    private void openGallery() {
        //TODO: open gallery intent and wait for user to pick an image !

        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent,REQUESCODE);
    }
//
    public static void getcurrentDateTime() {

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("hh:mm a");
        saveCurrentTime = currentTime.format(calendar.getTime());
    }

    private void updatePost(Post post) {


        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference().child("Articles");
//
//        // get post unique ID and upadte post key
//        String key = myRef.getKey();
        post.setPostKey(intPostkey);
        getcurrentDateTime();
        post.setTimeStamp(saveCurrentTime+" - "+saveCurrentDate);


        // add post data to firebase database

        myRef.child(intPostkey).setValue(post).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                showMessage("Post Updated successfully");
                popupClickProgress.setVisibility(View.INVISIBLE);
                popupAddBtn.setVisibility(View.VISIBLE);
                popAddPost.dismiss();
            }
        });
    }

//     when user picked an image ...
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == REQUESCODE && data != null ) {

            // the user has successfully picked an image
            // we need to save its reference to a Uri variable
            pickedImgUri = data.getData() ;
            popupPostImage.setImageURI(pickedImgUri);

        }

    }

}