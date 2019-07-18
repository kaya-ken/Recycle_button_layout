package com.websarva.wings.android.recycle_button_layout;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class IdSend extends FragmentActivity {
    private String ipAddress = "172.20.75.181";
    private String mySlackID = "UC138FKKQ";/*kayahara's ID*/
    private String myData;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        beforeStage();
    }

    protected void beforeStage(){
        Bundle bundle_ = new Bundle();
        bundle_.putString("IP_ADDRESS", ipAddress);
        bundle_.putString("DATA", mySlackID);
        getSupportLoaderManager().restartLoader(1, bundle_, callbacks);
    }
    public void setData(String _data){
        myData = _data;
    }



    private final LoaderManager.LoaderCallbacks<String> callbacks = new LoaderManager.LoaderCallbacks<String>() {
        @NonNull
        @Override
        public Loader<String> onCreateLoader(int i, @Nullable Bundle bundle) {
            return new ConnectSocketAsyncTaskLoader(getApplicationContext(),
                    bundle.getString("IP_ADDRESS"), bundle.getString("DATA"));
        }

        @Override
        public void onLoadFinished(@NonNull Loader<String> loader, String s) {
            getSupportLoaderManager().destroyLoader(loader.getId());
        }

        @Override
        public void onLoaderReset(@NonNull Loader<String> loader) { }
    };
}
