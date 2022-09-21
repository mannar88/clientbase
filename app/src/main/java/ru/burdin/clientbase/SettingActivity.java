package ru.burdin.clientbase;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

public class SettingActivity extends AppCompatActivity {

    private Spinner spinnerGetCalendar;
    private CheckBox checkBoxCalender;
    ArrayAdapter <?> arrayAdapter;
    CalendarSetting calendars;

        @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seting);
        checkBoxCalender = findViewById(R.id.checkBoxSettingCalender);
        spinnerGetCalendar = findViewById(R.id.spinerSettingCalendar);
   calendars = CalendarSetting.load(this);
List <String> nameCalendars = new ArrayList<>(calendars.getNameCalendar());
                arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, nameCalendars);
    arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    spinnerGetCalendar.setAdapter(arrayAdapter);
    spinnerGetCalendar.setSelection(calendars.indexSave(nameCalendars));
/*
Получаем настройку флажка календаря
 */
            checkBoxCalender.setChecked(calendars.getCheckBox());
spinnerGetCalendar.setEnabled(calendars.getCheckBox());
        /*

        Слушатель флажка
         */
            calendars.listenChexBox(checkBoxCalender, spinnerGetCalendar);
            /*
    Выбор календаря для синхронизации
     */
        calendars.listenCSpinner(spinnerGetCalendar, nameCalendars);
        }

    }