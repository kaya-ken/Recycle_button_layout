package com.websarva.wings.android.recycle_button_layout;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;


public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewHolder> {

    private List<Product> list;
    RecyclerViewAdapter( List<Product> _list) {
        this.list = _list;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup _parent, int _viewType) {
        View inflate = LayoutInflater.from(_parent.getContext()).inflate(R.layout.row, _parent, false);
        return  new RecyclerViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder _holder, int _pos) {
        Product data_ = list.get(_pos);
        _holder.menuView.setText(data_.getName());
        _holder.priceView.setText(String.format("￥%s", data_.getPrice()));
        _holder.orderedCountView.setText(String.format("\n注文回数 : %d", data_.getOrderedCount()));
        _holder.dateView.setText(String.format("追加日 : %d", data_.getAddedDate()));
        _holder.imageView.setImageResource(data_.getBitmapImage());
    }

     @Override
     public int getItemCount() {
            return list.size();
     }
}
