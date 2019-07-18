package com.websarva.wings.android.recycle_button_layout;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;


public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewHolder> {

    private List<Menu> list;


     RecyclerViewAdapter( List<Menu> _list) {
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
        Menu data_ = list.get(_pos);
        _holder.menuView.setText(data_.getMenuName());
        _holder.priceView.setText("￥"+data_.getMenuPrice());
        _holder.order_countView.setText("\n注文回数 : "+data_.getOrderCount());
        _holder.dateView.setText("追加日 :"+data_.getAddDate());
        _holder.imageView.setImageResource(data_.getBitImage());
       // _holder.itemView.setIma
        /*if(!data_.getIcon().equals("null")){
            byte[] decodedByteArray_ = Base64.decode(data_.getIcon(), 0);
            Bitmap receivedBmp_ = BitmapFactory.decodeByteArray(decodedByteArray_, 0, decodedByteArray_.length);
            Bitmap resourceBmp_ = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher);

            receivedBmp_ = Bitmap.createScaledBitmap(receivedBmp_, resourceBmp_.getWidth(), resourceBmp_.getHeight(), true);
            _holder.iconView.setImageBitmap(receivedBmp_);
        }*/
    }

     @Override
     public int getItemCount() {
            return list.size();
     }
}
