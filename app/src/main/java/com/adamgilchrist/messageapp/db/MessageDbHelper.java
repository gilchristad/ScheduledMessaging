package com.adamgilchrist.messageapp.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MessageDbHelper extends SQLiteOpenHelper {

    public MessageDbHelper(Context context) {
        super(context, MessageContract.DB_NAME, null, MessageContract.DB_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + MessageContract.MessageEntry.TABLE + " ( " + MessageContract.MessageEntry._ID +
                " INTEGER PRIMARY KEY AUTOINCREMENT, " + MessageContract.MessageEntry.COL_MESSAGE_TITLE + " TEXT NOT NULL, "
                + MessageContract.MessageEntry.COL_CONTACT_TITLE + " TEXT NOT NULL);";
        db.execSQL(createTable);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS " + MessageContract.MessageEntry.TABLE);
        onCreate(db);
    }
}
