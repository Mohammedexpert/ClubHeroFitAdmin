package com.arslan6015.clubherofitadmin.Adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.recyclerview.widget.RecyclerView;

import com.arslan6015.clubherofitadmin.Interface.ItemClickListener;
import com.arslan6015.clubherofitadmin.Model.ClassesList;
import com.arslan6015.clubherofitadmin.R;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class ClassesAdapter extends RecyclerView.Adapter<ClassesAdapter.ViewHolder> {
    private Context context;
    private List<ClassesList> classesLists;

    //in constructor pass the context, & arraylist type object
    public ClassesAdapter(Context context, List<ClassesList> classesLists) {
        this.context = context;
        this.classesLists = classesLists;
    }

    //in creating adapter we have to implement three method.
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //here list_data.xml layout converts into the view
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.classes_data_item, parent, false);
        //Whenever adapter calls return that view into the viewHolder() constructors which is seen down below.
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.name_classes_item.setText(classesLists.get(position).getName());
        holder.time_classes_item.setText(classesLists.get(position).getTime());

//        In onBindViewHolder whenever
        holder.setItemClickListener(new ItemClickListener() {
//            call the override method of interface here.
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                UpdateDialogBox(position);
            }
        });


    }

    //Returns the arraylist size.
    @Override
    public int getItemCount() {
        return classesLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
            //In order to get click events just implements the View.OnClickListener
            implements View.OnClickListener {
        // declare list_data.xml Textview
        public TextView name_classes_item, time_classes_item;
        // create object of interface
        private ItemClickListener itemClickListener;

        //ViewHolder constructor
        public ViewHolder(View itemView) {
            super(itemView);
            //initialize the textview of list_data.xml
            name_classes_item = itemView.findViewById(R.id.name_classes_item);
            time_classes_item = itemView.findViewById(R.id.time_classes_item);
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
                            database.getReference("Classes")
                                    .child(classesLists.get(p).getId())
                                    .removeValue();

                            classesLists.remove(p);                       // remove that particular index from arraylist
//                        adapter.notifyItemRemoved(p);                   // notify the adapter that the particular item is removed.
                            classesLists.clear();                         //make sure to clear the old list. other wise data will duplicate each time when the activity call
//                        adapter.notifyDataSetChanged();                 //notify the adapter that data is changed
                        }
                    })

                    // A null listener allows the button to dismiss the dialog and take no further action.
                    .setNegativeButton(android.R.string.no, null)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }

        private void UpdateDialogBox(final int position) {
            androidx.appcompat.app.AlertDialog.Builder alertDialog =
                    new AlertDialog.Builder(new ContextThemeWrapper(context, R.style.AlertDialogCustom));
            alertDialog.setTitle(" Update Item");
            alertDialog.setMessage("Please fill fulls Information");


            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View add_item_layout = inflater.inflate(R.layout.add_new_item_layout_classes, null);

            final EditText name = add_item_layout.findViewById(R.id.name_classes_item);
            final EditText time = add_item_layout.findViewById(R.id.time_classes_item);

//            name.setText(classesLists.get(position).getName());
//            time.setText(classesLists.get(position).getTime());

            alertDialog.setView(add_item_layout);


//        alertDialog.setIcon(R.drawable.ic_baseline_add_24);


            //set button
            alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();

                    ClassesList classesListItem = new ClassesList(
                            classesLists.get(position).getId(),
                            name.getText().toString(),
                            time.getText().toString()
                    );
                    if (classesListItem != null) {
                        FirebaseDatabase.getInstance().getReference("Classes")
                                .child(classesLists.get(position).getId()).setValue(classesListItem);
                        classesLists.clear();
//                    Snackbar.make(rootLayout, "New Category" + newFood.getName() + "was added", Snackbar.LENGTH_LONG).show();
                        Toast.makeText(context, "Item Updated", Toast.LENGTH_LONG).show();
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