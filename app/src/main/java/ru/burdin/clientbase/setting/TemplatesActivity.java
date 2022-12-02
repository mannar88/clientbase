package ru.burdin.clientbase.setting;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import ru.burdin.clientbase.R;
import ru.burdin.clientbase.SendSMS;
import ru.burdin.clientbase.TimeReceiver;

public class TemplatesActivity extends AppCompatActivity {

    private RadioGroup radioGroupSelectNotifacition;
    private RadioButton radioButtonNotificationFols;
private RadioButton radioButtonNotificationNextDay;
private RadioButton radioButtonNotificationToday;
private EditText editTextNow;
private  EditText editTextNotification;
private EditText editTextNotificationRead;
private  EditText editTextNotificationDelete;
    private List<EditText> editTexts;
    private DateFormat dateFormat = new SimpleDateFormat("HH:mm");
    private String [] selectTimes = new  String[24];
private TextView textViewSelectTime;
private Activity activity;
@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_templates);
    radioGroupSelectNotifacition = findViewById(R.id.radioGroupTempletesNotification);
        radioButtonNotificationFols = findViewById(R.id.radioButtonTempleetsNotificationNotCheck);
    radioButtonNotificationNextDay = findViewById(R.id.radioButtonTempleetsNotificationNextDey);
    radioButtonNotificationToday = findViewById(R.id.radioButtonTempleetsNotificationToday);
    radioGroupSelectNotifacition.check(Preferences.getInt(this, Preferences.SELECT_RADIO_BUTTON_NOTIFICETION_CLIENT, radioButtonNotificationFols.getId()));
    editTextNow = findViewById(R.id.editTextTempletesNow);
    editTextNotification = findViewById(R.id.editTextTempletesNotificetion);
    editTextNotificationRead = findViewById(R.id.editTextTempletesRead);
editTextNotificationDelete = findViewById(R.id.editTextTempletesDelete);
         editTexts =Arrays.asList(editTextNow, editTextNotification, editTextNotificationRead, editTextNotificationDelete);
        SendSMS.getTenpletes(this, editTexts);
textViewSelectTime = findViewById(R.id.textVieTempletsSelectHourNotification);
activity =this;
if (radioGroupSelectNotifacition.getCheckedRadioButtonId() == R.id.radioButtonTempleetsNotificationNotCheck) {
    textViewSelectTime.setEnabled(false);
}else {
    textViewSelectTime.setText(Preferences.getString(this, Preferences.APP_PREFERENSES_TIME_NOTIFICATION_SMS, textViewSelectTime.getText().toString()));
}
}


    @Override
    protected void onStart() {
        super.onStart();
setSelectTimes();
    }

/*
Собирает в массив время
 */
private  void  setSelectTimes () {
    Calendar calendar = Calendar.getInstance();

    calendar.set(Calendar.MINUTE, 0);
    for (int i = 0; i < selectTimes.length; i++) {
        calendar.set(Calendar.HOUR_OF_DAY, i);
        selectTimes[i] = dateFormat.format(calendar.getTime());
    }
}

    @Override
    protected void onResume() {
        super.onResume();
        TimeReceiver.load(this).setCheck(radioGroupSelectNotifacition, textViewSelectTime, activity);
    }

    @Override
    public void onBackPressed() {
SendSMS.setTemplets(this, editTexts);
Preferences.set(this, Preferences.APP_PREFERENSES_CHECK_SMS_NOTIFICATION, radioGroupSelectNotifacition.getCheckedRadioButtonId());
Preferences.set(this, Preferences.SELECT_RADIO_BUTTON_NOTIFICETION_CLIENT, radioGroupSelectNotifacition.getCheckedRadioButtonId());
super.onBackPressed();
    }

    public void onClickTextViewTempletesSelectNotification(View view) {
AlertDialog.Builder builder = new  AlertDialog.Builder(this);
builder.setItems(selectTimes, new DialogInterface.OnClickListener() {
    @Override
    public void onClick(DialogInterface dialogInterface, int i) {
    textViewSelectTime.setText("SMS отправится в " + selectTimes[i]);
    Preferences.set(activity, Preferences.APP_PREFERENSES_TIME_NOTIFICATION_SMS, selectTimes[i]);
}
});
builder.create().show();
}

    public void onClickButtonTemplatesKey(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
    builder.setItems(getResources().getStringArray(R.array.templetesKey), new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {

        }
    });
    builder.create().show();
    }
}