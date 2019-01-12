package com.example.android.f22eats.utilities;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.f22eats.R;
import com.example.android.f22eats.database.Cart;
import com.example.android.f22eats.database.CartViewModel;
import com.example.android.f22eats.model.FoodItem;

import java.util.List;

public class FoodItemsAdapter extends RecyclerView.Adapter<FoodItemsAdapter.FoodItemsAdapterViewHolder> {
    private List<FoodItem> mFoodItems;
    private CartViewModel mCartViewModel;
    private Context mContext;
    private int mItemQuantity;
    private String mFoodItemQuantity;
    /*
     * An on-click handler to make it easy for an Activity to interface with
     * the RecyclerView
     */
    final private FoodItemClickListener mOnClickListener;

    public FoodItemsAdapter(Context context, FoodItemClickListener mOnClickListener) {
        mContext = context;
        this.mOnClickListener = mOnClickListener;
    }

    public void getViewModelInstance(CartViewModel viewModel) {
        mCartViewModel = viewModel;
    }

    /**
     * The interface that receives onClick messages.
     */
    public interface FoodItemClickListener {
        void onFoodItemClick(FoodItem clickedFoodItem);
    }

    @NonNull
    @Override
    public FoodItemsAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.foodlist_item, parent, false);
        return new FoodItemsAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final FoodItemsAdapterViewHolder holder, int position) {
        final FoodItem foodItem = mFoodItems.get(position);
        final String foodItemName = foodItem.getItem_name();
        holder.mFoodItemName.setText(foodItemName);
        String foodRating = Double.toString(foodItem.getAverage_rating());
        holder.mFoodItemRating.setText(foodRating);
        final String foodPrice = Double.toString(foodItem.getItem_price());
        holder.mFoodItemPrice.setText(foodPrice);
        String foodItemImageUrl = foodItem.getImage_url();
        GlideApp.with(mContext)
                .load(foodItemImageUrl)
                .placeholder(R.drawable.fast_food_icons_311258)
                .error(R.drawable.fast_food_icons_311258)
                .into(holder.mFoodItemImage);

        updateItemQuantity(foodItemName);

        holder.mFoodItemQuantity.setText(Integer.toString(mItemQuantity));

        holder.mAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateItemQuantity(foodItemName);
                mItemQuantity = mItemQuantity + 1;
                mFoodItemQuantity = Integer.toString(mItemQuantity);
                Cart cart = new Cart(foodItemName, foodPrice, mFoodItemQuantity);
                mCartViewModel.insertCartItem(cart);
                holder.mFoodItemQuantity.setText(Integer.toString(mItemQuantity));
            }
        });

        holder.mRemoveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateItemQuantity(foodItemName);
                if (mItemQuantity > 1) {
                    mItemQuantity = mItemQuantity - 1;
                    mFoodItemQuantity = Integer.toString(mItemQuantity);
                    Cart cart = new Cart(foodItemName, foodPrice, mFoodItemQuantity);
                    mCartViewModel.insertCartItem(cart);
                } else {
                    mItemQuantity = 0;
                    mFoodItemQuantity = Integer.toString(mItemQuantity);
                    Cart cart = new Cart(foodItemName, foodPrice, mFoodItemQuantity);
                    mCartViewModel.deleteCartItem(cart);
                }
                holder.mFoodItemQuantity.setText(Integer.toString(mItemQuantity));
            }
        });

    }

    private void updateItemQuantity(String foodItemName) {
        mFoodItemQuantity = mCartViewModel.getItemQuantity(foodItemName);
        if (mFoodItemQuantity == null)
            mItemQuantity = 0;
        else
            mItemQuantity = Integer.valueOf(mFoodItemQuantity);
    }

    @Override
    public int getItemCount() {
        if (mFoodItems == null)
            return 0;
        else
            return mFoodItems.size();
    }

    public class FoodItemsAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final ImageView mFoodItemImage;
        private final TextView mFoodItemName;
        private final TextView mFoodItemPrice;
        private final TextView mFoodItemRating;
        private final TextView mFoodItemQuantity;
        private final Button mAddButton;
        private final Button mRemoveButton;

        public FoodItemsAdapterViewHolder(View itemView) {
            super(itemView);
            mFoodItemImage = itemView.findViewById(R.id.iv_food_image);
            mFoodItemName = itemView.findViewById(R.id.tv_food_name);
            mFoodItemPrice = itemView.findViewById(R.id.tv_food_price);
            mFoodItemRating = itemView.findViewById(R.id.tv_food_rating);
            mFoodItemQuantity = itemView.findViewById(R.id.tv_item_count);
            mAddButton = itemView.findViewById(R.id.btn_add);
            mRemoveButton = itemView.findViewById(R.id.btn_remove);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            FoodItem currentFoodItem = mFoodItems.get(adapterPosition);
            mOnClickListener.onFoodItemClick(currentFoodItem);
        }
    }

    public void setFoodItems(List<FoodItem> foodItems) {
        mFoodItems = foodItems;
        notifyDataSetChanged();
    }
}
