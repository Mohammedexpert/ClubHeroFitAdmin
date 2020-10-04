package com.arslan6015.clubherofitadmin.ui.News;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.arslan6015.clubherofitadmin.Adapters.ClassesAdapter;
import com.arslan6015.clubherofitadmin.Adapters.NewsAdapter;
import com.arslan6015.clubherofitadmin.Model.ClassesList;
import com.arslan6015.clubherofitadmin.Model.NewsList;
import com.arslan6015.clubherofitadmin.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class NewsFragment extends Fragment {
    public static String saveCurrentDate,saveCurrentTime;
    FloatingActionButton fab_news;
    EditText title, desp;

    //Firebase
    FirebaseDatabase db;
    DatabaseReference itemListNews;
    RecyclerView recycler_news;
    RecyclerView.LayoutManager layoutManager;
    private List<NewsList> newsLists;
    //I use RollNoAdapter as static because in the Viewholder i have to call.
    public static NewsAdapter adapterNews;
//    private String saveCurrentDate, saveCurrentTime;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_news, container, false);
        db = FirebaseDatabase.getInstance();
        itemListNews = db.getReference().child("News");

        fab_news = root.findViewById(R.id.fab_news);
        fab_news.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogBox();
            }
        });

        recycler_news = root.findViewById(R.id.recycler_news);
        recycler_news.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        recycler_news.setLayoutManager(layoutManager);
        newsLists = new ArrayList<>();
//        add predefined firebase listener
        itemListNews.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    //use foreach loop
                    newsLists.clear();
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        //get value from the model class
                        NewsList l = postSnapshot.getValue(NewsList.class);
//                        Log.e("TAG",l.getObtainedMarks());
                        //add these values inside Arraylist
                        Log.e("TAG", postSnapshot.getKey());
                        newsLists.add(l);
                    }
                    //pass the ArrayList in the constructor of RollNoAdapter
                    adapterNews = new NewsAdapter(getContext(), newsLists);
                    //After completing the process of Adapter set the adapter to the recyclerview
                    recycler_news.setAdapter(adapterNews);
                    //Whenever the data is changed it will inform the adapter
                    adapterNews.notifyDataSetChanged();
                }
            }

            //In case of any error.
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("TAGError", "The read failed: " + databaseError.getMessage());
            }
        });

        Log.e("TAG", "onCreateView");
        return root;
    }


    private void showDialogBox() {
        androidx.appcompat.app.AlertDialog.Builder alertDialog =
                new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), R.style.AlertDialogCustom));
        alertDialog.setTitle(" Add new Item");
        alertDialog.setMessage("Please fill fulls Information");


        LayoutInflater inflater = this.getLayoutInflater();
        View add_item_layout_news = inflater.inflate(R.layout.add_new_item_layout_news, null);

        title = add_item_layout_news.findViewById(R.id.title);
        desp = add_item_layout_news.findViewById(R.id.desp);
        alertDialog.setView(add_item_layout_news);

//        alertDialog.setIcon(R.drawable.ic_baseline_add_24);


        //set button
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

                getcurrentDateTime();
                String id = itemListNews.push().getKey();
                NewsList newsList = new NewsList(
                        id,
                        title.getText().toString(),
                        desp.getText().toString(),
                        saveCurrentTime+" - "+saveCurrentDate
                );
                if (newsList != null) {
                    itemListNews.child(id).setValue(newsList);
//                    beneficiaryLists.clear();
//                    Snackbar.make(rootLayout, "New Category" + newFood.getName() + "was added", Snackbar.LENGTH_LONG).show();
                    Toast.makeText(getActivity(), "New item added", Toast.LENGTH_LONG).show();
                }
            }
        });
        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.show();

    }

    public static void getcurrentDateTime(){

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("hh:mm a");
        saveCurrentTime = currentTime.format(calendar.getTime());
    }
}