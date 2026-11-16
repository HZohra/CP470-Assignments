
package com.zohrah.androidassignments;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class ChatDatabaseHelper extends SQLiteOpenHelper {

    // Database constants required by the assignment
    public static final String ACTIVITY_NAME = "ChatDatabaseHelper";
    public static final String DATABASE_NAME = "Messages.db";
    public static final int VERSION_NUM = 2;
    public static final String TABLE_NAME = "Messages";
    public static final String KEY_ID = "_id";
    public static final String KEY_MESSAGE = "MESSAGE";

    // Constructor opens the database
    public ChatDatabaseHelper(Context ctx) {
        super(ctx, DATABASE_NAME, null, VERSION_NUM);
    }

    // Creates the messages table
    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i(ACTIVITY_NAME, "Calling onCreate");

        String createTable =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        KEY_MESSAGE + " TEXT NOT NULL);";

        db.execSQL(createTable);
    }

    // Deletes and recreates the table on a version upgrade
    @Override
    public void onUpgrade(
            SQLiteDatabase db,
            int oldVer,
            int newVer
    ) {
        Log.i(
                ACTIVITY_NAME,
                "Calling onUpgrade, oldVersion=" +
                        oldVer + " newVersion=" + newVer
        );

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        onCreate(db);
    }
}
