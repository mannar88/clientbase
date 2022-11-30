package ru.burdin.clientbase.setting;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class Preferences {

private static final String APP_PREFERENCES = "preferenses";
    public static final String APP_PREFERENCES_NAME_CALENDAR = "name_calendar";
    public static final String APP_PREFERENCES_ID_CALENDER = "id_calender";
    public static final String APP_PREFERENCES_CheckBox = "checkBox_calender";
public  static  final  String  APP_PREFERENCES_START_WORK_HOUR = "start_work_hour";
    public  static  final  String APP_PREFERENCES_START_WORK_MINITS = "start_work_minits";
    public  static  final  String  APP_PREFERENCES_FINISH_HOUR = "ffinish_hour";
    public  static  final  String  APP_PREFERENCES_FINISH_MINUTES ="finish_minutes";
    public  static  final  String  APP_PREFERENCES_WORK_INTERVAL ="work_interval";
public  static  final  String SELECT_RADIO_BUTTON_NOTIFICETION_CLIENT = "selectRadioButtonNotificetionClient";
    public  static  final  String  APP_PREFERENCES_TEMPLETES_NOW = "app_preferenses_templetes_now";
    public  static  final  String  APP_PREFERENCES_TEMPLETESNOTIFICATION = "app_preferenses_templetes_notification";
    public  static  final  String  APP_PREFERENCES_TEMPLETES_READ = "app_preferenses_templetes_read";
    public  static  final  String  APP_PREFERENCES_TEMPLETES_DELETE = "app_preferenses_templetes_delete";

    public  static SharedPreferences  getSharedPreferences (Activity activity) {
        return   activity.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);

    }
    public  static  void set (Activity activity, String key, String value) {
        SharedPreferences.Editor editor = getSharedPreferences(activity).edit();
        editor.putString(key, value);
        editor.apply();
    }

    public  static  void  set (Activity activity, String key, long value) {
        SharedPreferences.Editor editor = getSharedPreferences(activity).edit();
editor.putLong(key, value);
editor.apply();
    }

public  static  void  set (Activity activity, String key, boolean value) {
        SharedPreferences.Editor editor = getSharedPreferences(activity).edit();
        editor.putBoolean(key, value);
        editor.apply();
}

public  static  int getInt (Activity activity, String key, int other) {
        return  getSharedPreferences(activity).getInt(key, other);
}

public  static Long getLong (Activity activity,  String key, long other) {
        return  getSharedPreferences(activity).getLong(key, other);
    }

    public  static  String getString (Activity activity, String key, String other) {
        return  getSharedPreferences(activity).getString(key, other);
    }

    public  static  boolean getBoolean (Activity activity, String key, boolean other) {
        return  getSharedPreferences(activity).getBoolean(key, other);
    }


}
