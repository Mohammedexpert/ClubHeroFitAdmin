package com.arslan6015.clubherofitadmin.Adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.arslan6015.clubherofitadmin.ArticleActivity;
import com.arslan6015.clubherofitadmin.Interface.ItemClickListener;
import com.arslan6015.clubherofitadmin.Model.NewsList;
import com.arslan6015.clubherofitadmin.Model.Post;
import com.arslan6015.clubherofitadmin.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.List;

import static android.app.Activity.RESULT_OK;
import static com.arslan6015.clubherofitadmin.ui.News.NewsFragment.getcurrentDateTime;
import static com.arslan6015.clubherofitadmin.ui.News.NewsFragment.saveCurrentDate;
import static com.arslan6015.clubherofitadmin.ui.News.NewsFragment.saveCurrentTime;


public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ViewHolder> {
    private Context context;
    private List<Post> articleLists;

    //for updates popup
    Dialog popAddPost ;
    ImageView popupPostImage,popupAddBtn;
    TextView popupTitle,popupDescription;
    ProgressBar popupClickProgress;
    private Uri pickedImgUri = null;
    private static final int REQUESCODE = 2 ;
    String imageDownlaodLink;


    //in constructor pass the context, & arraylist type object
    public ArticleAdapter(Context context, List<Post> articleLists) {
        this.context = context;
        this.articleLists = articleLists;
    }

    //in creating adapter we have to implement three method.
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //here list_data.xml layout converts into the view
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.new_data_item_articles, parent, false);
        //Whenever adapter calls return that view into the viewHolder() constructors which is seen down below.
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.title_article.setText(articleLists.get(position).getTitle());
        holder.desp_articles.setText(articleLists.get(position).getDescription());
        holder.time_article.setText(articleLists.get(position).getTimeStamp());
        Picasso.get().load(articleLists.get(position).getPicture()).into(holder.img_article);

        holder.read_articles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ArticleActivity.class);
                intent.putExtra("intentArtTitle",articleLists.get(position).getTitle());
                intent.putExtra("intentArtDesp",articleLists.get(position).getDescription());
                intent.putExtra("intentArtImg",articleLists.get(position).getPicture());
                intent.putExtra("intentArtPostKey",articleLists.get(position).getPostKey());
                intent.putExtra("intentArtTimeStamp",articleLists.get(position).getTimeStamp());

                context.startActivity(intent);
            }
        });

//        In onBindViewHolder whenever
        holder.setItemClickListener(new ItemClickListener() {
            //            call the override method of interface here.
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
//                UpdateDialogBox(position);
//                iniPopup(position);
            }
        });


    }

    //Returns the arraylist size.
    @Override
    public int getItemCount() {
        return articleLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
            //In order to get click events just implements the View.OnClickListener
            implements View.OnClickListener {
        // declare list_data.xml Textview
        public TextView title_article, desp_articles,read_articles,time_article;
        private ImageView img_article;
        // create object of interface
        private ItemClickListener itemClickListener;

        //ViewHolder constructor
        public ViewHolder(View itemView) {
            super(itemView);
            //initialize the textview of list_data.xml
            title_article = itemView.findViewById(R.id.title_article);
            desp_articles = itemView.findViewById(R.id.desp_articles);
            img_article = itemView.findViewById(R.id.img_article);
            read_articles = itemView.findViewById(R.id.read_articles);
            time_article = itemView.findViewById(R.id.time_article);
            //whenever any of the item in the recyclerview is clicked
            itemView.setOnClickListener(this);

//

//So, the following changes in my code, help me to achieve my output. 1)
// The method onBindViewHolder is called every time when you bind your view with data.
// So there is not the best place to set click listener. You don't have to set OnClickListener many times for the one View.
// Thats why, i wrote click listeners in ViewHolder, (actually that was not my question,
// but i read somewhere that it would be the best practice, thats why i am following it)
//
//like this,
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    int p = getLayoutPosition();
                    Log.e("TAG", "LongClick: " + p);
                    removeValues(p);
                    return true;
                }
            });
        }


        //        Make a setItemClickListener() so that we can pass Interface method inside it.
        public void setItemClickListener(ItemClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
        }
