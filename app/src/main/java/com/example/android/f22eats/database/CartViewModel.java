package com.example.android.f22eats.database;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.List;

public class CartViewModel extends AndroidViewModel {
    private CartRepository mCartRepository;
    private LiveData<List<Cart>> mAllCartItems;

    public CartViewModel(@NonNull Application application) {
        super(application);
        mCartRepository = new CartRepository(application);
        mAllCartItems = mCartRepository.getAllCartItems();
    }

    public void insertCartItem(Cart cartItem) {
        mCartRepository.insertCart(cartItem);
    }

    public void deleteCartItem(Cart cartItem) {
        mCartRepository.deleteCart(cartItem);
    }

    public void deleteAll(Cart cartItem) {
        mCartRepository.deleteAllCartItems();
    }

    public LiveData<List<Cart>> getAllCartItems() {
        return mAllCartItems;
    }

    public String getItemQuantity(String item_name) {
        return mCartRepository.getItemQuantity(item_name);
    }
}
