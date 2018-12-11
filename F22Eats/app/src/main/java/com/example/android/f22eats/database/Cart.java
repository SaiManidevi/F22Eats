package com.example.android.f22eats.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "cart_table")
public class Cart {

    @NonNull
    @PrimaryKey
    @ColumnInfo(name = "item_name")
    private String mItemName;

    @NonNull
    @ColumnInfo(name = "item_price")
    private String mItemPrice;

    @NonNull
    @ColumnInfo(name = "item_quantity")
    private String mItemQuantity;

    public Cart(@NonNull String mItemName, @NonNull String mItemPrice, @NonNull String mItemQuantity) {
        this.mItemName = mItemName;
        this.mItemPrice = mItemPrice;
        this.mItemQuantity = mItemQuantity;
    }

    @NonNull
    public String getItemName() {
        return mItemName;
    }

    @NonNull
    public String getItemPrice() {
        return mItemPrice;
    }

    @NonNull
    public String getItemQuantity() {
        return mItemQuantity;
    }
}
