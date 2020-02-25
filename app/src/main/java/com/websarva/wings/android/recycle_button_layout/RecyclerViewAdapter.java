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
        RecyclerViewHolder holder_ = new RecyclerViewHolder(inflate);
        return holder_;
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder _holder, int _pos) {
        Product data_ = list.get(_pos);
        _holder.menuView.setText(data_.getName());
        _holder.priceView.setText("￥"+data_.getPrice());
        _holder.orderedCountView.setText("\n注文回数 : "+data_.getOrderedCount());
        _holder.dateView.setText("追加日 :"+data_.getAddedDate());
        _holder.imageView.setImageResource(data_.getBitmapImage());
    }

     @Override
     public int getItemCount() {
            return list.size();
     }
}
