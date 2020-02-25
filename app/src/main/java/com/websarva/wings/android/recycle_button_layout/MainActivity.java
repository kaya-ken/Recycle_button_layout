package com.websarva.wings.android.recycle_button_layout;

import android.content.Intent;
import android.os.Bundle;
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

        Type listType_ = new TypeToken<List<Product>>(){}.getType();
        // ここでメニューの一覧のjsonを読み込み
        String json = "[\n" +
                "    {\n" +
                "        id:\"001\",\n" +
                "        name:\"バリスタ\",\n" +
                "        price:20,\n" +
                "        orderedCount:0,\n" +
                "        addedDate:20190601\n" +
                "    }\n" +
                "]";

        List receivedMenuList_ = gson.fromJson(json, listType_);
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

    private final LoaderManager.LoaderCallbacks<String> callbacks = new LoaderManager.LoaderCallbacks<String>() {
        @NonNull
        @Override
        public Loader<String> onCreateLoader(int i, @Nullable Bundle bundle) {
            return new ConnectSocketAsyncTaskLoader(getApplicationContext(),
                    bundle.getString("IPADDRESS"), bundle.getString("DATA"));
        }

        @Override
        public void onLoadFinished(@NonNull Loader<String> loader, String s) {
            getSupportLoaderManager().destroyLoader(loader.getId());
        }

        @Override
        public void onLoaderReset(@NonNull Loader<String> loader) { }
    };

    @Override
    public void onNextButtonClicked(int _productPosition, String _productID){
        String ipAddress_ = "";

        String sending_json =
                "{" + "\"slack_id\":" + "\"" + userID + "\","
                       + "\"product_id\":" + "\"" + _productID + "\"}";

        Bundle bundle_ = new Bundle();
        bundle_.putString("IPADDRESS", ipAddress_);
        bundle_.putString("DATA", sending_json);

        menu.get(_productPosition).increaseOrderedCount();
        mAdapter.notifyItemChanged(_productPosition, true);

        getSupportLoaderManager().restartLoader(0, bundle_, callbacks);
    }
}
