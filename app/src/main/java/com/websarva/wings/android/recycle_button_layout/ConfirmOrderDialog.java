package com.websarva.wings.android.recycle_button_layout;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;


public class ConfirmOrderDialog extends DialogFragment {

    public interface MainFragmentListener{
        void onNextButtonClicked(int productPosition, String productName);
    }

    MainFragmentListener mainFragmentListener_;
    @Override
    public void onAttach(Activity _activity){
        super.onAttach(_activity);
        mainFragmentListener_ = (MainFragmentListener)_activity;
    }
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState){
        final String productID = getArguments().getString("ProductID");
        final String productName = getArguments().getString("ProductName");
        final int productPrice = getArguments().getInt("ProductPrice");
        final int productPosition = getArguments().getInt("ProductPosition");
        AlertDialog.Builder confirm = new AlertDialog.Builder(getActivity());

        confirm.setTitle("以下のメニューを注文します．");
        confirm.setMessage("商品名 : " + productName + "\n価格 : ￥" + productPrice);
        confirm.setPositiveButton("OK", new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent_ = new Intent();
                if(getArguments() != null){
                    intent_.putExtras(getArguments());
                }
                mainFragmentListener_.onNextButtonClicked(productPosition, productID);
            }
        });

        // キャンセルボタン作成
        confirm.setNegativeButton("キャンセル", new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // キャンセルを押された処理
            }
        });

        return confirm.create();
    }
}
