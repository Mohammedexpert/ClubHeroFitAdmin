package com.arslan6015.clubherofitadmin.Interface;

import android.view.View;

//call the interface so that we call implements instead of extends.
public interface ItemClickListener {
    //according to interface rules we can only declare method here.
    void onClick(View view, int position, boolean isLongClick);
}