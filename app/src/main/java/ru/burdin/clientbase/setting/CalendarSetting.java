package ru.burdin.clientbase.setting;

import android.Manifest;
import android.app.Activity;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.CalendarContract;
import android.provider.Settings;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Spinner;

import androidx.appcompat.app.AlertDialog;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;
import java.util.concurrent.ExecutionException;
import java.util.function.Supplier;

import ru.burdin.clientbase.Bd;
import ru.burdin.clientbase.R;
import ru.burdin.clientbase.StaticClass;
import ru.burdin.clientbase.models.Record;
import ru.burdin.clientbase.models.User;

public class CalendarSetting {

    private static CalendarSetting calendarSetting;
    public long id = 0;
    private String name;
    private HashMap<String, Long> calendars = new HashMap<>();
    private static Activity activity;
    private boolean checkBoxCalender;
    public   final static  String EMPTY = "Календарь не выбран";
    private Bd bd;
    public   static final int Calender_PERMISSION = 2;



    private CalendarSetting(Activity activity) {
        this.activity = activity;
        if (requestSinglePermission()) {
            initCalendars();
        }

        if (Preferences.getSharedPreferences(activity).contains(Preferences.APP_PREFERENCES_ID_CALENDER) && Preferences.getSharedPreferences(activity).contains(Preferences.APP_PREFERENCES_NAME_CALENDAR)) {
            id = Preferences.getLong(activity, Preferences.APP_PREFERENCES_ID_CALENDER, 1);
            name = Preferences.getString(activity, Preferences.APP_PREFERENCES_NAME_CALENDAR, "");
            checkBoxCalender = Preferences.getBoolean (activity, Preferences.APP_PREFERENCES_CheckBox, false) && requestSinglePermission();
        }
        bd = Bd.load(activity.getApplicationContext());
    }

    /*
    Инициализация объекта
     */
    public static CalendarSetting load(Activity activity) {
        if (calendarSetting == null) {
            calendarSetting = new CalendarSetting(activity);
        }
        return calendarSetting;
    }

    public boolean getCheckBox() {
        return checkBoxCalender;
    }

