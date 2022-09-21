package ru.burdin.clientbase;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.provider.CalendarContract;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class CalendarSetting {
private  static CalendarSetting calendarSetting;
    private  long id;
private  String name;
private HashMap <String, Long> calendars = new HashMap<>();
private  Activity activity;
private  boolean checkBoxCalender;
public  static  final  String APP_PREFERENCES = "preferenses";
public  static  final  String APP_PREFERENCES_NAME_CALENDAR = "name_calendar";
public  static  final  String APP_PREFERENCES_ID_CALENDER = "id_calender";
public  static  final  String APP_PREFERENCES_CheckBox = "checkBox_calender";
private SharedPreferences preferences;

private   CalendarSetting (Activity activity) {
    this.activity = activity;
preferences = activity.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
    if (preferences.contains(APP_PREFERENCES_ID_CALENDER) && preferences.contains(APP_PREFERENCES_NAME_CALENDAR)) {
        id = preferences.getLong(APP_PREFERENCES_ID_CALENDER, 0);
        name = preferences.getString(APP_PREFERENCES_NAME_CALENDAR, "");
    checkBoxCalender = preferences.getBoolean(APP_PREFERENCES_CheckBox, false);
    }
    initCalendars();
}

/*
Инициализация объекта
 */
public  static  CalendarSetting load (Activity activity) {
    if (calendarSetting == null) {
        calendarSetting = new CalendarSetting(activity);
    }
return calendarSetting;
}

public  boolean getCheckBox () {
    return  checkBoxCalender;
}
/*
Получаем список календарей
 */
private  void  initCalendars () {
        String[] projection =
                new String[]{
                        CalendarContract.Calendars._ID,
                        CalendarContract.Calendars.NAME,
                        CalendarContract.Calendars.ACCOUNT_TYPE};
                Cursor calCursor =activity.getContentResolver().
                query(CalendarContract.Calendars.CONTENT_URI,
                        projection,
                        CalendarContract.Calendars.CALENDAR_ACCESS_LEVEL + " > 400",
                        null,
                        null);

            while (calCursor.moveToNext()) {
calendars.put(calCursor.getString(1), calCursor.getLong(0));
        }
    }
/*
Получаем список имен календарей
 */
public Set<String> getNameCalendar () {
    return  calendars.keySet();
}
/*
Выбираем календарь для синхронизации
 */
public  void  listenCSpinner (Spinner spinner, List <String> list) {
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
public  int indexSave (List <String> list) {
    int result = 0;
    if (id != 0) {
        result = list.indexOf(name);
    }
    return  result;
}

public  void  listenChexBox (CheckBox checkBox, Spinner spinner) {
    checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
checkBoxCalender = b;
spinner.setEnabled(checkBoxCalender);
SharedPreferences.Editor editor  = preferences.edit();
editor.putBoolean(APP_PREFERENCES_CheckBox, checkBoxCalender);
        }
    });
}

}
