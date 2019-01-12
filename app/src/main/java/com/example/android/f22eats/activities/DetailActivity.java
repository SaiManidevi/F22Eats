package com.example.android.f22eats.activities;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.android.f22eats.R;
import com.example.android.f22eats.database.Cart;
import com.example.android.f22eats.database.CartViewModel;
import com.example.android.f22eats.databinding.ActivityDetailBinding;
import com.example.android.f22eats.model.FoodItem;
import com.example.android.f22eats.utilities.GlideApp;

public class DetailActivity extends AppCompatActivity {

    private static final String TAG = DetailActivity.class.getSimpleName();
    ActivityDetailBinding binding;
    private FoodItem mClickedFoodItem;
    private String mFoodItemName;
    private String mFoodItemImageUrl;
    private String mFoodItemPrice;
    private String mFoodItemRating;
    private String mFoodItemQuantity;
    private int mQuantity;
    private CartViewModel mCartViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail);
        mCartViewModel = ViewModelProviders.of(this).get(CartViewModel.class);
        Intent intent = getIntent();
        if (intent != null) {
            if (intent.hasExtra(Intent.EXTRA_TEXT)) {
                mClickedFoodItem = (FoodItem) intent.getSerializableExtra(Intent.EXTRA_TEXT);
                mFoodItemName = mClickedFoodItem.getItem_name();
                mFoodItemImageUrl = mClickedFoodItem.getImage_url();
                mFoodItemPrice = Double.toString(mClickedFoodItem.getItem_price());
                mFoodItemRating = Double.toString(mClickedFoodItem.getAverage_rating());
            }
        }
        // Set title of ActionBar as the name of clicked food item
        if (!mFoodItemName.isEmpty())
            getSupportActionBar().setTitle(mFoodItemName);
        // Load the image and other data into their respective views
        GlideApp.with(this)
                .load(mFoodItemImageUrl)
                .placeholder(R.drawable.fast_food_icons_311258)
                .error(R.drawable.fast_food_icons_311258)
                .into(binding.ivDescImage);
        binding.tvDescPrice.setText(mFoodItemPrice);
        binding.tvDescRating.setText(mFoodItemRating);
    }

    @Override
    protected void onResume() {
        getItemQuantity();
        super.onResume();
    }

    public void getItemQuantity() {
        mFoodItemQuantity = mCartViewModel.getItemQuantity(mFoodItemName);
        if (mFoodItemQuantity == null)
            mQuantity = 0;
        else
            mQuantity = Integer.valueOf(mFoodItemQuantity);
        binding.tvItemCount.setText(Integer.toString(mQuantity));

    }

    public void onAddItemButtonClick(View view) {
        mQuantity = mQuantity + 1;
        mFoodItemQuantity = Integer.toString(mQuantity);
        Cart cart = new Cart(mFoodItemName, mFoodItemPrice, mFoodItemQuantity);
        mCartViewModel.insertCartItem(cart);
        binding.tvItemCount.setText(mFoodItemQuantity);
    }

    public void onRemoveItemButtomClick(View view) {
        if (mQuantity > 1) {
            mQuantity = mQuantity - 1;
            mFoodItemQuantity = Integer.toString(mQuantity);
            Cart cart = new Cart(mFoodItemName, mFoodItemPrice, mFoodItemQuantity);
            mCartViewModel.insertCartItem(cart);
        } else {
            mQuantity = 0;
            mFoodItemQuantity = Integer.toString(mQuantity);
            Cart cart = new Cart(mFoodItemName, mFoodItemPrice, mFoodItemQuantity);
            mCartViewModel.deleteCartItem(cart);
            Toast.makeText(this, getString(R.string.zero_quantity), Toast.LENGTH_LONG).show();
        }
        binding.tvItemCount.setText(mFoodItemQuantity);
    }

    public void onFabCartButtonClick(View view) {
        Intent intent = new Intent(this, CartActivity.class);
        startActivity(intent);
    }
}
