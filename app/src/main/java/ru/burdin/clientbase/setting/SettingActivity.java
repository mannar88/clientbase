package ru.burdin.clientbase.setting;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ExecutionException;

import ru.burdin.clientbase.Bd;
import ru.burdin.clientbase.R;
import ru.burdin.clientbase.StaticClass;
import ru.burdin.clientbase.TimeReceiver;

public class SettingActivity extends AppCompatActivity {

    private Spinner spinnerGetCalendar;
    private CheckBox checkBoxCalender;
   private ArrayAdapter <?> arrayAdapter;
    private CalendarSetting calendars;
    public static final int REQUEST_PERMISSIONS = 101;
private RadioGroup radioGroup;

    private Bd bd;
private  WorkScheduleSetting workScheduleSetting;
private  static List<String> nameCalendars;
@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seting);
        bd = Bd.load(this);
        checkBoxCalender = findViewById(R.id.checkBoxSettingCalender);
        spinnerGetCalendar = findViewById(R.id.spinerSettingCalendar);
   workScheduleSetting = new WorkScheduleSetting(this);
        calendars = CalendarSetting.load(this);
 nameCalendars = new ArrayList<>(calendars.getNameCalendar());
                arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, nameCalendars);
    arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    spinnerGetCalendar.setAdapter(arrayAdapter);

    }

    @Override
    protected void onStart() {
        super.onStart();
workScheduleSetting.setOnItemSelectedListener();
spinnerGetCalendar.setSelection(calendars.indexSave(nameCalendars));
        checkBoxCalender.setChecked(calendars.getCheckBox());
        spinnerGetCalendar.setEnabled(calendars.getCheckBox());
        calendars.listenChexBox(checkBoxCalender, spinnerGetCalendar, this);
}

    @Override
    protected void onResume() {
        super.onResume();
calendars.listenCSpinner(spinnerGetCalendar, nameCalendars);

}

    /*
    Ответна разрешение
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {
        switch (requestCode) {
            case CalendarSetting.Calender_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
checkBoxCalender.setChecked(false);
                } else {
                    spinnerGetCalendar.setEnabled(false);
                    StaticClass.getDialog(this, "чтение и запись календаря");
                }
                break;
            case REQUEST_PERMISSIONS:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED  && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                } else {
//                    StaticClass.getDialog(this, "чтение и запись файловой системы");
                String string = "";
                    for (int i = 0; i < permissions.length; i++) {
                        string = string + " " + permissions[i] + " " + grantResults[i] + " ";
                    }
                checkBoxCalender.setText(string);

                }
    break;
                default:
                            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                    }
                }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
    }

    public static List<String> getNameCalendars() {
        return nameCalendars;
    }


    public void onClickButtonSettingTemplets(View view) {
    Intent intent = new Intent(this, TemplatesActivity.class);
    startActivity(intent);
    }
}