package com.websarva.wings.android.recycle_button_layout;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Menu{
    private String MenuName;
    private int MenuPrice;
    private int OrderCount;
    private int AddDate;
    private int BitImage;

    Menu(String _MenuName, int _MenuPrice, int _OrderCount, int _AddDate){
        this.MenuName = _MenuName;
        this.MenuPrice = _MenuPrice;
        this.OrderCount = _OrderCount;
        this.AddDate = _AddDate;
        this.BitImage = R.drawable.item05;
    }

    String getMenuName() {
        return MenuName;
    }

    int getMenuPrice() {
        return MenuPrice;
    }

    int getOrderCount() {
        return OrderCount;
    }

    int getAddDate() {
        return AddDate;
    }

    int getBitImage(){
        return BitImage;
    }

    static void sortByCount(List<Menu> menuList_){
        Collections.sort(menuList_, new Comparator<Menu>() {
            @Override
            public int compare(Menu o1, Menu o2) {
                return o2.OrderCount - o1.OrderCount;
            }
        });
    }

    static void sortByPrice(List<Menu> menuList_){
        Collections.sort(menuList_, new Comparator<Menu>() {
            @Override
            public int compare(Menu o1, Menu o2) {
                return o2.MenuPrice - o1.MenuPrice;
            }
        });
    }

    static void sortByDate(List<Menu> menuList_){
        Collections.sort(menuList_, new Comparator<Menu>() {
            @Override
            public int compare(Menu o1, Menu o2) {
                return o2.AddDate - o1.AddDate;
            }
        });
    }

    public Menu getData() {
        return this;
    }
}
