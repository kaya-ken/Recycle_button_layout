package com.websarva.wings.android.recycle_button_layout;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Product {
    private String id;
    private String name;
    private int price;
    private int orderedCount;
    private int addedDate;
    private int bitmapImage;

    Product(String _id, String _name, int _price, int _orderedCount, int _addedDate){
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

    void setBitmapImage(int _bitmapID){
        this.bitmapImage = _bitmapID;
    }

    static void sortByCount(List<Product> _menu){
        Collections.sort(_menu, new Comparator<Product>() {
            @Override
            public int compare(Product o1, Product o2) {
                return o2.orderedCount - o1.orderedCount;
            }
        });
    }

    static void sortByPrice(List<Product> _menu){
        Collections.sort(_menu, new Comparator<Product>() {
            @Override
            public int compare(Product o1, Product o2) {
                return o2.price - o1.price;
            }
        });
    }

    static void sortByDate(List<Product> _menu){
        Collections.sort(_menu, new Comparator<Product>() {
            @Override
            public int compare(Product o1, Product o2) {
                return o2.addedDate - o1.addedDate;
            }
        });
    }
}
