package ru.burdin.clientbase.setting;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.Arrays;
import java.util.List;

import ru.burdin.clientbase.R;
import ru.burdin.clientbase.SendSMS;

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
    }

    @Override
    protected void onStart() {
        super.onStart();


    }


    @Override
    public void onBackPressed() {
SendSMS.setTemplets(this, editTexts);
        super.onBackPressed();
    }

    public void onClickTextViewTempletesSelectNotification(View view) {
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