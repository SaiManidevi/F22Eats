package com.example.android.f22eats.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface CartDao {
    // Insert cart data
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertCart(Cart cart);

    // Delete (or remove) a single cart item
    @Delete
    void deleteCart(Cart cart);

    // Delete all (or clear all) cart items
    // [used for clear all button in CartActivity]
    @Query("DELETE FROM cart_table")
    void deleteAll();

    // Get / Read all the cart data
    @Query("SELECT * FROM cart_table")
    LiveData<List<Cart>> getAllCartItems();

    @Query("SELECT item_quantity FROM cart_table where item_name LIKE :itemName")
    String getCartItemQuantity(String itemName);
}
