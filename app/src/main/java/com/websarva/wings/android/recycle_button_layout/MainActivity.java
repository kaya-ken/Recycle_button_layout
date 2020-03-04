package com.websarva.wings.android.recycle_button_layout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ConfirmOrderDialog.MainFragmentListener {
    private List<Product> menu;
    private RecyclerViewAdapter mAdapter;

    private Gson gson = new Gson();

    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        String sortOptions[] = {"人気順", "価格順", "新着順"};
        GridLayoutManager mLayoutManager;

        super.onCreate(savedInstanceState);
        final Intent intent = getIntent();
        userID = intent.getStringExtra("ID");

        setContentView(R.layout.activity_main);
        final Spinner spinner = findViewById(R.id.spinner);
        RecyclerView mRecyclerView = findViewById(R.id.recycler_view);
        TextView userName = findViewById(R.id.name);

        menu = new ArrayList<>();

        userName.setText(String.format("%sさん", userID));

        ArrayAdapter<String> adapter
                = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item, sortOptions);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    //人気順でソート
                    case 0:
                        Product.sortByCount(menu);
                        break;
                    //価格順でソート
                    case 1:
                        Product.sortByPrice(menu);
                        break;
                    //新着順でソート
                    case 2:
                        Product.sortByDate(menu);
                        break;
                }
                mAdapter.notifyDataSetChanged();
            }

            public void onNothingSelected(AdapterView<?> parent) { }
        });

        mAdapter = new RecyclerViewAdapter(this.menu){
            @Override
            public RecyclerViewHolder onCreateViewHolder(ViewGroup _parent, int _viewType) {
                final RecyclerViewHolder holder_ = super.onCreateViewHolder(_parent, _viewType);
                holder_.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final int position = holder_.getAdapterPosition();
                        Product item = menu.get(position);

                        ConfirmOrderDialog dialogFragment = new ConfirmOrderDialog();

                        Bundle bundle = new Bundle();
                        bundle.putString("ProductID", item.getId());
                        bundle.putString("ProductName", item.getName());
                        bundle.putInt("ProductPrice", item.getPrice());
                        bundle.putInt("ProductPosition", position);
                        dialogFragment.setArguments(bundle);

                        dialogFragment.show(getSupportFragmentManager(), "purchase_confirm_dialog");
                    }
                });
                return  holder_;
            }
        };
        mLayoutManager = new GridLayoutManager(this,2);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String json = pref.getString("MENUDATA", "");
        Type listType_ = new TypeToken<List<Product>>(){}.getType();
        List<Product> receivedMenuList_;

        if(json.equals("")) {
            try {
                Resources res_ = this.getResources();
                InputStream inputStream_ = res_.openRawResource(R.raw.menu);
                BufferedReader reader_ = new BufferedReader(new InputStreamReader(inputStream_));

                String str = "";
                while((str = reader_.readLine()) != null){
                    json += str;
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }

        receivedMenuList_ = gson.fromJson(json, listType_);
        initMenu(receivedMenuList_);
    }

    private void initMenu(List<Product> _receivedProductList){
        for(Product receivedProduct_ : _receivedProductList) {
            receivedProduct_.setBitmapImage(R.drawable.item05);
            addProduct(receivedProduct_);
        }
    }

    public void addProduct(Product _productData){
        menu.add(_productData);
        mAdapter.notifyItemChanged(menu.size()-1);
    }

    public void buttonClick(View view){
        QRCodeReader.mFlag.setFinishFlg(true);
        finish();
    }

    private final LoaderManager.LoaderCallbacks<LoggingResult> callbacks = new LoaderManager.LoaderCallbacks<LoggingResult>() {
        @NonNull
        @Override
        public Loader<LoggingResult> onCreateLoader(int i, @Nullable Bundle bundle) {
            return new ConnectSocketAsyncTaskLoader(getApplicationContext(),
                    bundle.getInt("POSITION"),
                    bundle.getString("IPADDRESS"), bundle.getString("DATA"));
        }

        @Override
        public void onLoadFinished(@NonNull Loader<LoggingResult> loader, LoggingResult res) {
            getSupportLoaderManager().destroyLoader(loader.getId());

            if(res.status.equals("ok")) {
                menu.get(res.position).increaseOrderedCount();
                mAdapter.notifyItemChanged(res.position, true);

                SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                String json = gson.toJson(menu);
                pref.edit().putString("MENUDATA", json).apply();
            }
        }

        @Override
        public void onLoaderReset(@NonNull Loader<LoggingResult> loader) { }
    };

    @Override
    public void onNextButtonClicked(int _productPosition, String _productID){
        String ipAddress_ = "";

        String sending_json =
                "{" + "\"slack_id\":" + "\"" + userID + "\","
                       + "\"product_id\":" + "\"" + _productID + "\"}";

        Bundle bundle_ = new Bundle();
        bundle_.putInt("POSITION", _productPosition);
        bundle_.putString("IPADDRESS", ipAddress_);
        bundle_.putString("DATA", sending_json);

        getSupportLoaderManager().restartLoader(0, bundle_, callbacks);
    }
}
