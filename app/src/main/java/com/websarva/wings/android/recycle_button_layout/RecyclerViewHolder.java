package com.websarva.wings.android.recycle_button_layout;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

class RecyclerViewHolder extends RecyclerView.ViewHolder {

    TextView menuView;
    TextView priceView;
    TextView orderedCountView;
    TextView dateView;
    ImageView imageView;

    RecyclerViewHolder(View _view){
        super(_view);
        menuView = _view.findViewById(R.id.txv_menu);
        priceView = _view.findViewById(R.id.txv_price);
        orderedCountView = _view.findViewById(R.id.txv_count);
        dateView = _view.findViewById(R.id.txv_date);
        imageView = _view.findViewById(R.id.txv_image);
    }
}
