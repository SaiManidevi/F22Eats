package com.example.android.f22eats.utilities;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.f22eats.R;
import com.example.android.f22eats.database.Cart;

import java.util.List;

public class CartItemAdapter extends RecyclerView.Adapter<CartItemAdapter.CartItemAdapterViewHolder> {
    private List<Cart> mCartItemsList;

    @NonNull
    @Override
    public CartItemAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.cart_item, viewGroup, false);
        return new CartItemAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartItemAdapterViewHolder holder, int position) {
        final Cart cartItem = mCartItemsList.get(position);
        if (!cartItem.getItemName().equals("dummy")) {
            holder.mCartItemName.setText(cartItem.getItemName());
            holder.mCartItemPrice.setText(cartItem.getItemPrice());
            holder.mCartItemQuantity.setText(cartItem.getItemQuantity());
            double price = Double.valueOf(cartItem.getItemPrice());
            int quantity = Integer.valueOf(cartItem.getItemQuantity());
            double totalPrice = price * quantity;
            holder.mCartItemTotal.setText(Double.toString(totalPrice));
        } else {
            holder.mCartItemName.setVisibility(View.GONE);
            holder.mCartItemPrice.setVisibility(View.GONE);
            holder.mCartItemTotal.setVisibility(View.GONE);
            holder.mCartItemQuantity.setVisibility(View.GONE);
            holder.mCartItemRupeeSymbol.setVisibility(View.GONE);
            holder.mCartItemXSymbol.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        if (mCartItemsList == null)
            return 0;
        else
            return mCartItemsList.size();
    }

    public class CartItemAdapterViewHolder extends RecyclerView.ViewHolder {
        private final TextView mCartItemName;
        private final TextView mCartItemPrice;
        private final TextView mCartItemQuantity;
        private final TextView mCartItemTotal;
        private final TextView mCartItemXSymbol;
        private final TextView mCartItemRupeeSymbol;

        public CartItemAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            mCartItemName = itemView.findViewById(R.id.tv_cart_item_name);
            mCartItemPrice = itemView.findViewById(R.id.tv_cart_item_price);
            mCartItemQuantity = itemView.findViewById(R.id.tv_cart_item_qunatity);
            mCartItemTotal = itemView.findViewById(R.id.tv_cart_item_total);
            mCartItemRupeeSymbol = itemView.findViewById(R.id.tv_cart_item_rupee);
            mCartItemXSymbol = itemView.findViewById(R.id.tv_cart_item_x);
        }
    }

    public void setCartItems(List<Cart> cartItems) {
        mCartItemsList = cartItems;
        notifyDataSetChanged();
    }
}
