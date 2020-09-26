package com.arslan6015.clubherofitadmin.ui.Classes;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.arslan6015.clubherofitadmin.Adapters.ClassesAdapter;
import com.arslan6015.clubherofitadmin.Model.ClassesList;
import com.arslan6015.clubherofitadmin.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static android.app.Activity.RESULT_OK;

public class ClassesFragment extends Fragment {

    FloatingActionButton fabClasses;
    EditText name, time;

    //Firebase
    FirebaseDatabase db;
    DatabaseReference itemListClasses;
    RecyclerView recycler_classes;
    RecyclerView.LayoutManager layoutManager;
    private List<ClassesList> classesLists;
    //I use RollNoAdapter as static because in the Viewholder i have to call.
    public static ClassesAdapter adapterClasses;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_classes, container, false);

        db = FirebaseDatabase.getInstance();
        itemListClasses = db.getReference().child("Classes");

        fabClasses = root.findViewById(R.id.fabClasses);
        fabClasses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogBox();
            }
        });

        recycler_classes = root.findViewById(R.id.recycler_classes);
        recycler_classes.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        recycler_classes.setLayoutManager(layoutManager);
        classesLists = new ArrayList<>();
//        add predefined firebase listener
        itemListClasses.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    //use foreach loop
                    classesLists.clear();
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        //get value from the model class
                        ClassesList l = postSnapshot.getValue(ClassesList.class);
//                        Log.e("TAG",l.getObtainedMarks());
                        //add these values inside Arraylist
                        Log.e("TAG", postSnapshot.getKey());
                        classesLists.add(l);
                    }
                    //pass the ArrayList in the constructor of RollNoAdapter
                    adapterClasses = new ClassesAdapter(getContext(), classesLists);
                    //After completing the process of Adapter set the adapter to the recyclerview
                    recycler_classes.setAdapter(adapterClasses);
                    //Whenever the data is changed it will inform the adapter
                    adapterClasses.notifyDataSetChanged();
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
        View add_item_layout_beneficiary = inflater.inflate(R.layout.add_new_item_layout_classes, null);

        name = add_item_layout_beneficiary.findViewById(R.id.name);
        time = add_item_layout_beneficiary.findViewById(R.id.time);
        alertDialog.setView(add_item_layout_beneficiary);

//        alertDialog.setIcon(R.drawable.ic_baseline_add_24);


        //set button
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

                String id = itemListClasses.push().getKey();
                ClassesList classesList = new ClassesList(
                        id,
                        name.getText().toString(),
                        time.getText().toString()
                );
                if (classesList != null) {
                    itemListClasses.child(id).setValue(classesList);
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

}