package ru.burdin.clientbase;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.CalendarContract;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import ru.burdin.clientbase.models.Record;
import ru.burdin.clientbase.models.User;

public class CalendarSetting {
    private static CalendarSetting calendarSetting;
    private long id;
    private String name;
    private HashMap<String, Long> calendars = new HashMap<>();
    private Activity activity;
    private boolean checkBoxCalender;
    public static final String APP_PREFERENCES = "preferenses";
    public static final String APP_PREFERENCES_NAME_CALENDAR = "name_calendar";
    public static final String APP_PREFERENCES_ID_CALENDER = "id_calender";
    public static final String APP_PREFERENCES_CheckBox = "checkBox_calender";
    private SharedPreferences preferences;
    private Bd bd;

    private CalendarSetting(Activity activity) {
        this.activity = activity;
        preferences = activity.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        if (preferences.contains(APP_PREFERENCES_ID_CALENDER) && preferences.contains(APP_PREFERENCES_NAME_CALENDAR)) {
            id = preferences.getLong(APP_PREFERENCES_ID_CALENDER, 0);
            name = preferences.getString(APP_PREFERENCES_NAME_CALENDAR, "");
            checkBoxCalender = preferences.getBoolean(APP_PREFERENCES_CheckBox, false);
        }
        initCalendars();
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
        return calendars.keySet();
    }

    /*
    Выбираем календарь для синхронизации
     */
    public void listenCSpinner(Spinner spinner, List<String> list) {
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                name = list.get(i);
                id = calendars.get(name);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putLong(APP_PREFERENCES_ID_CALENDER, id);
                editor.putString(APP_PREFERENCES_NAME_CALENDAR, name);
                editor.apply();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    /*
    Сохраненный выбор календаря
     */
    public int indexSave(List<String> list) {
        int result = 0;
        if (id != 0) {
            result = list.indexOf(name);
        }
        return result;
    }

    /*
    Слушатель флажка календаря
     */
    public void listenChexBox(CheckBox checkBox, Spinner spinner) {
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                checkBoxCalender = b;
                spinner.setEnabled(checkBoxCalender);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean(APP_PREFERENCES_CheckBox, checkBoxCalender);
                editor.apply();
            }
        });
    }

    /*
    Добавить событие в календарь
     */
    public long addRecordCalender (Record record) {
        long result = 0;
        AsyncTasCalender asyncTasCalender = new AsyncTasCalender();
        Supplier<Long> supplier = new Supplier<Long>() {
            @Override
            public Long get() {
                if (checkBoxCalender) {
                    Uri uri = activity.getContentResolver().insert(CalendarContract.Events.CONTENT_URI, getContentValues(record));
                    return Long.parseLong(uri.getLastPathSegment());
                }
                return 0l;
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
return  result;
    }

    /*
    Редактирование события календаря
     */
    public int update(Record record) {
        long result = 0;
        AsyncTasCalender  asyncTasCalender = new AsyncTasCalender();
        Supplier <Long> supplier = new Supplier<Long>() {
            @Override
            public Long get() {
                if (checkBoxCalender) {
                    Uri uri = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, record.getEvent_id());
                    return (long) activity.getContentResolver().update(uri, getContentValues(record), null, null);
                }
                return 0l;
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
        return  (int)result;
    }

    /*
удаляет запись в календаре
 */
    public int delete(long id) {
        long result = 0;
        AsyncTasCalender asyncTasCalender = new AsyncTasCalender();
Supplier < Long> supplier = new Supplier<Long>() {
    @Override
    public Long get() {
        if (checkBoxCalender) {
            Uri deleteUri = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, id);
            return (long)activity.getContentResolver().delete(deleteUri, null, null);
        }
        return 0l;
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

    private class AsyncTasCalender extends AsyncTask<Supplier<Long>, Void,Long> {

        @Override
        protected Long doInBackground(Supplier<Long>... suppliers) {

            return suppliers[0].get();
        }
    }


}

