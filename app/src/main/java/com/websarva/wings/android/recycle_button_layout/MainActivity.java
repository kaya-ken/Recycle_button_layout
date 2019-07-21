package com.websarva.wings.android.recycle_button_layout;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.support.v4.app.Fragment;
import android.widget.Toast;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class MainActivity extends AppCompatActivity implements ConfirmDialog.MainFragmentListener {

    private String sort_menu[] = {"人気順", "価格順", "新着順"};
    private List<Menu> menuList;
    private RecyclerViewAdapter mAdapter;
    private GridLayoutManager mLayoutManager;
    private Menu[] menu = new Menu[6];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Intent intent = getIntent();

        setContentView(R.layout.activity_main);
        final Spinner spinner = findViewById(R.id.spinner);
        RecyclerView mRecyclerView = findViewById(R.id.recycler_view);
        TextView userName = findViewById(R.id.name);

        //デバッグ用データ
        menu[0] = new Menu("バリスタ",20,5,20190601);
        menu[1] = new Menu("ココアオレ",30,10,20190502);
        menu[2] = new Menu("抹茶オレ",100,6,20190422);
        menu[3] = new Menu("チョコチーノ",120,3,20190602);
        menu[4] = new Menu("レギュラー",55,9,20190614);
        menu[5] = new Menu("チョコチーノ",60,0,20190622);

        userName.setText(intent.getStringExtra("ID"));

        ArrayAdapter<String> adapter
                = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item, sort_menu);
        menuList = new ArrayList<>();

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                /*スピナー内アイテム選択時の処理*/
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

            public void onNothingSelected(AdapterView<?> parent) {
                /*アイテム未選択時の処理*/

            }});

        mAdapter = new RecyclerViewAdapter(this.menuList){
            @Override
            public RecyclerViewHolder onCreateViewHolder(ViewGroup _parent, int _viewType) {
                final RecyclerViewHolder holder_ = super.onCreateViewHolder(_parent, _viewType);
                holder_.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //選択したpositionのアイテムを取得
                        final int position = holder_.getAdapterPosition();
                        Menu item = menuList.get(position);

                        ConfirmDialog dialog = new ConfirmDialog();

                        Bundle bundle = new Bundle();
                        bundle.putString("Name",item.getMenuName());
                        bundle.putInt("Price",item.getMenuPrice());
                        dialog.setArguments(bundle);

//                        dialog.setTargetFragment(MainActivity, 100);
                        dialog.show(getSupportFragmentManager(), "purchase_confirm_dialog");
                    }
                });
                return  holder_;
            }
        };
        mLayoutManager = new GridLayoutManager(this,2);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        for (Menu menu1 : menu) {
            addMenu(menu1);
        }
    }

    public void addMenu(Menu _menuData){
        menuList.add(_menuData);
        mAdapter.notifyItemChanged(menuList.size()-1);
    }

    public void buttonClick(View view){
        QRCodeReader.mFlag.setFinishFlg(true);
        finish();
    }

    @Override
    public void onActivityResult(final int _requestCode, final int _resultCode, final Intent _data){
        if(_requestCode == 100){
            if(_resultCode == DialogInterface.BUTTON_POSITIVE){
                // positive_button
                String url_ = "https://www.google.com";
                OkHttpClient client_ = new OkHttpClient();

                Request request_ = new Request.Builder().url(url_).build();
                Call call_ = client_.newCall(request_);
                try {
                    Response response_ = call_.execute();
                    System.out.println(response_.body().string());
                }
                catch (IOException e){
                    e.printStackTrace();
                }
            }
            else if(_resultCode == DialogInterface.BUTTON_NEGATIVE){
                // negative_button
            }
        }
    }

    @Override
    public void onNextButtonClicked(){
        // positive_button
        String url_ = "http://192.168.189.1:8084";
        OkHttpClient client_ = new OkHttpClient();
        RequestBody requestBody_ = RequestBody.create(MediaType.parse("text/plain"), "Hello");
        Request request = new Request.Builder().url(url_).post(requestBody_).build();
    }
}
