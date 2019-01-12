package com.example.android.f22eats.activities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.android.f22eats.R;
import com.example.android.f22eats.database.Cart;
import com.example.android.f22eats.database.CartViewModel;
import com.example.android.f22eats.databinding.ActivityCartBinding;
import com.example.android.f22eats.utilities.CartItemAdapter;

import java.util.List;

public class CartActivity extends AppCompatActivity {
    ActivityCartBinding binding;
    CartViewModel mCartViewModel;
    CartItemAdapter mCartItemAdapter;
    List<Cart> mCartItemsList;
    double grandTotal;
    int discountValue;
    int netTotal;
    String enteredCoupon;
    boolean isCouponValid = false;
    // By default, Delivery Fee is Rs.30
    int deliveryFee = 30;
    private static final int minPrice = 400;
    private static final String COUPON_F22LABS = "F22LABS";
    private static final String COUPON_FREEDEL = "FREEDEL";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_cart);
        mCartViewModel = ViewModelProviders.of(this).get(CartViewModel.class);
        mCartItemAdapter = new CartItemAdapter();
        binding.recyclerviewCart.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerviewCart.setAdapter(mCartItemAdapter);
        binding.recyclerviewCart.setSaveEnabled(true);

        mCartViewModel.getAllCartItems().observe(this, new Observer<List<Cart>>() {
            @Override
            public void onChanged(@Nullable List<Cart> carts) {
                mCartItemsList = carts;
                updateCostTotal();
                mCartItemAdapter.setCartItems(carts);
            }
        });
    }

    private void updateCostTotal() {
        double itemTotal = 0;
        for (int i = 0; i < mCartItemsList.size(); i++) {
            Cart cartItem = mCartItemsList.get(i);
            double itemPrice = Double.valueOf(cartItem.getItemPrice());
            int itemQuantity = Integer.valueOf(cartItem.getItemQuantity());
            if (itemQuantity > 0)
                itemTotal = itemTotal + (itemPrice * itemQuantity);
        }
        // Considering GST as 9%, the calculation is as follows
        int gstRate = (int) itemTotal * 6 / 100;
        // Assign the values to their respective views
        binding.tvCartTotal.setText(Double.toString(itemTotal));
        binding.tvCartDelivery.setText(Integer.toString(deliveryFee));
        binding.tvCartGst.setText(Integer.toString(gstRate));

        netTotal = (int) itemTotal + gstRate;

        if (isCouponValid) {
            grandTotal = (netTotal + gstRate) - discountValue;
            binding.tvCartDiscount.setText(Integer.toString(discountValue));
            if (binding.rupeeSymbol.getVisibility() == View.INVISIBLE)
                binding.rupeeSymbol.setVisibility(View.VISIBLE);
            if (enteredCoupon.equals(COUPON_FREEDEL)) {
                binding.tvCartDelivery.setText(getString(R.string.free_del));

            }
        } else {
            grandTotal = netTotal + deliveryFee;
            binding.tvCartDiscount.setText(getString(R.string.nill));
            if (binding.rupeeSymbol.getVisibility() == View.VISIBLE)
                binding.rupeeSymbol.setVisibility(View.INVISIBLE);
        }
        binding.tvCartGrandTotal.setText(Double.toString(grandTotal));
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home)
            onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    public void onApplyCouponClicked(View view) {
        enteredCoupon = binding.etCoupon.getText().toString().trim();
        switch (enteredCoupon) {
            case COUPON_F22LABS:
                if (netTotal > minPrice) {
                    isCouponValid = true;
                    discountValue = (int) netTotal * 20 / 100;
                    Snackbar.make(binding.btnApplyCoupon, getString(R.string.coupon_applied), Snackbar.LENGTH_SHORT)
                            .show();
                } else
                    Toast.makeText(this, getString(R.string.total_less), Toast.LENGTH_LONG).show();
                break;
            case COUPON_FREEDEL:
                if (netTotal > 100) {
                    isCouponValid = true;
                    discountValue = 30;
                    Snackbar.make(binding.btnApplyCoupon, getString(R.string.coupon_applied), Snackbar.LENGTH_SHORT)
                            .show();
                } else
                    Toast.makeText(this, getString(R.string.total_less), Toast.LENGTH_LONG).show();
                break;
            default:
                isCouponValid = false;
                discountValue = 0;
                Toast.makeText(this, getString(R.string.invalid_coupon), Toast.LENGTH_LONG).show();
                break;
        }
        updateCostTotal();
    }

    public void onViewCouponClicked(View view) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.coupon_layout, null);
        builder.setView(dialogView);

        final AlertDialog alertDialog = builder.create();
        alertDialog.show();

        final CardView cardViewF22Labs = dialogView.findViewById(R.id.coupon_f22);
        final CardView cardViewFreeDel = dialogView.findViewById(R.id.coupon_freedel);
        final Button cancelDialogButton = dialogView.findViewById(R.id.btn_coupon_close);

        cardViewF22Labs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.etCoupon.setText(COUPON_F22LABS);
                alertDialog.dismiss();
            }
        });

        cardViewFreeDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.etCoupon.setText(COUPON_FREEDEL);
                alertDialog.dismiss();
            }
        });

        cancelDialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.etCoupon.setText("");
                alertDialog.dismiss();
            }
        });
    }
}
