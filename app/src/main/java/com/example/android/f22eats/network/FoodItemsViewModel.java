package com.example.android.f22eats.network;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.android.f22eats.model.FoodItem;
import com.example.android.f22eats.utilities.SimpleIdlingResource;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FoodItemsViewModel extends AndroidViewModel {
    private static List<FoodItem> mFoodItemsList;
    private static final String TAG = FoodItemsViewModel.class.getSimpleName();


    public FoodItemsViewModel(@NonNull Application application) {
        super(application);
    }

    public interface FoodItemsCallBack {
        void onDoneLoadingFoodData(List<FoodItem> foodItemsList);
    }

    public void getFoodData(final FoodItemsCallBack callBack, @Nullable final SimpleIdlingResource idlingResource) {
        GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        Call<ArrayList<FoodItem>> call = service.getFoodList();

        call.enqueue(new Callback<ArrayList<FoodItem>>() {
            @Override
            public void onResponse(Call<ArrayList<FoodItem>> call, Response<ArrayList<FoodItem>> response) {
                mFoodItemsList = response.body();
                if (callBack != null) {
                    callBack.onDoneLoadingFoodData(mFoodItemsList);
                    if (idlingResource != null) {
                        idlingResource.setIdleState(true);
                    }
                }
            }

            @Override
            public void onFailure(Call<ArrayList<FoodItem>> call, Throwable t) {
                mFoodItemsList = null;
                Log.v(TAG, "On_Failure" + mFoodItemsList);
                t.printStackTrace();
                callBack.onDoneLoadingFoodData(mFoodItemsList);
            }
        });
    }
}
