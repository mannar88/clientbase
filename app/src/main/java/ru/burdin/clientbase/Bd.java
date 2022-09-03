package ru.burdin.clientbase;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.CharArrayBuffer;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.stream.Collectors;

import ru.burdin.clientbase.models.Expenses;
import ru.burdin.clientbase.models.Procedure;
import ru.burdin.clientbase.models.Record;
import ru.burdin.clientbase.models.User;

public class Bd {

private  static Bd bd;
public static final String DATABASE_NAME = "clientBase.db";
    public static final int SCHEMA = 2;
public  static  final int SCHEMA_PROCEDURE = 2;
    public  static final String TABLE = "users";
    public  static  final String TABLE_PROCEDURE = "procedures";
    public  static  final String TABLE_EXPENSES = "expenses";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_SURNAME = "surname";
    public static final String COLUMN_PHONE = "phone";
    public static final String COLUMN_PRICE = "price";
    public  static  final String COLUMN_COMMENT = "Comment";
    public  static  final  String COLUMN_TIME_END = "time_end";
    public  static  final  String TABLE_SESSION = "sessions";
    public  static  final  String COLUMN_TIME = "time";
    public  static final  String COLUMN_ID_USER = "id_user";
public  static  final  String COLUMN_PROCEDURE = "procedire";
    private  DatabaseHelper databaseHelper;
private  SQLiteDatabase sqLiteDatabase;
private ArrayList <User> users;
private  ArrayList <Procedure> procedures;
private  ArrayList <Record> records;
private  ArrayList <Expenses> expenses;

private  Bd (Context context) {
    databaseHelper = new DatabaseHelper(context);
    sqLiteDatabase = databaseHelper.getReadableDatabase();
    collectListUsers();
    collectProcedures();
collectRecord();
collectExpenses();
}

    public ArrayList<Expenses> getExpenses() {
        return expenses;
    }

    public  static  Bd load (Context context) {

    if (bd == null) {
        bd = new Bd(context);
    }
    return  bd;
}

    public ArrayList<User> getUsers() {
    return  users;
    }

    public ArrayList<Procedure> getProcedures() {
        return procedures;
    }

    public ArrayList<Record> getRecords() {
        return records;
    }

    public  long  add (String table, ContentValues contentValues) {
    long  result = 0;
    result =  sqLiteDatabase.insert(table, null, contentValues);
        return  result;
}

private  void  collectListUsers () {
users = new ArrayList<>();
    Cursor userCursor = sqLiteDatabase.rawQuery("select * from "+ TABLE, null);
    while (userCursor.moveToNext()) {
    users.add(new User(userCursor.getLong(0), userCursor.getString(1), userCursor.getString(2), userCursor.getString(3), userCursor.getString(4)));
}
userCursor.close();
users.sort(Comparator.naturalOrder());
}

private  void  collectProcedures () {
    procedures = new ArrayList<>();
    Cursor procedureCursor = sqLiteDatabase.rawQuery("select * from "+ TABLE_PROCEDURE, null);
    while (procedureCursor.moveToNext()) {
    procedures.add(new Procedure(procedureCursor.getLong(0), procedureCursor.getString(1), procedureCursor.getDouble(2), procedureCursor.getLong(3)));
    }
procedureCursor.close();
    }

private  void  collectRecord () {
    records = new ArrayList<>();
Cursor cursorRecord = sqLiteDatabase.rawQuery("select * from "+ TABLE_SESSION, null);
while (cursorRecord.moveToNext()) {
records.add(new Record(cursorRecord.getLong(0), cursorRecord.getLong(1), cursorRecord.getLong(2), cursorRecord.getLong(3), cursorRecord.getString(4),cursorRecord.getDouble(5), cursorRecord.getString(6)));
    }
cursorRecord.close();
}

private  void collectExpenses () {
    expenses = new ArrayList<>();
    Cursor cursorExpenses =sqLiteDatabase.rawQuery("select * from "+ TABLE_EXPENSES, null);
while (cursorExpenses.moveToNext()) {
    expenses.add(new Expenses(cursorExpenses.getLong(0), cursorExpenses.getLong(1), cursorExpenses.getString(2), cursorExpenses.getDouble(3)));
}
cursorExpenses.close();
}

public  int delete (String table, long id) {
int result = 0;
      result =sqLiteDatabase.delete(table, "_id = ?", new String[]{String.valueOf(id)});
return  result;
}

public     int update  (String table, ContentValues contentValues, long id) {
    int result = -1;
    result = sqLiteDatabase.update(table, contentValues, COLUMN_ID + "=" + id, null);
    return result;
}

private  class  DatabaseHelper extends SQLiteOpenHelper {

public  DatabaseHelper (Context context) {
    super(context, DATABASE_NAME, null, SCHEMA);
}
        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
sqLiteDatabase.execSQL("CREATE TABLE " + TABLE + "(" + COLUMN_ID
+ " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_NAME
+ " TEXT, " + COLUMN_SURNAME + " TEXT,"
        +COLUMN_PHONE + " TEXT,"
        + COLUMN_COMMENT + " TEXT" + ");");

sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_PROCEDURE + "(" + COLUMN_ID
                    + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_NAME
                    + " TEXT,"
+ COLUMN_PRICE + " REAL,"
         + COLUMN_TIME_END + " INTEGER" + ");");

            sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_SESSION + "(" + COLUMN_ID
                    + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_TIME + " INTEGER,"
                    + COLUMN_TIME_END + " INTEGER,"
                    + COLUMN_ID_USER + " INTEGER,"
+ COLUMN_PROCEDURE + " TEXT,"
                    + COLUMN_PRICE + " REAL,"
                    +COLUMN_COMMENT + " TEXT);");

            sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_EXPENSES + "(" + COLUMN_ID
                    + " INTEGER PRIMARY KEY AUTOINCREMENT,"
+ COLUMN_TIME + " INTEGER,"
                    + COLUMN_NAME + " TEXT, "
                    + COLUMN_PRICE + " REAL);");

}

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+TABLE);
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+TABLE_PROCEDURE);
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+TABLE_SESSION);
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+TABLE_EXPENSES);
            onCreate(sqLiteDatabase);
}
    }
}
