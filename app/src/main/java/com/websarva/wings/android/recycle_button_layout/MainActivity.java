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

    private String sort_menu[] = {"人気順", "価格順", "新着順"};
    private List<Menu> menuList;
    private RecyclerViewAdapter mAdapter;
    private GridLayoutManager mLayoutManager;

    private Gson gson = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Intent intent = getIntent();

        setContentView(R.layout.activity_main);
        final Spinner spinner = findViewById(R.id.spinner);
        RecyclerView mRecyclerView = findViewById(R.id.recycler_view);
        TextView userName = findViewById(R.id.name);

        menuList = new ArrayList<>();

        userName.setText(intent.getStringExtra("ID"));

        ArrayAdapter<String> adapter
                = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item, sort_menu);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    //人気順でソート
                    case 0:
                        Menu.sortByCount(menuList);
                        break;
                    //価格順でソート
                    case 1:
                        Menu.sortByPrice(menuList);
                        break;
                    //新着順でソート
                    case 2:
                        Menu.sortByDate(menuList);
                        break;
                }
                mAdapter.notifyDataSetChanged();
            }

            public void onNothingSelected(AdapterView<?> parent) { }
        });

        mAdapter = new RecyclerViewAdapter(this.menuList){
            @Override
            public RecyclerViewHolder onCreateViewHolder(ViewGroup _parent, int _viewType) {
                final RecyclerViewHolder holder_ = super.onCreateViewHolder(_parent, _viewType);
                holder_.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final int position = holder_.getAdapterPosition();
                        Menu item = menuList.get(position);

                        ConfirmOrderDialog dialogFragment = new ConfirmOrderDialog();

                        Bundle bundle = new Bundle();
                        bundle.putString("Name",item.getName());
                        bundle.putInt("Price",item.getPrice());
                        dialogFragment.setArguments(bundle);

//                        dialog.setTargetFragment(MainActivity, 100);
                        dialogFragment.show(getSupportFragmentManager(), "purchase_confirm_dialog");
                    }
                });
                return  holder_;
            }
        };
        mLayoutManager = new GridLayoutManager(this,2);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        Type listType_ = new TypeToken<List<Menu>>(){}.getType();
        // ここでサーバからメニューのjsonを受信する
        String json = "[\n" +
                "    {\n" +
                "        name:\"バリスタ\",\n" +
                "        price:20,\n" +
                "        orderedCount:5,\n" +
                "        addedDate:20190601\n" +
                "    }\n" +
                "]";

        List receivedMenuList_ = gson.fromJson(json, listType_);
        initMenu(receivedMenuList_);
    }

    private void initMenu(List<Menu> _receivedMenuList){
        for(Menu receivedMenu_: _receivedMenuList)
            addMenu(receivedMenu_);
    }

    public void addMenu(Menu _menuData){
        menuList.add(_menuData);
        mAdapter.notifyItemChanged(menuList.size()-1);
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
    public void onNextButtonClicked(){
        String ipAddress_ = "172.20.75.245";
        String mySlackID_ = "NB29979";

        Bundle bundle_ = new Bundle();
        bundle_.putString("IPADDRESS", ipAddress_);
        bundle_.putString("DATA", mySlackID_);

        getSupportLoaderManager().restartLoader(0, bundle_, callbacks);
    }
}