//

        //         if we implements View.OnClickListener then we have to call onClick() method here.
        public void onClick(View view) {
            // so we pass values to onClick in our custom ItemClickListener interface here.
            itemClickListener.onClick(view, getAdapterPosition(), false);
        }
    }


    private void removeValues(final int p) {

        //create a new dialog
        new AlertDialog.Builder(context)
                .setTitle("Delete entry")
                .setMessage("Are you sure you want to delete this entry?")

                // Specifying a listener allows you to take an action before dismissing the dialog.
                // The dialog is automatically dismissed when a dialog button is clicked.
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Continue with delete operation
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        database.getReference("Articles")
                                .child(articleLists.get(p).getPostKey())
                                .removeValue();

                        articleLists.remove(p);                       // remove that particular index from arraylist
//                        adapter.notifyItemRemoved(p);                   // notify the adapter that the particular item is removed.
                        articleLists.clear();                         //make sure to clear the old list. other wise data will duplicate each time when the activity call
//                        adapter.notifyDataSetChanged();                 //notify the adapter that data is changed
                    }
                })

                // A null listener allows the button to dismiss the dialog and take no further action.
                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

//    private void UpdateDialogBox(final int position) {
//        androidx.appcompat.app.AlertDialog.Builder alertDialog =
//                new AlertDialog.Builder(new ContextThemeWrapper(context, R.style.AlertDialogCustom));
//        alertDialog.setTitle(" Update Item");
//        alertDialog.setMessage("Please fill fulls Information");
//
//
//        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//
//        View add_item_layout_news = inflater.inflate(R.layout.add_new_item_layout_news, null);
//
//        final EditText title = add_item_layout_news.findViewById(R.id.title);
//        final EditText desp = add_item_layout_news.findViewById(R.id.desp);
//
//        title.setText(newsLists.get(position).getNewsTitle());
//        desp.setText(newsLists.get(position).getNewsDesp());
//
//        alertDialog.setView(add_item_layout_news);
////        alertDialog.setIcon(R.drawable.ic_baseline_add_24);
//
//
//        //set button
//        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//
//                getcurrentDateTime();
//                NewsList newsListItem = new NewsList(
//                        newsLists.get(position).getId(),
//                        title.getText().toString(),
//                        desp.getText().toString(),
//                        saveCurrentTime+" - "+saveCurrentDate
//
//                );
//                if (newsListItem != null) {
//                    FirebaseDatabase.getInstance().getReference("News")
//                            .child(newsLists.get(position).getId()).setValue(newsListItem);
//                    newsLists.clear();
////                    Snackbar.make(rootLayout, "New Category" + newFood.getName() + "was added", Snackbar.LENGTH_LONG).show();
//                    Toast.makeText(context, "Item Updated", Toast.LENGTH_LONG).show();
//                }
//            }
//        });
//        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//            }
//        });
//        alertDialog.show();
//    }


