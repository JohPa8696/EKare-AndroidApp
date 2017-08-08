package com.example.n.myfirstapplication;

/**
 * Created by n on 19/07/2017.
 */

public class ItemSeperator extends ItemInListView{
    public ItemSeperator(){}

    public ItemSeperator(String title){
        super(title);
        this.setSeparator(true);
    }
}
