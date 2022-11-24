package ru.burdin.clientbase.lits;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.provider.CallLog;
import android.provider.ContactsContract;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import ru.burdin.clientbase.Bd;
import ru.burdin.clientbase.StaticClass;
import ru.burdin.clientbase.add.AddClientActivity;
import ru.burdin.clientbase.models.User;

class SelectAddClient {

    private Activity activity;
public  static  final  int PERNISSION_LOG_COLL = 5;
public  static  final  int PERMISSION_PHONE_BOOK = 6;

public SelectAddClient(Activity activity) {
        this.activity = activity;
    }

    /*
    Вызывает диалог
     */

    public void getDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setCancelable(false);
        builder.setPositiveButton("Заполнить в ручную", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(activity, AddClientActivity.class);
                activity.startActivity(intent);
            }
        });
        builder.setNegativeButton("Взять номер из журнала вызовов", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                if (requestSinglePermission(Manifest.permission.READ_CALL_LOG, PERNISSION_LOG_COLL)) {
                    callLog();
                }
                }
        });
        builder.setNeutralButton("Заполнить из контактов", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            if (requestSinglePermission(Manifest.permission.READ_CONTACTS, PERMISSION_PHONE_BOOK)) {
                phoneBooke();
            }
            }
        });
        builder.create().show();

    }

    /*
    Журнал звонков
     */
    public void callLog() {
        Map<String, String> colls = new LinkedHashMap<>();
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss dd-MM-YYYY");
        AlertDialog.Builder builderColl = new AlertDialog.Builder(activity);
        String[] projection = new String[]{
                CallLog.Calls._ID,
                CallLog.Calls.DATE,
                CallLog.Calls.NUMBER,
                CallLog.Calls.CACHED_NAME,
                CallLog.Calls.DURATION,
                CallLog.Calls.TYPE
        };
        String where = "";
        Cursor cursor = activity.getContentResolver().query(
                CallLog.Calls.CONTENT_URI,
                projection,
                where,
                null,
                null
        );

        if (cursor.moveToFirst()) {
            do {
                colls.put(cursor.getString(cursor.getColumnIndex(CallLog.Calls.NUMBER)) + ", " + dateFormat.format(new Date(cursor.getLong(cursor.getColumnIndex(CallLog.Calls.DATE)))), cursor.getString(cursor.getColumnIndex(CallLog.Calls.NUMBER)));
            } while (cursor.moveToNext());
        }
        List<String> list = new ArrayList<>(colls.keySet());
        Collections.reverse(list);
        builderColl.setItems(list.toArray(new String[colls.size()]), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(activity, AddClientActivity.class);
                intent.putExtra(StaticClass.NUMBER_PHONE, colls.get(list.get(i)));
                activity.startActivity(intent);
            }
        });

        builderColl.create().show();

    }

/*
Запрос к телефонной книге
 */
  public void phoneBooke () {
      Map <String, String> contacts = new TreeMap<>();
      List <String[]> result = new ArrayList<>();
      AlertDialog.Builder builder = new AlertDialog.Builder(activity);
      Cursor cursor = activity.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
              null, null, null, null);
      if (cursor != null) {
          while (cursor.moveToNext()) {
          contacts.put(cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME_ALTERNATIVE)).trim() + "", cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)).trim() + "");
          }
      }
      if (cursor != null) {
          cursor.close();
      }
List <String> list = new ArrayList<>(contacts.keySet());
Comparator <String> comparator = (String::compareToIgnoreCase);
list.sort(comparator);
boolean [] checks = new  boolean[contacts.size()];
  builder.setMultiChoiceItems(list.toArray(new String[contacts.size()]), checks, new DialogInterface.OnMultiChoiceClickListener() {
      @Override
      public void onClick(DialogInterface dialogInterface, int i, boolean b) {
if (b) {
    result.add(new  String[] {list.get(i), contacts.get(list.get(i))});
}
      }
  });
  builder.setPositiveButton("Добавить", new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialogInterface, int i) {
      result.forEach(
              strings ->
                      setContact(
                              strings[0].split(" ", 2), strings[1]
                      )
      );
      }
  });
  builder.create().show();
  }

  /*
  Добавление контакта вбазу
   */
  private  void  setContact (String[] name, String phone) {
      Bd bd = Bd.load(activity);
      ContentValues contentValues =new ContentValues();
      for (int i = 0; i < name.length; i++) {
          if (i == 0) {
              contentValues.put(Bd.COLUMN_SURNAME, name[0]);
          }else {
              contentValues.put(Bd.COLUMN_NAME, name[1]);
          }
      }
contentValues.put(Bd.COLUMN_PHONE, phone);
 long id = bd.add(Bd.TABLE, contentValues);
  if (id > 0) {
      User user = new User(id, contentValues.getAsString(Bd.COLUMN_NAME) + "", contentValues.getAsString(Bd.COLUMN_SURNAME), phone, "");
  bd.getUsers().add(user);
  bd.getUsers().sort(Comparator.naturalOrder());
activity.recreate();
  }
  }

  /*
Разрешение
 */
    private boolean requestSinglePermission(String permission, int request) {
        boolean result = false;
        String callPermission = permission;
        int hasPermission = activity.checkSelfPermission(callPermission);
        String[] permissions = new String[]{callPermission};
        if (hasPermission != PackageManager.PERMISSION_GRANTED) {
           activity.requestPermissions(permissions, request);
        } else {
            result = true;
        }
        return result;
    }

}
