package com.example.n.myfirstapplication;

/**
 * Created by n on 19/07/2017.
 */

public class ItemRequest extends ItemInListView {
    public ItemRequest(String name, String email){
        super(name, email, 1);
        this.setRequest(true);
    }
}
