package ru.burdin.clientbase;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import ru.burdin.clientbase.models.Record;
import ru.burdin.clientbase.setting.Preferences;

public final class TimeReceiver extends BroadcastReceiver {

    private static    TimeReceiver timeReceiver;
private  int check;
private DateFormat dateFormat = new SimpleDateFormat("HH:mm");
private List<Record> records = new ArrayList<>();

private  TimeReceiver (Activity activity) {
    check = Preferences.getInt(activity, Preferences.APP_PREFERENSES_CHECK_SMS_NOTIFICATION, R.id.radioButtonTempleetsNotificationNotCheck);
}

/*
Инициализация объекта
 */
    public  static  TimeReceiver load(Activity activity) {
        if (timeReceiver == null) {
            timeReceiver = new TimeReceiver(activity);
        }
        return timeReceiver;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
    if (check != R.id.radioButtonTempleetsNotificationNotCheck) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (dateFormat.format(new Date()).equals(Preferences.getString(context, Preferences.APP_PREFERENSES_TIME_NOTIFICATION_SMS, "nuull"))) {
                    setListSession();
                    records.forEach(
                            record -> SendSMS.send(context, Preferences.getString(context, SendSMS.KEY_PREFERENSES.get(1), SendSMS.TEMPLETS.get(1)), record, R.id.radioButtonAddSessionSMS)
                    );

                }

            }
        };
    Thread thread = new Thread(runnable);
    thread.start();
    }
    }

    /*
    собирает список сеансов
     */
    private  void  setListSession () {
records.clear();
        Calendar calendar = Calendar.getInstance();
        if (check == R.id.radioButtonTempleetsNotificationNextDey) {
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }
    DateFormat dateFormat = new SimpleDateFormat("dd-MM-YY");
    for (Record record:Bd.getRecords()) {
        if (dateFormat.format(record.getStartDay()).equals(dateFormat.format(calendar.getTime()))){
        records.add(record);
        }
    }
    }

    /*
    Слушатель радиогрупп
     */
public  void  setCheck (RadioGroup radioGroup, TextView textView, Activity activity) {
    radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup radioGroup, int i) {
            if ((i == R.id.radioButtonTempleetsNotificationToday || i == R.id.radioButtonTempleetsNotificationNextDey) && SendSMS.permission(activity)) {
            }
            check = i;
boolean dis = i == R.id.radioButtonTempleetsNotificationNotCheck? false: true;
textView.setEnabled(dis);
        }
    });
}


}