package com.arslan6015.clubherofitadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;

import com.arslan6015.clubherofitadmin.Adapters.BookingAdapter;
import com.arslan6015.clubherofitadmin.Model.BookingInfo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SeeBooking extends AppCompatActivity {

    LinearLayout firstLayout;
    RecyclerView recycler_booking;
    //Firebase
    FirebaseDatabase db;
    DatabaseReference itemListBooking;
    RecyclerView.LayoutManager layoutManager;
    private List<BookingInfo> bookingInfoList;
    public static BookingAdapter adapterBooking;
    String currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_booking);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Booking list");

        Intent intent = getIntent();
        String classId = intent.getStringExtra("ClassId");
        Log.e("TAG", classId);


        db = FirebaseDatabase.getInstance();
        itemListBooking = db.getReference().child("Classes");

        currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
        recycler_booking = findViewById(R.id.recycler_booking);
        recycler_booking.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(SeeBooking.this);
        recycler_booking.setLayoutManager(layoutManager);
        bookingInfoList = new ArrayList<>();
//        add predefined firebase listener
        itemListBooking.child(classId).child("BookingList").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    //use foreach loop
                    bookingInfoList.clear();
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        //get value from the model class
                        BookingInfo l = postSnapshot.getValue(BookingInfo.class);
//                        Log.e("TAG",l.getObtainedMarks());
                        //add these values inside Arraylist
                        Log.e("TAG", postSnapshot.getKey());
                        bookingInfoList.add(l);
                    }
                    //pass the ArrayList in the constructor of RollNoAdapter
                    adapterBooking = new BookingAdapter(SeeBooking.this, bookingInfoList);
                    //After completing the process of Adapter set the adapter to the recyclerview
                    recycler_booking.setAdapter(adapterBooking);
                    //Whenever the data is changed it will inform the adapter
                    adapterBooking.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }



    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

}