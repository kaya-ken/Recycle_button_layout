package com.websarva.wings.android.recycle_button_layout;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Menu{
    private String id;
    private String name;
    private int price;
    private int orderedCount;
    private int addedDate;
    private int bitmapImage;

    Menu(String _id, String _name, int _price, int _orderedCount, int _addedDate){
        this.id = _id;
        this.name = _name;
        this.price = _price;
        this.orderedCount = _orderedCount;
        this.addedDate = _addedDate;
        this.bitmapImage = R.drawable.item05;
    }

    String getId(){
        return this.id;
    }

    String getName() {
        return this.name;
    }

    int getPrice() {
        return this.price;
    }

    int getOrderedCount() {
        return this.orderedCount;
    }

    int getAddedDate() {
        return this.addedDate;
    }

    int getBitmapImage(){
        return this.bitmapImage;
    }

    static void sortByCount(List<Menu> menuList_){
        Collections.sort(menuList_, new Comparator<Menu>() {
            @Override
            public int compare(Menu o1, Menu o2) {
                return o2.orderedCount - o1.orderedCount;
            }
        });
    }

    static void sortByPrice(List<Menu> menuList_){
        Collections.sort(menuList_, new Comparator<Menu>() {
            @Override
            public int compare(Menu o1, Menu o2) {
                return o2.price - o1.price;
            }
        });
    }

    static void sortByDate(List<Menu> menuList_){
        Collections.sort(menuList_, new Comparator<Menu>() {
            @Override
            public int compare(Menu o1, Menu o2) {
                return o2.addedDate - o1.addedDate;
            }
        });
    }
}
