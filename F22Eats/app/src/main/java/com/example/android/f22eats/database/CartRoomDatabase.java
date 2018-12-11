package com.example.android.f22eats.database;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.example.android.f22eats.R;

@Database(entities = {Cart.class}, version = 1)
public abstract class CartRoomDatabase extends RoomDatabase {
    public abstract CartDao cartDao();

    private static CartRoomDatabase INSTANCE;
    private static final String DATABASE_NAME = "cart_database";

    public static CartRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (CartRoomDatabase.class) {
                if (INSTANCE == null) {
                    // Creates Database
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            CartRoomDatabase.class,
                            DATABASE_NAME)
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    //To insert dummy data and repopulate the database
    // whenever the app is started, we create a RoomDatabase.Callback and override onOpen()
    private static RoomDatabase.Callback sRoomDatabaseCallback =
            new RoomDatabase.Callback() {

                @Override
                public void onOpen(@NonNull SupportSQLiteDatabase db) {
                    super.onOpen(db);
                    new PopulateDbAsync(INSTANCE).execute();
                }
            };

    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {

        private final CartDao mDao;

        PopulateDbAsync(CartRoomDatabase db) {
            mDao = db.cartDao();
        }

        @Override
        protected Void doInBackground(final Void... params) {
            Cart cart = new Cart("dummy", "00", "0");
            mDao.insertCart(cart);
            return null;
        }
    }
}
