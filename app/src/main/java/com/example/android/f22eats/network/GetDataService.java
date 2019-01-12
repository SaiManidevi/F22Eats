package com.example.android.f22eats.network;

import com.example.android.f22eats.model.FoodItem;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;

public interface GetDataService {

    @GET("data.json")
    Call<ArrayList<FoodItem>> getFoodList();
}
