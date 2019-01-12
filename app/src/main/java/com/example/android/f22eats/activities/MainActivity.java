package com.example.android.f22eats.activities;

import android.app.AlertDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.android.f22eats.R;
import com.example.android.f22eats.database.CartViewModel;
import com.example.android.f22eats.databinding.ActivityMainBinding;
import com.example.android.f22eats.model.FoodItem;
import com.example.android.f22eats.utilities.FoodItemsAdapter;
import com.example.android.f22eats.network.FoodItemsViewModel;
import com.example.android.f22eats.utilities.SimpleIdlingResource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MainActivity extends AppCompatActivity implements FoodItemsViewModel.FoodItemsCallBack, FoodItemsAdapter.FoodItemClickListener {
    private static final String TAG = MainActivity.class.getSimpleName();
    ActivityMainBinding binding;
    FoodItemsViewModel mFoodItemsViewModel;
    CartViewModel mCartViewModel;
    GridLayoutManager gridLayoutManager;
    FoodItemsAdapter adapter;
    private static final int RADIO_RATING = 111;
    private static final int RADIO_LOW = 122;
    private static final int RADIO_HIGH = 133;
    private int buttonCode;
    List<FoodItem> mFoodItemList;
    @Nullable
    private SimpleIdlingResource mIdlingResource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mFoodItemsViewModel = ViewModelProviders.of(this).get(FoodItemsViewModel.class);
        mCartViewModel = ViewModelProviders.of(this).get(CartViewModel.class);
        adapter = new FoodItemsAdapter(this, this);
        gridLayoutManager = new GridLayoutManager(this, 1);
        binding.recyclerviewFoodlist.setLayoutManager(gridLayoutManager);
        binding.recyclerviewFoodlist.setAdapter(adapter);
        binding.recyclerviewFoodlist.setSaveEnabled(true);
        adapter.getViewModelInstance(mCartViewModel);
        if (binding.progressBar.getVisibility() == View.GONE)
            binding.progressBar.setVisibility(View.VISIBLE);
        getIdlingResource();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mFoodItemsViewModel.getFoodData(this, mIdlingResource);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sort_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int menuId = item.getItemId();
        switch (menuId) {
            case R.id.sort_menu:
                showPopUp();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        if (mIdlingResource == null) {
            mIdlingResource = new SimpleIdlingResource();
        }
        return mIdlingResource;
    }

    private void showPopUp() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.popup_layout, null);
        builder.setView(dialogView);

        final AlertDialog alertDialog = builder.create();
        alertDialog.show();

        final RadioButton radioRating = dialogView.findViewById(R.id.radio_rating);
        final RadioButton radioLow = dialogView.findViewById(R.id.radio_sort_low);
        final RadioButton radioHigh = dialogView.findViewById(R.id.radio_sort_high);
        Button applyButton = dialogView.findViewById(R.id.btn_apply);
        applyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (radioRating.isChecked())
                    buttonCode = RADIO_RATING;
                else if (radioHigh.isChecked())
                    buttonCode = RADIO_HIGH;
                else if (radioLow.isChecked())
                    buttonCode = RADIO_LOW;
                Log.v(TAG, "Button Code: " + buttonCode);
                changeSort(buttonCode);
                alertDialog.dismiss();
            }
        });

        Button closeButton = (Button) dialogView.findViewById(R.id.btn_close);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
    }

    private void changeSort(int radioButtonCode) {
        if (mFoodItemList != null) {
            ArrayList<FoodItem> foodItemArrayList = new ArrayList<>(mFoodItemList.size());
            foodItemArrayList.addAll(mFoodItemList);
            switch (radioButtonCode) {
                case RADIO_RATING:
                    Collections.sort(mFoodItemList, new Comparator<FoodItem>() {
                        @Override
                        public int compare(FoodItem foodItem1, FoodItem foodItem2) {
                            return Double.valueOf(foodItem2.getAverage_rating()).compareTo(foodItem1.getAverage_rating());
                        }
                    });
                    break;
                case RADIO_HIGH:
                    Collections.sort(mFoodItemList, new Comparator<FoodItem>() {
                        @Override
                        public int compare(FoodItem foodItem1, FoodItem foodItem2) {
                            return Double.valueOf(foodItem2.getItem_price()).compareTo(foodItem1.getItem_price());
                        }
                    });
                    break;
                case RADIO_LOW:
                    Collections.sort(mFoodItemList, new Comparator<FoodItem>() {
                        @Override
                        public int compare(FoodItem foodItem1, FoodItem foodItem2) {
                            return Double.valueOf(foodItem1.getItem_price()).compareTo(foodItem2.getItem_price());
                        }
                    });
            }
            adapter.setFoodItems(mFoodItemList);
        }
    }

    // Called from FoodItemsViewModel after making the network call
    // and retrieves the data from the given link
    @Override
    public void onDoneLoadingFoodData(List<FoodItem> foodItemsList) {
        binding.progressBar.setVisibility(View.GONE);
        if (foodItemsList == null)
            Toast.makeText(this, getString(R.string.error_msg), Toast.LENGTH_LONG).show();
        else {
            mFoodItemList = foodItemsList;
            adapter.setFoodItems(foodItemsList);
        }
    }

    @Override
    public void onFoodItemClick(FoodItem clickedFoodItem) {
        Intent detailActivityIntent = new Intent(this, DetailActivity.class);
        detailActivityIntent.putExtra(Intent.EXTRA_TEXT, clickedFoodItem);
        startActivity(detailActivityIntent);
    }

    public void onCartButtonClicked(View view) {
        Intent cartActivityIntent = new Intent(this, CartActivity.class);
        startActivity(cartActivityIntent);
    }
}
