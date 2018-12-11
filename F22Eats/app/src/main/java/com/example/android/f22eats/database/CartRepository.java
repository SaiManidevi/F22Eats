package com.example.android.f22eats.database;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;

public class CartRepository {
    private CartDao mCartDao;
    private LiveData<List<Cart>> mAllCartItems;

    public CartRepository(Application application) {
        CartRoomDatabase db = CartRoomDatabase.getDatabase(application);
        mCartDao = db.cartDao();
        mAllCartItems = mCartDao.getAllCartItems();
    }

    LiveData<List<Cart>> getAllCartItems() {
        return mAllCartItems;
    }

    String getItemQuantity(String item_name) {
        String quantity = null;
        try {
            quantity = new getQuantityAsyncTask(mCartDao).execute(item_name).get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return quantity;
    }

    private static class getQuantityAsyncTask extends AsyncTask<String, Void, String> {
        private CartDao quantityDao;

        public getQuantityAsyncTask(CartDao quantityDao) {
            this.quantityDao = quantityDao;
        }

        @Override
        protected String doInBackground(String... strings) {
            return quantityDao.getCartItemQuantity(strings[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }

    public void insertCart(Cart cartItem) {
        new insertAsyncTask(mCartDao).execute(cartItem);
    }

    private static class insertAsyncTask extends AsyncTask<Cart, Void, Void> {
        private CartDao mAsyncTaskDao;

        insertAsyncTask(CartDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Cart... carts) {
            mAsyncTaskDao.insertCart(carts[0]);
            return null;
        }
    }

    public void deleteCart(Cart cartItem) {
        new deleteAsyncTask(mCartDao).execute(cartItem);
    }

    private static class deleteAsyncTask extends AsyncTask<Cart, Void, Void> {
        private CartDao mAsyncTaskDao;

        deleteAsyncTask(CartDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Cart... carts) {
            mAsyncTaskDao.deleteCart(carts[0]);
            return null;
        }
    }

    public void deleteAllCartItems() {
        new deleteAllAsyncTask(mCartDao).execute();
    }

    private static class deleteAllAsyncTask extends AsyncTask<Void, Void, Void> {
        private CartDao mAsyncTaskDao;

        deleteAllAsyncTask(CartDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            mAsyncTaskDao.deleteAll();
            return null;
        }
    }

}