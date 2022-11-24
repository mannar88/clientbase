package ru.burdin.clientbase.lits;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.provider.CallLog;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import ru.burdin.clientbase.StaticClass;
import ru.burdin.clientbase.add.AddClientActivity;

class SelectAddClient {

    private Activity activity;
public  static  final  int PERNISSION_LOG_COLL = 5;

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

                if (requestSinglePermission()) {
                    callLog();
                }
                }
        });
        builder.setNeutralButton("Заполнить из контактов", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
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

    private boolean requestSinglePermission() {
        boolean result = false;
        String callPermission = Manifest.permission.READ_CALL_LOG;
        int hasPermission = activity.checkSelfPermission(callPermission);
        String[] permissions = new String[]{callPermission};
        if (hasPermission != PackageManager.PERMISSION_GRANTED) {
           activity.requestPermissions(permissions, PERNISSION_LOG_COLL);
        } else {
            result = true;
        }
        return result;
    }

}
