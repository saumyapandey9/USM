package com.example.usm;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "USM_DB";
    private static final int DATABASE_VERSION = 3; // Increment for date column

    // Users Table
    public static final String TABLE_USERS = "users";
    public static final String COL_ID = "id";
    public static final String COL_NAME = "name";
    public static final String COL_EMAIL = "email";
    public static final String COL_PASSWORD = "password";
    public static final String COL_ROLE = "role";

    // Bookings Table
    public static final String TABLE_BOOKINGS = "bookings";
    public static final String COL_BOOKING_ID = "booking_id";
    public static final String COL_USER_EMAIL = "user_email";
    public static final String COL_SPORT = "sport";
    public static final String COL_PLAYGROUND = "playground";
    public static final String COL_SLOT = "slot";
    public static final String COL_DATE = "booking_date"; // New Column
    public static final String COL_IS_MAINTENANCE = "is_maintenance";
    public static final String COL_REASON = "reason";

    // Deleted Users Table
    public static final String TABLE_DELETED_USERS = "deleted_users";
    public static final String COL_DEL_ID = "del_id";
    public static final String COL_DEL_NAME = "del_name";
    public static final String COL_DEL_EMAIL = "del_email";
    public static final String COL_DEL_ROLE = "del_role";
    public static final String COL_DEL_REASON = "del_reason";
    public static final String COL_DEL_DATE = "del_date";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS + "("
                + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COL_NAME + " TEXT,"
                + COL_EMAIL + " TEXT UNIQUE,"
                + COL_PASSWORD + " TEXT,"
                + COL_ROLE + " TEXT" + ")";
        db.execSQL(CREATE_USERS_TABLE);

        String CREATE_BOOKINGS_TABLE = "CREATE TABLE " + TABLE_BOOKINGS + "("
                + COL_BOOKING_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COL_USER_EMAIL + " TEXT,"
                + COL_SPORT + " TEXT,"
                + COL_PLAYGROUND + " TEXT,"
                + COL_SLOT + " TEXT,"
                + COL_DATE + " TEXT,"
                + COL_IS_MAINTENANCE + " INTEGER DEFAULT 0,"
                + COL_REASON + " TEXT" + ")";
        db.execSQL(CREATE_BOOKINGS_TABLE);

        String CREATE_DELETED_USERS_TABLE = "CREATE TABLE " + TABLE_DELETED_USERS + "("
                + COL_DEL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COL_DEL_NAME + " TEXT,"
                + COL_DEL_EMAIL + " TEXT,"
                + COL_DEL_ROLE + " TEXT,"
                + COL_DEL_REASON + " TEXT,"
                + COL_DEL_DATE + " DATETIME DEFAULT CURRENT_TIMESTAMP" + ")";
        db.execSQL(CREATE_DELETED_USERS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_DELETED_USERS + "("
                    + COL_DEL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COL_DEL_NAME + " TEXT,"
                    + COL_DEL_EMAIL + " TEXT,"
                    + COL_DEL_ROLE + " TEXT,"
                    + COL_DEL_REASON + " TEXT,"
                    + COL_DEL_DATE + " DATETIME DEFAULT CURRENT_TIMESTAMP" + ")");
        }
        if (oldVersion < 3) {
            db.execSQL("ALTER TABLE " + TABLE_BOOKINGS + " ADD COLUMN " + COL_DATE + " TEXT");
        }
    }

    public boolean registerUser(String name, String email, String password, String role) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_NAME, name);
        values.put(COL_EMAIL, email);
        values.put(COL_PASSWORD, password);
        values.put(COL_ROLE, role);
        long result = db.insert(TABLE_USERS, null, values);
        return result != -1;
    }

    public boolean checkUser(String email, String password, String role) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USERS + " WHERE " + COL_EMAIL + "=? AND " + COL_PASSWORD + "=? AND " + COL_ROLE + "=?", new String[]{email, password, role});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    public Cursor getUserDetails(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_USERS + " WHERE " + COL_EMAIL + "=?", new String[]{email});
    }

    public Cursor getAllUsers() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_USERS, null);
    }

    public boolean deleteUserWithReason(String email, String name, String role, String reason) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(COL_DEL_NAME, name);
            values.put(COL_DEL_EMAIL, email);
            values.put(COL_DEL_ROLE, role);
            values.put(COL_DEL_REASON, reason);
            db.insert(TABLE_DELETED_USERS, null, values);
            db.delete(TABLE_USERS, COL_EMAIL + "=?", new String[]{email});
            db.setTransactionSuccessful();
            return true;
        } catch (Exception e) {
            return false;
        } finally {
            db.endTransaction();
        }
    }

    public Cursor getDeletedUsers() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_DELETED_USERS + " ORDER BY " + COL_DEL_DATE + " DESC", null);
    }

    public boolean addBooking(String email, String sport, String playground, String slot, String date, int isMaint, String reason) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_USER_EMAIL, email);
        values.put(COL_SPORT, sport);
        values.put(COL_PLAYGROUND, playground);
        values.put(COL_SLOT, slot);
        values.put(COL_DATE, date);
        values.put(COL_IS_MAINTENANCE, isMaint);
        values.put(COL_REASON, reason);
        long result = db.insert(TABLE_BOOKINGS, null, values);
        return result != -1;
    }

    public int getBookedCount(String sport, String playground, String date) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_BOOKINGS + " WHERE " + COL_SPORT + "=? AND " + COL_PLAYGROUND + "=? AND " + COL_DATE + "=?", new String[]{sport, playground, date});
        int count = 0;
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }
        cursor.close();
        return count;
    }

    public Cursor getUserBookings(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_BOOKINGS + " WHERE " + COL_USER_EMAIL + "=? AND " + COL_IS_MAINTENANCE + "=0", new String[]{email});
    }

    public Cursor getAllStudentBookings() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT b.*, u." + COL_NAME + " FROM " + TABLE_BOOKINGS + " b JOIN " + TABLE_USERS + " u ON b." + COL_USER_EMAIL + " = u." + COL_EMAIL + " WHERE b." + COL_IS_MAINTENANCE + "=0", null);
    }

    public Cursor getMaintenanceBookings() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_BOOKINGS + " WHERE " + COL_IS_MAINTENANCE + "=1", null);
    }
}
