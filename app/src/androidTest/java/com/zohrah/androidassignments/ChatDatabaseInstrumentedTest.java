
package com.zohrah.androidassignments;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class ChatDatabaseInstrumentedTest {

    // Get the Android application context
    private Context getContext() {
        return InstrumentationRegistry
                .getInstrumentation()
                .getTargetContext();
    }

    // Create a temporary in-memory database for testing
    // This will NOT affect the real Messages.db file
    private SQLiteDatabase createTestDatabase() {

        SQLiteDatabase db = SQLiteDatabase.create(null);

        ChatDatabaseHelper helper =
                new ChatDatabaseHelper(getContext());

        helper.onCreate(db);

        helper.close();

        return db;
    }

    // Helper method to insert a message into the database
    private long insertMessage(
            SQLiteDatabase db,
            String message
    ) {
        ContentValues values = new ContentValues();

        values.put(
                ChatDatabaseHelper.KEY_MESSAGE,
                message
        );

        long id = db.insert(
                ChatDatabaseHelper.TABLE_NAME,
                null,
                values
        );

        assertTrue("Message insertion failed", id > 0);

        return id;
    }

    // TEST 1: Verify database creates the correct columns
    @Test
    public void database_createsCorrectColumns() {

        try (SQLiteDatabase db = createTestDatabase();
             Cursor cursor = db.query(
                     ChatDatabaseHelper.TABLE_NAME,
                     null,
                     null, null, null, null, null
             )) {

            assertEquals(2, cursor.getColumnCount());

            assertTrue(
                    cursor.getColumnIndex(
                            ChatDatabaseHelper.KEY_ID
                    ) >= 0
            );

            assertTrue(
                    cursor.getColumnIndex(
                            ChatDatabaseHelper.KEY_MESSAGE
                    ) >= 0
            );
        }
    }

    // TEST 2: Verify a message can be inserted and retrieved
    @Test
    public void database_insertsAndReadsMessage() {

        try (SQLiteDatabase db = createTestDatabase()) {

            insertMessage(db, "Hello SQLite");

            try (Cursor cursor = db.query(
                    ChatDatabaseHelper.TABLE_NAME,
                    null,
                    null, null, null, null, null
            )) {

                assertTrue(cursor.moveToFirst());

                int messageColumn =
                        cursor.getColumnIndexOrThrow(
                                ChatDatabaseHelper.KEY_MESSAGE
                        );

                assertEquals(
                        "Hello SQLite",
                        cursor.getString(messageColumn)
                );
            }
        }
    }

    // TEST 3: Verify multiple messages retain their order
    @Test
    public void database_retainsMessageOrder() {

        try (SQLiteDatabase db = createTestDatabase()) {

            insertMessage(db, "First");
            insertMessage(db, "Second");
            insertMessage(db, "Third");

            try (Cursor cursor = db.query(
                    ChatDatabaseHelper.TABLE_NAME,
                    null,
                    null,
                    null,
                    null,
                    null,
                    ChatDatabaseHelper.KEY_ID + " ASC"
            )) {

                int messageColumn =
                        cursor.getColumnIndexOrThrow(
                                ChatDatabaseHelper.KEY_MESSAGE
                        );

                assertEquals(3, cursor.getCount());

                assertTrue(cursor.moveToNext());
                assertEquals(
                        "First",
                        cursor.getString(messageColumn)
                );

                assertTrue(cursor.moveToNext());
                assertEquals(
                        "Second",
                        cursor.getString(messageColumn)
                );

                assertTrue(cursor.moveToNext());
                assertEquals(
                        "Third",
                        cursor.getString(messageColumn)
                );
            }
        }
    }

    // TEST 4: Verify IDs are generated automatically
    @Test
    public void database_autoIncrementsIds() {

        try (SQLiteDatabase db = createTestDatabase()) {

            long firstId = insertMessage(db, "First");
            long secondId = insertMessage(db, "Second");

            assertTrue(
                    "Second ID should be greater than first ID",
                    secondId > firstId
            );
        }
    }

    // TEST 5: Verify a new database starts empty
    @Test
    public void database_startsEmpty() {

        try (SQLiteDatabase db = createTestDatabase();
             Cursor cursor = db.query(
                     ChatDatabaseHelper.TABLE_NAME,
                     null,
                     null, null, null, null, null
             )) {

            assertEquals(0, cursor.getCount());
        }
    }

    // TEST 6: Verify upgrading deletes previous messages
    @Test
    public void database_upgradeDeletesMessages() {

        try (SQLiteDatabase db = createTestDatabase()) {

            insertMessage(db, "Old message");

            ChatDatabaseHelper helper =
                    new ChatDatabaseHelper(getContext());

            helper.onUpgrade(db, 1, 2);
            helper.close();

            try (Cursor cursor = db.query(
                    ChatDatabaseHelper.TABLE_NAME,
                    null,
                    null, null, null, null, null
            )) {

                assertEquals(
                        "Messages should be deleted after upgrade",
                        0,
                        cursor.getCount()
                );
            }
        }
    }

    // TEST 7: Verify the table still works after upgrading
    @Test
    public void database_allowsInsertAfterUpgrade() {

        try (SQLiteDatabase db = createTestDatabase()) {

            ChatDatabaseHelper helper =
                    new ChatDatabaseHelper(getContext());

            helper.onUpgrade(db, 1, 2);
            helper.close();

            insertMessage(db, "New message");

            try (Cursor cursor = db.query(
                    ChatDatabaseHelper.TABLE_NAME,
                    null,
                    null, null, null, null, null
            )) {

                assertEquals(1, cursor.getCount());

                assertTrue(cursor.moveToFirst());

                assertEquals(
                        "New message",
                        cursor.getString(
                                cursor.getColumnIndexOrThrow(
                                        ChatDatabaseHelper.KEY_MESSAGE
                                )
                        )
                );
            }
        }
    }
}
