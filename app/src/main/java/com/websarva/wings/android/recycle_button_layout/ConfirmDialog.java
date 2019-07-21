package com.websarva.wings.android.recycle_button_layout;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;


public class ConfirmDialog extends DialogFragment {

    public interface MainFragmentListener{
        public void onNextButtonClicked();
    }

    MainFragmentListener mainFragmentListener_;
    @Override
    public void onAttach(Activity _activity){
        super.onAttach(_activity);
        mainFragmentListener_ = (MainFragmentListener)_activity;
    }
    // ダイアログが生成された時に呼ばれるメソッド ※必須
    public Dialog onCreateDialog(Bundle savedInstanceState){
        final String menuName = getArguments().getString("Name");
        int menuPrice = getArguments().getInt("Price");
        AlertDialog.Builder confirm = new AlertDialog.Builder(getActivity());

        confirm.setTitle("以下のメニューを注文します．");
        confirm.setMessage("商品名 : "+menuName+"\n価格 : ￥"+menuPrice);
        confirm.setPositiveButton("OK", new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialog, int which) {
                /*
                注文時処理をここに書く
                */

                Intent intent_ = new Intent();
                if(getArguments() != null){
                    intent_.putExtras(getArguments());
                }
                mainFragmentListener_.onNextButtonClicked();

//                getParentFragment().onActivityResult(getTargetRequestCode(), which, intent_);

//                SendingStart(menuName);
                /*
                 */

                //メニューを閉じてQRコード読み取り画面へ戻る
                if (getActivity() != null) {
                    QRCodeReader.mFlag.setFinishFlg(true);
                    getActivity().finish();
                }
            }
        });

        // キャンセルボタン作成
        confirm.setNegativeButton("キャンセル", new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // キャンセルを押された処理
            }
        });

        // dialogBuilderを返す
        return confirm.create();
    }



//    public void SendingStart(String SendData){
//        String ipAddress = "172.20.75.181";
//        String mySlackID = "UC138FKKQ";/*kayahara's ID*/
//
//        Bundle bundle_ = new Bundle();
//        bundle_.putString("IP_ADDRESS", ipAddress);
//        bundle_.putString("DATA", SendData);
//        getSupportLoaderManager().restartLoader(1, bundle_, callbacks);
//    }
//    private final LoaderManager.LoaderCallbacks<String> callbacks = new LoaderManager.LoaderCallbacks<String>() {
//        @NonNull
//        @Override
//        public Loader<String> onCreateLoader(int i, @Nullable Bundle bundle) {
//            return new ConnectSocketAsyncTaskLoader(getApplicationContext(),
//                    bundle.getString("IP_ADDRESS"), bundle.getString("DATA"));
//        }
//
//        @Override
//        public void onLoadFinished(@NonNull Loader<String> loader, String s) {
//            getSupportLoaderManager().destroyLoader(loader.getId());
//        }
//
//        @Override
//        public void onLoaderReset(@NonNull Loader<String> loader) { }
//    };
}
