package com.arslan6015.clubherofitadmin.Adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.arslan6015.clubherofitadmin.Interface.ItemClickListener;
import com.arslan6015.clubherofitadmin.Model.BookingInfo;
import com.arslan6015.clubherofitadmin.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.ViewHolder> {
    private Context context;
    private List<BookingInfo> bookingInfoList;
//        EditText areas, numberOfChildren, monthlyExpensis;

    //in constructor pass the context, & arraylist type object
    public BookingAdapter(Context context, List<BookingInfo> bookingInfoList) {
        this.context = context;
        this.bookingInfoList = bookingInfoList;
    }

    //in creating adapter we have to implement three method.
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //here list_data.xml layout converts into the view
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.booking_data_item, parent, false);
        //Whenever adapter calls return that view into the viewHolder() constructors which is seen down below.
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.nameContacts.setText(bookingInfoList.get(position).getFullName());
        holder.emailContacts.setText(bookingInfoList.get(position).getEmail());
//        holder.txtAddressBene.setText(beneficiaryLists.get(position).getAddress());
        Picasso.get().load(bookingInfoList.get(position).getImage()).placeholder(R.drawable.maleicon).into(holder.profile_image);

        Log.e("TAG",String.valueOf(bookingInfoList.size()));
        Log.e("TAG",bookingInfoList.get(position).getFullName());
        Log.e("TAG",bookingInfoList.get(position).getEmail());

        //In onBindViewHolder whenever
//        holder.setItemClickListener(new ItemClickListener() {
//            //call the override method of interface here.
//            @Override
//            public void onClick(View view, int position, boolean isLongClick) {
////                String key = beneficiaryDataLists.get(position)
////                UpdateDialogBox(position);
//                Intent intent = new Intent(context, ContactsFriend.class);
//                intent.putExtra("txtName",contactsLists.get(position).getFullName());
//                intent.putExtra("txtEmail",contactsLists.get(position).getEmail());
//                intent.putExtra("txtId",contactsLists.get(position).getId());
//                intent.putExtra("txtProfileImage",contactsLists.get(position).getImage());
//                context.startActivity(intent);
//            }
//        });
//

    }

    //Returns the arraylist size.
    @Override
    public int getItemCount() {
        return bookingInfoList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
            //In order to get click events just implements the View.OnClickListener
            implements View.OnClickListener {
        // declare list_data.xml Textview
        public TextView nameContacts, emailContacts, isOnline;
        public CircleImageView profile_image,profile_online;
        // create object of interface
        private ItemClickListener itemClickListener;
        private LinearLayout firstLayout;

        //ViewHolder constructor
        public ViewHolder(View itemView) {
            super(itemView);
            //initialize the textview of list_data.xml
            firstLayout = itemView.findViewById(R.id.firstLayout);
            profile_image = itemView.findViewById(R.id.profile_image_con);
            nameContacts = itemView.findViewById(R.id.nameContacts);
            emailContacts = itemView.findViewById(R.id.emailContacts);
            isOnline = itemView.findViewById(R.id.isOnline);
            profile_online = itemView.findViewById(R.id.profile_online_con);
            //whenever any of the item in the recyclerview is clicked
            itemView.setOnClickListener(this);
        }
//

//So, the following changes in my code, help me to achieve my output. 1)
// The method onBindViewHolder is called every time when you bind your view with data.
// So there is not the best place to set click listener. You don't have to set OnClickListener many times for the one View.
// Thats why, i wrote click listeners in ViewHolder, (actually that was not my question,
// but i read somewhere that it would be the best practice, thats why i am following it)
//
//like this,
//            itemView.setOnLongClickListener(new View.OnLongClickListener() {
//                @Override
//                public boolean onLongClick(View v) {
//
//                    int p = getLayoutPosition();
//                    Log.e("TAG", "LongClick: " + p);
////                        removeValues(p);
//                    return true;
//                }
//            });
//        }


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
}

//        private void removeValues(final int p) {
//
//            //create a new dialog
//            new AlertDialog.Builder(context)
//                    .setTitle("Delete entry")
//                    .setMessage("Are you sure you want to delete this entry?")
//
//                    // Specifying a listener allows you to take an action before dismissing the dialog.
//                    // The dialog is automatically dismissed when a dialog button is clicked.
//                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int which) {
//                            // Continue with delete operation
//                            FirebaseDatabase database = FirebaseDatabase.getInstance();
//                            database.getReference("BeneficiaryData")
//                                    .child(beneficiaryDataLists.get(p).getId())
//                                    .removeValue();
//
//                            beneficiaryDataLists.remove(p);                       // remove that particular index from arraylist
////                        adapter.notifyItemRemoved(p);                   // notify the adapter that the particular item is removed.
//                            beneficiaryDataLists.clear();                         //make sure to clear the old list. other wise data will duplicate each time when the activity call
////                        adapter.notifyDataSetChanged();                 //notify the adapter that data is changed
//                        }
//                    })
//
//                    // A null listener allows the button to dismiss the dialog and take no further action.
//                    .setNegativeButton(android.R.string.no, null)
//                    .setIcon(android.R.drawable.ic_dialog_alert)
//                    .show();
//        }
//
//        private void UpdateDialogBox(final int position) {
//            androidx.appcompat.app.AlertDialog.Builder alertDialog =
//                    new AlertDialog.Builder(new ContextThemeWrapper(context, R.style.AlertDialogCustom));
//            alertDialog.setTitle(" Update Item");
//            alertDialog.setMessage("Please fill fulls Information");
//
//
//            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//
//            View add_item_layout = inflater.inflate(R.layout.add_new_item_layout, null);
//
//            areas = add_item_layout.findViewById(R.id.areas);
//            numberOfChildren = add_item_layout.findViewById(R.id.numberOfChildren);
//            monthlyExpensis = add_item_layout.findViewById(R.id.monthlyExpensis);
//
//            areas.setText(beneficiaryDataLists.get(position).getArea());
//            numberOfChildren.setText(beneficiaryDataLists.get(position).getNumberOfChildren());
//            monthlyExpensis.setText(beneficiaryDataLists.get(position).getMonthlyExpensis());
//
//            alertDialog.setView(add_item_layout);
//
//
////        alertDialog.setIcon(R.drawable.ic_baseline_add_24);
//
//
//            //set button
//            alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    dialog.dismiss();
//
//                    BeneficiaryDataList beneficiaryDataList = new BeneficiaryDataList(
//                            beneficiaryDataLists.get(position).getId(),
//                            areas.getText().toString(),
//                            numberOfChildren.getText().toString(),
//                            monthlyExpensis.getText().toString()
//                    );
//                    if (beneficiaryDataList != null) {
//                        FirebaseDatabase.getInstance().getReference("BeneficiaryData")
//                                .child(beneficiaryDataLists.get(position).getId()).setValue(beneficiaryDataList);
//                        beneficiaryDataLists.clear();
////                    Snackbar.make(rootLayout, "New Category" + newFood.getName() + "was added", Snackbar.LENGTH_LONG).show();
//                        Toast.makeText(context, "Item Updated", Toast.LENGTH_LONG).show();
//                    }
//                }
//            });
//            alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    dialog.dismiss();
//                }
//            });
//            alertDialog.show();
//        }
//    };
