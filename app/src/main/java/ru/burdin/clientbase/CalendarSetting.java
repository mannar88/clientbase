package ru.burdin.clientbase;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.CalendarContract;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class CalendarSetting {
private  static CalendarSetting calendarSetting;
    private  long id;
private  String name;
private HashMap < Long, String> calendars = new HashMap<>();
private  Activity activity;

private   CalendarSetting (Activity activity) {
    this.activity = activity;
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
calendars.put(calCursor.getLong(0), calCursor.getString(1));
        }
    }

public Collection<String> getNameCalendar () {
    return  calendars.values();
}

}