//    public void iniPopup(int position) {
//
//        popAddPost = new Dialog(context);
//        popAddPost.setContentView(R.layout.popup_add_post);
//        popAddPost.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        popAddPost.getWindow().setLayout(Toolbar.LayoutParams.MATCH_PARENT,Toolbar.LayoutParams.WRAP_CONTENT);
//        popAddPost.getWindow().getAttributes().gravity = Gravity.TOP;
//
//        // ini popup widgets
//        popupPostImage = popAddPost.findViewById(R.id.popup_img);
//        popupTitle = popAddPost.findViewById(R.id.popup_title);
//        popupDescription = popAddPost.findViewById(R.id.popup_description);
//        popupAddBtn = popAddPost.findViewById(R.id.popup_add);
//        popupClickProgress = popAddPost.findViewById(R.id.popup_progressBar);
//
//        popupTitle.setText(articleLists.get(position).getTitle());
//        popupDescription.setText(articleLists.get(position).getDescription());
//        Picasso.get().load(articleLists.get(position).getPicture()).into(popupPostImage);
//
//        // Add post click Listener
//
//        popupAddBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                popupAddBtn.setVisibility(View.INVISIBLE);
//                popupClickProgress.setVisibility(View.VISIBLE);
//
//                // we need to test all input fields (Title and description ) and post image
//
//                if (!popupTitle.getText().toString().isEmpty()
//                        && !popupDescription.getText().toString().isEmpty()
//                        && pickedImgUri != null ) {
//
//                    //everything is okey no empty or null value
//                    // TODO Create Post Object and add it to firebase database
//                    // first we need to upload post Image
//                    // access firebase storage
//                    StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("Article_images");
//                    final StorageReference imageFilePath = storageReference.child(pickedImgUri.getLastPathSegment());
//                    imageFilePath.putFile(pickedImgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                        @Override
//                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//
//                            imageFilePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                                @Override
//                                public void onSuccess(Uri uri) {
//                                    imageDownlaodLink = uri.toString();
//                                    // create post Object
//                                    Post post = new Post(
//                                            popupTitle.getText().toString(),
//                                            popupDescription.getText().toString(),
//                                            imageDownlaodLink,
//                                            FirebaseAuth.getInstance().getCurrentUser().getUid()
//                                    );
//
//                                    // Add post to firebase database
//
//                                    addPost(post);
//
//                                }
//                            }).addOnFailureListener(new OnFailureListener() {
//                                @Override
//                                public void onFailure(@NonNull Exception e) {
//                                    // something goes wrong uploading picture
//
//                                    showMessage(e.getMessage());
//                                    popupClickProgress.setVisibility(View.INVISIBLE);
//                                    popupAddBtn.setVisibility(View.VISIBLE);
//                                }
//                            });
//                        }
//                    });
//                }
//                else {
//                    showMessage("Please verify all input fields and choose Post Image") ;
//                    popupAddBtn.setVisibility(View.VISIBLE);
//                    popupClickProgress.setVisibility(View.INVISIBLE);
//
//                }
//
//
//
//            }
//        });
//
//
//
//    }

//    private void openGallery() {
//        //TODO: open gallery intent and wait for user to pick an image !
//
//        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
//        galleryIntent.setType("image/*");
//        context.startActivityForResult(galleryIntent,REQUESCODE);
//    }

//    private void addPost(Post post) {
//
//
//        DatabaseReference myRef = db.getReference().child("Articles").push();
//
//        // get post unique ID and upadte post key
//        String key = myRef.getKey();
//        post.setPostKey(key);
//        getcurrentDateTime();
//        post.setTimeStamp(saveCurrentTime+" - "+saveCurrentDate);
//
//
//        // add post data to firebase database
//
//        myRef.setValue(post).addOnSuccessListener(new OnSuccessListener<Void>() {
//            @Override
//            public void onSuccess(Void aVoid) {
//                showMessage("Post Added successfully");
//                popupClickProgress.setVisibility(View.INVISIBLE);
//                popupAddBtn.setVisibility(View.VISIBLE);
//                popAddPost.dismiss();
//            }
//        });
//    }

    // when user picked an image ...
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (resultCode == RESULT_OK && requestCode == REQUESCODE && data != null ) {
//
//            // the user has successfully picked an image
//            // we need to save its reference to a Uri variable
//            pickedImgUri = data.getData() ;
//            popupPostImage.setImageURI(pickedImgUri);
//        }
//    }

    private void showMessage(String message) {

        Toast.makeText(context,message,Toast.LENGTH_LONG).show();

    }
}