    /*
    Получаем список календарей
     */
    private void initCalendars()  {
        AsyncTasCalender asyncTasCalender =new AsyncTasCalender();
        Supplier <Long> supplier = new Supplier<Long>() {
    @Override
    public Long get() {
                String[] projection =
                        new String[]{
                                CalendarContract.Calendars._ID,
                                CalendarContract.Calendars.NAME,
                                CalendarContract.Calendars.ACCOUNT_TYPE};
                Cursor calCursor = activity.getContentResolver().
                        query(CalendarContract.Calendars.CONTENT_URI,
                                projection,
                                CalendarContract.Calendars.CALENDAR_ACCESS_LEVEL + " > 400",
                                null,
                                null);

                while (calCursor.moveToNext()) {
                    calendars.put(calCursor.getString(1), calCursor.getLong(0));
                }
                calCursor.close();
    return  null;
    }
        };
        asyncTasCalender.execute(supplier);
        try {
            asyncTasCalender.get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    /*
Получаем список имен календарей
 */
    public Set<String> getNameCalendar() {
Set <String> strings = new HashSet<>();
                return calendars.keySet();
    }

    /*
    Выбираем календарь для синхронизации
     */
    public void listenCSpinner(Spinner spinner, List<String> list) {
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i != 0) {
                    name = list.get(i);
                    id = calendars.get(name);
                }else {
                    id   = 0l;
                    name = EMPTY;
                }
                Preferences.set(activity, Preferences.APP_PREFERENCES_ID_CALENDER, id);
                Preferences.set(activity, Preferences.APP_PREFERENCES_NAME_CALENDAR, name);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public void setCheckBoxCalender(boolean checkBoxCalender) {
        this.checkBoxCalender = checkBoxCalender;
    }

    /*
        Сохраненный выбор календаря
         */
    public int indexSave(List<String> list) {
        int result = 0;
            result = list.indexOf(name);
        return result;
    }

    /*
    Слушатель флажка календаря
     */
    public void listenChexBox(CheckBox checkBox, Spinner spinner, Activity calenderAxtivity) {
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                checkBoxCalender = b && requestSinglePermission();
if (!checkBoxCalender) {
    String [] permissions = new  String[] {Manifest.permission.READ_CALENDAR, Manifest.permission.WRITE_CALENDAR};
calenderAxtivity.requestPermissions(permissions, Calender_PERMISSION);
spinner.setEnabled(checkBoxCalender);
}else {
                initCalendars();
spinner.setEnabled(checkBoxCalender);
calenderAxtivity.recreate();
}
             Preferences.set(activity, Preferences.APP_PREFERENCES_CheckBox, checkBoxCalender);
            }
        });
    }

    /*
    Добавить событие в календарь
     */
    public long addRecordCalender (Record record) {
        long result = 0;
        if (checkBoxCalender && requestSinglePermission() && id != 0) {
            AsyncTasCalender asyncTasCalender = new AsyncTasCalender();
            Supplier<Long> supplier = new Supplier<Long>() {
                @Override
                public Long get() {
                    Uri uri = activity.getContentResolver().insert(CalendarContract.Events.CONTENT_URI, getContentValues(record));
                    return Long.parseLong(uri.getLastPathSegment());
                                    }
            };
            asyncTasCalender.execute(supplier);
            try {
                result = asyncTasCalender.get();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }        return  result;
    }

    /*
    Редактирование события календаря
     */
    public int update(Record record) {
        long result = 0;
        if (checkBoxCalender && requestSinglePermission() && id !=0) {
            AsyncTasCalender asyncTasCalender = new AsyncTasCalender();
            Supplier<Long> supplier = new Supplier<Long>() {
                @Override
                public Long get() {
                    Uri uri = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, record.getEvent_id());
                    return (long) activity.getContentResolver().update(uri, getContentValues(record), null, null);
                }
            };
            asyncTasCalender.execute(supplier);
            try {
                result = asyncTasCalender.get();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return  (int)result;
    }

    /*
удаляет запись в календаре
 */
    public int delete(long id) {
        long result = 0;
        if (checkBoxCalender && requestSinglePermission() && id !=0) {

            AsyncTasCalender asyncTasCalender = new AsyncTasCalender();
            Supplier<Long> supplier = new Supplier<Long>() {
                @Override
                public Long get() {
                    Uri deleteUri = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, id);
                    return (long) activity.getContentResolver().delete(deleteUri, null, null);
                }
            };
            asyncTasCalender.execute(supplier);
            try {
                result = asyncTasCalender.get();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return  (int) result;
}
    /*
    создает и наполняет contentValues
     */
    private ContentValues getContentValues(Record record) {
        User user = bd.getUsers().get(StaticClass.indexList(record.getIdUser(), bd.getUsers()));
        ContentValues contentValues = new ContentValues();
        contentValues.put(CalendarContract.Events.DTSTART, record.getStart());
        contentValues.put(CalendarContract.Events.DTEND, record.getStart() + record.getEnd());
        contentValues.put(CalendarContract.Events.TITLE, activity.getResources().getString(R.string.app_name) + " " + user.getSurname() + " " + user.getName() + " " + record.getProcedure());
        contentValues.put(CalendarContract.Events.DESCRIPTION, "");
        contentValues.put(CalendarContract.Events.CALENDAR_ID, this.id);
        contentValues.put(CalendarContract.Events.EVENT_TIMEZONE, TimeZone.getDefault().getDisplayName());
        return contentValues;
    }

    /*
Проверка разрешения
 */
    private static boolean requestSinglePermission() {

        String calenderPermission = Manifest.permission.READ_CALENDAR;
        String calenderPermission1 = Manifest.permission.WRITE_CALENDAR;
        int hasPermission = activity.checkSelfPermission(calenderPermission);
        int hasPermission2 =activity.checkSelfPermission(calenderPermission1);
        return hasPermission  == PackageManager.PERMISSION_GRANTED && hasPermission2 == PackageManager.PERMISSION_GRANTED;
    }

    private class AsyncTasCalender extends AsyncTask<Supplier<Long>, Void,Long> {

        @Override
        protected Long doInBackground(Supplier<Long>... suppliers) {

            return suppliers[0].get();
        }
    }

            public  void getDialog (Context context) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Нет разрешения чтение и записи календаря! Те чё, западло разрешить?");
                builder.setPositiveButton("Нет, не западло, сейчас разрешу",   new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                Uri uri = Uri.fromParts("package", context.getPackageName(), null);
                                intent.setData(uri);
                                context.startActivity(intent);

                            }
                        }
                );
                builder.setNegativeButton("Не разрешу, мало ли чё там в коде хакерского зарыто", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                builder.create().show();
            }
        }
